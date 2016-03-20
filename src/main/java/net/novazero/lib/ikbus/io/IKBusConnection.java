package net.novazero.lib.ikbus.io;

import java.io.Closeable;

/**
 * Interface for classes that provide a communication link between the application and an I/K-bus.
 * 
 * @author  Daan Scheerens
 */
public interface IKBusConnection extends Closeable {

	/**
	 * Retrieves a reader that reads I/K-bus packets from the connection.
	 *  
	 * @return                    A reader that reads I/K-bus packets from the connection.
	 * @throws  IKBusIOException  If an I/O error occurs while retrieving the packet reader.
	 */
	IKBusPacketReader getPacketReader() throws IKBusIOException;

	/**
	 * Retrieves a writer that writes I/K-bus packets to the connection.
	 *  
	 * @return                    A writer that writes I/K-bus packets to the connection.
	 * @throws  IKBusIOException  If an I/O error occurs while retrieving the packet writer.
	 */
	IKBusPacketWriter getPacketWriter() throws IKBusIOException;
	
	/**
	 * Closes the communication link with the I/K-bus.
	 * 
	 * @throws  IKBusIOException  If an I/O error occurs while the communication link is closed.
	 */
	void close() throws IKBusIOException;
	
}
