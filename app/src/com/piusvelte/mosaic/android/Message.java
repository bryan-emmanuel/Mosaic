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
package com.piusvelte.mosaic.android;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

public class Message extends HashMap<String, String> {
	
	private static final long serialVersionUID = 1L;

	public enum Properties {
		id, body, latitude, longitude, radius, expiry, created, userid, nickname;
	}
	
	public Message(String id, String body, String latitude, String longitude, String radius, String expiry, String created, String userId, String nickname) {
		setId(id);
		setBody(body);
		setLatitude(latitude);
		setLongitude(longitude);
		setRadius(radius);
		setExpiry(expiry);
		setCreated(created);
		setUserId(userId);
		setNickname(nickname);
	}
	
	public void setId(String key) {
		this.put(Properties.id.name(), key);
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
	
	public void setUserId(String userId) {
		this.put(Properties.userid.name(), userId);
	}
	
	public void setNickname(String nickname) {
		this.put(Properties.nickname.name(), nickname);
	}
	
	public String getNick() {
		return this.get(Properties.nickname.name());
	}
	
	public String getBody() {
		return this.get(Properties.body.name());
	}
	
	public double getLatitudeDegrees() {
		return Integer.parseInt(this.get(Properties.latitude.name())) / 1E6;
	}
	
	public double getLongitudeDegrees() {
		return Integer.parseInt(this.get(Properties.longitude.name())) / 1E6;
	}
	
}
