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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class MosaicMap extends Fragment implements ServiceConnection, OnMapLongClickListener, OnMarkerClickListener {

	private Button btnNickname;
	private HashMap<String, Long> messages = new HashMap<String, Long>();
	private HashMap<Long, Marker> markers = new HashMap<Long, Marker>();
	private HashMap<Long, Polygon> polygons = new HashMap<Long, Polygon>();

	private ILocationService iLocationService;
	private GoogleMap map;
	private static final int REQUEST_INSERT_MESSAGE = 0;
	private static final int REQUEST_UPDATE_MESSAGE = 1;
	private static final int REQUEST_VIEW_MESSAGE = 2;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GooglePlayServicesUtil.getOpenSourceSoftwareLicenseInfo(getActivity());
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.map, container, false);
        btnNickname = ((Button) rootView.findViewById(R.id.nickname));
        btnNickname.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Activity activity = getActivity();
				final EditText newNickname = new EditText(activity);
				newNickname.setText(btnNickname.getText().toString());
				new AlertDialog.Builder(activity)
				.setTitle("Change nickname")
				.setView(newNickname)
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
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
				.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				})
				.show();
			}

		});
        return rootView;
    }

	@Override
	public void onResume() {
		super.onResume();
		if (map == null)
			map = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map))
			.getMap();
		map.setMyLocationEnabled(true);
		map.setOnMapLongClickListener(this);
		UiSettings uiSettings = map.getUiSettings();
		uiSettings.setMyLocationButtonEnabled(false);
		uiSettings.setScrollGesturesEnabled(false);
		uiSettings.setZoomControlsEnabled(false);
		uiSettings.setZoomGesturesEnabled(false);
		map.setOnMarkerClickListener(this);
		getActivity();
		getActivity().bindService(new Intent(getActivity(), MosaicService.class), this, Context.BIND_AUTO_CREATE);
	}

	@Override
	public void onPause() {
		super.onPause();
		if (iLocationService != null) {
			try {
				iLocationService.setCallback(null);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		getActivity().unbindService(this);
	}

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

	private List<LatLng> getMarkerCircle(double lat, double lng, int radius) {
		ArrayList<LatLng> coords = new ArrayList<LatLng>();
		for (int a = 0; a < 360; a++)
			coords.add(new LatLng(getOffsetLatitude(lat, Math.sin(a) * radius),
					getOffsetLongitude(lat, lng, Math.cos(a) * radius)));
		return coords;
	}

	private double getOffsetLatitude(double lat, double offset) {
		return lat + Math.toDegrees(offset / Mosaic.EARTH_RADIUS);
	}

	private double getOffsetLongitude(double lat, double lng, double offset) {
		return lng + Math.toDegrees(offset / (Mosaic.EARTH_RADIUS * Math.cos(Math.toRadians(lat))));
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		if (messages.containsKey(marker.getId())) {
			try {
				iLocationService.getMessage(messages.get(marker.getId()));
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else
			marker.remove();
		return true;
	}

	@Override
	public void onMapLongClick(LatLng point) {
		startActivityForResult(new Intent(getActivity(), MessageEditor.class)
		.putExtra(Mosaic.EXTRA_LATITUDE, (int) (point.latitude * 1E6))
		.putExtra(Mosaic.EXTRA_LONGITUDE, (int) (point.longitude * 1E6)), REQUEST_INSERT_MESSAGE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		getActivity();
		if ((requestCode == REQUEST_INSERT_MESSAGE) && (resultCode == Activity.RESULT_OK)) {
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
		} else {
			getActivity();
			if ((requestCode == REQUEST_UPDATE_MESSAGE) && (resultCode == Activity.RESULT_OK)) {
				if (data.hasExtra(Mosaic.EXTRA_TITLE)) {
					try {
						iLocationService.updateMessage(data.getLongExtra(Mosaic.EXTRA_ID, Mosaic.INVALID_ID),
								data.getStringExtra(Mosaic.EXTRA_TITLE),
								data.getStringExtra(Mosaic.EXTRA_BODY),
								data.getIntExtra(Mosaic.EXTRA_RADIUS, 0),
								data.getLongExtra(Mosaic.EXTRA_EXPIRY, Mosaic.NEVER_EXPIRES));
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					Long id = data.getLongExtra(Mosaic.EXTRA_ID, Mosaic.INVALID_ID);
					if (markers.containsKey(id)) {
						messages.remove(markers.get(id).getId());
						markers.remove(id).remove();
					}
					if (polygons.containsKey(id))
						polygons.remove(id).remove();
					try {
						iLocationService.removeMessage(id);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else {
				if ((requestCode == REQUEST_VIEW_MESSAGE) && (resultCode == Activity.RESULT_OK)) {
					try {
						iLocationService.reportMessage(data.getLongExtra(Mosaic.EXTRA_ID, Mosaic.INVALID_ID));
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	private IMain.Stub iMain = new IMain.Stub() {

		@Override
		public void setCoordinates(double latitude, double longitude)
				throws RemoteException {
			if (latitude != Integer.MAX_VALUE) {
				if (map != null)
					map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 18F));
			} else {
				new AlertDialog.Builder(getActivity())
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
						getActivity().finish();
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
				new AlertDialog.Builder(getActivity())
				.setTitle("Sign in")
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						startActivity(new Intent(getActivity(), SignIn.class));
					}
				})
				.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						getActivity().finish();
					}
				})
				.show();
			}
		}

		@Override
		public void clearMessages() throws RemoteException {
			messages.clear();
			markers.clear();
			polygons.clear();
			map.clear();
		}

		@Override
		public void addMessage(long id, double latitude, double longitude, int radius, String title, String body, String nick)
				throws RemoteException {
			Long idL = (Long) id;
			Marker marker = null;
			if (markers.containsKey(idL))
				marker = markers.get(idL);
			if ((radius != Mosaic.RADIUS_UNCHANGED) && polygons.containsKey(idL))
				polygons.remove(idL).remove();
			if (marker != null) {
				marker.setTitle(title + " -" + nick);
				marker.setSnippet(body);
			} else {
				marker = map.addMarker(new MarkerOptions()
				.position(new LatLng(latitude, longitude))
				.title(title + " -" + nick)
				.snippet(body)
				.draggable(false));
				//				.icon(getMarkerIcon(latitude, longitude, radius)));
				markers.put(idL, marker);
				messages.put(marker.getId(), idL);
			}
			if (radius > 0)
				polygons.put(idL, map.addPolygon(new PolygonOptions()
				.addAll(getMarkerCircle(latitude, longitude, radius))
				.fillColor(getResources().getColor(R.color.radius_fill))
				.strokeColor(getResources().getColor(R.color.radius_stroke))
				.strokeWidth(1)));
			marker.showInfoWindow();
		}

		@Override
		public void editMessage(long id, String title, String body,
				int radius, long expiry) throws RemoteException {
			startActivityForResult(new Intent(getActivity(), MessageEditor.class)
			.putExtra(Mosaic.EXTRA_ID, id)
			.putExtra(Mosaic.EXTRA_TITLE, title)
			.putExtra(Mosaic.EXTRA_BODY, body)
			.putExtra(Mosaic.EXTRA_RADIUS, radius)
			.putExtra(Mosaic.EXTRA_EXPIRY, expiry), REQUEST_UPDATE_MESSAGE);
		}

		@Override
		public void viewMessage(long id, String title, String body, String nick) throws RemoteException {
			startActivityForResult(new Intent(getActivity(), MessageViewer.class)
			.putExtra(Mosaic.EXTRA_ID, id)
			.putExtra(Mosaic.EXTRA_TITLE, title + " -" + nick)
			.putExtra(Mosaic.EXTRA_BODY, body), REQUEST_VIEW_MESSAGE);
		}

		@Override
		public void removeMarker(long id) throws RemoteException {
			Long idL = (Long) id;
			if (markers.containsKey(idL)) {
				messages.remove(markers.get(idL).getId());
				markers.remove(idL).remove();
			}
			if (polygons.containsKey(idL))
				polygons.remove(idL).remove();
		}
	};

}
