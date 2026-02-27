package com.nimblix.attendance.entity;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;

@Getter
public class CustomUserDetails implements UserDetails {

	private final Long id;
	private final String name;
	private final String email;
	private final String password;
	private final Role role;
	private final boolean enabled;
	private final boolean locked;
	private final User user;

	 public CustomUserDetails(User user) {
		this.id = user.getId();
		this.name = user.getName();
		this.email = user.getEmail();
		this.password = user.getPassword();
		this.role = user.getRole();
		this.enabled = user.isEnabled();
		this.locked = user.isLocked();
		this.user =user;
	}

	public CustomUserDetails(Long id, String name, String email, String password, Role role, boolean enabled,
			boolean locked, User user) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
		this.enabled = enabled;
		this.locked = locked;
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return !locked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	// Add a getter for role if your AuthController needs it
	public Role getRole() {
		return role;
	}

	public User getUser() {
		return user;
	}

}
