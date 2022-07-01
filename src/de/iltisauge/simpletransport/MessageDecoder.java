package de.iltisauge.simpletransport;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

public class MessageDecoder {
	
	private final INetworkComponent component;
	
	public MessageDecoder(INetworkComponent component) {
		this.component = component;
	}
	
	@SuppressWarnings("unchecked")
	public <T> Message decode(InputStream input) throws IOException {
		final String clazzName = MessageUtil.read(input);
		Class<?> clazz = null;
		try {
			clazz = Class.forName(clazzName);
		} catch (ClassNotFoundException exception) {
			Transport.getLogger().log(Level.WARNING, "An error occurred while decoding message: Could not find class " + clazzName);
			return null;
		}
		final int channelSize = input.read();
		final String[] channels = new String[channelSize];
		for (int i = 0; i < channels.length; i++) {
			channels[i] = MessageUtil.read(input);
		}
		final boolean receiveSelf = input.read() == 1 ? true : false;
		Message message = null;
		if (component instanceof TransportServer) {
			// Server is handeling a packet he does not know.
			// Use ServerMessageWrapper to forward the message to the clients.
			// The input now only contains the encoded MessageCodec for this message. 
			message = new ServerMessageWrapper(clazzName, input);
		} else if (component instanceof TransportClient) {
			final MessageCodec<T> codec = (MessageCodec<T>) Transport.getCodecManager().getCodec(clazz);
			if (clazz == null || codec == null) {
				Transport.getLogger().log(Level.WARNING, "An error occurred while decoding message: Could not find a codec for class " + clazzName);
				return null;
			}
			message = (Message) codec.decode(input);
			message.getChannels().clear();
			message.addChannels(channels);
			message.setReceiveSelf(receiveSelf);
		}
		return message;
	}
}
