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

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.Settings;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Main extends android.support.v4.app.FragmentActivity implements ServiceConnection, OnMapLongClickListener, OnMarkerClickListener {

	private static final String TAG = "Main";
	private ILocationService iLocationService;
	private GoogleMap map;
	private Button btnNickname;
	public static final String EXTRA_TITLE = "com.piusvelte.mosaic.android.EXTRA_TITLE";
	public static final String EXTRA_BODY = "com.piusvelte.mosaic.android.EXTRA_BODY";
	public static final String EXTRA_EXPIRY = "com.piusvelte.mosaic.android.EXTRA_EXPIRY";
	public static final String EXTRA_LATITUDE = "com.piusvelte.mosaic.android.EXTRA_LATITUDE";
	public static final String EXTRA_LONGITUDE = "com.piusvelte.mosaic.android.EXTRA_LONGITUDE";
	public static final String EXTRA_RADIUS = "com.piusvelte.mosaic.android.EXTRA_RADIUS";
	private static final int REQUEST_EDIT_MESSAGE = 0;
	private static final int REQUEST_VIEW_MESSAGE = 0;
	private ArrayList<String> markerIds = new ArrayList<String>();
	private static final int INVALID_MESSAGE = -1;
	private int markerIndex = INVALID_MESSAGE;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		btnNickname = ((Button) findViewById(R.id.nickname));
		btnNickname.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				final EditText newNickname = new EditText(Main.this);
				newNickname.setText(btnNickname.getText().toString());
				new AlertDialog.Builder(Main.this)
				.setTitle("Change nickname")
				.setView(newNickname)
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						String nickname = newNickname.getText().toString();
						if (nickname.length() > 0) {
							if (!nickname.equals(btnNickname.getText().toString())) {
								try {
									iLocationService.changeNickname(nickname);
								} catch (RemoteException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						} else {
							//TODO no name
						}
					}
					
				})
				.show();
			}
			
		});
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
		map.setMyLocationEnabled(true);
		map.setOnMapLongClickListener(this);
		UiSettings uiSettings = map.getUiSettings();
		uiSettings.setMyLocationButtonEnabled(false);
		uiSettings.setScrollGesturesEnabled(false);
		uiSettings.setZoomControlsEnabled(false);
		uiSettings.setZoomGesturesEnabled(false);
		map.setOnMarkerClickListener(this);
		bindService(new Intent(this, LocationService.class), this, BIND_AUTO_CREATE);
	}

	@Override
	protected void onPause() {
		super.onPause();
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
		public void setCoordinates(double latitude, double longitude)
				throws RemoteException {
			// TODO Auto-generated method stub
			if (latitude != Integer.MAX_VALUE) {
				if (map != null)
					map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 20F));
			} else {
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
		public void setNickname(String nickname) throws RemoteException {
			if (nickname != null) {
				btnNickname.setEnabled(true);
				btnNickname.setText(nickname);
			} else {
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
			map.clear();
			markerIds.clear();
		}

		@Override
		public void addMessage(double latitude, double longitude, String title, String body)
				throws RemoteException {
			// TODO Auto-generated method stub
			markerIds.add(map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(title).snippet(body)).getId());
		}
	};

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		// TODO Auto-generated method stub
		iLocationService = ILocationService.Stub.asInterface(service);
		try {
			iLocationService.setCallback(iMain);
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
	public boolean onMarkerClick(Marker marker) {
		// TODO Auto-generated method stub
		markerIndex = markerIds.indexOf(marker.getId());
		
		return false;
	}

	@Override
	public void onMapLongClick(LatLng point) {
		// TODO Auto-generated method stub
		startActivityForResult(new Intent(this, MessageEditor.class)
		.putExtra(EXTRA_LATITUDE, (int) (point.latitude * 1E6))
		.putExtra(EXTRA_LONGITUDE, (int) (point.longitude * 1E6)), REQUEST_EDIT_MESSAGE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if ((requestCode == REQUEST_EDIT_MESSAGE) && (resultCode == RESULT_OK)) {
			//TODO save the message
		} else if ((requestCode == REQUEST_VIEW_MESSAGE) && (resultCode == RESULT_OK)) {
			//TODO report the message
		}
	}

}
