package com.nimblix.attendance.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record AdminAttendanceResponse(Long attendanceId, Long userId, String employeeName, String email, LocalDate date,
		LocalDateTime checkInTime, LocalDateTime checkOutTime, boolean late, boolean earlyCheckout, Integer totalWorkMinutes,
		AttendanceStatus status,
		String photoPath) {

	
}
