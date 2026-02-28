package com.nimblix.attendance.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;

@Entity
@Table(name = "attendance", uniqueConstraints = @UniqueConstraint(columnNames = { "user_id", "date" }), indexes = {
		@Index(name = "idx_attendance_date", columnList = "date"),
		@Index(name = "idx_attendance_user", columnList = "user_id")
})
public class Attendance {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// ✅ WORK MODE (FIXED)
	@Enumerated(EnumType.STRING)
	@Column(name = "work_mode")
	private WorkMode workMode;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(nullable = false, updatable = false)
	private LocalDate date;

	@Column(updatable = false)
	private LocalDateTime checkInTime;

	private LocalDateTime checkOutTime;

	private Double checkInLatitude;
	private Double checkInLongitude;

	private Double checkOutLatitude;
	private Double checkOutLongitude;

	@Column(nullable = false, updatable = false)
	private String checkInPhotoPath;

	private String checkOutPhotoPath;

	private Integer totalWorkMinutes;

	private boolean late;
	private boolean earlyCheckout;

	@Column(nullable = false)
	private boolean autoCheckedOut = false;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AttendanceStatus status;

	@CreationTimestamp
	private LocalDateTime createdAt;

	@UpdateTimestamp
	private LocalDateTime updatedAt;

	// ===============================
	// GETTERS & SETTERS
	// ===============================

	public Long getId() {
		return id;
	}

	public WorkMode getWorkMode() {
		return workMode;
	}

	public void setWorkMode(WorkMode workMode) {
		this.workMode = workMode;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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

	public Double getCheckInLatitude() {
		return checkInLatitude;
	}

	public void setCheckInLatitude(Double checkInLatitude) {
		this.checkInLatitude = checkInLatitude;
	}

	public Double getCheckInLongitude() {
		return checkInLongitude;
	}

	public void setCheckInLongitude(Double checkInLongitude) {
		this.checkInLongitude = checkInLongitude;
	}

	public Double getCheckOutLatitude() {
		return checkOutLatitude;
	}

	public void setCheckOutLatitude(Double checkOutLatitude) {
		this.checkOutLatitude = checkOutLatitude;
	}

	public Double getCheckOutLongitude() {
		return checkOutLongitude;
	}

	public void setCheckOutLongitude(Double checkOutLongitude) {
		this.checkOutLongitude = checkOutLongitude;
	}

	public String getCheckInPhotoPath() {
		return checkInPhotoPath;
	}

	public void setCheckInPhotoPath(String checkInPhotoPath) {
		this.checkInPhotoPath = checkInPhotoPath;
	}

	public String getCheckOutPhotoPath() {
		return checkOutPhotoPath;
	}

	public void setCheckOutPhotoPath(String checkOutPhotoPath) {
		this.checkOutPhotoPath = checkOutPhotoPath;
	}

	public Integer getTotalWorkMinutes() {
		return totalWorkMinutes;
	}

	public void setTotalWorkMinutes(Integer totalWorkMinutes) {
		this.totalWorkMinutes = totalWorkMinutes;
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

	public boolean isAutoCheckedOut() {
		return autoCheckedOut;
	}

	public void setAutoCheckedOut(boolean autoCheckedOut) {
		this.autoCheckedOut = autoCheckedOut;
	}

	public AttendanceStatus getStatus() {
		return status;
	}

	public void setStatus(AttendanceStatus status) {
		this.status = status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
}
