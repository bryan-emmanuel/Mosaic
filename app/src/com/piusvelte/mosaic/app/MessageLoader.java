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

import android.os.AsyncTask;

public class MessageLoader extends AsyncTask<Void, String, Void> {
	
	private Main activity;
	private String token;
	private String secret;
	
	public MessageLoader(Main activity, String token, String secret) {
		this.activity = activity;
		this.token = token;
		this.secret = secret;
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected void onProgressUpdate(String... values) {
		
	}
	
	@Override
	protected void onPostExecute(Void result) {
		
	}

}
