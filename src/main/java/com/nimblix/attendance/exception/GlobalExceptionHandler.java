package com.nimblix.attendance.exception;


import java.time.LocalDateTime;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body(new ErrorResponse(403, "Forbidden", ex.getMessage(), LocalDateTime.now()));
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(400, "Bad Request",
				"Invalid value for parameter: " + ex.getName(), LocalDateTime.now()));
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<ErrorResponse> handleMissingParam(MissingServletRequestParameterException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(400, "Bad Request", ex.getMessage(), LocalDateTime.now()));
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(new ErrorResponse(
						HttpStatus.UNAUTHORIZED.value(),
						"Unauthorized",
						"Invalid email or password",
						LocalDateTime.now()
				));
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex) {

		ErrorResponse error = new ErrorResponse(
				HttpStatus.BAD_REQUEST.value(),
				"Bad Request",
				ex.getMessage(),
				LocalDateTime.now()
		);

		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}


	// ⛔ LAST — generic handler
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
		ex.printStackTrace(); // VERY IMPORTANT for debugging
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ErrorResponse(500, "Internal Server Error", ex.getMessage(), LocalDateTime.now()));
	}
}
