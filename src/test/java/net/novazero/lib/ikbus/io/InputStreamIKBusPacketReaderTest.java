package net.novazero.lib.ikbus.io;

import static net.novazero.lib.ikbus.IKBusUtils.bytes;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.Queue;

import net.novazero.lib.ikbus.io.IKBusIOException;
import net.novazero.lib.ikbus.io.IKBusPacketReader;
import net.novazero.lib.ikbus.io.IKBusPacketStreamElement;
import net.novazero.lib.ikbus.io.InputStreamIKBusPacketReader;

import org.junit.Test;

/**
 * Tests for the InputStreamIKBusPacketReaderTest class.
 */
public class InputStreamIKBusPacketReaderTest {
	
	/**
	 * Tests whether a sequence of valid packets is read correctly.
	 * @throws IKBusIOException 
	 */
	@Test
	public void testValidPacketSequence() throws IKBusIOException {
		IKBusPacketReader reader = reader(
			data("50 03 C8 01 9A"),
			data("3F 06 00 0C 01 30 01 05"),
			data("80 0F FF 24 02 00 30 31 2E 30 36 2E 32 30 30 37 54"),
			data("3F 05 00 0C 52 01 65")
		);
		
		try {
			IKBusPacketStreamElement element;
			
			element = reader.read();
			assertArrayEquals(bytes("50 03 C8 01 9A"), element.getData());
			assertTrue(element.isValidPacket());
			assertEquals((byte) 0x50, element.getPacket().getSource());
			assertEquals((byte) 0xC8, element.getPacket().getDestination());
			assertArrayEquals(bytes("01"), element.getPacket().getData().toArray());
			
			element = reader.read();
			assertArrayEquals(bytes("3F 06 00 0C 01 30 01 05"), element.getData());
			assertTrue(element.isValidPacket());
			assertEquals((byte) 0x3F, element.getPacket().getSource());
			assertEquals((byte) 0x00, element.getPacket().getDestination());
			assertArrayEquals(bytes("0C 01 30 01"), element.getPacket().getData().toArray());
			
			element = reader.read();
			assertArrayEquals(bytes("80 0F FF 24 02 00 30 31 2E 30 36 2E 32 30 30 37 54"), element.getData());
			assertTrue(element.isValidPacket());
			assertEquals((byte) 0x80, element.getPacket().getSource());
			assertEquals((byte) 0xFF, element.getPacket().getDestination());
			assertArrayEquals(bytes("24 02 00 30 31 2E 30 36 2E 32 30 30 37"), element.getPacket().getData().toArray());		
			
			element = reader.read();
			assertArrayEquals(bytes("3F 05 00 0C 52 01 65"), element.getData());
			assertTrue(element.isValidPacket());
			assertEquals((byte) 0x3F, element.getPacket().getSource());
			assertEquals((byte) 0x00, element.getPacket().getDestination());
			assertArrayEquals(bytes("0C 52 01"), element.getPacket().getData().toArray());
			
			element = reader.read();
			assertNull(element);
		} finally {
			reader.close();
		}
	}
	
	/**
	 * Tests whether invalid packet data detected.
	 * @throws IKBusIOException 
	 */
	@Test
	public void testInvalidPacketSequence() throws IKBusIOException {
		IKBusPacketReader reader = reader(
			data("00 10 20 30 40 50 E0 70 80 90 AC B0 C0 D0 E0 F0"),
			data("01 11 21 A1 41 51 61 71 81 91 A1 B1 B1 D1 E1 F1"),
			data("02 12 22 32 42 52 62 72 82 92 A2 B2 C2 D2 E2 FF"),
			data("03 13 2F 33 43 53 63 73 83 93 A3 B3 C3 D3 E3 F3"),
			data("04 1E 24 34 B4 54 64 74 84 94 A4 B4 C4 D4 E4 F4"),
			data("C5 15 25 35 45 E5 65 75 A5 95 A5 B5 CB DC E5 F5"),
			data("06 16 26 36 46 56 66 76 86 96 A6 B6 C6 D6 E6 F6"),
			data("07 17 A7 37 47 57 67 77 87 9B A7 B7 C7 DF E7 F7")
		);
		
		try {
			IKBusPacketStreamElement element;
			
			element = reader.read();
			assertFalse(element.isValidPacket());
			assertEquals(16 * 8, element.getData().length);
			
			element = reader.read();
			assertNull(element);
		} finally {
			reader.close();
		}
	}
	
