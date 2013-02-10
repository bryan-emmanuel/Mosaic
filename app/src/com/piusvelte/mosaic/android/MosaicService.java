package com.piusvelte.mosaic.android;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
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
	public String appengineAppId;

	private MosaicService(String appengineAppId) {
		this.appengineAppId = appengineAppId;
	}

	public static MosaicService getInstance(String appengineAppId) {
		if (service == null)
			service = new MosaicService(appengineAppId);
		return service;
	}

	public GetUserTask getUser(LocationService callback) {
		return new GetUserTask(callback);
	}

	class GetUserTask extends AsyncTask<Void, Void, String> {

		LocationService callback;

		GetUserTask(LocationService callback) {
			this.callback = callback;
		}

		@Override
		protected String doInBackground(Void... params) {
			Log.d(TAG, "audience: " + appengineAppId);
			Log.d(TAG, "accountName: " + accountName);
			GoogleAccountCredential credential = GoogleAccountCredential.usingAudience(callback, appengineAppId);
			credential.setSelectedAccountName(accountName);
			Mosaicuserendpoint.Builder endpointBuilder = new Mosaicuserendpoint.Builder(new NetHttpTransport(),
					new JacksonFactory(),
					credential);
			Mosaicuserendpoint endpoint = CloudEndpointUtils.updateBuilder(endpointBuilder).build();
			MosaicUser user;
			try {
				user = endpoint.getMosaicUser(accountName).execute();
				return user.getNickname();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			callback.setNickname(result);
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

	class GetMessagesTask extends AsyncTask<Void, Void, String> {

		LocationService callback;
		String latitude;
		String longitude;
		List<MosaicMessage> messages;

		GetMessagesTask(LocationService callback, int latitude, int longitude) {
			this.callback = callback;
			this.latitude = Long.toString(latitude);
			this.longitude = Long.toString(longitude);
		}

		@Override
		protected String doInBackground(Void... params) {
			GoogleAccountCredential credential = GoogleAccountCredential.usingAudience(callback, appengineAppId)
					.setSelectedAccountName(accountName);
			Mosaicmessageendpoint.Builder endpointBuilder = new Mosaicmessageendpoint.Builder(new NetHttpTransport(),
					new JacksonFactory(),
					credential);
			Mosaicmessageendpoint endpoint = CloudEndpointUtils.updateBuilder(endpointBuilder).build();
			List<MosaicMessage> messages;
			try {
				messages = endpoint.listMosaicMessage().execute().getItems();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			callback.setMessages(messages);
		}

	}

}
