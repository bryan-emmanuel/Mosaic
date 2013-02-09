package com.piusvelte.mosaic.android;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.piusvelte.mosaic.android.Message.Properties;

import android.os.AsyncTask;
import android.util.Log;

public class MosaicService {

	private static final String TAG = "MosaicService";
	private static MosaicService service;
	public String email;
	public String scope;

	private MosaicService(String scope) {
		this.scope = "audience:server:client_id:" + scope;
	}

	public static MosaicService getInstance(String scope) {
		if (service == null)
			service = new MosaicService(scope);
		return service;
	}

	public MosaicService setEmail(String email) {
		this.email = email;
		return this;
	}

	public AddAccountTask addAccount(SignIn callback) {
		return new AddAccountTask(callback);
	}

	private static String readResponse(InputStream is) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] data = new byte[2048];
		int len = 0;
		while ((len = is.read(data, 0, data.length)) >= 0)
			bos.write(data, 0, len);
		return new String(bos.toByteArray(), "UTF-8");
	}

	class AddAccountTask extends AsyncTask<Void, Void, String> {

		SignIn callback;

		AddAccountTask(SignIn callback) {
			this.callback = callback;
		}

		@Override
		protected String doInBackground(Void... params) {
			try {
				GoogleAuthUtil.getToken(callback, email, scope);
				callback.storeEmail(email);
				return "account added";
			} catch (IOException e) {
				// TODO Auto-generated catch block
				email = null;
				Log.e(TAG, e.getMessage());
				return e.getMessage();
			} catch (GoogleAuthException e) {
				// TODO Auto-generated catch block
				email = null;
				Log.e(TAG, e.getMessage());
				return e.getMessage();
			}
		}
		
		@Override
		protected void onPostExecute(String result) {
			callback.taskFinished(result);
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

		GetMessagesTask(LocationService callback, int latitude, int longitude) {
			this.callback = callback;
			this.latitude = Long.toString(latitude);
			this.longitude = Long.toString(longitude);
		}

		@Override
		protected String doInBackground(Void... params) {
			String token = null;
			try {
				token = GoogleAuthUtil.getToken(callback, email, scope);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (GoogleAuthException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			
			URL url = null;
			try {
				url = new URL("https://mosaic-messaging.appspot.com/messages?lat=" + latitude + "&lon=" + longitude + "&access_token=" + token);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			
			HttpURLConnection con;
			try {
				con = (HttpURLConnection) url.openConnection();
				int sc = con.getResponseCode();
				if (sc == 200) {
					InputStream is = con.getInputStream();
					JSONObject msgsJobj;
					try {
						msgsJobj = new JSONObject(readResponse(is));
						if (msgsJobj.has("message")) {
							callback.clearMessages();
							JSONArray msgsJarr = msgsJobj.getJSONArray("message");
							for (int i = 0, l = msgsJarr.length(); i < l; i++)
								callback.addMessage(messageFrom(msgsJarr.getJSONObject(i)));
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					is.close();
				} else if (sc == 401) {
//					GoogleAuthUtil.invalidateToken(callback, token);
//					onError("Server auth error, please try again.", null);
					Log.i(TAG, "Server auth error: " + readResponse(con.getErrorStream()));
				} else {
//					onError("Server returned the following error code: " + sc, null);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			callback.taskFinished(result);
		}

	}

}
