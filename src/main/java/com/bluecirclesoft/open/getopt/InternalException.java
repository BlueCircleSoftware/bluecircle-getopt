package com.bluecirclesoft.open.getopt;

/**
 * Exception that represents some sort of unexpected error condition (almost like an assertion)
 */
public class InternalException extends RuntimeException {

	public InternalException(String message) {
		super(message);
	}

	public InternalException(String message, Throwable cause) {
		super(message, cause);
	}
}
