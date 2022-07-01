package de.iltisauge.simpletransport;

import java.util.HashMap;
import java.util.Map;

public class CodecManager {
	
	private final Map<Class<?>, MessageCodec<?>> codecs = new HashMap<>();
	
	public void registerCodec(Class<?> clazz, MessageCodec<?> codec) {
		synchronized (codecs) {
			codecs.put(clazz, codec);
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> MessageCodec<T> getCodec(Class<T> clazz) {
		synchronized (codecs) {
			return (MessageCodec<T>) codecs.get(clazz);
		}
	}
	
	public void unregisterCodec(Class<?> clazz) {
		synchronized (codecs) {
			codecs.remove(clazz);
		}
	}
}
