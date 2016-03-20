package net.novazero.lib.ikbus;

/**
 * Exception that is thrown when an {@link IKBusMessageParser} encounters an error while parsing a message from a packet it recognizes.
 * Note that this exception <b>should not</b> be thrown by a parser that does not support a particular type of packet. In such case the
 * parser should simply return {@code null} (see {@link IKBusMessageParser#parseMessage}). 
 * 
 * @author  Daan Scheerens
 */
public class IKBusMessageParseException extends RuntimeException {
	
	/** Version identifier used for serialization of objects of this type. */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new {@link IKBusMessageParseException} without any further details.
	 */
	public IKBusMessageParseException() {
	}

	/**
	 * Creates a new {@link IKBusMessageParseException} with the specified detail message.
	 * 
	 * @param  message  A message that gives a description of the exception. May be {@code null}.
	 */
	public IKBusMessageParseException(String message) {
		super(message);
	}

	/**
	 * Creates a new {@link IKBusMessageParseException} with the specified cause.
	 * 
	 * @param  cause  The error or exception that was the cause for the InvalidIKBusPacketException. May be {@code null}.
	 */
	public IKBusMessageParseException(Throwable cause) {
		super(cause);
	}

	/**
	 * Creates a new {@link IKBusMessageParseException} with the specified detail message and cause.
	 * 
	 * @param  message  A message that gives a description of the exception. May be {@code null}.
	 * @param  cause    The error or exception that was the cause for the InvalidIKBusPacketException. May be {@code null}.
	 */
	public IKBusMessageParseException(String message, Throwable cause) {
		super(message, cause);
	}

    /**
     * Creates a new runtime {@link IKBusMessageParseException} with the specified detail message, cause, suppression enabled or disabled,
     * and writable stack trace enabled or disabled.
     *
	 * @param  message             A message that gives a description of the exception. May be {@code null}.
	 * @param  cause               The error or exception that was the cause for the InvalidIKBusPacketException. May be {@code null}.
     * @param  enableSuppression   Whether suppression is enabled or disabled.
     * @param  writableStackTrace  Whether or not the stack trace should be writable.
     */
	public IKBusMessageParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
