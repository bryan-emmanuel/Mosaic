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
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.users.User;
import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.piusvelte.mosaic.gwt.client.WebClientMessage;

public class MessageHelper {

	public static final String kind = "message";
	public static final String id = "id";
	public static final String body = "body";
	public static final String created = "created";
	public static final String latitude = "latitude";
	public static final String minlatitude = "minlatitude";
	public static final String maxlatitude = "maxlatitude";
	public static final String longitude = "longitude";
	public static final String minlongitude = "minlongitude";
	public static final String maxlongitude = "maxlongitude";
	public static final String radius = "radius";
	public static final String expiry = "expiry";
	public static final String visits = "visits";
	public static final String reports = "reports";
	public static final String flagged = "flagged";

	public static Query getMessagesQuery(String latitude, String longitude, String self, Key userKey) throws Exception {
		Query query = new Query(kind);
		try {
			if (Boolean.parseBoolean(self)) {
				query.setFilter(getCoordinatesFilter(latitude, longitude));
				query.setAncestor(userKey);
			} else
				query.setFilter(getNonFlaggedFilter(getCoordinatesFilter(latitude, longitude)));
		} catch (Exception e) {
			if ((userKey != null) && Boolean.parseBoolean(self))
				query.setAncestor(userKey);
			else
				throw new Exception("invalid parameters");
		}
		return query;
	}

	private static Filter getNonFlaggedFilter(Filter coordinatesFilter) {
		return CompositeFilterOperator.and(coordinatesFilter, new FilterPredicate(flagged, FilterOperator.NOT_EQUAL, true));
	}

	private static Filter getCoordinatesFilter(String latitude, String longitude) throws Exception {
		if ((latitude != null) && (longitude != null))
			return CompositeFilterOperator.and(getCoordinateFilter(latitude, minlatitude, maxlatitude),
					getCoordinateFilter(longitude, minlongitude, maxlongitude));
		else
			throw new Exception("no coordinates");
	}

	private static Filter getCoordinateFilter(String coordinate, String min, String max) {
		return CompositeFilterOperator.and(new FilterPredicate(coordinate, FilterOperator.GREATER_THAN_OR_EQUAL, min),
				new FilterPredicate(coordinate, FilterOperator.LESS_THAN_OR_EQUAL, max));
	}

	public static String jsonFromRequest(String latitude, String longitude, String self, Key userKey) throws Exception {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		return jsonFromEntities(datastore.prepare(getMessagesQuery(latitude, longitude, self, userKey)).asList(FetchOptions.Builder.withDefaults()));
	}

	public static String jsonFromEntities(List<Entity> messages) throws Exception {
		JSONArray jarr = new JSONArray();
		for (Entity message : messages) {
			try {
				jarr.put(jsonFromEntity(message));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return jarr.toString();
	}

	public static String jsonFromEntity(Entity message) throws Exception {
		JSONObject jobj = new JSONObject();
		jobj.put(id, message.getKey().getId());
		jobj.put(body, message.getProperty(body));
		jobj.put(created, message.getProperty(created));
		jobj.put(latitude, message.getProperty(latitude));
		jobj.put(longitude, message.getProperty(longitude));
		jobj.put(radius, message.getProperty(radius));
		jobj.put(expiry, message.getProperty(expiry));
		jobj.put(UserHelper.kind, UserHelper.jsonFromKey(message.getParent()));
		return jobj.toString();
	}

	public static Entity entityFromRequest(String body, String lat, String lon, String rad, String exp) {
		Entity newMessage = new Entity(kind);
		newMessage.setProperty(body, body);
		newMessage.setProperty(latitude, lat);
		newMessage.setProperty(longitude, lon);
		newMessage.setProperty(radius, rad);
		newMessage.setProperty(expiry, exp);
		newMessage.setProperty(created, System.currentTimeMillis());
		//TODO: calc range
		return newMessage;
	}

	public static WebClientMessage[] getMessages(User user, int page) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query(kind);
		query.setAncestor(UserHelper.getUserKey(user));
		List<Entity> messages = datastore.prepare(query).asList(FetchOptions.Builder.withOffset(page).limit(10));
		WebClientMessage[] webClientMessages = new WebClientMessage[messages.size()];
		int i = 0;
		for (Entity message : messages) {
			WebClientMessage webClientMessage = new WebClientMessage();
			webClientMessage.id = message.getKey().getId();
			webClientMessage.body = (String) message.getProperty(body);
			webClientMessage.latitude = (Integer) message.getProperty(latitude);
			webClientMessage.longitude = (Integer) message.getProperty(longitude);
			webClientMessage.radius = (Integer) message.getProperty(radius);
			webClientMessage.expiry = (Long) message.getProperty(expiry);
			webClientMessage.created = (Long) message.getProperty(created);
			webClientMessages[i++] = webClientMessage;
		}
		return webClientMessages;
	}
}
