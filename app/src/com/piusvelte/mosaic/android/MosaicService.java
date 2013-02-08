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

	public void addAccount(SignIn callback, int requestCode) {
		new addAccount(callback, requestCode).execute();
	}

	private String getToken(SignIn callback, int requestCode) throws IOException {
		try {
			return GoogleAuthUtil.getToken(callback, email, scope);
		} catch (GooglePlayServicesAvailabilityException playEx) {
			// GooglePlayServices.apk is either old, disabled, or not present.
			callback.showErrorDialog(playEx.getConnectionStatusCode());
		} catch (UserRecoverableAuthException userRecoverableException) {
			// Unable to authenticate, but the user can fix this.
			// Forward the user to the appropriate activity.
			callback.startActivityForResult(userRecoverableException.getIntent(), requestCode);
		} catch (GoogleAuthException fatalException) {
			if (fatalException != null)
				Log.e(TAG, "Exception: ", fatalException);
			callback.show("Unrecoverable error " + fatalException.getMessage());
		}
		return null;
	}

	private static String readResponse(InputStream is) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] data = new byte[2048];
		int len = 0;
		while ((len = is.read(data, 0, data.length)) >= 0)
			bos.write(data, 0, len);
		return new String(bos.toByteArray(), "UTF-8");
	}

	class addAccount extends AsyncTask<Void, Void, Void> {

		SignIn callback;
		int requestCode;

		addAccount(SignIn callback, int requestCode) {
			this.callback = callback;
			this.requestCode = requestCode;
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				String token = getToken(callback, requestCode);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
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
	
	public void getMessages(LocationService callback, int latitude, int longitude) {
		
	}

	class getMessages extends AsyncTask<Void, Void, Void> {

		LocationService callback;
		String latitude;
		String longitude;

		getMessages(LocationService callback, String latitude, String longitude) {
			this.callback = callback;
			this.latitude = latitude;
			this.longitude = longitude;
		}

		@Override
		protected Void doInBackground(Void... params) {
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
				url = new URL("http://mosaic-messaging.appspot.com/messages?lat=" + latitude + "&lon=" + longitude + "&access_token=" + token);
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

	}

}
