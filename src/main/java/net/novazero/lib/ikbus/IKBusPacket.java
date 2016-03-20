package net.novazero.lib.ikbus;

import net.novazero.lib.ikbus.util.ByteArrayByteString;
import net.novazero.lib.ikbus.util.ByteString;

/**
 * Class for describing I/K-bus packets in their most basic form. This means that it describes a packet in terms of a source address, a
 * destination address and it's data. It excludes the length and checksum found in the raw packet format, since these can be computed from
 * the other fields.
 * 
 * @author  Daan Scheerens
 */
public class IKBusPacket {

	/** Source identifier of the packet. */
	private final byte source;
	
	/** Destination identifier of the packet. */
	private final byte destination;
	
	/** The packet contents. */
	private final ByteString data;
	
	/**
	 * Creates a new I/K-bus packet with the specified source, destination and data.
	 * 
	 * @param  source       Source identifier of the packet.
	 * @param  destination  Destination identifier of the packet.
	 * @param  data         The packet contents.
	 */
	public IKBusPacket(byte source, byte destination, byte[] data) {
		this(source, destination, new ByteArrayByteString(data));
	}
	
	/**
	 * Creates a new I/K-bus packet with the specified source, destination and data.
	 * 
	 * @param  source       Source identifier of the packet.
	 * @param  destination  Destination identifier of the packet.
	 * @param  data         The packet contents.
	 */
	public IKBusPacket(byte source, byte destination, ByteString data) {
		this.source = source;
		this.destination = destination;
		this.data = data;
	}
	
	/**
	 * Retrieves the source identifier of the packet.
	 * 
	 * @return  The source identifier of the packet.
	 */
	public byte getSource() {
		return source;
	}
	
	/**
	 * Retrieves the destination identifier of the packet.
	 * 
	 * @return  The destination identifier of the packet.
	 */
	public byte getDestination() {
		return destination;
	}
	
	/**
	 * Retrieves the packet contents.
	 * 
	 * @return  The packet contents.
	 */
	public ByteString getData() {
		return data;
	}
	
	
	/**
	 * Converts the I/K-bus packet to its raw format that represents the packet as it would have been transmitted on the I/K-bus.
	 * 
	 * @return  An array that represents the raw packet.
	 */
	public byte[] toRaw() {
		// Create packet array.
		byte[] packet = new byte[data.length() + 4];
		
		// Set source, packet length, destination and data.
		packet[0] = source;
		packet[1] = (byte) (data.length() + 2);
		packet[2] = destination;
		data.copyTo(0, packet, 3, data.length());
		
		// Compute & set checksum.
		byte checksum = 0;
		for (int index = 0; index < packet.length - 1; index++) {
			checksum = (byte) (checksum ^ packet[index]);
		}
		packet[packet.length - 1] = checksum;
		
		// Return the packet.
		return packet;
	}
	
	/**
	 * Converts the I/K-bus packet to it's string representation. This is done by converting it to a hexadecimal notation the of the raw
	 * packet data.
	 * 
	 * @return  A string representation of the I/K-bus packet.
	 */
	@Override
	public String toString() {
		return IKBusUtils.bytesToHex(toRaw());
	}
	
	/**
	 * Attempts to parse the I/K-bus packet that is represented by the given byte array.
	 * 
	 * @param   rawPacketData                A byte array that contains the raw packet data which is to be parsed.
	 * @return                               An IKBusPacket instance that represents the packet for the given packet data.
	 * @throws  InvalidIKBusPacketException  If the packet data does not represent a valid I/K-bus packet.
	 */
	public static IKBusPacket parse(byte[] rawPacketData) throws InvalidIKBusPacketException {
		// Check packet length.
		if (rawPacketData.length < 5) {
			throw new InvalidIKBusPacketException("Packet size should be at least 5 bytes, got " + rawPacketData.length + " instead");
		}
		int messageDataLength = (rawPacketData[1] & 0xff) - 2;
		int expectedTotalPacketSize = messageDataLength + 4;
		if (rawPacketData.length != expectedTotalPacketSize) {
			throw new InvalidIKBusPacketException(
				"Packet length byte indicates a total packet size of " + expectedTotalPacketSize +
				" bytes, got " + rawPacketData.length + " bytes instead");
		}
		
		// Verify checksum.
		byte computedChecksum = 0;
		for (int index = 0; index < rawPacketData.length - 1; index++) {
			computedChecksum = (byte) (computedChecksum ^ rawPacketData[index]);
		}
		byte actualChecksum = rawPacketData[rawPacketData.length - 1]; 
		if (actualChecksum != computedChecksum) {
			throw new InvalidIKBusPacketException(
				"Invalid checksum, computed 0x" + IKBusUtils.byteToHex(computedChecksum) +
				", got 0x" + IKBusUtils.byteToHex(actualChecksum) + " instead"
			);
		}
		
		// All checks passed, so create the packet.
		byte[] messageData = new byte[messageDataLength];
		System.arraycopy(rawPacketData, 3, messageData, 0, messageDataLength);
		return new IKBusPacket(rawPacketData[0], rawPacketData[2], new ByteArrayByteString(messageData, false));
	}
}
