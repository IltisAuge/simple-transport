package de.iltisauge.simpletransport;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ServerMessageWrapper extends Message {
	
	private final String messageClazz;
	// Contains unread MessageCodec data
	private final InputStream data;
	
	public ServerMessageWrapper(String messageClazz, InputStream data) {
		this.messageClazz = messageClazz;
		this.data = data;
	}
	
	public String getMessageClazz() {
		return messageClazz;
	}
	
	public InputStream getData() {
		return data;
	}
	
	public static final MessageCodec<ServerMessageWrapper> CODEC = new MessageCodec<ServerMessageWrapper>() {
		
		@Override
		public void encode(OutputStream output, ServerMessageWrapper message) {
			try {
				MessageUtil.write(output, message.getMessageClazz());
				// Write unread codec
				output.write(message.getData().readAllBytes());
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
		
		@Override
		public ServerMessageWrapper decode(InputStream data) {
			final String clazzName = MessageUtil.read(data);
			return new ServerMessageWrapper(clazzName, data);
		}
	};
}
