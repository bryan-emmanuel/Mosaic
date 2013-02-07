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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;

import android.os.AsyncTask;
import android.util.Log;

public class MessageLoader extends AsyncTask<Void, Void, Void> {
	
	private static final String TAG = "MessageLoader";
	private LocationService service;
	
	public MessageLoader(LocationService service) {
		this.service = service;
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		HttpRequest request = service.oAuthManager.getSignedRequest("http://mosaic-messaging.appspot.com/messages?lat=" + service.latitude + "&lon=" + service.longitude,
				null);
		HttpResponse httpResponse;
		InputStream is;
		ByteArrayOutputStream content;
		byte[] buffer;
		int readBytes;
		String response = null;
		try {
			httpResponse = request.execute();
			switch(httpResponse.getStatusCode()) {
			case 200:
			case 201:
			case 204:
				is = httpResponse.getContent();
				content = new ByteArrayOutputStream();
				buffer = new byte[512];
				readBytes = 0;
				while ((readBytes = is.read(buffer)) != -1)
					content.write(buffer, 0, readBytes);
				httpResponse.disconnect();
				response = new String(content.toByteArray());
			default:
				Log.e(TAG, request.getUrl().toString());
				Log.e(TAG, "" + httpResponse.getStatusCode() + " " + httpResponse.getStatusMessage());
				is = httpResponse.getContent();
				content = new ByteArrayOutputStream();
				buffer = new byte[512];
				readBytes = 0;
				while ((readBytes = is.read(buffer)) != -1)
					content.write(buffer, 0, readBytes);
				Log.e(TAG, "response: " + new String(content.toByteArray()));
			}
			httpResponse.disconnect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (response != null) {
			try {
				JSONObject msgsJobj = new JSONObject(response);
				if (msgsJobj.has("message")) {
					service.clearMessages();
					JSONArray msgsJarr = msgsJobj.getJSONArray("message");
					for (int i = 0, l = msgsJarr.length(); i < l; i++)
						service.addMessage(msgsJarr.getJSONObject(i));
				}
			} catch (JSONException e) {
				service.requestFinished(e.getMessage());
				e.printStackTrace();
			}
		} else
			service.requestFinished("no response");
		return null;
	}

}
