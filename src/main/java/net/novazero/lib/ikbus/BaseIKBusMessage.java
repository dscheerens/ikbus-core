package net.novazero.lib.ikbus;

/**
 * Abstract base class for objects that represent I/K-bus messages.
 * 
 * @author  Daan Scheerens
 */
public abstract class BaseIKBusMessage implements IKBusMessage {
	
	/**	The source address of the I/K-bus message. */
	private final byte source;
	
	/**	The destination address of the I/K-bus message. */
	private final byte destination;
	
	/**
	 * Creates a new I/K-bus message with the specified source and destination.
	 * 
	 * @param  source       The source address of the I/K-bus message.
	 * @param  destination  The destination address of the I/K-bus message.
	 */
	public BaseIKBusMessage(byte source, byte destination) {
		this.source = source;
		this.destination = destination;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte getSource() {
		return source;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte getDestination() {
		return destination;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMessageDetailDescription() {
		return null; // Override in subclass if necessary.
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String detailDescription = getMessageDetailDescription();
		return getMessageTypeDescription() + (detailDescription == null ? "" : ": " + detailDescription);
	}

}
