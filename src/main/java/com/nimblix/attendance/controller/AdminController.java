package com.nimblix.attendance.controller;

import java.io.IOException;
import java.time.LocalDate;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimblix.attendance.entity.AttendanceResponse;
import com.nimblix.attendance.entity.AttendanceStatus;
import com.nimblix.attendance.entity.CreateEmployeeRequest;
import com.nimblix.attendance.exception.BadRequestException;
import com.nimblix.attendance.service.AdminAttendanceService;
import com.nimblix.attendance.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

	private final UserService userService;

	private final AdminAttendanceService adminAttendanceService;

	// ===============================
	// CREATE EMPLOYEE (POST)
	// ===============================
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping(value = "/employees", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> createEmployee(
			@RequestPart("data") String data,
			@RequestPart("photo") MultipartFile photo) {

		try {
			ObjectMapper mapper = new ObjectMapper();
			CreateEmployeeRequest request = mapper.readValue(data, CreateEmployeeRequest.class);

			userService.createEmployee(request, photo);
			return ResponseEntity.ok("Employee created successfully");

		} catch (IOException e) {
			throw new BadRequestException("Invalid request data");
		}
	}


	// ===============================
	// GET ALL EMPLOYEES (GET)
	// ===============================
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/employees")
	public ResponseEntity<?> getAllEmployees() {
		return ResponseEntity.ok(userService.getAllEmployees());
	}



	// --- All Attendance Records ---
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/attendance")
	public Page<AttendanceResponse> getAttendanceReport(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
			@RequestParam(required = false) AttendanceStatus status, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "20") int size) {

		Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
		return adminAttendanceService.getAttendanceReport(date, status, pageable);
	}

	// --- Late Arrivals ---
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/attendance/late")
	public Page<AttendanceResponse> getLateArrivals(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {

		Pageable pageable = PageRequest.of(page, size, Sort.by("checkInTime").ascending());
		return adminAttendanceService.getLateArrivals(date, pageable);
	}

	// --- Early Checkouts ---
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/attendance/early")
	public Page<AttendanceResponse> getEarlyCheckouts(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {

		Pageable pageable = PageRequest.of(page, size, Sort.by("checkOutTime").ascending());
		return adminAttendanceService.getEarlyCheckouts(date, pageable);
	}
}
