package net.novazero.lib.ikbus.io;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;

import net.novazero.lib.ikbus.io.TimeRestrictedInputStream;

import org.junit.Test;

public class TimeRestrictedInputStreamTest {
	
	@Test(timeout=2000)
	public void testStream() throws IOException {
		// Create an input stream with simulated delays.
		InputStream inputStream = new InputStream() {
			
			private int numberOfReads = 0;
			
			@Override
			public int read() throws IOException {
				if (numberOfReads < 10) {
					long sleepTime = numberOfReads % 5 * 50;
					if (sleepTime > 0) {
						try {
							Thread.sleep(sleepTime);
						} catch (InterruptedException e) {
							throw new RuntimeException(e);
						}
					}
					return numberOfReads++;
				} else {
					return -1;
				}
			}
		};
		
		// Create a time restricted input stream.
		try (TimeRestrictedInputStream timedInputStream = new TimeRestrictedInputStream(inputStream)) {		
			// Check results from reading the input stream.
			assertEquals(0, timedInputStream.read(125));
			assertEquals(1, timedInputStream.read(125));
			assertEquals(2, timedInputStream.read(125));
			assertEquals(TimeRestrictedInputStream.TIMEOUT, timedInputStream.read(125));
			assertEquals(3, timedInputStream.read(125));
			assertEquals(TimeRestrictedInputStream.TIMEOUT, timedInputStream.read(125));
			assertEquals(4, timedInputStream.read(125));
			assertEquals(5, timedInputStream.read(175));
			assertEquals(6, timedInputStream.read(175));
			assertEquals(7, timedInputStream.read(175));
			assertEquals(8, timedInputStream.read(175));
			assertEquals(TimeRestrictedInputStream.TIMEOUT, timedInputStream.read(175));
			assertEquals(9, timedInputStream.read(175));
			assertEquals(TimeRestrictedInputStream.END_OF_STREAM, timedInputStream.read(0));
		}
	}
}
