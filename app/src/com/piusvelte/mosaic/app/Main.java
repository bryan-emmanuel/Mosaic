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
package com.piusvelte.mosaic.app;

import java.util.ArrayList;
import java.util.List;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.view.Menu;
import android.webkit.WebView;

public class Main extends Activity implements ServiceConnection {
	
	//TODO: need to call
	// GooglePlayServicesUtil.getOpenSourceSoftwareLicenseInfo(context)

	private static final String preferenceToken = "oauth_token";
	private static final String preferenceSecret = "oauth_secret";
	
	protected int latitude = 0;
	protected int longitude = 0;
	protected OAuthManager oAuthManager;
	private MessageLoader messageLoader;
	private ProgressDialog loadingDialog;
	protected List<Message> messages = new ArrayList<Message>();
	private ILocationService iLocationService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		loadingDialog = new ProgressDialog(getApplicationContext());
		loadingDialog.setMessage("loading");
		loadingDialog.setCancelable(true);
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
		bindService(new Intent(getApplicationContext(), LocationService.class), this, BIND_AUTO_CREATE);
		if (hasSignedIn()) {
			loadMessages();
		} else {
			new AlertDialog.Builder(getApplicationContext())
			.setTitle("Sign in")
			.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					try {
						oAuthManager.loadAuthURL(Main.this);
					} catch (OAuthMessageSignerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (OAuthNotAuthorizedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (OAuthExpectationFailedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (OAuthCommunicationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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

	private boolean hasSignedIn() {
		SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
		if (sharedPreferences.contains(preferenceToken) && sharedPreferences.contains(preferenceSecret)) {
			String token = sharedPreferences.getString(preferenceToken, null);
			String secret = sharedPreferences.getString(preferenceSecret, null);
			if ((token != null) && (secret != null)) {
				oAuthManager = new OAuthManager(getString(R.string.consumer_key), getString(R.string.consumer_secret), token, secret);
				return true;
			}
		}
		oAuthManager = new OAuthManager(getString(R.string.consumer_key), getString(R.string.consumer_secret));
		return false;
	}

	private void loadMessages() {
		loadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				if (!messageLoader.isCancelled())
					messageLoader.cancel(true);
			}
			
		});
		loadingDialog.show();
		messageLoader = new MessageLoader(this);
		messageLoader.execute();
	}
	
	protected void reloadMessagesList() {
		if (loadingDialog.isShowing())
			loadingDialog.dismiss();
		//TODO list adapter reload
	}

	protected void doAuth(String url) {
		if (url != null) {
			WebView webView = new WebView(getApplicationContext());
			setContentView(webView);
			webView.setWebViewClient(new SignInWebViewClient(this));
			webView.loadUrl(url);
		} else
			finish();
	}
	
	private void enableGPSPrompt() {
		new AlertDialog.Builder(getApplicationContext())
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
	
	private IMain.Stub iMain = new IMain.Stub() {
		
		@Override
		public void setGPSEnabled(boolean enabled) throws RemoteException {
			// TODO Auto-generated method stub
			if (!enabled)
				enableGPSPrompt();
		}
		
		@Override
		public void setCoordinates(int latitude, int longitude)
				throws RemoteException {
			// TODO Auto-generated method stub
			
		}
	};

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		// TODO Auto-generated method stub
		iLocationService = ILocationService.Stub.asInterface(service);
		try {
			iLocationService.setCallback(iMain);
			iLocationService.checkGPS();
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

}
