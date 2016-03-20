package net.novazero.lib.ikbus.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import net.novazero.lib.ikbus.IKBusPacket;
import net.novazero.lib.ikbus.InvalidIKBusPacketException;

/**
 * An I/K-bus packet reader that reads and parses its data from an input stream. The input stream is assumed to provide live data and thus
 * the reader incorporates detection of errors due to timeouts.
 * 
 * @author  Daan Scheerens
 */
public class InputStreamIKBusPacketReader implements IKBusPacketReader {
	
	/**
	 * The timeout period in milliseconds for reading packets. A packet is considered incomplete when having to wait longer than this period
	 * for the next byte to be received.
	 */
	public static final int PACKET_TIMEOUT = 50;
	
	/** Minimal length of a valid I/K-bus packet. */
	private static final int MINIMAL_PACKET_LENGTH = 4;
	
	/** The time restricted input stream from which the packet data is read. */
	private final TimeRestrictedInputStream inputStream;
	
	/** Whether the end of the input stream has been reached. */
	private boolean endOfStreamReached = false;
	
	/** Indicates whether to suspend buffering until the packet buffer has been fully cleared. */
	private boolean suspendBuffering = false;
	
	/** A list which holds the bytes which cannot constitute a valid I/K-bus packet. */
	private List<Byte> corruptedData = new ArrayList<>(); 
	
	/** Buffer containing a sequence of bytes which have not been processed yet. */
	private LinkedList<Byte> packetBuffer = new LinkedList<>();
	
	/** Result that is to be returned at the next read call. */
	private IKBusPacketStreamElement pendingResult = null;
	
	/**
	 * Creates a new I/K-bus packet reader that uses the specified input stream as its data source.
	 * 
	 * @param  inputStream  The input stream from which the packet data is to be read.
	 */
	public InputStreamIKBusPacketReader(InputStream inputStream) {
		this.inputStream = new TimeRestrictedInputStream(inputStream, PACKET_TIMEOUT);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IKBusPacketStreamElement read() throws IKBusIOException {
		// Select pending result if there is any.
		IKBusPacketStreamElement result = pendingResult;
		pendingResult = null;
		
		// Attempt to obtain a stream element.
		try {
			// Continue reading until a result is available or no more data is available and the packet buffer cannot contain a valid packet.
			boolean noMoreDataAvailable = endOfStreamReached;
			suspendBuffering = suspendBuffering && packetBuffer.size() > 0;
			while (result == null && (!noMoreDataAvailable || packetBuffer.size() >= MINIMAL_PACKET_LENGTH)) {
				
				// Check for a valid packet sequence and purge bytes of the packet buffer if they cannot be part of a valid packet sequence.
				boolean moreDataRequired = false;
				while (!moreDataRequired && result == null) {
					// Start by assuming another byte needs to be read, until proved otherwise in the code below.
					moreDataRequired = true;
					
					// Only check packet buffer if the packet length byte is available.
					if (packetBuffer.size() > 1) {
						
						// Check whether the current packet sequence is (partially) valid.
						boolean isValidPacketSequence = true;
						int expectedMessageLength = (packetBuffer.get(1) & 0xFF) + 2;
						if (expectedMessageLength < MINIMAL_PACKET_LENGTH) {
							// The expected length of the packet is less than the minimal packet length, so this cannot be valid.
							isValidPacketSequence = false;
						} else if (packetBuffer.size() >= expectedMessageLength) {
							// Possible complete packet sequence found, compute checksum.
							byte data[] = new byte[expectedMessageLength];
							byte checksum = 0;
							for (int index = 0; index < expectedMessageLength; index++) {
								byte b = packetBuffer.get(index);
								checksum ^= b;
								data[index] = b;
							}
							
							// Check if checksum matches with the one in the packet.
							if (checksum == 0) {
								// A valid packet sequence was found, create stream element for this packet.
								IKBusPacketStreamElement packetStreamElement;
								try {
									packetStreamElement = IKBusPacketStreamElement.valid(IKBusPacket.parse(data));
								} catch (InvalidIKBusPacketException e) {
									// This exception will never be thrown, unless the code bugged and recovery is not possible. 
									throw new RuntimeException(e);
								}
								
								// Clear the packet buffer.
								for (int index = 0; index < expectedMessageLength; index++) {
									packetBuffer.remove();
								}
								
								// Return the packet unless it is preceded by corrupted data.
								if (corruptedData.size() > 0) {
									// Store packet as a pending result.
									pendingResult = packetStreamElement;
									
									// Create a stream element for the corrupted data and return that.
									result = IKBusPacketStreamElement.invalid(toByteArray(corruptedData));
									corruptedData.clear();
								} else {
									result = packetStreamElement;
								}
								
								// No more data is required for this read call to return a result.
								moreDataRequired = false;
								
							} else {
								// Checksum does not match, so this is an invalid packet sequence.
								isValidPacketSequence = false;
							}
						}
						
						// Purge first byte from packet buffer if it does not constitute to a valid packet sequence.
						if (!isValidPacketSequence) {
							corruptedData.add(packetBuffer.poll());
							moreDataRequired = false;
						}
					}
				}
				
				// Read next byte if more data is required.
				if (moreDataRequired) {
					if (noMoreDataAvailable || suspendBuffering) {
						// More data is required, but none is available or buffering is suspended.
						if (packetBuffer.size() > MINIMAL_PACKET_LENGTH) {
							corruptedData.add(packetBuffer.poll());
						} else {
							corruptedData.addAll(packetBuffer);
							packetBuffer.clear();
							if (corruptedData.size() > 0) {
								result = IKBusPacketStreamElement.invalid(toByteArray(corruptedData));
								corruptedData.clear();
							}
						}
					} else {
						// More data is available, so read next byte from input stream (without a timeout for the first byte of the packet).
						int byteRead = inputStream.read(packetBuffer.size() == 0 ? 0 : PACKET_TIMEOUT);
						
						// Check whether the read action succeeded or not.
						if (byteRead >= 0) {
							// A byte was successfully read, so append it to the packet buffer.
							packetBuffer.add((byte) byteRead);
						} else {
							// Next byte could not be read, so there is no more data available for this read call.
							noMoreDataAvailable = true;
							suspendBuffering = true;
							endOfStreamReached = (byteRead == TimeRestrictedInputStream.END_OF_STREAM);
							if (packetBuffer.size() > 0){
								corruptedData.add(packetBuffer.poll());
							}
						}
					}
				}
			}
			
			// Return corrupted data on timeouts.
			if (result == null && noMoreDataAvailable && (packetBuffer.size() > 0 || corruptedData.size() > 0)) {
				corruptedData.addAll(packetBuffer);
				packetBuffer.clear();
				result = IKBusPacketStreamElement.invalid(toByteArray(corruptedData));
				corruptedData.clear();
			}
		} catch (IOException e) {
			throw new IKBusIOException(e);
		}
		
		// Return result.
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() throws IKBusIOException {
		try {
			inputStream.close();
		} catch (IOException e) {
			throw new IKBusIOException(e);
		}
	}
	
	/**
	 * Converts the given collection of bytes to an array of bytes.
	 * 
	 * @param   bytes  The byte collection that is to be converted to an array of bytes.
	 * @return         An array that represents the given byte collection.
	 */
	private static byte[] toByteArray(Collection<Byte> bytes) {
		byte[] result = new byte[bytes.size()];
		
		int index = 0;
		
		for (byte b : bytes) {
			result[index++] = b;
		}
		
		return result;
	}
	
}
