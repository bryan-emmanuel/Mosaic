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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.users.User;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

public class MosaicUser {
	
	public static final String kind = "user";
	public static final String id = "id";
	public static final String userid = "userid";
	public static final String nickname = "nickname";
	public static final String email = "email";
	
	public static Key getUserKey(User user) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Filter filter = new FilterPredicate(userid, FilterOperator.EQUAL, user.getUserId());
		Query query = new Query(kind).setFilter(filter);
		List<Entity> users = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(1));
		if (users.isEmpty())
			return null;
		return users.get(0).getKey();
	}
	
	public static Key storeUser(User user) {
		Key userKey = getUserKey(user);
		if (userKey != null)
			return userKey;
		Entity newUser = new Entity(kind);
		newUser.setProperty(userid, user.getUserId());
		newUser.setProperty(nickname, user.getNickname());
		newUser.setProperty(email, user.getEmail());
		return DatastoreServiceFactory.getDatastoreService().put(newUser);
	}
	
	public static Entity getUser(Key key) throws Exception {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Filter filter = new FilterPredicate(Entity.KEY_RESERVED_PROPERTY, FilterOperator.EQUAL, key);
		Query query = new Query(kind).setFilter(filter);
		List<Entity> users = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(1));
		if (users.isEmpty())
			throw new Exception("user does not exist");
		return users.get(0);
	}
	
	public static String jsonFromEntity(Entity user) {
		JSONObject jobj = new JSONObject();
		try {
			jobj.put(id, user.getKey().getId());
			jobj.put(nickname, user.getProperty(nickname));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jobj.toString();
	}
	
	public static String jsonFromKey(Key key) throws Exception {
		return jsonFromEntity(getUser(key));
	}
	
}
