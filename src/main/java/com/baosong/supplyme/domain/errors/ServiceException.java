package com.baosong.supplyme.domain.errors;

public class ServiceException extends AbstractApplicationException {

	/**
	 *
	 */
    private static final long serialVersionUID = -5635854215631699865L;

	public ServiceException() {
	}

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
