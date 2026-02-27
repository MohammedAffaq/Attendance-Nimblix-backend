package com.nimblix.attendance.entity;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;

@Entity
@Table(name = "payslips", uniqueConstraints = @UniqueConstraint(name = "uk_payslip_user_month", columnNames = {
		"user_id", "month" }), indexes = { @Index(name = "idx_payslip_user", columnList = "user_id"),
				@Index(name = "idx_payslip_month", columnList = "month") })
public class Payslip {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// ================= USER =================
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	// ================= PERIOD =================
	@Column(nullable = false, length = 7)
	private YearMonth month; // e.g. 2026-01

	// ================= ATTENDANCE SUMMARY =================
	@Column(nullable = false)
	private Integer workingDays;

	@Column(nullable = false)
	private Integer presentDays;

	@Column(nullable = false)
	private Integer absentDays;

	@Column(nullable = false)
	private Integer lateDays;

	@Column(nullable = false)
	private Integer earlyCheckoutDays;

	// ================= SALARY DETAILS =================
	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal baseSalary;

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal deductions;

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal netSalary;

	// ================= METADATA =================
	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime generatedAt;

	// ================= GETTERS & SETTERS =================

	public Long getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public YearMonth getMonth() {
		return month;
	}

	public void setMonth(YearMonth month) {
		this.month = month;
	}

	public Integer getWorkingDays() {
		return workingDays;
	}

	public void setWorkingDays(Integer workingDays) {
		this.workingDays = workingDays;
	}

	public Integer getPresentDays() {
		return presentDays;
	}

	public void setPresentDays(Integer presentDays) {
		this.presentDays = presentDays;
	}

	public Integer getAbsentDays() {
		return absentDays;
	}

	public void setAbsentDays(Integer absentDays) {
		this.absentDays = absentDays;
	}

	public Integer getLateDays() {
		return lateDays;
	}

	public void setLateDays(Integer lateDays) {
		this.lateDays = lateDays;
	}

	public Integer getEarlyCheckoutDays() {
		return earlyCheckoutDays;
	}

	public void setEarlyCheckoutDays(Integer earlyCheckoutDays) {
		this.earlyCheckoutDays = earlyCheckoutDays;
	}

	public BigDecimal getBaseSalary() {
		return baseSalary;
	}

	public void setBaseSalary(BigDecimal baseSalary) {
		this.baseSalary = baseSalary;
	}

	public BigDecimal getDeductions() {
		return deductions;
	}

	public void setDeductions(BigDecimal deductions) {
		this.deductions = deductions;
	}

	public BigDecimal getNetSalary() {
		return netSalary;
	}

	public void setNetSalary(BigDecimal netSalary) {
		this.netSalary = netSalary;
	}

	public LocalDateTime getGeneratedAt() {
		return generatedAt;
	}
}
