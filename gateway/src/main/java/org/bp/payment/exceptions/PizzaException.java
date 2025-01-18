package org.bp.payment.exceptions;

public class PizzaException extends Exception {

	public PizzaException() {
	}

	public PizzaException(String message) {
		super(message);
	}

	public PizzaException(Throwable cause) {
		super(cause);
	}

	public PizzaException(String message, Throwable cause) {
		super(message, cause);
	}

	public PizzaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
