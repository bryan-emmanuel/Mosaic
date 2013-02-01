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

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.oauth.OAuthServiceFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

public class MessagesServlet extends HttpServlet {
	
	public static final String kind = "messages";
	public static final String key = "key";
	public static final String body = "body";
	public static final String created = "created";
	public static final String latitude = "latitude";
	public static final String longitude = "longitude";
	public static final String radius = "radius";
	public static final String expiry = "expiry";
	public static final String user = "user";
	public static final String userid = "userid";
	
	private static final long serialVersionUID = 1L;
	private static final String lat = "lat";
	private static final String lon = "lon";
	private static final String self = "self";

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		try {
			Key userKey = getUserKey(OAuthServiceFactory.getOAuthService().getCurrentUser());
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			JSONObject responseObj = new JSONObject();
			try {
				responseObj.put(kind, jsonFromEntities(datastore.prepare(getMessagesQuery(req.getParameter(lat), req.getParameter(lon), req.getParameter(self), userKey)).asList(FetchOptions.Builder.withDefaults())));
				resp.setContentType("application/json");
				resp.getWriter().write(responseObj.toString());
			} catch (JSONException e) {
				resp.setStatus(500);
				e.printStackTrace();
			} catch (Exception e) {
				resp.setStatus(400);
				resp.getWriter().write(e.getMessage());
				e.printStackTrace();
			}
		} catch (OAuthRequestException e) {
			e.printStackTrace();
			resp.setStatus(401);
		}
	}
	
	private Query getMessagesQuery(String latitude, String longitude, String self, Key userKey) throws Exception {
		Query query = new Query(kind);
		try {
			query.setFilter(getRangeFilter(latitude, longitude));
			if (Boolean.parseBoolean(self))
				query.setAncestor(userKey);
		} catch (Exception e) {
			if ((userKey != null) && Boolean.parseBoolean(self))
				query.setAncestor(userKey);
			else
				throw new Exception("invalid parameters");
		}
		return query;
	}
	
	private Filter getRangeFilter(String latitude, String longitude) throws Exception {
		if ((latitude != null) && (longitude != null))
			return CompositeFilterOperator.and(getCoordinateFilter(latitude, latitude),
					getCoordinateFilter(longitude, longitude));
		else
			throw new Exception("no coordinates");
	}
	
	private Filter getCoordinateFilter(String coordinate, String type) {
		Filter minFilter = new FilterPredicate(coordinate,
				FilterOperator.GREATER_THAN_OR_EQUAL,
				type + "-" + radius);
		Filter maxFilter = new FilterPredicate(coordinate,
				FilterOperator.LESS_THAN_OR_EQUAL,
				type + "+" + radius);
		return CompositeFilterOperator.and(minFilter, maxFilter);
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
		jobj.put(key, message.getKey());
		jobj.put(body, message.getProperty(body));
		jobj.put(created, message.getProperty(created));
		jobj.put(latitude, message.getProperty(latitude));
		jobj.put(longitude, message.getProperty(longitude));
		jobj.put(radius, message.getProperty(radius));
		jobj.put(expiry, message.getProperty(expiry));
		jobj.put(user, message.getProperty(user));
		return jobj.toString();
	}
	
	private Key getUserKey(User user) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Filter filter = new FilterPredicate(userid, FilterOperator.EQUAL, user.getUserId());
		Query query = new Query("user").setFilter(filter);
		List<Entity> users = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(1));
		if (users.isEmpty())
			return null;
		else
			return users.get(0).getKey();
	}

}
