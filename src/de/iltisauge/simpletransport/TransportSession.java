package de.iltisauge.simpletransport;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.StandardSocketOptions;
import java.util.logging.Level;

public class TransportSession {
	
	private InetSocketAddress clientAddress;
	private final InetSocketAddress serverAddress;
	private Socket socket;
	private boolean established;
	
	public TransportSession(InetSocketAddress serverAddress) {
		this.serverAddress = serverAddress;
	}

	public InetSocketAddress getClientAddress() {
		return clientAddress;
	}
	
	public void establish() {
		socket = new Socket();
		try {
			socket.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
			socket.setOption(StandardSocketOptions.TCP_NODELAY, true);
			socket.connect(serverAddress);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		/*try {
			clientAddress = (InetSocketAddress) socket.getChannel().getLocalAddress();
		} catch (IOException exception) {
			exception.printStackTrace();
		}*/
	}
	
	public boolean isEstablished() {
		return established;
	}
	
	public void sendMessage(Message message) {
		@SuppressWarnings("unchecked")
		final MessageCodec<Message> codec = (MessageCodec<Message>) Transport.getCodecManager().getCodec(message.getClass());
		if (codec == null) {
			Transport.getLogger().log(Level.WARNING, "No codec is registered for " + message.getClass());
			return;
		}
		try {
			final OutputStream output = socket.getOutputStream();
			final byte[] messageData = convertMessage(message, codec);
			//output.write(messageData.length);
			output.write(messageData);
			output.flush();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
	
	private byte[] convertMessage(Message message, MessageCodec<Message> codec) {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		for (String channel : message.getChannels()) {
			MessageUtil.write(output, channel);
		}
		MessageUtil.write(output, message.getClass().getName());
		codec.encode(output, message);
		return output.toByteArray();
	}
	
	public void listenForMessages() {
		try {
			Message message = null;
			while ((message = Transport.getDecoder().decode(socket.getInputStream())) != null) {
				System.out.println("[<-] " + message.toString());
			}
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
	
	public void destroy() {
		
	}
}
