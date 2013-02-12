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

import com.piusvelte.mosaic.android.mosaicmessageendpoint.model.MosaicMessage;

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
	private MosaicService mosaicService = null;
	private LocationManager locationManager;
	protected int latitude = Integer.MAX_VALUE;
	protected int longitude = Integer.MAX_VALUE;
	protected List<MosaicMessage> messages = new ArrayList<MosaicMessage>();
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
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		for (String provider : providers) {
			if (locationManager.isProviderEnabled(provider))
				initLocationUpdates(provider);
		}
		loadMosaicService();
	}

	private void loadMosaicService() {
		Log.d(TAG, "loadMosaicService");
		if (mosaicService == null) {
			SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
			if (sharedPreferences.contains(getString(R.string.preference_account_name))) {
				try {
					mosaicService = MosaicService.getInstance(this, getString(R.string.client_id), sharedPreferences.getString(getString(R.string.preference_account_name), null));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					setNickname(null);
				}
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
		locationManager.removeUpdates(this);
	}

	private void start(Intent intent) {
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

	protected void setMessages(List<MosaicMessage> messages) {
		this.messages.clear();
		if (messages != null)
			this.messages = messages;
		if (iMain != null) {
			try {
				iMain.clearMessages();
				for (MosaicMessage message : this.messages)
					iMain.addMessage(message.getLatitude(), message.getLongitude(), message.getTitle(), message.getBody());
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	protected void setNickname(String nickname) {
		if (iMain != null) {
			try {
				iMain.setNickname(nickname);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private IMain iMain;

	private ILocationService.Stub iLocationService = new ILocationService.Stub() {

		@Override
		public void getCoordinates() throws RemoteException {
			// TODO Auto-generated method stub
			iMain.setCoordinates(latitude, longitude);
		}

		@Override
		public void setCallback(IBinder mainBinder)
				throws RemoteException {
			// TODO Auto-generated method stub
			iMain = IMain.Stub.asInterface(mainBinder);
		}

		@Override
		public void getMessages() throws RemoteException {
			// TODO Auto-generated method stub
			Log.d(TAG, "getMessages");
			try {
				mosaicService.getMessages(LocationService.this, latitude, longitude).execute();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void getNickname() throws RemoteException {
			// TODO Auto-generated method stub
			if ((mosaicService != null) && (mosaicService.user != null))
				iMain.setNickname(mosaicService.user.getNickname());
			else
				loadMosaicService();
		}

		@Override
		public void changeNickname(String nickname) throws RemoteException {
			// TODO Auto-generated method stub
			try {
				mosaicService.changeNickname(LocationService.this, nickname);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onLocationChanged");
		setCoordinates(location);
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		if ((LocationManager.GPS_PROVIDER.equals(provider) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) || (LocationManager.NETWORK_PROVIDER.equals(provider) && !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))) {
			latitude = Integer.MAX_VALUE;
			longitude = Integer.MAX_VALUE;
			if (iMain != null) {
				try {
					iMain.setCoordinates(latitude, longitude);
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
		if (LocationManager.GPS_PROVIDER.equals(provider) || LocationManager.NETWORK_PROVIDER.equals(provider))
			initLocationUpdates(provider);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle arg2) {
		// TODO Auto-generated method stub

	}

}
