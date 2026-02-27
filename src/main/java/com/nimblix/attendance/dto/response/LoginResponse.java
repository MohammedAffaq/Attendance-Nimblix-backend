
package com.nimblix.attendance.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {

	private String token;
	private long expiresIn;
	private String role;   // ✅ NEW FIELD

//	public LoginResponse(String token, long expiresIn, String role) {
//		this.token = token;
//		this.expiresIn = expiresIn;
//		this.role = role;
//	}

	public String getToken() {
		return token;
	}

	public long getExpiresIn() {
		return expiresIn;
	}

	public String getRole() {
		return role;
	}
}
