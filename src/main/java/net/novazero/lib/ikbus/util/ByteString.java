package net.novazero.lib.ikbus.util;

import java.util.Iterator;

/**
 * Interface definition for immutable byte strings.
 * 
 * @author  Daan Scheerens
 */
public interface ByteString {

	/**
	 * Retrieves the length of the {@link ByteString}.
	 * 
	 * @return  Number of bytes which are stored in the {@link ByteString}.
	 */
	int length();

	/**
	 * Retrieves the byte at the specified index in the {@link ByteString}.
	 * 
	 * @param   index                           Index of the byte which is to be retrieved.
	 * @return                                  The byte at the specified index.
	 * @throws  ArrayIndexOutOfBoundsException  If the specified index is not valid: {@code index < 0 || index >= this.length()}
	 */
	byte get(int index);

	/**
	 * Converts the ByteString to an array of bytes.
	 * 
	 * @return  An array of bytes that contains the same bytes stored in the {@link ByteString}
	 */
	byte[] toArray();

	/**
	 * Returns an {@link Iterator} that iterates over the bytes in the {@link ByteString}.
	 * 
	 * @return  An {@link Iterator} for the bytes in the byte string.
	 */
	Iterator<Byte> iterator();

	/**
	 * Copies the bytes from the {@link ByteString}, starting at the specified position, to the specified position of the destination array.
	 * This method is actually a special case of {@link System#arraycopy} which uses the {@link ByteString} as source.
	 * 
	 * @param   sourceIndex                Starting position in the source {@link ByteString}.
	 * @param   destination                The destination array.
	 * @param   destinationIndex           Starting position in the destination data.
	 * @param   length                     Number of bytes that should be copied.
	 * @throws  IndexOutOfBoundsException  If copying would cause access of data outside array bounds.
	 */
	void copyTo(int sourceIndex, byte[] destination, int destinationIndex, int length);

}