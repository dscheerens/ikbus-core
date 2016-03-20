package net.novazero.lib.ikbus;

import java.util.ArrayList;
import java.util.List;

/**
 * An I/K-bus message parser that is composed of a set of of other parsers. When this parser is asked to parse an I/K-bus message from a given
 * packet it simply iterates over its set of parsers and asks each parser to parse the message. Once a parser returns a non-null value,
 * then that value will be returned by the compound message parser.
 * 
 * @author  Daan Scheerens
 */
public class CompoundIKBusMessageParser implements IKBusMessageParser {
	
	/** The list of parsers which are used by the compound parser. */
	private final List<IKBusMessageParser> parsers = new ArrayList<>();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IKBusMessage parseMessage(IKBusPacket packet) throws IKBusMessageParseException {
		for (IKBusMessageParser parser : parsers) {
			IKBusMessage message = parser.parseMessage(packet);
			if (message != null) {
				return message;
			}
		}
		return null;
	}
	
	/**
	 * Adds a new parser to the set of parsers that are utilized by the compound parser to parse I/K-bus message.
	 * 
	 * @param  parser  The parser that should be used by the compound I/K-bus message parser.
	 */
	protected void addParser(IKBusMessageParser parser) {
		parsers.add(parser);
	}
	
}
