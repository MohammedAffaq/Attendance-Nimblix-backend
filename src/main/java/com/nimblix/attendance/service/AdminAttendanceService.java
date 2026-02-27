package com.nimblix.attendance.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nimblix.attendance.entity.AttendanceResponse;
import com.nimblix.attendance.entity.AttendanceStatus;

public interface AdminAttendanceService {

    /**
     * Fetch paginated attendance records for all users
     *
     * @param date Optional date filter
     * @param status Optional status filter
     * @param pageable Pageable object
     * @return Page of AttendanceResponse
     */
    Page<AttendanceResponse> getAttendanceReport(LocalDate date, AttendanceStatus status, Pageable pageable);

    /**
     * Fetch paginated late arrivals
     */
    Page<AttendanceResponse> getLateArrivals(LocalDate date, Pageable pageable);

    /**
     * Fetch paginated early checkouts
     */
    Page<AttendanceResponse> getEarlyCheckouts(LocalDate date, Pageable pageable);
}
