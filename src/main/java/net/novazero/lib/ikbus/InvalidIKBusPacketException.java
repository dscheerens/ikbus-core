package net.novazero.lib.ikbus;

/**
 * Exception that will be thrown when an array of bytes does not represent a valid I/K-bus packet.
 * 
 * @author  Daan Scheerens
 */
public class InvalidIKBusPacketException extends Exception {

	/** Version identifier used for serialization of objects of this type. */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new {@link InvalidIKBusPacketException} without any further details.
	 */
	public InvalidIKBusPacketException() {
		super();
	}
	
	/**
	 * Creates a new {@link InvalidIKBusPacketException} with the specified detail message.
	 * 
	 * @param  message  A message that gives a description of the exception. May be {@code null}.
	 */
	public InvalidIKBusPacketException(String message) {
		super(message);
	}
	
	/**
	 * Creates a new {@link InvalidIKBusPacketException} with the specified cause.
	 * 
	 * @param  cause  The error or exception that was the cause for the InvalidIKBusPacketException. May be {@code null}.
	 */
	public InvalidIKBusPacketException(Throwable cause) {
		super(cause);
	}

	/**
	 * Creates a new {@link InvalidIKBusPacketException} with the specified detail message and cause.
	 * 
	 * @param  message  A message that gives a description of the exception. May be {@code null}.
	 * @param  cause    The error or exception that was the cause for the InvalidIKBusPacketException. May be {@code null}.
	 */
	public InvalidIKBusPacketException(String message, Throwable cause) {
		super(message, cause);
	}
	
    /**
     * Creates a new runtime {@link InvalidIKBusPacketException} with the specified detail message, cause, suppression enabled or disabled,
     * and writable stack trace enabled or disabled.
     *
	 * @param  message             A message that gives a description of the exception. May be {@code null}.
	 * @param  cause               The error or exception that was the cause for the InvalidIKBusPacketException. May be {@code null}.
     * @param  enableSuppression   Whether suppression is enabled or disabled.
     * @param  writableStackTrace  Whether or not the stack trace should be writable.
     */
	public InvalidIKBusPacketException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
