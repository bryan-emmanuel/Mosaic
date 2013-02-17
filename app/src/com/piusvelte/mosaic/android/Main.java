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

import java.util.HashMap;

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
	private static final int REQUEST_INSERT_MESSAGE = 0;
	private static final int REQUEST_UPDATE_MESSAGE = 1;
	private static final int REQUEST_VIEW_MESSAGE = 2;
	private HashMap<String, String> markerIds = new HashMap<String, String>();
	private HashMap<String, Marker> markers = new HashMap<String, Marker>();

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
			markers.clear();
		}

		@Override
		public void addMessage(String id, double latitude, double longitude, String title, String body, String nickname)
				throws RemoteException {
			// TODO Auto-generated method stub
			Marker marker = map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(title + " - " + nickname).snippet(body));
			markerIds.put(marker.getId(), id);
			markers.put(id, marker);
		}

		@Override
		public void editMessage(String id, String title, String body,
				int radius, long expiry) throws RemoteException {
			startActivityForResult(new Intent(Main.this, MessageEditor.class)
			.putExtra(Mosaic.EXTRA_ID, id)
			.putExtra(Mosaic.EXTRA_TITLE, title)
			.putExtra(Mosaic.EXTRA_BODY, body)
			.putExtra(Mosaic.EXTRA_RADIUS, radius)
			.putExtra(Mosaic.EXTRA_EXPIRY, expiry), REQUEST_UPDATE_MESSAGE);
		}

		@Override
		public void viewMessage(String id, String title, String body,
				String nickname) throws RemoteException {
			startActivityForResult(new Intent(Main.this, MessageViewer.class)
			.putExtra(Mosaic.EXTRA_ID, id)
			.putExtra(Mosaic.EXTRA_TITLE, title + " - " + nickname)
			.putExtra(Mosaic.EXTRA_BODY, body), REQUEST_VIEW_MESSAGE);
		}

		@Override
		public void updateMarker(String id, String title, String body, String nickname)
				throws RemoteException {
			Marker marker = markers.get(id);
			marker.setTitle(title + " - " + nickname);
			marker.setSnippet(body);
		}
	};

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
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
		iLocationService = null;
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		if (markerIds.containsKey(marker.getId())) {
			try {
				iLocationService.getMessage(markerIds.get(marker.getId()));
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			//TODO error!
		}
		return false;
	}

	@Override
	public void onMapLongClick(LatLng point) {
		startActivityForResult(new Intent(this, MessageEditor.class)
		.putExtra(Mosaic.EXTRA_LATITUDE, (int) (point.latitude * 1E6))
		.putExtra(Mosaic.EXTRA_LONGITUDE, (int) (point.longitude * 1E6)), REQUEST_INSERT_MESSAGE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if ((requestCode == REQUEST_INSERT_MESSAGE) && (resultCode == RESULT_OK)) {
			try {
				iLocationService.insertMessage(data.getStringExtra(Mosaic.EXTRA_TITLE),
						data.getStringExtra(Mosaic.EXTRA_BODY),
						data.getIntExtra(Mosaic.EXTRA_LATITUDE, 0),
						data.getIntExtra(Mosaic.EXTRA_LONGITUDE, 0),
						data.getIntExtra(Mosaic.EXTRA_RADIUS, 0),
						data.getLongExtra(Mosaic.EXTRA_EXPIRY, Mosaic.NEVER_EXPIRES));
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if ((requestCode == REQUEST_UPDATE_MESSAGE) && (resultCode == RESULT_OK)) {
			if (data.hasExtra(Mosaic.EXTRA_TITLE)) {
				try {
					iLocationService.updateMessage(data.getStringExtra(Mosaic.EXTRA_ID),
							data.getStringExtra(Mosaic.EXTRA_TITLE),
							data.getStringExtra(Mosaic.EXTRA_BODY),
							data.getIntExtra(Mosaic.EXTRA_RADIUS, 0),
							data.getLongExtra(Mosaic.EXTRA_EXPIRY, Mosaic.NEVER_EXPIRES));
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				String id = data.getStringExtra(Mosaic.EXTRA_ID);
				Marker marker = markers.get(id);
				markers.remove(id);
				markerIds.remove(marker.getId());
				marker.remove();
				try {
					iLocationService.deleteMessage(id);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else if ((requestCode == REQUEST_VIEW_MESSAGE) && (resultCode == RESULT_OK)) {
			try {
				iLocationService.reportMessage(markerIds.get(data.getStringExtra(Mosaic.EXTRA_ID)));
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
