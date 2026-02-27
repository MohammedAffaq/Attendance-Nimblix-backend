package com.nimblix.attendance.serviceimpl;

import java.io.IOException;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.nimblix.attendance.entity.CreateEmployeeRequest;
import com.nimblix.attendance.entity.Role;
import com.nimblix.attendance.entity.User;
import com.nimblix.attendance.entity.UserResponse;
import com.nimblix.attendance.exception.BadRequestException;
import com.nimblix.attendance.repository.UserRepository;
import com.nimblix.attendance.service.FileStorageService;
import com.nimblix.attendance.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

	private final UserRepository userRepo;
	private final FileStorageService storage;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public void createEmployee(CreateEmployeeRequest request, MultipartFile photo) {

		if (userRepo.existsByEmailIgnoreCase(request.getEmail())) {
			throw new BadRequestException("Email already exists");
		}

		String photoPath = null;
		if (photo != null && !photo.isEmpty()) {
			try {
				photoPath = storage.save(
						photo,
						"users",
						"user_" + System.currentTimeMillis() + ".jpg");
			} catch (IOException e) {
				throw new RuntimeException("Photo upload failed", e);
			}
		}

		User user = new User();
		user.setEmployeeId(request.getEmployeeId());
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRole(Role.EMPLOYEE);
		user.setPhotoPath(photoPath);
		user.setEnabled(true);

		userRepo.save(user);
	}

	@Override
	public void updateStatus(Long userId, boolean enabled) {
		User user = userRepo.findById(userId).orElseThrow(() -> new IllegalStateException("User not found"));
		user.setEnabled(enabled);
	}

	@Override
	public void updateLock(Long userId, boolean locked) {
		User user = userRepo.findById(userId).orElseThrow(() -> new IllegalStateException("User not found"));
		user.setLocked(locked);
	}

	private UserResponse mapToResponse(User user) {
		UserResponse r = new UserResponse();
		r.setId(user.getId());
		r.setEmployeeId(user.getEmployeeId());
		r.setName(user.getName());
		r.setEmail(user.getEmail());
		r.setRole(user.getRole());
		r.setEnabled(user.isEnabled());
		r.setLocked(user.isLocked());
		return r;
	}

	@Override
	public List<UserResponse> getAllEmployees() {
		return userRepo.findAll()
				.stream()
				.map(this::mapToResponse)
				.toList();
	}

	private void validatePhoto(MultipartFile photo) {
		if (photo == null || photo.isEmpty()) {
			throw new IllegalStateException("Photo is required");
		}
		if (!photo.getContentType().startsWith("image/")) {
			throw new IllegalStateException("Only image files are allowed");
		}
		if (photo.getSize() > 5 * 1024 * 1024) { // 5MB limit
			throw new IllegalStateException("Max photo size is 5MB");
		}
	}
}
