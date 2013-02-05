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

import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

public class MessageLoader extends AsyncTask<Void, Void, Void> {
	
	private LocationService service;
	
	public MessageLoader(LocationService service) {
		this.service = service;
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		String msgsResponse = HttpClientManager.httpResponse(service.getApplicationContext(),
				service.oAuthManager.getSignedRequest(new HttpGet("http://mosaic-messaging.appspot.com/messages?lat=" + service.latitude + "&longitude=" + service.longitude)));
		if (msgsResponse != null) {
			try {
				JSONObject msgsJobj = new JSONObject(msgsResponse);
				if (msgsJobj.has("message")) {
					service.clearMessages();
					JSONArray msgsJarr = msgsJobj.getJSONArray("message");
					for (int i = 0, l = msgsJarr.length(); i < l; i++)
						service.addMessage(msgsJarr.getString(i));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		service.finishedMessageLoading();
	}

}
