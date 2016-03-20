package net.novazero.lib.ikbus;

/**
 * I/K-bus message parser that serves as a fall-back in case its delegate parser is unable to parse a message. This message parser will
 * ensure that the parseMessage method never returns {@code null}.. In case the delegate parser is unable to parse a message, this class
 * will wrap the packet in an instance of {@link UnknownIKBusMessage}.
 * 
 * @author  Daan Scheerens
 */
public class UnknownIKBusMessageCatcher implements IKBusMessageParser {

	/** The delegate parser which is used for parsing messages. */
	protected final IKBusMessageParser parser;
	
	/**
	 * Creates a new UnknownIKBusMessageCatcher with the specified delegate parser.
	 * 
	 * @param  parser  The delegate parser which is used for parsing messages.
	 */
	public UnknownIKBusMessageCatcher(IKBusMessageParser parser) {
		this.parser = parser;
	}
	
	/**
	 * Parses I/K-bus message that is contained in the specified packet using the delegate parser. If this parser is unable to parse the
	 * message, then this method will wrap the packet in an UnknownIKBusMessage instance, thereby ensuring the result of this function is
	 * never {@code null}.
	 * 
	 * @param   packet                      The packet containing the I/K-bus message that is to be parsed.
	 * @return                              The message that was parsed from the given packet.
	 * @throws  IKBusMessageParseException  If an error occurs while the delegate parser is trying to parse a recognized packet.
	 */
	@Override
	public IKBusMessage parseMessage(IKBusPacket packet) throws IKBusMessageParseException {
		IKBusMessage message = parser.parseMessage(packet);
		return message == null ? new UnknownIKBusMessage(packet) : message;
	}
	
}
