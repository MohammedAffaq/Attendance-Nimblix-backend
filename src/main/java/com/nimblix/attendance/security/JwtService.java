package com.nimblix.attendance.security;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.nimblix.attendance.entity.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private final Key key;
	private final long expirationMs;
	private static final String ISSUER = "attendance-system";
	private static final String AUDIENCE = "attendance-api";

	public JwtService(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") long expirationMs) {
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		if (keyBytes.length < 32) {
			throw new IllegalStateException("JWT key must be at least 256 bits");
		}
		this.key = Keys.hmacShaKeyFor(keyBytes);
		this.expirationMs = expirationMs;
	}

	public String generateToken(String email, Role role) {
		return Jwts.builder().setSubject(email).setIssuer(ISSUER).setAudience(AUDIENCE)
				.addClaims(Map.of("role", role.name(), "type", "ACCESS")).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + expirationMs)).signWith(key).compact();
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		Claims claims = getClaims(token);
		return claims.getSubject().equals(userDetails.getUsername()) && !claims.getExpiration().before(new Date())
				&& "ACCESS".equals(claims.get("type")) && ISSUER.equals(claims.getIssuer())
				&& AUDIENCE.equals(claims.getAudience());
	}

	public String getEmail(String token) {
		return getClaims(token).getSubject();
	}

	public Role getRole(String token) {
		return Role.valueOf(getClaims(token).get("role", String.class));
	}

	private Claims getClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(key).requireIssuer(ISSUER).requireAudience(AUDIENCE).build()
				.parseClaimsJws(token).getBody();
	}

	public long getExpirationSeconds() {
		return expirationMs / 1000;
	}
}
