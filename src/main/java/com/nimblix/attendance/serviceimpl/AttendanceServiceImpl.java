package com.nimblix.attendance.serviceimpl;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.nimblix.attendance.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.nimblix.attendance.exception.BadRequestException;
import com.nimblix.attendance.exception.ForbiddenException;
import com.nimblix.attendance.repository.AttendanceRepository;
import com.nimblix.attendance.repository.UserRepository;
import com.nimblix.attendance.service.AttendanceService;
import com.nimblix.attendance.service.FileStorageService;
import com.nimblix.attendance.util.GeoUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AttendanceServiceImpl implements AttendanceService {

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AttendanceServiceImpl.class);

	private final AttendanceRepository attendanceRepository;
	private final UserRepository userRepository;
	private final FileStorageService storage;

	private static final LocalTime OFFICE_START = LocalTime.of(9, 30);
	private static final LocalTime OFFICE_END = LocalTime.of(18, 30);

	// ==============================
	// ✅ CHECK-IN (WFO / WFH Supported)
	// ==============================
	@Override
	public AttendanceResponse checkin(MultipartFile photo, double lat, double lng) {

		User user = getCurrentUser();
		LocalDate today = LocalDate.now();

		log.info("[CheckIn] userId={} date={} lat={} lng={}", user.getId(), today, lat, lng);

		// ── Step 1: Check if an attendance record already exists for today ──
		Attendance existing = attendanceRepository
				.findByUserAndDate(user, today)
				.orElse(null);

		// ── Case 3: Both check-in and check-out already done ────────────────
		if (existing != null && existing.getCheckOutTime() != null) {
			log.info("[CheckIn] ALREADY_MARKED | userId={} date={}", user.getId(), today);
			AttendanceResponse resp = new AttendanceResponse();
			resp.setAlreadyMarked(true);
			resp.setMessage("Attendance already marked for today");
			return resp;
		}

		// ── Case 2: Checked-in but not yet checked out ───────────────────────
		if (existing != null && existing.getCheckOutTime() == null) {
			log.info("[CheckIn] ALREADY_CHECKED_IN (no checkout yet) | userId={} date={}", user.getId(), today);
			// Return the existing record — no DB write, no error
			AttendanceResponse resp = mapToResponse(existing);
			resp.setMessage("Already checked in — please use checkout endpoint");
			return resp;
		}

		// ── Case 1: No record for today → fresh check-in ────────────────────
		// Determine Work Mode via Haversine geo-fence
		boolean atOffice = GeoUtil.isInside(lat, lng);
		WorkMode workMode = atOffice ? WorkMode.WORK_FROM_OFFICE : WorkMode.WORK_FROM_HOME;
		log.info("[CheckIn] workMode={} | userId={} lat={} lng={}", workMode, user.getId(), lat, lng);

		validatePhoto(photo);

		String photoPath;
		try {
			photoPath = storage.save(photo);
		} catch (IOException e) {
			throw new BadRequestException("Failed to store attendance photo");
		}

		LocalDateTime now = LocalDateTime.now();

		Attendance attendance = new Attendance();
		attendance.setUser(user);
		attendance.setDate(today);
		attendance.setCheckInTime(now);
		attendance.setCheckInLatitude(lat);
		attendance.setCheckInLongitude(lng);
		attendance.setCheckInPhotoPath(photoPath);
		attendance.setWorkMode(workMode);
		attendance.setLate(now.toLocalTime().isAfter(OFFICE_START));
		attendance.setEarlyCheckout(false);
		attendance.setStatus(AttendanceStatus.PRESENT);

		Attendance saved = attendanceRepository.save(attendance);
		log.info("[CheckIn] SUCCESS | userId={} date={} attendanceId={}", user.getId(), today, saved.getId());

		return mapToResponse(saved);
	}

	// ==============================
	// ✅ CHECK-OUT
	// ==============================
	@Override
	public AttendanceResponse checkout(MultipartFile photo, double lat, double lng) {

		validatePhoto(photo);

		User user = getCurrentUser();
		LocalDate today = LocalDate.now();

		Attendance attendance = attendanceRepository
				.findByUserIdAndDateAndCheckOutTimeIsNull(user.getId(), today)
				.orElseThrow(() -> new BadRequestException("No active check-in found"));

		LocalDateTime now = LocalDateTime.now();

		attendance.setCheckOutTime(now);
		attendance.setCheckOutLatitude(lat);
		attendance.setCheckOutLongitude(lng);

		// Cap working minutes at 9 hours (540 min) — rule #2
		int totalMinutes = (int) Math.min(
				Duration.between(attendance.getCheckInTime(), now).toMinutes(),
				540);

		attendance.setTotalWorkMinutes(totalMinutes);

		try {
			String photoPath = storage.save(photo);
			attendance.setCheckOutPhotoPath(photoPath);
		} catch (IOException e) {
			throw new BadRequestException("Failed to store attendance photo");
		}

		attendance.setEarlyCheckout(now.toLocalTime().isBefore(OFFICE_END));

		Attendance saved = attendanceRepository.save(attendance);

		return mapToResponse(saved);
	}

	// ==============================
	// ✅ MY REPORT
	// ==============================
	@Override
	@Transactional(readOnly = true)
	public Page<AttendanceResponse> myReport(LocalDate start, LocalDate end, Pageable pageable) {
		User user = getCurrentUser();
		return attendanceRepository
				.findByUserIdAndDateBetween(user.getId(), start, end, pageable)
				.map(this::mapToResponse);
	}

	// ==============================
	// ✅ GET CURRENT USER
	// ==============================
	private User getCurrentUser() {

		var authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			throw new ForbiddenException("Unauthenticated access");
		}

		Object principal = authentication.getPrincipal();

		if (!(principal instanceof org.springframework.security.core.userdetails.UserDetails userDetails)) {
			throw new ForbiddenException("Invalid authentication principal");
		}

		String email = userDetails.getUsername();

		return userRepository.findByEmailIgnoreCase(email)
				.orElseThrow(() -> new BadRequestException("User not found or disabled"));
	}

	// ==============================
	// ✅ MAP TO RESPONSE
	// ==============================
	private AttendanceResponse mapToResponse(Attendance a) {

		String photoPath = a.getCheckOutPhotoPath() != null
				? a.getCheckOutPhotoPath()
				: a.getCheckInPhotoPath();

		String url = storage.getFileUrl(photoPath);

		String empId = a.getUser() != null
				? String.valueOf(a.getUser().getId())
				: null;
		String empName = a.getUser() != null ? a.getUser().getName() : "Unknown";

		AttendanceResponse response = new AttendanceResponse(
				empId,
				empName,
				a.getDate(),
				a.getCheckInTime(),
				a.getCheckOutTime(),
				a.isLate(),
				a.isEarlyCheckout(),
				a.getTotalWorkMinutes(),
				a.getStatus(),
				url,
				a.getWorkMode());
		response.setAutoCheckedOut(a.isAutoCheckedOut());
		return response;
	}

	// ==============================
	// ✅ PHOTO VALIDATION
	// ==============================
	private void validatePhoto(MultipartFile photo) {
		if (photo == null || photo.isEmpty()) {
			throw new BadRequestException("Photo is required");
		}
		if (!photo.getContentType().startsWith("image/")) {
			throw new BadRequestException("Only image files are allowed");
		}
		if (photo.getSize() > 5 * 1024 * 1024) {
			throw new BadRequestException("Max photo size is 5MB");
		}
	}
}
