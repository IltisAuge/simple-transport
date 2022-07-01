package de.iltisauge.simpletransport;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class MessageUtil {
	
	public static void write(OutputStream output, String string) {
		try {
			output.write(string.length());
			output.write(string.getBytes());
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
	
	public static String read(InputStream input) {
		try {
			final int length = input.read();
			final byte[] bytes = new byte[length];
			input.read(bytes);
			return new String(bytes);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		return null;
	}
}
