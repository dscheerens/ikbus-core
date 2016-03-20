package net.novazero.lib.ikbus.io;

import java.io.Closeable;

/**
 * Interface for classes that can provide a reader for a stream of I/K-bus packets.
 * 
 * @author  Daan Scheerens
 */
public interface IKBusPacketReader extends Closeable {

	/**
	 * Attempts to read an I/K-bus packet from the stream. The packet is wrapped in an IKBusPacketStreamElement instance. This allows this
	 * method to return the data of both valid and corrupted packets, since corrupted packets cannot be represented by an IKBusPacket
	 * instance. Corrupted packets may occur due to noise or contention on the bus. If the end of the stream has been reached this method
	 * will return {@code null}.
	 * 
	 * @return                    An IKBusPacketStreamElement instance that represents the I/K-bus packet that was read (which may be
	 *                            corrupted) or {@code null} if the end of the stream has been reached.
	 * @throws  IKBusIOException  If there was an I/O exception while reading the packet from the underlying stream.          
	 */
	IKBusPacketStreamElement read() throws IKBusIOException;
	
	/**
	 * Closes the I/K-bus packet reader and releases any system resources that were allocated. After the stream has been closed calling the
	 * read() method will result in an exception.
	 * 
	 * @throws  IKBusIOException  If there was an I/O exception while closing the underlying data source or if the reader has already been
	 *                            closed.
	 */
	void close() throws IKBusIOException;
	
}
