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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MessageViewer extends Activity {
	
	private Button btnReport;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messageview);
		Intent i = getIntent();
		if (i.hasExtra(Mosaic.EXTRA_TITLE))
			((TextView) findViewById(R.id.title)).setText(i.getStringExtra(Mosaic.EXTRA_TITLE));
		if (i.hasExtra(Mosaic.EXTRA_BODY))
			((TextView) findViewById(R.id.body)).setText(i.getStringExtra(Mosaic.EXTRA_BODY));
		btnReport = (Button) findViewById(R.id.report);
		btnReport.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_OK, getIntent());
				finish();
			}
			
		});
		setResult(RESULT_CANCELED);
	}

}
