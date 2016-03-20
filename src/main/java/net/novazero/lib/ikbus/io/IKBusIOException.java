package net.novazero.lib.ikbus.io;

import java.io.IOException;

/**
 * Exception that will be thrown when an error occurs while reading or writing I/K-bus data.
 * 
 * @author  Daan Scheerens
 */
public class IKBusIOException extends IOException {

	/** Version identifier used for serialization of objects of this type. */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new {@link IKBusIOException} without any further details.
	 */
	public IKBusIOException() {
		super();
	}
	
	/**
	 * Creates a new {@link IKBusIOException} with the specified detail message.
	 * 
	 * @param  message  A message that gives a description of the exception. May be {@code null}.
	 */
	public IKBusIOException(String message) {
		super(message);
	}
	
	/**
	 * Creates a new {@link IKBusIOException} with the specified cause.
	 * 
	 * @param  cause  The error or exception that was the cause for the IKBusIOException. May be {@code null}.
	 */
	public IKBusIOException(Throwable cause) {
		super(cause);
	}

	/**
	 * Creates a new {@link IKBusIOException} with the specified detail message and cause.
	 * 
	 * @param  message  A message that gives a description of the exception. May be {@code null}.
	 * @param  cause    The error or exception that was the cause for the IKBusIOException. May be {@code null}.
	 */
	public IKBusIOException(String message, Throwable cause) {
		super(message, cause);
	}
}
