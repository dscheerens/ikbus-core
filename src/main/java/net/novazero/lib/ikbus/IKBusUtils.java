package net.novazero.lib.ikbus;

/**
 * Class which holds generic I/K-bus utility methods.
 * 
 * @author  Daan Scheerens
 */
public final class IKBusUtils {
	
	/** Private constructor used to prevent instantiation of the class. */
	private IKBusUtils() { }
	
	/**
	 * Helper function to convert a series of hex codes to an array of bytes. Each individual byte (represented by a hex code) is expected
	 * to be separated by whitespace. Using hex codes larger than 0xFF in the input string will result in a NumberFormatException.
	 * 
	 * @param   bytes                  The string containing the hexadecimal byte codes that is to be converted. 
	 * @return                         An array of bytes that was converted from the given hex code string.
	 * @throws  NumberFormatException  When the given byte string contains an invalid hex code.
	 */
	public static byte[] bytes(String bytes) {
		String trimmedBytes = bytes.trim();
		
		// Check if the string is empty. If so, simply return a zero-length array.
		if (trimmedBytes.isEmpty()) return new byte[0];
		
		// Split string on whitespace.
		String[] parts = trimmedBytes.split("\\s+");
		
		// Convert each part to a byte & store it in the result array.
		byte[] result = new byte[parts.length];
		int index = 0;
		for (String part : parts) {
			if (part.length() > 2) {
				throw new NumberFormatException("For input string: " + part);
			}
			result[index++] = (byte) Integer.parseInt(part, 16);
		}
		
		// Return result.
		return result;
	}
	
	/**
	 * Converts the given array of integers to an array of bytes. Any overflows are discarded. 
	 * 
	 * @param  bytes  The array of integers that is to be converted.
	 * @return        An array of bytes that corresponds with the given array of integers.
	 */
	public static byte[] bytes(int ... bytes) {
		byte[] result = new byte[bytes.length];
		for (int index = 0; index < bytes.length; index++) {
			result[index] = (byte) bytes[index];
		}
		return result;
	}
	
	/**
	 * Converts the given byte array to its hexadecimal representation as a string. The hexadecimals are written using upper-case
	 * characters and individual bytes are separated with a space character.
	 * 
	 * @param   bytes  The byte array which is to be converted to a hex string.
	 * @return         The string which represents the given byte array in hexadecimal format.
	 */
	public static String bytesToHex(byte[] bytes) {
		StringBuilder result = new StringBuilder();
		
		for (byte b : bytes) {
			if (result.length() > 0) {
				result.append(' ');
			}
			
			if ((b & 0xFF) <= 0x0F) {
				result.append('0');
			}
			result.append(Integer.toHexString(b & 0xFF).toUpperCase());
		}
		
		return result.toString();
	}
	
	/**
	 * Converts the given byte value to its hexadecimal representation as a string. The hexadecimals are written using upper-case characters.
	 * 
	 * @param   b  Byte which is to be converted to a hex string.
	 * @return     The string which represents the given byte in hexadecimal format.
	 */
	public static String byteToHex(byte b) {
		return ((b & 0xFF) < 0x0F ? "0" : "") + Integer.toHexString(b & 0xFF).toUpperCase();
	}
	
}
