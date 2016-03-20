package net.novazero.lib.ikbus.io;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * An input stream wrapper class used for creating an input stream whose read calls can timeout. Note that you always close your streams
 * after use to prevent resource leaks. This is especially true for the {@link TimeRestrictedInputStream} since it starts a daemon thread to
 * read from the underlying {@link InputStream}. Failing to close the {@link TimeRestrictedInputStream} may result in stale daemon threads.
 * 
 * @author  Daan Scheerens
 */
public class TimeRestrictedInputStream implements Closeable {
	
	/** Result returned by the read method if the end of the stream has been reached. */
	public static final int END_OF_STREAM = -1;
	
	/** Result returned by the read method if a timeout occurred while reading the next byte from the input stream. */
	public static final int TIMEOUT = -2;
	
	/** A buffer which holds the byte that will be returned upon the next read call. */
	private final Queue<InputStreamReadResult> buffer = new ArrayDeque<>(1);
	
	/** The input stream that is wrapped. */
	private final InputStream inputStream;
	
	/** The thread that performs the reading from the input stream. */
	private final Thread readerThread;
	
	/** Whether the end of the stream has been reached. */
	private boolean endOfStreamReached = false;
	
	/** Whether the stream has already been closed. */
	private boolean streamClosed = false;
	
	/** Default timeout to use for read calls with an unspecified timeout period. */
	private long defaultTimeout;
	
	/**
	 * Creates a new TimeRestrictedInputStream instance for the given input stream, with an indefinite default timeout period. 
	 * 
	 * @param  inputStream  The input stream from which a time restricted variant is to be created.
	 */
	public TimeRestrictedInputStream(final InputStream inputStream) {
		this(inputStream, 0);
	}
	
	/**
	 * Creates a new TimeRestrictedInputStream instance for the given input stream and with the specified default timeout period.
	 * 
	 * @param  inputStream     The input stream from which a time restricted variant is to be created.
	 * @param  defaultTimeout  Default timeout to use for read calls with an unspecified timeout period.
	 */
	public TimeRestrictedInputStream(final InputStream inputStream, long defaultTimeout) {
		this.inputStream = inputStream;
		this.defaultTimeout = defaultTimeout;
		
		// Create thread that will actually perform the reading from the input stream.
		this.readerThread = new Thread(new Runnable() {
			@Override public void run() {
				try {
					// Keep reading until the end of the stream has been reached.
					while (!endOfStreamReached) {
						// Wait until buffer is empty before reading next byte.
						synchronized(buffer) {
							while (!buffer.isEmpty()) buffer.wait();
						}
						
						// Attempt to read next byte from input stream.
						InputStreamReadResult readResult;
						int data = inputStream.read();
						if (data < 0) {
							endOfStreamReached = true;
							readResult = EndOfStreamReadResult.INSTANCE;
						} else {
							readResult = new DataReadResult((byte) data);
						}
						
						// Append reading result to the buffer.
						synchronized(buffer) {
							buffer.add(readResult);
							buffer.notify();
						}
					}
				} catch (Exception e) {
					// When an exception occurs while reading from the input stream, there is likely no way to recover, so quit the thread.
					synchronized(buffer) {
						endOfStreamReached = true;
						buffer.add(new ExceptionReadResult(e));
						buffer.notify();
					}
				}
			}
		});
		
		// Start reader thread.
		readerThread.setDaemon(true);
		readerThread.start();
	}
	
	/**
	 * Retrieves the default timeout to use for read calls with an unspecified timeout period.
	 * 
	 * @return  The default timeout to use for read calls with an unspecified timeout period.
	 */
	public long getDefaultTimeout() {
		return defaultTimeout;
	}
	
	/**
	 * Sets the default timeout to use for read calls with an unspecified timeout period.
	 * 
	 * @param  defaultTimeout  The default timeout to use for read calls with an unspecified timeout period.
	 */
	public void setDefaultTimeout(long defaultTimeout) {
		this.defaultTimeout = defaultTimeout;
	}

	/**
	 * Reads a byte from the input stream. If no byte was received within the default timeout period, then this method will quit and return
	 * the value of {@link #TIMEOUT}. Otherwise of the end of stream has been reached, this method returns {@link #END_OF_STREAM}.
	 * 
	 * @return               The byte that was read, {@link #END_OF_STREAM} if the end of the stream has been reached or {@link #TIMEOUT} if
	 *                       no byte was received within the specified amount of time.
	 * @throws  IOException  If an I/O occurs while reading from the stream.
	 */
	public int read() throws IOException {
		return read(defaultTimeout);
	}
	
