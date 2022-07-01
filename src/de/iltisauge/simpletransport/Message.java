package de.iltisauge.simpletransport;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Message {
	
	private boolean receiveSelf = true;
	private Set<String> channels = new HashSet<>();
	
	public boolean isReceiveSelf() {
		return receiveSelf;
	}
	
	public void setReceiveSelf(boolean receiveSelf) {
		this.receiveSelf = receiveSelf;
	}
	
	public Set<String> getChannels() {
		return channels;
	}
	
	public void addChannels(String... channels) {
		this.channels.addAll(Arrays.asList(channels));
	}
	
	public void remmoveChannels(String... channels) {
		this.channels.removeAll(Arrays.asList(channels));
	}
	
	@Override
	public String toString() {
		return "Message[class=" + getClass().getName() + ", channels=" + channels.toString() + "]"; 
	}
}
