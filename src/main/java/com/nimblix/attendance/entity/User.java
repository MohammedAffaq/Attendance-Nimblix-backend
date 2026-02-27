//updated
package com.nimblix.attendance.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users", indexes = { @Index(name = "idx_user_email", columnList = "email"),
		@Index(name = "idx_user_role", columnList = "role") })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, length = 50)
	private String employeeId;

	@Column(nullable = false, unique = true, length = 150)
	private String email;

	@Column(nullable = false, length = 100)
	private String name;

	@JsonIgnore
	@Column(nullable = false)
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 30)
	private Role role;

	@Column(nullable = false)
	private boolean enabled = true;

	@Column(nullable = false)
	private boolean active = true;

	@Column(nullable = false)
	private boolean locked = false;

	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	private LocalDateTime updatedAt;

	/** Profile/selfie reference */
	@Column(length = 255)
	private String photoPath;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}

// @Entity
// @Table(name = "users", indexes = { @Index(name = "idx_user_email", columnList
// = "email"),
// @Index(name = "idx_user_role", columnList = "role") })
// public class User {
//
// @Id
// @GeneratedValue(strategy = GenerationType.IDENTITY)
// private Long id;
//
// @NotBlank
// @Email
// @Column(nullable = false, unique = true, length = 150)
// private String email;
//
// @NotBlank
// @Column(nullable = false, length = 100)
// private String name;
//
// @JsonIgnore
// @Column(nullable = false, length = 255)
// private String password;
//
// @Enumerated(EnumType.STRING)
// @Column(nullable = false, length = 30)
// private Role role;
//
// @Column(nullable = false)
// private boolean enabled = true;
//
// @Column(nullable = false)
// private boolean locked = false;
//
// @CreationTimestamp
// @Column(updatable = false)
// private LocalDateTime createdAt;
//
// @UpdateTimestamp
// private LocalDateTime updatedAt;
//
// public Long getId() {
// return id;
// }
//
// public void setId(Long id) {
// this.id = id;
// }
//
// public String getEmail() {
// return email;
// }
//
// public void setEmail(String email) {
// this.email = email;
// }
//
// public String getName() {
// return name;
// }
//
// public void setName(String name) {
// this.name = name;
// }
//
// public String getPassword() {
// return password;
// }
//
// public void setPassword(String password) {
// this.password = password;
// }
//
// public Role getRole() {
// return role;
// }
//
// public void setRole(Role role) {
// this.role = role;
// }
//
// public boolean isEnabled() {
// return enabled;
// }
//
// public void setEnabled(boolean enabled) {
// this.enabled = enabled;
// }
//
// public boolean isLocked() {
// return locked;
// }
//
// public void setLocked(boolean locked) {
// this.locked = locked;
// }
//
// public LocalDateTime getCreatedAt() {
// return createdAt;
// }
//
// public void setCreatedAt(LocalDateTime createdAt) {
// this.createdAt = createdAt;
// }
//
// public LocalDateTime getUpdatedAt() {
// return updatedAt;
// }
//
// public void setUpdatedAt(LocalDateTime updatedAt) {
// this.updatedAt = updatedAt;
// }
//
// }
// @Entity
// @Table(name = "users", indexes = { @Index(name = "idx_user_email", columnList
// = "email"),
// @Index(name = "idx_user_role", columnList = "role") })
// @Getter
// @Setter
// @NoArgsConstructor
// @AllArgsConstructor
// @Builder
// public class User {
//
// @Id
// @GeneratedValue(strategy = GenerationType.IDENTITY)
// private Long id;
//
// @Column(nullable = false, unique = true, length = 150, updatable = false)
// private String email;
//
// @Column(nullable = false, length = 100)
// private String name;
//
// @JsonIgnore
// @Column(nullable = false)
// private String password;
//
// @Enumerated(EnumType.STRING)
// @Column(nullable = false, length = 30)
// private Role role;
//
// @Column(nullable = false)
// private boolean enabled = true;
//
// @Column(nullable = false)
// private boolean locked = false;
//
// @CreationTimestamp
// @Column(updatable = false)
// private LocalDateTime createdAt;
//
// @UpdateTimestamp
// private LocalDateTime updatedAt;
// @Column(length = 255)
// private String photoPath;
//
// public Long getId() {
// return id;
// }
//
// public void setId(Long id) {
// this.id = id;
// }
//
// public String getEmail() {
// return email;
// }
//
// public void setEmail(String email) {
// this.email = email;
// }
//
// public String getName() {
// return name;
// }
//
// public void setName(String name) {
// this.name = name;
// }
//
// public String getPassword() {
// return password;
// }
//
// public void setPassword(String password) {
// this.password = password;
// }
//
// public Role getRole() {
// return role;
// }
//
// public void setRole(Role role) {
// this.role = role;
// }
//
// public boolean isEnabled() {
// return enabled;
// }
//
// public void setEnabled(boolean enabled) {
// this.enabled = enabled;
// }
//
// public boolean isLocked() {
// return locked;
// }
//
// public void setLocked(boolean locked) {
// this.locked = locked;
// }
//
// public LocalDateTime getCreatedAt() {
// return createdAt;
// }
//
// public void setCreatedAt(LocalDateTime createdAt) {
// this.createdAt = createdAt;
// }
//
// public LocalDateTime getUpdatedAt() {
// return updatedAt;
// }
//
// public void setUpdatedAt(LocalDateTime updatedAt) {
// this.updatedAt = updatedAt;
// }
//
// public String getPhotoPath() {
// return photoPath;
// }
//
// public void setPhotoPath(String photoPath) {
// this.photoPath = photoPath;
// }
//
// }
