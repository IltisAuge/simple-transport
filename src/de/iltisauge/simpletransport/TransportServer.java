package de.iltisauge.simpletransport;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TransportServer implements INetworkComponent {

	private final InetSocketAddress address;
	private ServerSocket serverSocket;
	private static List<Socket> clients = new ArrayList<>();
	
	public TransportServer(InetSocketAddress address) {
		this.address = address;
	}
	
	public InetSocketAddress getAddress() {
		return address;
	}
	
	public static void main(String[] args) {
		new TransportServer(new InetSocketAddress(28390)).start();
	}
	
	@Override
	public void start() {
		Transport.setDecoder(new MessageDecoder(this));
		try {
			serverSocket = new ServerSocket();
			serverSocket.bind(address);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		Transport.getCodecManager().registerCodec(TextMessage.class, TextMessage.CODEC);
		listenForMessages();
	}
	
	private void listenForMessages() {
		Socket socket = null;
		try {
			while ((socket = serverSocket.accept()) != null) {
				if (!clients.contains(socket)) {
					clients.add(socket);
					System.out.println("Added to connections");
				}
				final InputStream input = socket.getInputStream();
				final Message message = Transport.getDecoder().decode(input);
				broadcast(message);
			}
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
	
	private static void broadcast(Message message) {
		for (Socket s : clients) {
			System.out.println("bc to " + s.toString());
			sendMessage(s, message);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> void sendMessage(Socket socket, Message message) {
		final MessageCodec<T> codec = (MessageCodec<T>) Transport.getCodecManager().getCodec(message.getClass());
		try {
		codec.encode(socket.getOutputStream(), (T) message);
		socket.getOutputStream().flush();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	@Override
	public void stop() {
	}

	@Override
	public boolean isRunning() {
		return serverSocket.isBound() && !serverSocket.isClosed();
	}
}
