package net.novazero.lib.ikbus.util;

import java.util.Iterator;

/**
 * An immutable sequence of bytes. This class can be used as a lightweight wrapper for byte arrays to guarantee read only access to the data.
 * 
 * @author  Daan Scheerens
 */
public class ByteArrayByteString implements Iterable<Byte>, ByteString {
	
	/** Array which holds the bytes that can be read. */
	private final byte[] bytes;

	/**
	 * Creates a new {@link ByteArrayByteString} from the specified byte array. Note that the byte array will be cloned in order to prevent
	 * external mutation of the array.
	 * 
	 * @param  bytes  An array of bytes that serves as the source data for the {@link ByteArrayByteString}.
	 */
	public ByteArrayByteString(byte[] bytes) {
		this.bytes = bytes.clone();
	}

	/**
	 * Creates a new {@link ByteArrayByteString} from the specified byte array. The <code>cloneArray</code> parameter controls whether a
	 * clone is made of the given byte array. In general it is recommended to do so as this guarantees immutability. However, if you are
	 * certain that the given array will not be modified outside the scope of this class, then it safe to use the original array. In that
	 * case you can pass <code>false</code> as an argument for the <code>cloneArray</code> parameter.
	 * 
	 * @param  bytes       An array of bytes that serves as the source data for the {@link ByteArrayByteString}.
	 * @param  cloneArray  Whether the array should be cloned to ensure immutability.
	 */
	public ByteArrayByteString(byte[] bytes, boolean cloneArray) {
		this.bytes = cloneArray ? bytes.clone() : bytes;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int length() {
		return bytes.length;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte get(int index) {
		if (index < 0 || index >= bytes.length) {
			throw new ArrayIndexOutOfBoundsException(index);
		}
		return bytes[index];
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] toArray() {
		return bytes.clone();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<Byte> iterator() {
		return new ByteStringIterator();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void copyTo(int sourceIndex, byte[] destination, int destinationIndex, int length) {
		System.arraycopy(bytes, sourceIndex, destination, destinationIndex, length);
	}
	
	/**
	 * An iterator for {@link ByteArrayByteString} instances.
	 */
	private class ByteStringIterator implements Iterator<Byte> {
		
		/**	Index of the {@link ByteArrayByteString} that should be retrieved next. */
		private int index = 0;

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasNext() {
			return index < bytes.length;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Byte next() {
			return bytes[index++];
		}

	}


}
