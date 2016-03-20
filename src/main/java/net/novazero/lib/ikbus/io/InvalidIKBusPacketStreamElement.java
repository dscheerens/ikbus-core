package net.novazero.lib.ikbus.io;

import net.novazero.lib.ikbus.IKBusPacket;

/**
 * Implementation of the {@link IKBusPacketStreamElement} interface to represent invalid I/K-bus packet stream elements.
 */
public class InvalidIKBusPacketStreamElement implements IKBusPacketStreamElement {
	
	/** Invalid sequence of bytes read from the I/K-bus communication link. */
	private final byte[] data;
	
	/**
	 * Creates a new {@link InvalidIKBusPacketStreamElement} instance for the specified data that represents an invalid packet.
	 * 
	 * @param  data  Invalid sequence of bytes read from the I/K-bus communication link.
	 */
	public InvalidIKBusPacketStreamElement(byte[] data) {
		this.data = data;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValidPacket() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] getData() {
		return data;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IKBusPacket getPacket() {
		throw new UnsupportedOperationException("Stream element does not contain a valid packet");
	}
	
}