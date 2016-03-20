package net.novazero.lib.ikbus;

import net.novazero.lib.ikbus.util.ByteString;

/**
 * Message used to encapsulate unrecognized I/K-bus messages which preserves the message contents.
 * 
 * @author  Daan Scheerens
 */
public class UnknownIKBusMessage extends BaseIKBusMessage {
	
	/** The data that is stored in the I/K-bus message. */
	public final ByteString data;

	/**
	 * Creates a new UnknownIKBusMessage with the specified properties.
	 * 
	 * @param  source       The source address of the I/K-bus message.
	 * @param  destination  The destination address of the I/K-bus message.
	 * @param  data         The data that is stored in the I/K-bus message.
	 */
	public UnknownIKBusMessage(byte source, byte destination, ByteString data) {
		super(source, destination);
		this.data = data;
	}
	
	/**
	 * Creates a new UnknownIKBusMessage for the specified packet.
	 * 
	 * @param  packet  The packet for which the I/K-bus message is to be created.
	 */
	public UnknownIKBusMessage(IKBusPacket packet) {
		this(packet.getSource(), packet.getDestination(), packet.getData());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IKBusPacket toPacket() {
		return new IKBusPacket(getSource(), getDestination(), data);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMessageTypeDescription() {
		return "Unknown message \"" + toPacket() + "\"";
	}
}
