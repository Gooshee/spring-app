package br.com.app.spring.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created on 22/01/2016.
 */
@Slf4j
@ControllerAdvice
public class SpringAppExceptionHandler {

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(RuntimeException.class)
	public void handleRuntimeException(final HttpServletRequest req, final HttpServletResponse resp, final RuntimeException ex) {
		log.error("Ocorreu um erro durante o processamento do parâmetro. {}", ex);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleIllegalArgumentException(final HttpServletRequest req, final HttpServletResponse resp, final IllegalArgumentException ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro durante o processamento do parâmetro. " + ex);
	}

}
