package net.novazero.lib.ikbus.io;

import net.novazero.lib.ikbus.IKBusPacket;

/**
 * Interface used to represent data elements read from an I/K-bus communication link. Each data stream element is a sequence of bytes that
 * that either represents a valid I/K-bus packet or an invalid sequence of bytes (due to a read timeout or a checksum mismatch caused by bus
 * contention). 
 * 
 * @author  Daan Scheerens
 */
public interface IKBusPacketStreamElement {
	
	/**
	 * Checks whether the element represents a valid packet.
	 * 
	 * @return  {@code true} if the element stores a valid I/K-bus packet, {@code false} if not.
	 */
	boolean isValidPacket();
	
	/**
	 * Retrieves the data that constitutes the stream element. In case the stream element does not represent a valid I/K-bus packet this
	 * method returns the raw sequence of bytes that was read from the I/K-bus communication link. Calling this method when
	 * {@link #isValidPacket} returns {@code true} returns in the raw packet data (see {@link IKBusPacket#toRaw}.  
	 * 
	 * @return  The raw data of the stream element that was read from the I/K-bus communication link.
	 */
	byte[] getData();
	
	/**
	 * Returns the I/K-bus packet that is stored the stream element. This method should only be called if {@link #isValidPacket} returns
	 * {@code true}. If the stream element represents an invalid I/K-bus packet, this method will throw an
	 * {@link UnsupportedOperationException}.
	 *  
	 * @return  The I/K-bus packet that is stored the stream element.
	 */
	IKBusPacket getPacket();
	
	/**
	 * Convenience method for creating an {@link IKBusPacketStreamElement} instance that represents a valid I/K-bus packet.
	 *  
	 * @param   packet  The I/K-bus packet for which the {@link IKBusPacketStreamElement} is to be created. 
	 * @return          An {@link IKBusPacketStreamElement} instance that represents a valid I/K-bus packet.
	 */
	public static IKBusPacketStreamElement valid(IKBusPacket packet) {
		return new ValidIKBusPacketStreamElement(packet);
	}
	
	/**
	 * Convenience method for creating an {@link IKBusPacketStreamElement} instance that represents a invalid I/K-bus packet.
	 * 
	 * @param   data  The raw (invalid packet) data that was read from the I/K-bus communication link. 
	 * @return        An {@link IKBusPacketStreamElement} instance that represents a valid I/K-bus packet.
	 */
	public static IKBusPacketStreamElement invalid(byte[] data) {
		return new InvalidIKBusPacketStreamElement(data);
	}
	
}
