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

import com.google.api.client.auth.oauth.OAuthAuthorizeTemporaryTokenUrl;
import com.google.api.client.auth.oauth.OAuthCallbackUrl;
import com.google.api.client.auth.oauth.OAuthCredentialsResponse;
import com.google.api.client.auth.oauth.OAuthGetAccessToken;
import com.google.api.client.auth.oauth.OAuthGetTemporaryToken;
import com.google.api.client.auth.oauth.OAuthHmacSigner;
import com.google.api.client.auth.oauth.OAuthParameters;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.javanet.NetHttpTransport;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

public class OAuthHelper {

	private static final String TAG = "OAuthManager";

	protected static final String realm = "https://mosaic-messaging.appspot.com";
	private static final String requestURL = realm + "/_ah/OAuthGetRequestToken";
	private static final String accessURL = realm + "/_ah/OAuthGetAccessToken";
	private static final String authorizeURL = realm + "/_ah/OAuthAuthorizeToken";
	protected static final String callbackURL = "mosaic://signin";
	protected static final String callbackScheme = "mosaic";

	private static OAuthHelper oAuthManager;
	private String consumerKey;
	private String token;
	private OAuthHmacSigner signer;

	private OAuthHelper(String consumerKey, String consumerSecret) {
		if (signer == null) {
			signer = new OAuthHmacSigner();
			signer.clientSharedSecret = consumerSecret;
		}
		this.consumerKey = consumerKey;
	}

	public static OAuthHelper getInstance(String consumerKey, String consumerSecret) {
		if (oAuthManager == null)
			oAuthManager = new OAuthHelper(consumerKey, consumerSecret);
		return oAuthManager;
	}

	public static OAuthHelper getInstance(String consumerKey, String consumerSecret, String token, String secret) {
		oAuthManager = getInstance(consumerKey, consumerSecret);
		oAuthManager.token = token;
		oAuthManager.signer.tokenSharedSecret = secret;
		return oAuthManager;
	}

	public void loadAuthURL(SignIn activity) {
		new AuthorizationURLLoader(activity).execute();
	}

	public static boolean isCallback(String url) {
		if (url == null)
			return false;
		return callbackScheme.equals(Uri.parse(url).getScheme());
	}

	public void retrieveAccessToken(SignIn activity, String url) {
		OAuthCallbackUrl callback = new OAuthCallbackUrl(url);
		new AccessTokenLoader(activity, callback.verifier).execute();
	}

	public boolean hasCredentials() {
		return token != null && signer.tokenSharedSecret != null;
	}
	
	public HttpRequest getSignedRequest(String url, HttpContent content) {

		OAuthParameters parameters = new OAuthParameters();
		parameters.consumerKey = consumerKey;
		parameters.token = token;
		parameters.signer = signer;
		parameters.realm = realm;

		HttpRequestFactory requestFactory = (new NetHttpTransport()).createRequestFactory(parameters);

		HttpRequest request = null;

		if (content != null) {
			try {
				request = requestFactory.buildPostRequest(new GenericUrl(url), content);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				request = requestFactory.buildGetRequest(new GenericUrl(url));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return request;
	}

	class AuthorizationURLLoader extends AsyncTask<Void, Void, String> {

		private SignIn activity;

		public AuthorizationURLLoader(SignIn activity) {
			this.activity = activity;
		}

		@Override
		protected String doInBackground(Void... arg0) {
			OAuthGetTemporaryToken tempTokenRequest = new OAuthGetTemporaryToken(requestURL);
			tempTokenRequest.transport = new NetHttpTransport();
			tempTokenRequest.callback = callbackURL;
			tempTokenRequest.consumerKey = consumerKey;
			tempTokenRequest.signer = signer;
			// parse the url
			OAuthCredentialsResponse tempTokenResponse;
			try {
				tempTokenResponse = tempTokenRequest.execute();
				token = tempTokenResponse.token;
				signer.tokenSharedSecret = tempTokenResponse.tokenSecret;

				OAuthAuthorizeTemporaryTokenUrl authorizationRequest = new OAuthAuthorizeTemporaryTokenUrl(authorizeURL);
				authorizationRequest.temporaryToken = token;

				return authorizationRequest.build();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String url) {
			activity.loadWebView(url);
		}

	}

	class AccessTokenLoader extends AsyncTask<Void, Void, Void> {

		private SignIn activity;
		private String verifier;

		public AccessTokenLoader(SignIn activity, String verifier) {
			this.activity = activity;
			this.verifier = verifier;
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			OAuthGetAccessToken accessTokenRequest = new OAuthGetAccessToken(accessURL);
			accessTokenRequest.transport = new NetHttpTransport();
			accessTokenRequest.temporaryToken = token;
			accessTokenRequest.signer = signer;
			accessTokenRequest.consumerKey = consumerKey;
			accessTokenRequest.verifier = verifier;

			OAuthCredentialsResponse accessTokenResponse;
			try {
				accessTokenResponse = accessTokenRequest.execute();
				token = accessTokenResponse.token;
				signer.tokenSharedSecret = accessTokenResponse.tokenSecret;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				token = null;
				signer.tokenSharedSecret = null;
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void arg0) {
			activity.storeTokenSecret(token, signer.tokenSharedSecret);
		}

	}

}
