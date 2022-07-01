package de.iltisauge.simpletransport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager {
	
	private final Map<Class<?>, List<MessageEvent>> events = new HashMap<>();
	
	public void registerEvents(Class<?> clazz, MessageEvent... events) {
		synchronized (events) {
			if (this.events.containsKey(clazz)) {
				this.events.get(clazz).addAll(Arrays.asList(events));
				return;
			}
			this.events.put(clazz, new ArrayList<>(Arrays.asList(events)));
		}
	}
	
	public List<MessageEvent> getEvents(Class<?> clazz) {
		synchronized (clazz) {
			return events.get(clazz);
		}
	}
	
	public void unregisterEvents(Class<?> clazz, MessageEvent... events) {
		synchronized (events) {
			if (!this.events.containsKey(clazz)) {
				return;
			}
			final List<MessageEvent> list = this.events.get(clazz);
			for (MessageEvent event : events) {
				list.remove(event);
			}
		}
	}
	
	public void callEvents(Message message) {
		synchronized (events) {
			for (MessageEvent event : this.events.get(message.getClass())) {
				event.onReceive(message);
			}
		}
	}
}
