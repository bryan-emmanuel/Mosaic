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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MessageEditor extends Activity implements OnCheckedChangeListener, OnClickListener, OnSeekBarChangeListener {
	
	private Button btnSave;
	private Button btnDelete;
	private CheckBox expires;
	private DatePicker expiry;
	private TextView radius;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messageedit);
		Intent i = getIntent();
		if (i.hasExtra(Mosaic.EXTRA_TITLE))
			((TextView) findViewById(R.id.title)).setText(i.getStringExtra(Mosaic.EXTRA_TITLE));
		if (i.hasExtra(Mosaic.EXTRA_BODY))
			((TextView) findViewById(R.id.body)).setText(i.getStringExtra(Mosaic.EXTRA_BODY));
		if (i.hasExtra(Mosaic.EXTRA_RADIUS))
			((SeekBar) findViewById(R.id.radius)).setProgress(i.getIntExtra(Mosaic.EXTRA_RADIUS, 0));
		expires = (CheckBox) findViewById(R.id.expires);
		expiry = (DatePicker) findViewById(R.id.expiry);
		if (i.hasExtra(Mosaic.EXTRA_EXPIRY) && (i.getIntExtra(Mosaic.EXTRA_EXPIRY, Mosaic.NEVER_EXPIRES) != Mosaic.NEVER_EXPIRES)) {
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(i.getIntExtra(Mosaic.EXTRA_EXPIRY, Mosaic.NEVER_EXPIRES));
			expiry.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
			expires.setChecked(true);		
			expiry.setEnabled(true);
		} else		
			expiry.setEnabled(false);
		expires.setOnCheckedChangeListener(this);
		btnSave = (Button) findViewById(R.id.save);
		btnSave.setOnClickListener(this);
		btnDelete = (Button) findViewById(R.id.delete);
		if (i.hasExtra(Mosaic.EXTRA_ID))
			btnDelete.setOnClickListener(this);
		else
			btnDelete.setEnabled(false);
		radius = (TextView) findViewById(R.id.lbl_radius);
		((SeekBar) findViewById(R.id.radius)).setOnSeekBarChangeListener(this);
		setResult(RESULT_CANCELED);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		expiry.setEnabled(isChecked);
	}

	@Override
	public void onClick(View v) {
		if (v == btnSave) {
			Intent i = getIntent();
			i.putExtra(Mosaic.EXTRA_TITLE, ((TextView) findViewById(R.id.title)).getText().toString());
			i.putExtra(Mosaic.EXTRA_BODY, ((TextView) findViewById(R.id.body)).getText().toString());
			i.putExtra(Mosaic.EXTRA_RADIUS, ((SeekBar) findViewById(R.id.radius)).getProgress());
			if (expires.isChecked()) {
				Calendar c = Calendar.getInstance();
				c.set(Calendar.DAY_OF_MONTH, expiry.getDayOfMonth());
				c.set(Calendar.MONTH, expiry.getMonth());
				c.set(Calendar.YEAR, expiry.getYear());
				i.putExtra(Mosaic.EXTRA_EXPIRY, c.getTimeInMillis());
			} else
				i.putExtra(Mosaic.EXTRA_EXPIRY, -1L);
			setResult(RESULT_OK, i);
			finish();
		} else if (v == btnDelete) {
			Intent i = getIntent();
			i.removeExtra(Mosaic.EXTRA_TITLE);
			setResult(RESULT_OK);
			finish();
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		radius.setText(String.format(getString(R.string.radius_progress), progress));
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	}

}
