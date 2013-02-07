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

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.Settings;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.util.Log;
import android.view.Menu;

public class Main extends android.support.v4.app.FragmentActivity implements ServiceConnection, OnMapLongClickListener, OnMarkerClickListener {

	private static final String TAG = "Main";
	private ProgressDialog loadingDialog;
	private ILocationService iLocationService;
	private GoogleMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		loadingDialog = new ProgressDialog(this);
		loadingDialog.setMessage("loading");
		loadingDialog.setCancelable(true);
		GooglePlayServicesUtil.getOpenSourceSoftwareLicenseInfo(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (map == null)
			map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
					.getMap();
		if (map != null)
			map.setMyLocationEnabled(true);
		map.setOnMapLongClickListener(this);
		//map.setOnMarkerClickListener(this);
		loadingDialog.show();
		bindService(new Intent(this, LocationService.class), this, BIND_AUTO_CREATE);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if (loadingDialog.isShowing())
			loadingDialog.dismiss();
		if (iLocationService != null) {
			try {
				iLocationService.setCallback(null);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		unbindService(this);
	}

	private IMain.Stub iMain = new IMain.Stub() {

		@Override
		public void setGPSEnabled(boolean enabled) throws RemoteException {
			// TODO Auto-generated method stub
			Log.d(TAG, "setGPSEnabled: " + enabled);
			if (enabled) {
//				loadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//
//					@Override
//					public void onCancel(DialogInterface dialog) {
//						try {
//							iLocationService.cancelMessages();
//						} catch (RemoteException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//
//				});
//				loadingDialog.show();
				iLocationService.loadMessages();
			} else {
				if (loadingDialog.isShowing())
					loadingDialog.dismiss();
				new AlertDialog.Builder(Main.this)
				.setTitle("GPS Settings")
				.setMessage("Please enable GPS.")
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
					}
				})
				.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						finish();
					}
				})
				.show();
			}
		}

		@Override
		public void setCoordinates(double latitude, double longitude)
				throws RemoteException {
			// TODO Auto-generated method stub
			Log.d(TAG, "setCoordinates: " + latitude + ", " + longitude);
			map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 16F));
		}

		@Override
		public void hasSignedIn(boolean signedIn) throws RemoteException {
			Log.d(TAG, "hasSignedIn: " + signedIn);
			if (signedIn)
				iLocationService.checkGPS();
			else {
				if (loadingDialog.isShowing())
					loadingDialog.dismiss();
				new AlertDialog.Builder(Main.this)
				.setTitle("Sign in")
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						startActivity(new Intent(getApplicationContext(), SignIn.class));
					}
				})
				.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						Main.this.finish();
					}
				})
				.show();
			}
		}

		@Override
		public void clearMessages() throws RemoteException {
			// TODO Auto-generated method stub
			if (map != null)
				map.clear();
		}

		@Override
		public void addMessage(double latitude, double longitude, String nick, String body)
				throws RemoteException {
			// TODO Auto-generated method stub
			if (loadingDialog.isShowing())
				loadingDialog.dismiss();
			if (map != null)
				map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(nick).snippet(body));
		}

		@Override
		public void setRequestFinished(String nessage) throws RemoteException {
			// TODO Auto-generated method stub
			if (loadingDialog.isShowing())
				loadingDialog.dismiss();
		}
	};

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		// TODO Auto-generated method stub
		iLocationService = ILocationService.Stub.asInterface(service);
		try {
			iLocationService.setCallback(iMain);
			iLocationService.checkSignIn();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		// TODO Auto-generated method stub
		iLocationService = null;
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onMapLongClick(LatLng point) {
		// TODO Auto-generated method stub
		//create a new message
	}

}
