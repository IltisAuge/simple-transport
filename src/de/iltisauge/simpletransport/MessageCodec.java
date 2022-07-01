package de.iltisauge.simpletransport;

import java.io.InputStream;
import java.io.OutputStream;

public abstract class MessageCodec<T> {
	
	public abstract void encode(OutputStream output, T message);
	
	public abstract T decode(InputStream data);

}
