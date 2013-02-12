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

import java.io.IOException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.piusvelte.mosaic.android.Message.Properties;
import com.piusvelte.mosaic.android.mosaicmessageendpoint.Mosaicmessageendpoint;
import com.piusvelte.mosaic.android.mosaicmessageendpoint.model.MosaicMessage;
import com.piusvelte.mosaic.android.mosaicuserendpoint.Mosaicuserendpoint;
import com.piusvelte.mosaic.android.mosaicuserendpoint.model.MosaicUser;

import android.os.AsyncTask;
import android.util.Log;

public class MosaicService {

	private static final String TAG = "MosaicService";
	private static MosaicService service;
	public String appengineWebappClientId;
	private HttpTransport transport = AndroidHttp.newCompatibleTransport();
	private JsonFactory jsonFactory = new GsonFactory();
	protected MosaicUser user = null;

	private MosaicService(LocationService callback, String appengineAppId, String accountName) {
		this.appengineWebappClientId = "server:client_id:" + appengineAppId;
		Log.d(TAG, "GetUserTask");
		new GetUserTask(callback, accountName).execute();
	}

	public static MosaicService getInstance(LocationService callback, String appengineAppId, String accountName) throws Exception {
		if (accountName == null)
			throw new Exception("account name is null");
		if (service == null)
			service = new MosaicService(callback, appengineAppId, accountName);
		return service;
	}
	
	public void changeNickname(LocationService callback, String nickname) throws Exception {
		if (callback == null)
			throw new Exception("callback is null");
		else if ((nickname == null) || (nickname.length() == 0))
			throw new Exception("nickname cannot be null");
		else if (user == null)
			throw new Exception("mosaic user is null");
		user.setNickname(nickname);
		new UpdateUserTask(callback).execute();
	}
	
	class UpdateUserTask extends AsyncTask<Void, Void, Void> {

		LocationService callback;
		Mosaicuserendpoint endpoint;
		
		UpdateUserTask(LocationService callback) {
			this.callback = callback;
			GoogleAccountCredential credential = GoogleAccountCredential.usingAudience(callback, appengineWebappClientId);
			credential.setSelectedAccountName(user.getEmail());
			Mosaicuserendpoint.Builder endpointBuilder = new Mosaicuserendpoint.Builder(transport,
					jsonFactory,
					credential);
			endpoint = CloudEndpointUtils.updateBuilder(endpointBuilder).build();
		}

		@Override
		protected Void doInBackground(Void... params) { 
			try {
				user = endpoint.getMosaicUser(user.getEmail()).execute();
			} catch (IOException e) {
				try {
					user = endpoint.insertMosaicUser(new MosaicUser()).execute();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
		}
	}

	class GetUserTask extends AsyncTask<Void, Void, Void> {

		LocationService callback;
		Mosaicuserendpoint endpoint;
		String accountName;
		
		GetUserTask(LocationService callback, String accountName) {
			this.callback = callback;
			this.accountName = accountName;
			GoogleAccountCredential credential = GoogleAccountCredential.usingAudience(callback, appengineWebappClientId);
			credential.setSelectedAccountName(accountName);
			Mosaicuserendpoint.Builder endpointBuilder = new Mosaicuserendpoint.Builder(transport,
					jsonFactory,
					credential);
			endpoint = CloudEndpointUtils.updateBuilder(endpointBuilder).build();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				user = endpoint.getMosaicUser(accountName).execute();
			} catch (IOException e) {
				try {
					user = endpoint.insertMosaicUser(new MosaicUser()).execute();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			if (user != null)
				callback.setNickname(user.getNickname());
			else
				callback.setNickname(null);
		}
	}

	public GetMessagesTask getMessages(LocationService callback, int latitude, int longitude) throws Exception {
		if (callback == null)
			throw new Exception("callback is null");
		else if (latitude == Integer.MAX_VALUE)
			throw new Exception("invalid latitude");
		else if (longitude == Integer.MAX_VALUE)
			throw new Exception("invalid longitude");
		else if (user == null)
			throw new Exception("user is null");
		return new GetMessagesTask(callback, latitude, longitude);
	}

	class GetMessagesTask extends AsyncTask<Void, Void, List<MosaicMessage>> {

		LocationService callback;
		String latitude;
		String longitude;
		Mosaicmessageendpoint endpoint;

		GetMessagesTask(LocationService callback, int latitude, int longitude) {
			this.callback = callback;
			this.latitude = Long.toString(latitude);
			this.longitude = Long.toString(longitude);
			Log.d(TAG, "GetMessagesTask");
			Log.d(TAG, "user: " + user.getEmail());
			GoogleAccountCredential credential = GoogleAccountCredential.usingAudience(callback, appengineWebappClientId)
					.setSelectedAccountName(user.getEmail());
			Mosaicmessageendpoint.Builder endpointBuilder = new Mosaicmessageendpoint.Builder(transport,
					jsonFactory,
					credential);
			endpoint = CloudEndpointUtils.updateBuilder(endpointBuilder).build();
		}

		@Override
		protected List<MosaicMessage> doInBackground(Void... params) {
			try {
				return endpoint.listMosaicMessage().execute().getItems();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(List<MosaicMessage> messages) {
			Log.d(TAG, "GetMessagesTask.onPostExecute");
			callback.setMessages(messages);
		}

	}

}
