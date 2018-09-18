/**
 * 
 */
package com.baosong.supplyme.domain.errors;

/**
 * Application base level exception
 * All the exceptions defined in the application must inherit from this class
 * 
 * @author olivier
 *
 */
public abstract class AbstractApplicationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1289310724551895743L;

	/**
	 * 
	 */
	public AbstractApplicationException() {
	}

	/**
	 * @param message
	 */
	public AbstractApplicationException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public AbstractApplicationException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public AbstractApplicationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public AbstractApplicationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
