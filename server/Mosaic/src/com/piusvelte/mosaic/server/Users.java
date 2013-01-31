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
package com.piusvelte.mosaic.server;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.users.User;

public class Users {
	
	public static final String Table = "accounts";
	
	public static enum Columns {
		id, userid, email, nickname;
	}
	
	private Users() {
	}
	
	protected static Entity store(User user) {
		
		Entity newUser = new Entity("Users");
		newUser.setProperty(Columns.userid.name(), user.getUserId());
		newUser.setProperty(Columns.email.name(), user.getEmail());
		newUser.setProperty(Columns.nickname.name(), user.getNickname());

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		datastore.put(newUser);
		
		return newUser;
	}

}
