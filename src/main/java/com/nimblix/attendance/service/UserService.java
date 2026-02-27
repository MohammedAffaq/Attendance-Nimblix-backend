package com.nimblix.attendance.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.nimblix.attendance.entity.CreateEmployeeRequest;
import com.nimblix.attendance.entity.UserResponse;

public interface UserService {

	void createEmployee(CreateEmployeeRequest request, MultipartFile photo);

	void updateStatus(Long userId, boolean enabled);

	void updateLock(Long userId, boolean locked);

	// ✅ ADD THIS METHOD
	List<UserResponse> getAllEmployees();
}
