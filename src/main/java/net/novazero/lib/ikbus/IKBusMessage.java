package net.novazero.lib.ikbus;

/**
 * Interface for classes that can represent I/K-bus messages.
 * 
 * @author  Daan Scheerens
 */
public interface IKBusMessage {

	/**
	 * Retrieves the source address of the I/K-bus message.
	 * 
	 * @return  The source address of the I/K-bus message.
	 */
	byte getSource();

	/**
	 * Retrieves the destination address of the I/K-bus message.
	 * 
	 * @return  The destination address of the I/K-bus message.
	 */
	byte getDestination();

	/**
	 * Converts the message to its packet representation.
	 * 
	 * @return  The packet that represents the I/K-bus message.
	 */
	IKBusPacket toPacket();

	/**
	 * Retrieves a short description of the I/K-bus message, that defines the type of content of the message.
	 * 
	 * @return  A short description of the I/K-bus message content.
	 */
	String getMessageTypeDescription();

	/**
	 * Retrieves a detailed description of the I/K-bus message that specifies the contents of the message (e.g. sensor values, status codes,
	 * etc.) Since not all messages can provide such a description, this method returns null when no detail description is available.
	 * 
	 * @return  The long description of the I/K-bus message or null if no such description is available.
	 */
	String getMessageDetailDescription();

}