	/**
	 * Tests whether a mix of valid and invalid packet data results in the proper identification of I/K-bus packets and corrupted data.
	 * @throws IKBusIOException 
	 */
	@Test
	public void testMixedValidityPacketSequence() throws IKBusIOException {
		
		IKBusPacketReader reader = reader(
			data("3F 04 60 0C 80 D7"), // Valid 
			data("01 02 03 04 05 06"), // Invalid
			data("50 04 C8 3B A0 07"), // Valid
			data("BF 03 80 16 2A"),    // Valid
			data("FE DC BA 98 76 54"), // Invalid
			data("50 03 C8 01 9A")     // Valid
		);
		
		try {
			IKBusPacketStreamElement element;
			
			element = reader.read();
			assertArrayEquals(bytes("3F 04 60 0C 80 D7"), element.getData());
			assertTrue(element.isValidPacket());
			assertEquals((byte) 0x3F, element.getPacket().getSource());
			assertEquals((byte) 0x60, element.getPacket().getDestination());
			assertArrayEquals(bytes("0C 80"), element.getPacket().getData().toArray());
			
			element = reader.read();
			assertArrayEquals(bytes("01 02 03 04 05 06"), element.getData());
			assertFalse(element.isValidPacket());
			
			element = reader.read();
			assertArrayEquals(bytes("50 04 C8 3B A0 07"), element.getData());
			assertTrue(element.isValidPacket());
			assertEquals((byte) 0x50, element.getPacket().getSource());
			assertEquals((byte) 0xC8, element.getPacket().getDestination());
			assertArrayEquals(bytes("3B A0"), element.getPacket().getData().toArray());
			
			element = reader.read();
			assertArrayEquals(bytes("BF 03 80 16 2A"), element.getData());
			assertTrue(element.isValidPacket());
			assertEquals((byte) 0xBF, element.getPacket().getSource());
			assertEquals((byte) 0x80, element.getPacket().getDestination());
			assertArrayEquals(bytes("16"), element.getPacket().getData().toArray());
			
			element = reader.read();
			assertArrayEquals(bytes("FE DC BA 98 76 54"), element.getData());
			assertFalse(element.isValidPacket());
			
			element = reader.read();
			assertArrayEquals(bytes("50 03 C8 01 9A"), element.getData());
			assertTrue(element.isValidPacket());
			assertEquals((byte) 0x50, element.getPacket().getSource());
			assertEquals((byte) 0xC8, element.getPacket().getDestination());
			assertArrayEquals(bytes("01"), element.getPacket().getData().toArray());
			
			element = reader.read();
			assertNull(element);
		} finally {
			reader.close();
		}
	}
	
	/**
	 * Tests how the reader reacts to varying delays in the data stream and if it properly detects  timeouts.
	 * @throws IKBusIOException 
	 */
	@Test
	public void testTimeout() throws IKBusIOException {
		IKBusPacketReader reader = reader(
			// Valid, no delay
			data("72 05 BF 78 05 00 B5"),
			
			// Valid, no timeout
			data("72 05 BF 78"), delay(10), data("05 00 B5"),
			
			// Valid, but with timeout
			data("72 05 BF 78"), delay(InputStreamIKBusPacketReader.PACKET_TIMEOUT * 2), data("05 00 B5")
		);
		
		try {
			IKBusPacketStreamElement element;
			
			element = reader.read();
			assertArrayEquals(bytes("72 05 BF 78 05 00 B5"), element.getData());
			assertTrue(element.isValidPacket());
			assertEquals((byte) 0x72, element.getPacket().getSource());
			assertEquals((byte) 0xBF, element.getPacket().getDestination());
			assertArrayEquals(bytes("78 05 00"), element.getPacket().getData().toArray());
			
			element = reader.read();
			assertArrayEquals(bytes("72 05 BF 78 05 00 B5"), element.getData());
			assertTrue(element.isValidPacket());
			assertEquals((byte) 0x72, element.getPacket().getSource());
			assertEquals((byte) 0xBF, element.getPacket().getDestination());
			assertArrayEquals(bytes("78 05 00"), element.getPacket().getData().toArray());
			
			element = reader.read();
			assertArrayEquals(bytes("72 05 BF 78"), element.getData());
			assertFalse(element.isValidPacket());
			
			element = reader.read();
			assertArrayEquals(bytes("05 00 B5"), element.getData());
			assertFalse(element.isValidPacket());
			
			element = reader.read();
			assertNull(element);
		} finally {
			reader.close();
		}
	}
	
