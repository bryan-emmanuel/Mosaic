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

import static oauth.signpost.OAuth.OAUTH_VERIFIER;

import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class SignInWebViewClient extends WebViewClient {
	
	Main activity;
	
	public SignInWebViewClient(Main activity) {
		this.activity = activity;
	}

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		//TODO need to eval url
		if (url != null) {
			Uri uri = Uri.parse(url);
			uri.getQueryParameter(OAUTH_VERIFIER);
			// return OAUTH_VERIFIER to activity
		}
		return true;
	}
	
	@Override
	public void onPageFinished(WebView view, String url) {
		
	}

}
