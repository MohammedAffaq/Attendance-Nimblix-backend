package com.nimblix.attendance.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AttendanceResponse {

	private String employeeId; // Admin-assigned employee ID (e.g. EMP001)
	private String employeeName; // ✅ Added for admin report
	private LocalDate date;
	private LocalDateTime checkInTime;
	private LocalDateTime checkOutTime;
	private boolean late;
	private boolean earlyCheckout;
	private Integer totalWorkMinutes;
	private AttendanceStatus status;
	private String photoUrl;
	private WorkMode workMode;

	/** Informational message returned on graceful "already marked" response. */
	private String message;

	/**
	 * True when attendance for today is already fully complete (check-in +
	 * check-out done).
	 */
	private boolean alreadyMarked;

	/**
	 * True when the system auto-checked-out an employee who forgot to check out.
	 */
	private boolean autoCheckedOut;

	// Full constructor including employeeId + employeeName
	public AttendanceResponse(String employeeId,
			String employeeName,
			LocalDate date,
			LocalDateTime checkInTime,
			LocalDateTime checkOutTime,
			boolean late,
			boolean earlyCheckout,
			Integer totalWorkMinutes,
			AttendanceStatus status,
			String photoUrl,
			WorkMode workMode) {

		this.employeeId = employeeId;
		this.employeeName = employeeName;
		this.date = date;
		this.checkInTime = checkInTime;
		this.checkOutTime = checkOutTime;
		this.late = late;
		this.earlyCheckout = earlyCheckout;
		this.totalWorkMinutes = totalWorkMinutes;
		this.status = status;
		this.photoUrl = photoUrl;
		this.workMode = workMode;
	}

	// ✅ Default constructor (important for Jackson)
	public AttendanceResponse() {
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalDateTime getCheckInTime() {
		return checkInTime;
	}

	public void setCheckInTime(LocalDateTime checkInTime) {
		this.checkInTime = checkInTime;
	}

	public LocalDateTime getCheckOutTime() {
		return checkOutTime;
	}

	public void setCheckOutTime(LocalDateTime checkOutTime) {
		this.checkOutTime = checkOutTime;
	}

	public boolean isLate() {
		return late;
	}

	public void setLate(boolean late) {
		this.late = late;
	}

	public boolean isEarlyCheckout() {
		return earlyCheckout;
	}

	public void setEarlyCheckout(boolean earlyCheckout) {
		this.earlyCheckout = earlyCheckout;
	}

	public Integer getTotalWorkMinutes() {
		return totalWorkMinutes;
	}

	public void setTotalWorkMinutes(Integer totalWorkMinutes) {
		this.totalWorkMinutes = totalWorkMinutes;
	}

	public AttendanceStatus getStatus() {
		return status;
	}

	public void setStatus(AttendanceStatus status) {
		this.status = status;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public WorkMode getWorkMode() {
		return workMode;
	}

	public void setWorkMode(WorkMode workMode) {
		this.workMode = workMode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isAlreadyMarked() {
		return alreadyMarked;
	}

	public void setAlreadyMarked(boolean alreadyMarked) {
		this.alreadyMarked = alreadyMarked;
	}

	public boolean isAutoCheckedOut() {
		return autoCheckedOut;
	}

	public void setAutoCheckedOut(boolean autoCheckedOut) {
		this.autoCheckedOut = autoCheckedOut;
	}
}