	/**
	 * Tests whether the reader will not timeout on the first byte that is read after a new packet is expected.
	 * @throws IKBusIOException 
	 */
	@Test
	public void testTimeoutOnStart() throws IKBusIOException {
		IKBusPacketReader reader = reader(
			delay(InputStreamIKBusPacketReader.PACKET_TIMEOUT * 2),
			data("80 04 BF 11 01 2B"), // Valid
			delay(InputStreamIKBusPacketReader.PACKET_TIMEOUT * 2),
			data("AB CD EF"), // Invalid
			delay(InputStreamIKBusPacketReader.PACKET_TIMEOUT * 2),
			delay(InputStreamIKBusPacketReader.PACKET_TIMEOUT * 2),
			data("3B 07 F0 43 80 03 01 00 0D"),
			delay(InputStreamIKBusPacketReader.PACKET_TIMEOUT * 2)
		);
		
		try {
			IKBusPacketStreamElement element;
			
			element = reader.read();
			assertArrayEquals(bytes("80 04 BF 11 01 2B"), element.getData());
			assertTrue(element.isValidPacket());
			assertEquals((byte) 0x80, element.getPacket().getSource());
			assertEquals((byte) 0xBF, element.getPacket().getDestination());
			assertArrayEquals(bytes("11 01"), element.getPacket().getData().toArray());
			
			element = reader.read();
			assertArrayEquals(bytes("AB CD EF"), element.getData());
			assertFalse(element.isValidPacket());
			
			element = reader.read();
			assertArrayEquals(bytes("3B 07 F0 43 80 03 01 00 0D"), element.getData());
			assertTrue(element.isValidPacket());
			assertEquals((byte) 0x3B, element.getPacket().getSource());
			assertEquals((byte) 0xF0, element.getPacket().getDestination());
			assertArrayEquals(bytes("43 80 03 01 00"), element.getPacket().getData().toArray());
			
			element = reader.read();
			assertNull(element);
		} finally {
			reader.close();
		}
	}
	
	// Private test support methods and classes. 
	
	private static IKBusPacketReader reader(ReaderElement ... stream) {
		return new InputStreamIKBusPacketReader(new SimulatedIKBusPacketDataStream(stream));
	}
	
	private static class SimulatedIKBusPacketDataStream extends InputStream {
		
		private Queue<ReaderElement> streamElements = new ArrayDeque<>();
		
		private Queue<Byte> queuedBytes = new ArrayDeque<>();
		
		public SimulatedIKBusPacketDataStream(ReaderElement ... streamElements) {
			for (ReaderElement streamElement : streamElements) {
				this.streamElements.add(streamElement);	
			}
		}

		@Override
		public int read() throws IOException {
			while (streamElements.size() > 0 && queuedBytes.size() == 0) {
				ReaderElement streamElement = streamElements.poll();
				
				if (streamElement instanceof DataReaderElement) {
					for (byte b : ((DataReaderElement)streamElement).data) {
						queuedBytes.add(b);
					}
				} else if (streamElement instanceof DelayReaderElement) {
					try {
						Thread.sleep(((DelayReaderElement)streamElement).delay);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				}
			}
			
			if (queuedBytes.size() > 0) {
				return queuedBytes.poll().byteValue() & 0xFF;
			}
			
			return -1;
		}
		
	}
	
	private interface ReaderElement {
		
	}
	
	private static DataReaderElement data(String data) {
		return new DataReaderElement(bytes(data));
	}
	
	private static class DataReaderElement implements ReaderElement {
		public final byte[] data;
		
		public DataReaderElement(byte ... data) {
			this.data = data;
		}
	}
	
	private static DelayReaderElement delay(long delay) {
		return new DelayReaderElement(delay);
	}
	
	private static class DelayReaderElement implements ReaderElement {
		public final long delay;
		
		public DelayReaderElement(long delay) {
			this.delay = delay;
		}
		
	}	
	
}
