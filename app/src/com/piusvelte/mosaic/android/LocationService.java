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

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class LocationService extends Service implements LocationListener {

	private static final String TAG = "LocationService";
	protected OAuthHelper oAuthManager = null;
	private LocationManager locationManager;
	protected int latitude = 0;
	protected int longitude = 0;
	protected List<Message> messages = new ArrayList<Message>();
	private MessageLoader messageLoader;
	private static long UPDATE_TIME = 10000L;
	private static float UPDATE_DISTANCE = 10F;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		start(intent);
		return START_STICKY;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		oAuthManager = OAuthHelper.getInstance(getString(R.string.consumer_key), getString(R.string.consumer_secret));
	}

	@Override
	public void onStart(Intent intent, int startId) {
		start(intent);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if ((messageLoader != null) && !messageLoader.isCancelled())
			messageLoader.cancel(true);
	}

	private void start(Intent intent) {
		loadTokenSecret();
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, UPDATE_TIME, UPDATE_DISTANCE, this);
		} else {
			if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
				locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, UPDATE_TIME, UPDATE_DISTANCE, this);
			else {
				//TODO notify about no GPS
			}
		}
	}

	private void loadTokenSecret() {
		Log.d(TAG, "loadTokenSecret");
		if (!oAuthManager.hasCredentials()) {
			SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
			if (sharedPreferences.contains(getString(R.string.preference_token)) && sharedPreferences.contains(getString(R.string.preference_secret))) {
				String token = sharedPreferences.getString(getString(R.string.preference_token), null);
				String secret = sharedPreferences.getString(getString(R.string.preference_secret), null);
				if ((token != null) && (secret != null))
					oAuthManager = OAuthHelper.getInstance(getString(R.string.consumer_key),
							getString(R.string.consumer_secret),
							token,
							secret);
			}
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return iLocationService;
	}

	private void setCoordinates(Location location) {
		Log.d(TAG, "setCoordinates");
		if (location != null) {
			latitude = (int) (location.getLatitude() * 1E6);
			longitude = (int) (location.getLongitude() * 1E6);
			if (iMain != null) {
				try {
					iMain.setCoordinates(location.getLatitude(), location.getLongitude());
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	protected void clearMessages() {
		messages.clear();
		if (iMain != null) {
			try {
				iMain.clearMessages();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	protected void addMessage(JSONObject json) {
		Message msg;
		try {
			msg = Message.messageFrom(json);
			messages.add(msg);
			if (iMain != null) {
				try {
					iMain.addMessage(msg.getLatitudeDegrees(), msg.getLongitudeDegrees(), msg.getNick(), msg.getBody());
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void requestFinished(String message) {
		if (iMain != null) {
			try {
				iMain.setRequestFinished(message);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	protected void startMessageLoading() {
		messageLoader = new MessageLoader(this);
		messageLoader.execute();
	}

	private IMain iMain;

	private ILocationService.Stub iLocationService = new ILocationService.Stub() {

		@Override
		public void getCoordinates() throws RemoteException {
			// TODO Auto-generated method stub
			setCoordinates(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
		}

		@Override
		public void checkGPS() throws RemoteException {
			Log.d(TAG, "checkGPS");
			// TODO Auto-generated method stub
			iMain.setGPSEnabled(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
		}

		@Override
		public void setCallback(IBinder mainBinder)
				throws RemoteException {
			// TODO Auto-generated method stub
			iMain = IMain.Stub.asInterface(mainBinder);
		}

		@Override
		public void checkSignIn() throws RemoteException {
			Log.d(TAG, "checkSignIn");
			// TODO Auto-generated method stub
			loadTokenSecret();
			iMain.hasSignedIn(oAuthManager.hasCredentials());
		}

		@Override
		public void loadMessages() throws RemoteException {
			// TODO Auto-generated method stub
			startMessageLoading();
		}

		@Override
		public void cancelMessages() throws RemoteException {
			// TODO Auto-generated method stub
			if (!messageLoader.isCancelled())
				messageLoader.cancel(true);
		}
	};

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		setCoordinates(location);
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		if (LocationManager.GPS_PROVIDER.equals(provider)) {
			if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
				locationManager.removeUpdates(this);
				locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, UPDATE_TIME, UPDATE_DISTANCE, this);
			} else {
				if (iMain != null) {
					try {
						iMain.setGPSEnabled(false);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					//TODO notify about no GPS
				}
			}
		} else if (LocationManager.NETWORK_PROVIDER.equals(provider) && !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			if (iMain != null) {
				try {
					iMain.setGPSEnabled(false);
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
		// TODO Auto-generated method stub
		if (LocationManager.GPS_PROVIDER.equals(provider)) {
			locationManager.removeUpdates(this);
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, UPDATE_TIME, UPDATE_DISTANCE, this);
			if (iMain != null) {
				try {
					iMain.setGPSEnabled(false);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				//TODO clear no GPS notification
			}
		} else if (LocationManager.NETWORK_PROVIDER.equals(provider) && !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			locationManager.removeUpdates(this);
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, UPDATE_TIME, UPDATE_DISTANCE, this);
			if (iMain != null) {
				try {
					iMain.setGPSEnabled(false);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				//TODO clear no GPS notification
			}
		}
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle arg2) {
		// TODO Auto-generated method stub

	}

}
