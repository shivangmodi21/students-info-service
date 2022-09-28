package com.reactivespring.exception;

public class StudentNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private String message;
	private Throwable ex;

	public StudentNotFoundException(String message) {
		super();
		this.message = message;
	}

	public StudentNotFoundException(String message, Throwable ex) {
		super();
		this.message = message;
		this.ex = ex;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Throwable getEx() {
		return ex;
	}

	public void setEx(Throwable ex) {
		this.ex = ex;
	}

}
