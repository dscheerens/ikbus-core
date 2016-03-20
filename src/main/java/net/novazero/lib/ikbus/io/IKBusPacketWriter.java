package net.novazero.lib.ikbus.io;

import java.io.Closeable;

import net.novazero.lib.ikbus.IKBusPacket;

/**
 * Interface for classes that can provide a writer for a stream of I/K-bus packets.
 * 
 * @author  Daan Scheerens
 */
public interface IKBusPacketWriter extends Closeable {

	/**
	 * Attempts to write an I/K-bus packet to the stream.
	 * 
	 * @param   packet            I/K-bus packet that should be written.
	 * @throws  IKBusIOException  If there was an I/O exception while writing the packet to the underlying stream.
	 */
	void write(IKBusPacket packet) throws IKBusIOException;
	
	/**
	 * Closes the I/K-bus packet writer and releases any system resources that were allocated. After the stream has been closed calling the
	 * write() method will result in an exception.
	 * 
	 * @throws  IKBusIOException  If there was an I/O exception while closing the underlying stream or if the writer was already closed.
	 */
	void close() throws IKBusIOException;
	
}
