package net.novazero.lib.ikbus;

import static org.junit.Assert.*;
import static net.novazero.lib.ikbus.IKBusUtils.bytes;

import org.junit.Test;

public class IKBusPacketTest {
	
	@Test
	public void testConvertToPacket() {
		assertArrayEquals(bytes("00 02 00 02"),                               new IKBusPacket((byte) 0x00, (byte) 0x00, bytes("")).toRaw());
		assertArrayEquals(bytes("50 04 68 32 11 1F"),                         new IKBusPacket((byte) 0x50, (byte) 0x68, bytes("32 11")).toRaw());
		assertArrayEquals(bytes("50 03 C8 01 9A"),                            new IKBusPacket((byte) 0x50, (byte) 0xC8, bytes("01")).toRaw());
		assertArrayEquals(bytes("80 0A BF 13 02 00 00 00 00 00 38 1C"),       new IKBusPacket((byte) 0x80, (byte) 0xBF, bytes("13 02 00 00 00 00 00 38")).toRaw());
		assertArrayEquals(bytes("00 04 BF 72 22 EB"),                         new IKBusPacket((byte) 0x00, (byte) 0xBF, bytes("72 22")).toRaw());
		assertArrayEquals(bytes("F0 03 68 01 9A"),                            new IKBusPacket((byte) 0xF0, (byte) 0x68, bytes("01")).toRaw());
		assertArrayEquals(bytes("F0 0B 68 39 00 02 00 01 00 01 00 00 A8"),    new IKBusPacket((byte) 0xF0, (byte) 0x68, bytes("39 00 02 00 01 00 01 00 00")).toRaw());
		assertArrayEquals(bytes("44 05 BF 74 00 FF 75"),                      new IKBusPacket((byte) 0x44, (byte) 0xBF, bytes("74 00 FF")).toRaw());
		assertArrayEquals(bytes("80 0C FF 24 01 00 20 38 3A 33 37 20 20 70"), new IKBusPacket((byte) 0x80, (byte) 0xFF, bytes("24 01 00 20 38 3A 33 37 20 20")).toRaw());
		assertArrayEquals(bytes("80 04 BF 11 03 29"),                         new IKBusPacket((byte) 0x80, (byte) 0xBF, bytes("11 03")).toRaw());
		assertArrayEquals(bytes("F0 04 FF 48 88 CB"),                         new IKBusPacket((byte) 0xF0, (byte) 0xFF, bytes("48 88")).toRaw());
	}

	@Test
	public void testParse() throws InvalidIKBusPacketException {
		IKBusPacket packet;
		
		// Test parsing of valid messages.
		packet = IKBusPacket.parse(bytes("F0 04 3B 49 81 07"));
		assertEquals((byte) 0xF0, packet.getSource());
		assertEquals((byte) 0x3B, packet.getDestination());
		assertArrayEquals(bytes("49 81"), packet.getData().toArray());
		
		packet = IKBusPacket.parse(bytes("80 0A BF 13 02 10 00 00 00 00 38 0C"));
		assertEquals((byte) 0x80, packet.getSource());
		assertEquals((byte) 0xBF, packet.getDestination());
		assertArrayEquals(bytes("13 02 10 00 00 00 00 38"), packet.getData().toArray());
		
		// Test parsing of invalid messages.
		try {
			IKBusPacket.parse(bytes(""));
			fail("Expecting an exception of class " + InvalidIKBusPacketException.class.getCanonicalName() + " but none was thrown.");
		} catch (InvalidIKBusPacketException e) { }
		
		try {
			IKBusPacket.parse(bytes("00"));
			fail("Expecting an exception of class " + InvalidIKBusPacketException.class.getCanonicalName() + " but none was thrown.");
		} catch (InvalidIKBusPacketException e) { }
		
		try {
			IKBusPacket.parse(bytes("00 11 22 33 44"));
			fail("Expecting an exception of class " + InvalidIKBusPacketException.class.getCanonicalName() + " but none was thrown.");
		} catch (InvalidIKBusPacketException e) { }
		
		try {
			IKBusPacket.parse(bytes("01 03 05 07 09"));
			fail("Expecting an exception of class " + InvalidIKBusPacketException.class.getCanonicalName() + " but none was thrown.");
		} catch (InvalidIKBusPacketException e) { }	
	}
}
