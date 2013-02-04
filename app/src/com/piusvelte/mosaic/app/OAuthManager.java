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

import org.apache.http.client.methods.HttpUriRequest;

import android.os.AsyncTask;
import android.util.Log;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import oauth.signpost.signature.HmacSha1MessageSigner;

public class OAuthManager {
	
	private static final String TAG = "OAuthManager";

	private OAuthConsumer oAuthConsumer;
	private OAuthProvider oAuthProvider;
	
	public OAuthManager(String apiKey, String apiSecret) {
		oAuthConsumer = new CommonsHttpOAuthConsumer(apiKey, apiSecret);
		oAuthConsumer.setMessageSigner(new HmacSha1MessageSigner());
	}
	
	public OAuthManager(String apiKey, String apiSecret, String token, String tokenSecret) {
		this(apiKey, apiSecret);
		oAuthConsumer.setTokenWithSecret(token, tokenSecret);
	}
	
	public void loadAuthURL(Main activity) throws OAuthMessageSignerException, OAuthNotAuthorizedException, OAuthExpectationFailedException, OAuthCommunicationException {
		oAuthProvider = new CommonsHttpOAuthProvider("https://mosaic-messaging.appspot.com/_ah/OAuthGetRequestToken",
				"https://mosaic-messaging.appspot.com/_ah/OAuthGetAccessToken",
				"https://mosaic-messaging.appspot.com/_ah/OAuthAuthorizeToken",
				HttpClientManager.getThreadSafeClient(activity.getApplicationContext()));
		oAuthProvider.setOAuth10a(false);
		new URLLoader(activity).execute();
	}

	public void retrieveAccessToken(String verifier, Main activity) {
		new TokenLoader(activity).execute();
	}

	public HttpUriRequest getSignedRequest(HttpUriRequest httpRequest) {
		try {
			oAuthConsumer.sign(httpRequest);
			return httpRequest;
		} catch (OAuthMessageSignerException e) {
			Log.e(TAG,e.toString());
		} catch (OAuthExpectationFailedException e) {
			Log.e(TAG,e.toString());
		} catch (OAuthCommunicationException e) {
			Log.e(TAG,e.toString());
		}
		return null;
	}

	public String getToken() {
		return oAuthConsumer.getToken();
	}

	public String getTokenSecret() {
		return oAuthConsumer.getTokenSecret();
	}
	
	class URLLoader extends AsyncTask<Void, Void, String> {
		
		private Main activity;
		
		public URLLoader(Main activity) {
			this.activity = activity;
		}

		@Override
		protected String doInBackground(Void... arg0) {
			try {
				return oAuthProvider.retrieveRequestToken(oAuthConsumer, "mosaic://oauth");
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
			return null;
		}
		
		@Override
		protected void onPostExecute(String url) {
			activity.doAuth(url);
		}
		
	}
	
	class TokenLoader extends AsyncTask<Void, Void, Boolean> {
		
		private Main activity;
		private String verifier;
		
		public TokenLoader(Main activity) {
			this.activity = activity;
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {
			try {
				oAuthProvider.retrieveAccessToken(oAuthConsumer, verifier);
				return true;
			} catch (OAuthMessageSignerException e) {
				Log.e(TAG, e.toString());
			} catch (OAuthNotAuthorizedException e) {
				Log.e(TAG, e.toString());
			} catch (OAuthExpectationFailedException e) {
				Log.e(TAG, e.toString());
			} catch (OAuthCommunicationException e) {
				Log.e(TAG, e.toString());
			}
			return false;
		}
		
		@Override
		protected void onPostExecute(Boolean hasToken) {
			//TODO success?
		}
		
	}

}
