/*
 * Mosaic - Location Based Messaging
 * Copyright (C) 2013 Bryan Emmanuel
 * 
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *  
 *  Bryan Emmanuel piusvelte@gmail.com
 */
package com.piusvelte.mosaic.app;

import java.util.HashMap;

public class Message extends HashMap<String, String> {

	public enum Properties {
		key, body, latitude, longitude, radius, expiry, created;
	}
	
	public void setKey(String key) {
		this.put(Properties.key.name(), key);
	}
	
	public void setBody(String body) {
		this.put(Properties.body.name(), body);
	}
	
	public void setLatitude(String latitude) {
		this.put(Properties.latitude.name(), latitude);
	}
	
	public void setLongitude(String longitude) {
		this.put(Properties.longitude.name(), longitude);
	}
	
	public void setRadius(String radius) {
		this.put(Properties.radius.name(), radius);
	}
	
	public void setExpiry(String expiry) {
		this.put(Properties.expiry.name(), expiry);
	}
	
	public void setCreated(String created) {
		this.put(Properties.created.name(), created);
	}
	
}
