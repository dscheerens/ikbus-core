package net.novazero.lib.ikbus.io;

import net.novazero.lib.ikbus.IKBusPacket;

/**
 * Implementation of the {@link IKBusPacketStreamElement} interface to represent valid I/K-bus packet stream elements.
 */
public class ValidIKBusPacketStreamElement implements IKBusPacketStreamElement {
	
	/** Packet that was read from the I/K-bus communication link. */
	private final IKBusPacket packet;
	
	/**
	 * Creates a new {@link ValidIKBusPacketStreamElement} instance for the specified packet.
	 * 
	 * @param  packet  Packet that was read from the I/K-bus communication link.
	 */
	public ValidIKBusPacketStreamElement(IKBusPacket packet) {
		this.packet = packet;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValidPacket() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] getData() {
		return packet.toRaw();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IKBusPacket getPacket() {
		return packet;
	}
	
}