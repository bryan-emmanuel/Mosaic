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
import java.util.HashMap;
import java.util.List;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.piusvelte.mosaic.android.mosaicmessages.Mosaicmessages;
import com.piusvelte.mosaic.android.mosaicmessages.model.MosaicMessage;
import com.piusvelte.mosaic.android.mosaicusers.Mosaicusers;
import com.piusvelte.mosaic.android.mosaicusers.model.MosaicUser;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class MosaicService extends Service implements LocationListener {

	private static final String TAG = "MosaicService";
	private GoogleAccountCredential credential;
	private HttpTransport transport = AndroidHttp.newCompatibleTransport();
	private JsonFactory jsonFactory = new GsonFactory();
	protected MosaicUser mosaicUser = null;
	private LocationManager locationManager = null;
	protected int latitudeE6 = Integer.MAX_VALUE;
	protected int longitudeE6 = Integer.MAX_VALUE;
	protected HashMap<Long, MosaicMessage> messages = new HashMap<Long, MosaicMessage>();
	private static long UPDATE_TIME = 10000L;
	private static float UPDATE_DISTANCE = 10F;
	private String[] providers = new String[]{LocationManager.NETWORK_PROVIDER, LocationManager.GPS_PROVIDER};

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		start(intent);
		return START_STICKY;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	private void initLocationManager() {
		if ((mosaicUser != null) && (locationManager == null)) {
			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			for (String provider : providers) {
				if (locationManager.isProviderEnabled(provider))
					initLocationUpdates(provider);
			}
		}
	}

	private void loadMosaicUser() {
		if (mosaicUser != null)
			setNickname(mosaicUser.getNickname());
		else {
			SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
			String accountName = sharedPreferences.getString(getString(R.string.preference_account_name), null);
			long user_id = sharedPreferences.getLong(getString(R.string.preference_user_id), Mosaic.INVALID_ID);
			if (accountName != null) {
				credential = GoogleAccountCredential.usingAudience(this, "server:client_id:" + getString(R.string.client_id));
				credential.setSelectedAccountName(accountName);
				if (user_id != Mosaic.INVALID_ID)
					new GetUserTask(this, user_id).execute();
				else
					new InsertUserTask(this).execute();
			} else
				setNickname(null);
		}
	}

	@Override
	public void onStart(Intent intent, int startId) {
		start(intent);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (locationManager != null)
			locationManager.removeUpdates(this);
	}

	private void start(Intent intent) {
		loadMosaicUser();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return iLocationService;
	}

	private void initLocationUpdates(String provider) {
		locationManager.requestLocationUpdates(provider, UPDATE_TIME, UPDATE_DISTANCE, this);
		setCoordinates(locationManager.getLastKnownLocation(provider));
	}

	private void setCoordinates(Location location) {
		if (location != null) {
			latitudeE6 = toE6(location.getLatitude());
			longitudeE6 = toE6(location.getLongitude());
			if (iMain != null) {
				try {
					iMain.setCoordinates(location.getLatitude(), location.getLongitude());
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			new GetMessagesTask(this, latitudeE6, longitudeE6).execute();
		}
	}

	protected void addMessage(MosaicMessage message) {
		messages.put(message.getId(), message);
		if (iMain != null) {
			try {
				iMain.addMessage(message.getId(), fromE6(message.getLatitudeE6()), fromE6(message.getLongitudeE6()), message.getTitle(), message.getBody(), message.getUser().getNickname());
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	protected void setMessages(List<MosaicMessage> messages) {
		this.messages.clear();
		if (iMain != null) {
			try {
				iMain.clearMessages();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (messages != null) {
			for (MosaicMessage message : messages)
				addMessage(message);
		}
	}

	protected void setNickname(String nickname) {
		initLocationManager();
		if (iMain != null) {
			try {
				iMain.setNickname(nickname);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	protected void setUserId(long id) {
		getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE)
		.edit()
		.putLong(getString(R.string.preference_user_id), id)
		.commit();
		setNickname(mosaicUser.getNickname());
	}
	
	protected void clearAccount() {
		getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE)
		.edit()
		.putString(getString(R.string.preference_account_name), null)
		.putLong(getString(R.string.preference_user_id), Mosaic.INVALID_ID)
		.commit();
		setNickname(null);
	}

	protected void updateMessage(MosaicMessage message) {
		messages.put(message.getId(), message);
		if (iMain != null) {
			try {
				iMain.updateMarker(message.getId(), message.getTitle(), message.getBody(), message.getUser().getNickname());
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private int toE6(double d) {
		return (int) (d * 1E6);
	}

	private double fromE6(int e6) {
		return e6 / 1E6;
	}

	private IMain iMain;

	private ILocationService.Stub iLocationService = new ILocationService.Stub() {

		@Override
		public void setCallback(IBinder mainBinder)
				throws RemoteException {
			if (mainBinder != null) {
				iMain = IMain.Stub.asInterface(mainBinder);
				loadMosaicUser();
			} else
				iMain = null;
		}

		@Override
		public void changeNickname(String nickname) throws RemoteException {
			if ((nickname != null) && (nickname.length() > 0)) {
				mosaicUser.setNickname(nickname);
				new UpdateUserTask(MosaicService.this).execute();
			}
		}

		@Override
		public void insertMessage(String title, String body, int latE6,
				int lonE6, int radius, long expiry) throws RemoteException {
			MosaicMessage message = new MosaicMessage();
			message.setTitle(title);
			message.setBody(body);
			message.setLatitudeE6(latE6);
			message.setLongitudeE6(lonE6);
			message.setRadius(radius);
			message.setCreated(System.currentTimeMillis());
			message.setUserId(mosaicUser.getId());
			new InsertMessageTask(MosaicService.this, message).execute();
		}

		@Override
		public void updateMessage(long id, String title, String body,
				int radius, long expiry)
						throws RemoteException {
			MosaicMessage message = messages.get(id);
			message.setTitle(title);
			message.setBody(body);
			message.setRadius(radius);
			message.setExpiry(expiry);
			new UpdateMessageTask(MosaicService.this, message).execute();
		}

		@Override
		public void getMessage(long id) throws RemoteException {
			if (messages.containsKey(id)) {
				MosaicMessage message = messages.get(id);
				new ViewMessageTask(MosaicService.this, id).execute();
				if (mosaicUser.getId() == message.getUserId())
					iMain.editMessage(message.getId(), message.getTitle(), message.getBody(), message.getRadius(), message.getExpiry());
				else
					iMain.viewMessage(message.getId(), message.getTitle(), message.getBody(), message.getUser().getNickname());
			} else {
				//TODO error
			}
		}

		@Override
		public void reportMessage(long id) throws RemoteException {
			new ReportMessageTask(MosaicService.this, id).execute();
		}

		@Override
		public void removeMessage(long id) throws RemoteException {
			messages.remove(id);
			new RemoteMessageTask(MosaicService.this, id).execute();
		}
	};

	@Override
	public void onLocationChanged(Location location) {
		setCoordinates(location);
	}

	@Override
	public void onProviderDisabled(String provider) {
		if ((LocationManager.GPS_PROVIDER.equals(provider) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) || (LocationManager.NETWORK_PROVIDER.equals(provider) && !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))) {
			latitudeE6 = Integer.MAX_VALUE;
			longitudeE6 = Integer.MAX_VALUE;
			if (iMain != null) {
				try {
					iMain.setCoordinates(latitudeE6, longitudeE6);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				//TODO notify about no GPS
			}
		}
	}

	@Override
	public void onProviderEnabled(String provider) {
		if (LocationManager.GPS_PROVIDER.equals(provider) || LocationManager.NETWORK_PROVIDER.equals(provider))
			initLocationUpdates(provider);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle arg2) {
		// TODO Auto-generated method stub
	}

	class InsertUserTask extends AsyncTask<Void, Void, Void> {

		MosaicService callback;
		Mosaicusers endpoint;

		InsertUserTask(MosaicService callback) {
			this.callback = callback;
			Mosaicusers.Builder endpointBuilder = new Mosaicusers.Builder(transport,
					jsonFactory,
					credential);
			endpoint = CloudEndpointUtils.updateBuilder(endpointBuilder).build();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				mosaicUser = endpoint.user().insert().execute();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (mosaicUser != null)
				callback.setUserId(mosaicUser.getId());
			else
				callback.clearAccount();
		}
	}

	class UpdateUserTask extends AsyncTask<Void, Void, Void> {

		MosaicService callback;
		Mosaicusers endpoint;

		UpdateUserTask(MosaicService callback) {
			this.callback = callback;
			Mosaicusers.Builder endpointBuilder = new Mosaicusers.Builder(transport,
					jsonFactory,
					credential);
			endpoint = CloudEndpointUtils.updateBuilder(endpointBuilder).build();
		}

		@Override
		protected Void doInBackground(Void... params) { 
			try {
				mosaicUser = endpoint.user().update(mosaicUser).execute();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
		}
	}

	class GetUserTask extends AsyncTask<Void, Void, Void> {

		MosaicService callback;
		Mosaicusers endpoint;
		String accountName;
		long id;

		GetUserTask(MosaicService callback, long id) {
			this.callback = callback;
			this.id = id;
			Mosaicusers.Builder endpointBuilder = new Mosaicusers.Builder(transport,
					jsonFactory,
					credential);
			endpoint = CloudEndpointUtils.updateBuilder(endpointBuilder).build();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				mosaicUser = endpoint.user().get(id).execute();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (mosaicUser != null)
				callback.setNickname(mosaicUser.getNickname());
			else
				callback.setNickname(null);
		}
	}

	class GetMessagesTask extends AsyncTask<Void, Void, List<MosaicMessage>> {

		MosaicService callback;
		int latitude;
		int longitude;
		Mosaicmessages endpoint;

		GetMessagesTask(MosaicService callback, int latitude, int longitude) {
			this.callback = callback;
			this.latitude = latitude;
			this.longitude = longitude;
			Mosaicmessages.Builder endpointBuilder = new Mosaicmessages.Builder(transport,
					jsonFactory,
					credential);
			endpoint = CloudEndpointUtils.updateBuilder(endpointBuilder).build();
		}

		@Override
		protected List<MosaicMessage> doInBackground(Void... params) {
			try {
				return endpoint.message().list(latitude, longitude).execute().getItems();
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

	class InsertMessageTask extends AsyncTask<Void, Void, MosaicMessage> {

		MosaicService callback;
		Mosaicmessages endpoint;
		MosaicMessage message;

		InsertMessageTask(MosaicService callback, MosaicMessage message) {
			this.callback = callback;
			this.message = message;
			Mosaicmessages.Builder endpointBuilder = new Mosaicmessages.Builder(transport,
					jsonFactory,
					credential);
			endpoint = CloudEndpointUtils.updateBuilder(endpointBuilder).build();
		}

		@Override
		protected MosaicMessage doInBackground(Void... params) {
			try {
				return endpoint.message().insert(message).execute();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(MosaicMessage message) {
			if (message != null)
				callback.addMessage(message);
		}
	}

	class ViewMessageTask extends AsyncTask<Void, Void, Void> {

		MosaicService callback;
		Mosaicmessages endpoint;
		long id;

		ViewMessageTask(MosaicService callback, long id) {
			this.callback = callback;
			this.id = id;
			Mosaicmessages.Builder endpointBuilder = new Mosaicmessages.Builder(transport,
					jsonFactory,
					credential);
			endpoint = CloudEndpointUtils.updateBuilder(endpointBuilder).build();
		}

		@Override
		protected Void doInBackground(Void... params) { 
			try {
				endpoint.message().view(id).execute();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}

	class UpdateMessageTask extends AsyncTask<Void, Void, MosaicMessage> {

		MosaicService callback;
		Mosaicmessages endpoint;
		MosaicMessage message;

		UpdateMessageTask(MosaicService callback, MosaicMessage message) {
			this.callback = callback;
			this.message = message;
			Mosaicmessages.Builder endpointBuilder = new Mosaicmessages.Builder(transport,
					jsonFactory,
					credential);
			endpoint = CloudEndpointUtils.updateBuilder(endpointBuilder).build();
		}

		@Override
		protected MosaicMessage doInBackground(Void... params) { 
			try {
				return endpoint.message().update(message).execute();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(MosaicMessage message) {
			if (message != null)
				callback.updateMessage(message);
		}
	}

	class ReportMessageTask extends AsyncTask<Void, Void, Void> {

		MosaicService callback;
		Mosaicmessages endpoint;
		long id;

		ReportMessageTask(MosaicService callback, long id) {
			this.callback = callback;
			this.id = id;
			Mosaicmessages.Builder endpointBuilder = new Mosaicmessages.Builder(transport,
					jsonFactory,
					credential);
			endpoint = CloudEndpointUtils.updateBuilder(endpointBuilder).build();
		}

		@Override
		protected Void doInBackground(Void... params) { 
			try {
				endpoint.message().report(id).execute();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}

	class RemoteMessageTask extends AsyncTask<Void, Void, Void> {

		MosaicService callback;
		Mosaicmessages endpoint;
		long id;

		RemoteMessageTask(MosaicService callback, long id) {
			this.callback = callback;
			this.id = id;
			Mosaicmessages.Builder endpointBuilder = new Mosaicmessages.Builder(transport,
					jsonFactory,
					credential);
			endpoint = CloudEndpointUtils.updateBuilder(endpointBuilder).build();
		}

		@Override
		protected Void doInBackground(Void... params) { 
			try {
				endpoint.message().remove(id).execute();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}

}
