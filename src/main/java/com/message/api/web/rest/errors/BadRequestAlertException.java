package com.message.api.web.rest.errors;

public class BadRequestAlertException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BadRequestAlertException(String defaultMessage) {
		super(defaultMessage);
	}

	@Override
	public String getMessage() {
		return super.getMessage();
	}
}
