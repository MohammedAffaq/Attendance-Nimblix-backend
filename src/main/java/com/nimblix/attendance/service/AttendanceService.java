package com.nimblix.attendance.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.nimblix.attendance.entity.AttendanceResponse;

public interface AttendanceService {

    Page<AttendanceResponse> myReport(LocalDate start, LocalDate end, Pageable pageable);

	AttendanceResponse checkin(MultipartFile photo, double lat, double lng);

	AttendanceResponse checkout(MultipartFile photo, double lat, double lng);



}
