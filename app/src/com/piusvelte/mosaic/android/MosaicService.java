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
	public String accountName;
	public String appengineWebappClientId;
	private HttpTransport transport = AndroidHttp.newCompatibleTransport();
	private JsonFactory jsonFactory = new GsonFactory();

	private MosaicService(String appengineAppId) {
		this.appengineWebappClientId = "server:client_id:" + appengineAppId;
	}

	public static MosaicService getInstance(String appengineAppId) {
		if (service == null)
			service = new MosaicService(appengineAppId);
		return service;
	}

	public GetUserTask getUser(LocationService callback) {
		return new GetUserTask(callback);
	}

	class GetUserTask extends AsyncTask<Void, Void, MosaicUser> {

		LocationService callback;
		Mosaicuserendpoint endpoint;
		
		GetUserTask(LocationService callback) {
			this.callback = callback;
			GoogleAccountCredential credential = GoogleAccountCredential.usingAudience(callback, appengineWebappClientId);
			credential.setSelectedAccountName(accountName);
			Mosaicuserendpoint.Builder endpointBuilder = new Mosaicuserendpoint.Builder(transport,
					jsonFactory,
					credential);
			endpoint = CloudEndpointUtils.updateBuilder(endpointBuilder).build();
		}

		@Override
		protected MosaicUser doInBackground(Void... params) { 
			try {
				return endpoint.getMosaicUser(accountName).execute();
			} catch (IOException e) {
				try {
					return endpoint.insertMosaicUser(new MosaicUser()).execute();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(MosaicUser user) {
			callback.setMosaicUser(user);
		}
	}

	public static Message messageFrom(JSONObject msg) throws JSONException {
		JSONObject usr = msg.getJSONObject("user");
		return new Message(msg.getString(Properties.id.name()),
				msg.getString(Properties.body.name()),
				msg.getString(Properties.latitude.name()),
				msg.getString(Properties.longitude.name()),
				msg.getString(Properties.radius.name()),
				msg.getString(Properties.expiry.name()),
				msg.getString(Properties.created.name()),
				usr.getString(Properties.id.name()),
				usr.getString(Properties.nickname.name()));
	}

	public GetMessagesTask getMessages(LocationService callback, int latitude, int longitude) {
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
			GoogleAccountCredential credential = GoogleAccountCredential.usingAudience(callback, appengineWebappClientId)
					.setSelectedAccountName(accountName);
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
			callback.setMessages(messages);
		}

	}

}
