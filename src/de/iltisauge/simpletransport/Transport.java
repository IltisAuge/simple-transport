package de.iltisauge.simpletransport;

import java.util.logging.Logger;

public final class Transport {
	
	private static CodecManager CODEC_MANAGER = new CodecManager();
	private static Logger LOGGER = Logger.getLogger("transport");
	private static MessageDecoder DECODER = null;
	private static MessageEncoder ENCODER = new MessageEncoder();
	
	public static void setCodecManager(CodecManager codecManager) {
		Transport.CODEC_MANAGER = codecManager;
	}
	
	public static CodecManager getCodecManager() {
		return Transport.CODEC_MANAGER;
	}
	
	public static void setDecoder(MessageDecoder decoder) {
		Transport.DECODER = decoder;
	}
	
	public static MessageDecoder getDecoder() {
		return Transport.DECODER;
	}
	
	public static void setEncoder(MessageEncoder encoder) {
		Transport.ENCODER = encoder;
	}
	
	public static MessageEncoder getEncoder() {
		return Transport.ENCODER;
	}
	
	public static void setLogger(Logger logger) {
		Transport.LOGGER = logger;
	}
	
	public static Logger getLogger() {
		return Transport.LOGGER;
	}
}
