package com.piusvelte.mosaic.server;

public class Message {
	
	public static final String Table = "messages";
	
	public static enum Columns {
		id, body, created, latitude, longitude, radius, expiry;
	}

}
