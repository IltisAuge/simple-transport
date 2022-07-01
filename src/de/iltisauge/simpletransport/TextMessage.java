package de.iltisauge.simpletransport;

import java.io.InputStream;
import java.io.OutputStream;

public class TextMessage extends Message {
	
	private final String text;
	
	public TextMessage(String text) {
		this.text = text;
	}
	
	public static final MessageCodec<TextMessage> CODEC = new MessageCodec<TextMessage>() {
		
		@Override
		public void encode(OutputStream output, TextMessage message) {
			MessageUtil.write(output, message.text);
		}
		
		@Override
		public TextMessage decode(InputStream data) {
			return new TextMessage(MessageUtil.read(data));
		}
	};
}