	/**
	 * Reads a byte from the input stream. If no byte was received within the default timeout period, then this method will quit and return
	 * the value of {@link #TIMEOUT}. Otherwise of the end of stream has been reached, this method returns {@link #END_OF_STREAM}.
	 * 
	 * When a timeout period of 0 milliseconds is specified, then this will be interpreted as an infinite period and the read call will
	 * block indefinitely.
	 * 
	 * @param   timeout      The number of milliseconds to wait for a byte before timing out. If a timeout period of 0 milliseconds is
	 *                       specified, then this method waits indefinitely until a byte is available from the input stream.
	 * @return               The byte that was read, {@link #END_OF_STREAM} if the end of the stream has been reached or {@link #TIMEOUT} if
	 *                       no byte was received within the specified amount of time.
	 * @throws  IOException  If an I/O occurs while reading from the stream.
	 */
	public int read(long timeout) throws IOException {
		if (streamClosed) {
			throw new IOException("Attepted to read from closed input stream");
		}
		
		// Retrieve byte from the reader thread through the buffer.
		synchronized(buffer) {
			InputStreamReadResult result = buffer.poll();
			
			// Quit if the end of the stream has been reached.
			if (endOfStreamReached && result == null) {
				return END_OF_STREAM;
			}
			
			// If no byte is available wait until the the reader thread has read another byte.
			if (result == null) {
				
				// Signal reader thread to read another byte.
				buffer.notify();
				
				// Wait until the reader thread provides another byte or until a timeout occurs.
				try {
					buffer.wait(timeout);
					result = buffer.poll();
				} catch (InterruptedException e) {
					throw new IOException("Thread was interrupted while waiting for new data", e);
				}
			}
			
			// If the buffer is still empty assume that a timeout has occurred.
			if (result == null) {
				return TIMEOUT;
			}
			
			// Return byte if there was no error.
			if (result.isException()) {
				// An exception occurred, wrap exception if necessary and throw it.
				if (result.getException() instanceof IOException) {
					throw (IOException) result.getException();
				} else if (result.getException() instanceof RuntimeException) {
					throw (RuntimeException) result.getException();
				} else {
					throw new RuntimeException(result.getException());
				}
			} else if (result.isEndOfStream()) {
				// The end of the stream was reached.
				return END_OF_STREAM;
			} else {
				// Byte was successfully read.
				return result.getData() & 0xFF;
			}
			
		}
	}
	
	/**
	 * Closes the input stream. If the input stream was already closed, nothing will happen.
	 * 
	 * @throws IOException  If there was an I/O when closing the wrapped input stream.
	 */
	public void close() throws IOException {
		if (!streamClosed) {
			streamClosed = true;
			
			// Attempt the close the input stream. If an IOException is caught, temporary swallow it so that the reader thread can be
			// interrupted. Note that reader thread cannot be interrupt first, otherwise the input stream might not get a chance to close.
			IOException exceptionToThrow = null;
			try {
				inputStream.close();
			} catch (IOException e) {
				exceptionToThrow = e;
			}
			
			// Interrupt the reader thread in case it might have got stuck.
			readerThread.interrupt();
			
			// If an IOException occurred while closing the input stream then it should be thrown again now.  
			if (exceptionToThrow != null) {
				throw exceptionToThrow;
			}
		}
	}

	/**
	 * Data structure used to represent the read results produced from the thread that reads from the wrapped input stream.
	 */
	private static abstract class InputStreamReadResult {
		
		/**
		 * Checks whether the read result contains data.
		 * 
		 * @return  {@code true} if the read result contains data, {@code false} if not.
		 */
		@SuppressWarnings("unused")
		public boolean isData() {
			return false;
		}
		
		/**
		 * Retrieves the data stored in the read result. An {@link UnsupportedOperationException} is thrown when the {@link #isData()}
		 * method returns {@code false}.
		 * 
		 * @return  The byte that was read.
		 */
		public byte getData() {
			throw new UnsupportedOperationException("Read result is not data");
		}

		/**
		 * Checks whether the read result contains an exception.
		 * 
		 * @return  {@code true} if the read result contains an exception, {@code false} if not.
		 */
		public boolean isException() {
			return false;
		}

		/**
		 * Retrieves the exception stored in the read result. An {@link UnsupportedOperationException} is thrown when the
		 * {@link #isException()} method returns {@code false}.
		 * 
		 * @return  The exception that occurred.
		 */
		public Exception getException() {
			throw new UnsupportedOperationException("Read result is not an exception");
		}
		
		/**
		 * Checks whether the read result represents the end of stream.
		 * 
		 * @return  {@code true} if the read result represents the end of stream, {@code false} if not.
		 */
		public boolean isEndOfStream() {
			return false;
		}
	}
	
	/**
	 * An {@link InputStreamReadResult} implementation for storing data.
	 */
	private static class DataReadResult extends InputStreamReadResult {
		
		/** The byte that was read. */
		private final byte data;
		
		/**
		 * Creates a new {@link DataReadResult} instance for the specified data.
		 * 
		 * @param  data  The byte that was read.
		 */
		public DataReadResult(byte data) {
			this.data = data;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isData() {
			return true;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public byte getData() {
			return data;
		}
	}

	/**
	 * An {@link InputStreamReadResult} implementation for storing exceptions.
	 */
	private static class ExceptionReadResult extends InputStreamReadResult {
		
		/** The exception that occurred. */
		private final Exception exception;
		
		/**
		 * Creates new {@link ExceptionReadResult} for the specified exception.
		 * 
		 * @param  exception  The exception that occurred.
		 */
		public ExceptionReadResult(Exception exception) {
			this.exception = exception;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isException() {
			return true;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Exception getException() {
			return exception;
		}
	}
	
	/**
	 * An {@link InputStreamReadResult} implementation for storing end of stream tokens.
	 */
	private static class EndOfStreamReadResult extends InputStreamReadResult {
		
		/** The singleton instance of the {@link EndOfStreamReadResult} token. */
		public static final EndOfStreamReadResult INSTANCE = new EndOfStreamReadResult();
		
		/**
		 * Private constructor to enforce a single instance.
		 */
		private EndOfStreamReadResult() {
			
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isEndOfStream() {
			return true;
		}
		
	}
}
