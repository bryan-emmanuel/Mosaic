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
package com.piusvelte.mosaic.gwt.server;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@Entity
public class MosaicUser {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key key;
	
	private String email;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private List<MosaicMessage> mosaicMessages;
	
	private String nickname;

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public MosaicUser() {
	}
	
	public List<MosaicMessage> getMosaicMessages() {
		return mosaicMessages;
	}

	public void setMosaicMessages(List<MosaicMessage> mosaicMessages) {
		this.mosaicMessages = mosaicMessages;
	}
	
	public void setKey(Key key) {
		this.key = key;
	}
	
	public void setEncodedKey(String key) {
		this.key = KeyFactory.stringToKey(key);
	}

	public Key getKey() {
		return key;
	}
	
	public String getEncodedId() {
		return KeyFactory.keyToString(key);
	}

}
