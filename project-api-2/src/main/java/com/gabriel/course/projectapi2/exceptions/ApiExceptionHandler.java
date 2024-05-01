package com.gabriel.course.projectapi2.exceptions;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
//Anotação lombok que faz a mesma função do @Autowired(injeta classes sem precisar inicializa-las)
@RequiredArgsConstructor
@RestControllerAdvice
public class ApiExceptionHandler {

	private final MessageSource messageSource;

	@ExceptionHandler({UsernameUniqueViolationException.class, CpfUniqueViolationException.class, CodeUniqueViolationException.class})
	public ResponseEntity<ErrorMessage> usernameUniqueViolationException(RuntimeException exception, 
																		HttpServletRequest request) {
		
		log.error("Api error!", exception);
		
		return ResponseEntity.
				status(HttpStatus.CONFLICT).
				contentType(MediaType.APPLICATION_JSON).
				body(new ErrorMessage(request, HttpStatus.CONFLICT, exception.getMessage()));
	}



	
	@ExceptionHandler(PasswordInvalidException.class)
	public ResponseEntity<ErrorMessage> passwordInvalidException(RuntimeException exception, 
			HttpServletRequest request) {
		
		log.error("Api error!", exception);
		
		return ResponseEntity.
				status(HttpStatus.BAD_REQUEST).
				contentType(MediaType.APPLICATION_JSON).
				body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, exception.getMessage()));
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorMessage> accessDeniedException(AccessDeniedException exception,
			HttpServletRequest request) {

		log.error("Api error!", exception);

		return ResponseEntity.
				status(HttpStatus.FORBIDDEN).
				contentType(MediaType.APPLICATION_JSON).
				body(new ErrorMessage(request, HttpStatus.FORBIDDEN, exception.getMessage()));
	}

	
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ErrorMessage> entityNotFoundException(RuntimeException exception, 
			HttpServletRequest request) {
		
		log.error("Api error!", exception);
		
		return ResponseEntity.
				status(HttpStatus.NOT_FOUND).
				contentType(MediaType.APPLICATION_JSON).
				body(new ErrorMessage(request, HttpStatus.NOT_FOUND, exception.getMessage()));
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorMessage> methodArgumentNotValidException(MethodArgumentNotValidException exception, 
			HttpServletRequest request, 
			BindingResult result) {
		
		log.error("Api error!", exception);
		
		return ResponseEntity.
				status(HttpStatus.UNPROCESSABLE_ENTITY).
				contentType(MediaType.APPLICATION_JSON).
				body(new ErrorMessage(request, HttpStatus.UNPROCESSABLE_ENTITY,
						messageSource.getMessage("message.invalid.field", null, request.getLocale()),
						result, messageSource));
	}
}
