package com.nimblix.attendance.controller;

import java.time.LocalDate;

import org.springframework.security.core.Authentication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nimblix.attendance.entity.AttendanceResponse;
import com.nimblix.attendance.exception.BadRequestException;
import com.nimblix.attendance.service.AttendanceService;
import com.nimblix.attendance.service.PayslipService;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeController {

	private final AttendanceService attendanceService;
	private final PayslipService payslipService;


	@PreAuthorize("hasRole('EMPLOYEE')")
	@PostMapping(value = "/checkin", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public AttendanceResponse checkin(@RequestPart("photo") MultipartFile photo,
			@RequestParam @DecimalMin("-90.0") @DecimalMax("90.0") double lat,
			@RequestParam @DecimalMin("-180.0") @DecimalMax("180.0") double lng) {

		return attendanceService.checkin(photo, lat, lng);
	}

	@PreAuthorize("hasRole('EMPLOYEE')")
	@PostMapping(value = "/checkout", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public AttendanceResponse checkout(
			@RequestPart("photo") MultipartFile photo,
			@RequestParam @DecimalMin("-90.0") @DecimalMax("90.0") double lat,
			@RequestParam @DecimalMin("-180.0") @DecimalMax("180.0") double lng) {

		return attendanceService.checkout(photo, lat, lng);
	}

	@PreAuthorize("hasRole('EMPLOYEE')")
	@GetMapping("/my-report")
	public Page<AttendanceResponse> myReport(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

		if (start.isAfter(end)) {
			throw new BadRequestException("Start date must be before end date");
		}

		Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
//		return attendanceService.myReport(userDetails.getUser().getId(), start, end, pageable);
		return attendanceService.myReport(start, end, pageable);
	}

	// --- Payslip ---
//	@PreAuthorize("hasRole('EMPLOYEE')")
//	@GetMapping("/payslip")
//	public Payslip getPayslip(@RequestParam YearMonth month) {
//		CustomUserDetails userDetails = getCurrentUser();
//		return payslipService.generatePayslip(userDetails.getUser().getId(), month);
//	}

}
