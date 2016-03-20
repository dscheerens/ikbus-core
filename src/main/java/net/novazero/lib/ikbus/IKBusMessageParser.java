package net.novazero.lib.ikbus;

/**
 * Interface for classes that can parse I/K-bus messages from a packet.
 * 
 * @author  Daan Scheerens
 */
public interface IKBusMessageParser {
	
	/**
	 * Attempts to parse the I/K-bus message that is represented by the given packet.
	 * 
	 * @param   packet                      The I/K-bus packet that is to be parsed.
	 * @return                              An IKBusMessage message if the given packet was parsed successfully or {@code null} if the
	 *                                      packet was not recognized/supported by the parser.
	 * @throws  IKBusMessageParseException  If an error occurs while parsing a recognized packet. This exception will not be thrown if the
	 *                                      parser does not recognize the packet. In that case this method will return {@code null}.
	 */
	IKBusMessage parseMessage(IKBusPacket packet) throws IKBusMessageParseException;
	
}
