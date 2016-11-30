package br.com.app.spring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created on 22/01/2016.
 */
@ResponseStatus(code = HttpStatus.I_AM_A_TEAPOT, reason = "Error test")
public class SpringAppException extends RuntimeException {

	private static final long serialVersionUID = 1248400442848315205L;

	public SpringAppException() {
	}

	public SpringAppException(final String message) {
		super(message);
	}

	public SpringAppException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public SpringAppException(final Throwable cause) {
		super(cause);
	}

	public SpringAppException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
