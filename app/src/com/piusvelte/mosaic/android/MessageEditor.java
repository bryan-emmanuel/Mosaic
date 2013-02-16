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

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.TextView;

public class MessageEditor extends Activity implements OnCheckedChangeListener {
	
	private Button btnSave;
	private CheckBox expires;
	private DatePicker expiry;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messageedit);
		expires = (CheckBox) findViewById(R.id.expires);
		expires.setOnCheckedChangeListener(this);
		expiry = (DatePicker) findViewById(R.id.expiry);
		expiry.setEnabled(false);
		btnSave = (Button) findViewById(R.id.save);
		btnSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = getIntent();
				i.putExtra(Main.EXTRA_TITLE, ((TextView) findViewById(R.id.title)).getText().toString());
				i.putExtra(Main.EXTRA_BODY, ((TextView) findViewById(R.id.body)).getText().toString());
				i.putExtra(Main.EXTRA_RADIUS, ((SeekBar) findViewById(R.id.radius)).getProgress());
				if (expires.isChecked()) {
					Calendar c = Calendar.getInstance();
					c.set(Calendar.DAY_OF_MONTH, expiry.getDayOfMonth());
					c.set(Calendar.MONTH, expiry.getMonth());
					c.set(Calendar.YEAR, expiry.getYear());
					i.putExtra(Main.EXTRA_EXPIRY, c.getTimeInMillis());
				} else
					i.putExtra(Main.EXTRA_EXPIRY, -1L);
				setResult(RESULT_OK, i);
				finish();
			}
			
		});
		setResult(RESULT_CANCELED);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked)
			expiry.setMinDate(System.currentTimeMillis());
		expiry.setEnabled(isChecked);
	}

}
