package com.nimblix.attendance.serviceimpl;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nimblix.attendance.entity.Attendance;
import com.nimblix.attendance.entity.AttendanceResponse;
import com.nimblix.attendance.entity.AttendanceStatus;
import com.nimblix.attendance.exception.BadRequestException;
import com.nimblix.attendance.repository.AttendanceRepository;
import com.nimblix.attendance.service.AdminAttendanceService;
import com.nimblix.attendance.service.FileStorageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminAttendanceServiceImpl implements AdminAttendanceService {

	private final AttendanceRepository attendanceRepository;
	private final FileStorageService fileStorageService;
	private final FileStorageService storage;

	@Override
	@Transactional(readOnly = true)
	public Page<AttendanceResponse> getAttendanceReport(LocalDate date, AttendanceStatus status, Pageable pageable) {

		Page<Attendance> records;
		if (date != null && status != null) {
			records = attendanceRepository.findByDateAndStatusWithUser(date, status, pageable);
		} else if (date != null) {
			records = attendanceRepository.findByDateWithUser(date, pageable);
		} else if (status != null) {
			records = attendanceRepository.findByStatusWithUser(status, pageable);
		} else {
			records = attendanceRepository.findAllWithUser(pageable);
		}

		return records.map(this::mapToResponse);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<AttendanceResponse> getLateArrivals(LocalDate date, Pageable pageable) {
		if (date == null) {
			throw new BadRequestException("Date is required");
		}
		return attendanceRepository.findByDateAndLateTrueWithUser(date, pageable).map(this::mapToResponse);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<AttendanceResponse> getEarlyCheckouts(LocalDate date, Pageable pageable) {

		if (date == null) {
			throw new BadRequestException("Date is required");
		}

		return attendanceRepository.findByDateAndEarlyCheckoutTrueWithUser(date, pageable).map(this::mapToResponse);
	}

	private AttendanceResponse mapToResponse(Attendance a) {

		// Choose checkout photo if available, otherwise check-in photo
		String photoPath = a.getCheckOutPhotoPath() != null
				? a.getCheckOutPhotoPath()
				: a.getCheckInPhotoPath();

		String photoUrl = storage.getFileUrl(photoPath);

		// Safely read user fields (within @Transactional so LAZY proxy is fine)
		String empId = a.getUser() != null ? a.getUser().getEmployeeId() : null;
		String empName = a.getUser() != null ? a.getUser().getName() : "Unknown";

		return new AttendanceResponse(
				empId,
				empName,
				a.getDate(),
				a.getCheckInTime(),
				a.getCheckOutTime(),
				a.isLate(),
				a.isEarlyCheckout(),
				a.getTotalWorkMinutes(),
				a.getStatus(),
				photoUrl,
				a.getWorkMode());
	}

}
