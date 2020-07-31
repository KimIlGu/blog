package com.sbs.java.blog.exception;

public class SQLErrorException extends RuntimeException {
	private Exception origin;

	public SQLErrorException(String message, Exception origin) {
		super(message);
		System.out.println("SQLErrorException 생성자");
		
		this.origin = origin;
	}

	public Exception getOrigin() {
		return origin;
	}
}