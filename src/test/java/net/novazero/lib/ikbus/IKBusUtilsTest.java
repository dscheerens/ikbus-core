package net.novazero.lib.ikbus;

import static net.novazero.lib.ikbus.IKBusUtils.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class IKBusUtilsTest {
	
	public static class TestBytesFromString {

		@Test
		public void testNonEmptyString() {
			byte[] bytes = bytes("  00 01 \t 02\n0A 10 \r22 70\t\t7F\r\n \t80 81 92 A0 F0 f1 Fe ff  ");
			assertArrayEquals(new byte[] {
				(byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x0A,
				(byte) 0x10, (byte) 0x22, (byte) 0x70, (byte) 0x7F,
				(byte) 0x80, (byte) 0x81, (byte) 0x92, (byte) 0xA0,
				(byte) 0xF0, (byte) 0xF1, (byte) 0xFE, (byte) 0xFF
			}, bytes);
		}
	
		@Test
		public void testEmptyString() {
			assertArrayEquals(new byte[0], bytes(""));
			assertArrayEquals(new byte[0], bytes("\t"));
			assertArrayEquals(new byte[0], bytes(" \n \t \r "));
		}
		
		@Test(expected=NumberFormatException.class)
		public void testInvalidHexString() {
			bytes("this is a bad input string");
		}
		
		@Test(expected=NumberFormatException.class)
		public void testStringWithTooLargeHexCode() {
			bytes("100");
		}
		
	}
	
	public static class TestBytesFromIntArray {
		
		@Test
		public void testEmptyArray() {
			assertArrayEquals(new byte[0], bytes());
		}
		
		@Test
		public void testNonEmptyArray() {
			byte[] bytes = bytes(0x00, 0x01, 0x02, 0x0A, 0x10, 0x22, 0x70, 0x7F, 0x80, 0x81, 0x92, 0xA0, 0xF0, 0xF1, 0xFE, 0xFF);
			assertArrayEquals(new byte[] {
				(byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x0A,
				(byte) 0x10, (byte) 0x22, (byte) 0x70, (byte) 0x7F,
				(byte) 0x80, (byte) 0x81, (byte) 0x92, (byte) 0xA0,
				(byte) 0xF0, (byte) 0xF1, (byte) 0xFE, (byte) 0xFF
			}, bytes);
		}
		
	}
	
	public static class TestBytesToHex {
		
		@Test
		public void testEmptyArray() {
			assertEquals("", bytesToHex(new byte[0]));
		}
		
		@Test
		public void testNonEmptyArray() {
			assertEquals("00 01 02 0A 10 22 70 7F 80 81 92 A0 F0 F1 FE FF", bytesToHex(new byte[] {
				(byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x0A,
				(byte) 0x10, (byte) 0x22, (byte) 0x70, (byte) 0x7F,
				(byte) 0x80, (byte) 0x81, (byte) 0x92, (byte) 0xA0,
				(byte) 0xF0, (byte) 0xF1, (byte) 0xFE, (byte) 0xFF
			}));
		}
		
	}
}
