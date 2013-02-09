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

import com.google.android.gms.auth.GoogleAuthUtil;
import com.piusvelte.mosaic.android.MosaicService.AddAccountTask;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SignIn extends ListActivity implements OnCancelListener {

	private static final String TAG = "SignIn";
	private ProgressDialog loadingDialog;
	
	private MosaicService mosaicService;
	private AccountManager accountManager;
	private String[] accountNames;
	private AddAccountTask task;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);
		loadingDialog = new ProgressDialog(this);
		loadingDialog.setMessage("loading");
		loadingDialog.setCancelable(true);
	}

	@Override
	public void onResume() {
		super.onResume();
		loadAccountList();
	}

	@Override
	protected void onListItemClick(ListView list, View view, int position, long id) {
		super.onListItemClick(list, view, position, id);
        mosaicService = MosaicService.getInstance(getString(R.string.webapp_client_id)).setEmail(accountNames[position]);
		task = mosaicService.addAccount(this);
		loadingDialog.setOnCancelListener(this);
		loadingDialog.show();
		task.execute();
	}
	
	protected void taskFinished(String message) {
		if (loadingDialog.isShowing())
			loadingDialog.dismiss();
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
		finish();
	}
	
	private void loadAccountList() {
		accountNames = getAccountNames();
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, accountNames));
	}
	
	protected void storeEmail(String email) {
		getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE)
		.edit()
		.putString(getString(R.string.preference_account_email), email)
		.commit();
	}

    private String[] getAccountNames() {
    	accountManager = AccountManager.get(this);
        Account[] accounts = accountManager.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
        String[] names = new String[accounts.length];
        for (int i = 0; i < names.length; i++)
            names[i] = accounts[i].name;
        return names;
    }

	@Override
	protected void onPause() {
		super.onPause();
		if ((task != null) && !task.isCancelled())
			task.cancel(true);
		if (loadingDialog.isShowing())
			loadingDialog.dismiss();
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		dialog.cancel();
		if ((task != null) && !task.isCancelled())
			task.cancel(true);
	}
}
