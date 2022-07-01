package de.iltisauge.simpletransport;

import java.io.IOException;
import java.io.OutputStream;

public class MessageEncoder {
	
	@SuppressWarnings("unchecked")
	public <T> void encode(OutputStream output, Message message) throws IOException {
		MessageUtil.write(output, message.getClass().getName());
		output.write(message.getChannels().size());
		for (String channel : message.getChannels()) {
			MessageUtil.write(output, channel);
		}
		output.write(message.isReceiveSelf() ? 1 : 0);
		if (message instanceof ServerMessageWrapper) {
			output.write(((ServerMessageWrapper) message).getData().readAllBytes());
		} else {
			final MessageCodec<T> codec = (MessageCodec<T>) Transport.getCodecManager().getCodec(message.getClass());
			codec.encode(output, (T) message);
		}
	}
}
