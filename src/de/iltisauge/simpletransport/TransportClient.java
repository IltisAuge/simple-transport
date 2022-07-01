package de.iltisauge.simpletransport;

import java.net.InetSocketAddress;
import java.util.logging.Level;

public class TransportClient implements INetworkComponent {

	private final InetSocketAddress serverAddress;
	private TransportSession session;
	
	public TransportClient(InetSocketAddress serverAddress) {
		this.serverAddress = serverAddress;
	}
	
	public static void main(String[] args) {
		new TransportClient(new InetSocketAddress(28390)).start();
	}
	
	public InetSocketAddress getClientAddress() {
		return session.getClientAddress();
	}
	
	@Override
	public void start() {
		Transport.setDecoder(new MessageDecoder(this));
		session = new TransportSession(serverAddress);
		session.establish();
		session.listenForMessages();
	}

	public void sendMessage(Message message) {
		if (!session.isEstablished()) {
			Transport.getLogger().log(Level.WARNING, "Can not send message through unestablished session!");
			return;
		}
		session.sendMessage(message);
	}
	
	@Override
	public void stop() {
		session.destroy();
	}

	@Override
	public boolean isRunning() {
		return session.isEstablished();
	}
}
