package com.mentorstudies.automationframework.exception;

public class KeywordNotFoundException extends AutomationFrameworkException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6305119911413869375L;

	public KeywordNotFoundException() {
		// TODO Auto-generated constructor stub
	}

	public KeywordNotFoundException(String message) {
		super("Keyword :" + message + " not found in Keywords file");
	}

	public KeywordNotFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public KeywordNotFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public KeywordNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
