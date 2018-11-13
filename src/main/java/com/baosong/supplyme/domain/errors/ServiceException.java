package com.baosong.supplyme.domain.errors;

public class ServiceException extends AbstractApplicationException {

	/**
	 *
	 */
    private static final long serialVersionUID = -5635854215631699865L;

    /**
     * Message key to send client-side
     */
    private String keyCode = "unexpected";

    /**
     * @return the keyCode
     */
    public String getKeyCode() {
        return keyCode;
    }

	public ServiceException() {
	}

	public ServiceException(String message, String keyCode) {
        super(message);
        this.keyCode = keyCode;
	}

	public ServiceException(Throwable cause) {
        super(cause);
        if (cause instanceof ServiceException) {
            this.keyCode = ((ServiceException) cause).getKeyCode();
        }
	}

    public ServiceException(Throwable cause, String keyCode) {
        super(cause);
        this.keyCode = keyCode;
	}

	public ServiceException(String message, Throwable cause) {
        super(message, cause);
        if (cause instanceof ServiceException) {
            this.keyCode = ((ServiceException) cause).getKeyCode();
        }
    }

    public ServiceException(String message, Throwable cause, String keyCode) {
        super(message, cause);
        this.keyCode = keyCode;
	}

	public ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
