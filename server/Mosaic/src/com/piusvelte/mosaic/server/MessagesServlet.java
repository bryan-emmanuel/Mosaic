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

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.oauth.OAuthServiceFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

public class MessagesServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private static final String lat = "lat";
	private static final String lon = "lon";
	private static final String rad = "rad";
	private static final String exp = "exp";
	private static final String self = "self";

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		try {
			Key userKey = MosaicUser.getUserKey(OAuthServiceFactory.getOAuthService().getCurrentUser());
			JSONObject responseObj = new JSONObject();
			try {
				responseObj.put(Message.kind, Message.jsonFromRequest(req.getParameter(lat), req.getParameter(lon), req.getParameter(self), userKey));
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
			resp.setStatus(401);
		}
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		try {
			User user = OAuthServiceFactory.getOAuthService().getCurrentUser();
			//TODO: handle creating a message
		} catch (OAuthRequestException e) {
			resp.setStatus(401);
		}
	}

}
