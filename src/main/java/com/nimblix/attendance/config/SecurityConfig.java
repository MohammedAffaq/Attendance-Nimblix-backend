package com.nimblix.attendance.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.nimblix.attendance.security.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()).cors(Customizer.withDefaults())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.exceptionHandling(ex -> ex.authenticationEntryPoint(authenticationEntryPoint())
						.accessDeniedHandler(accessDeniedHandler()))
				.authorizeHttpRequests(auth -> auth

						// ✅ Public Endpoints
						.requestMatchers(
								"/api/auth/**",
								"/v3/api-docs/**",
								"/swagger-ui/**",
								"/swagger-ui.html"
						).permitAll()

						// ✅ Actuator (Admin Only)
						.requestMatchers("/actuator/**").hasRole("ADMIN")

						// ✅ Admin APIs
						.requestMatchers("/api/admin/**").hasRole("ADMIN")

						// ✅ Employee APIs
						.requestMatchers("/api/employee/**").hasAnyRole("EMPLOYEE", "ADMIN")

						// ✅ Everything else requires authentication
						.anyRequest().authenticated()
				)
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return (request, response, ex) -> {
			response.setStatus(401);
			response.setContentType("application/json");
			response.getWriter().write("{\"error\":\"Unauthorized\"}");
		};
	}

	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return (request, response, ex) -> {
			response.setStatus(403);
			response.setContentType("application/json");
			response.getWriter().write("{\"error\":\"Forbidden\"}");
		};
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {

		CorsConfiguration config = new CorsConfiguration();

		// ✅ Allow React frontend (development)
		config.setAllowedOrigins(List.of(
				"http://localhost:5173",
				"http://127.0.0.1:3000"
		));

		// ✅ Allow all standard HTTP methods
		config.setAllowedMethods(List.of(
				"GET",
				"POST",
				"PUT",
				"DELETE",
				"OPTIONS"
		));

		// ✅ Allow headers required for JWT + file upload
		config.setAllowedHeaders(List.of(
				"Authorization",
				"Content-Type",
				"Accept"
		));

		// ✅ Allow sending JWT token
		config.setAllowCredentials(true);

		// Optional (good practice)
		config.setMaxAge(3600L);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);

		return source;
	}
}
