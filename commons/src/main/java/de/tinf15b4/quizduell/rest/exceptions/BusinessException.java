package de.tinf15b4.quizduell.rest.exceptions;

public class BusinessException extends Exception {

	private static final long serialVersionUID = 5863095874865649190L;

	public BusinessException(String message, Throwable t) {
		super(message, t);
	}
	
	
	public BusinessException(String message) {
		super(message);
	}
	
}
