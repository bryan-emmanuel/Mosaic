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
import com.google.android.gms.common.GooglePlayServicesUtil;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SignIn extends ListActivity {

	private static final String TAG = "SignIn";
//	private ProgressDialog loadingDialog;
	
	private MosaicService mosaicService;
	private AccountManager accountManager;
	private String[] accountNames;

    private static final int REQUEST_CODE_RECOVER_FROM_AUTH_ERROR = 1001;
    private static final int REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR = 1002;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);
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
		mosaicService.addAccount(this, REQUEST_CODE_RECOVER_FROM_AUTH_ERROR);
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_RECOVER_FROM_AUTH_ERROR) {
            if (data == null) {
                show("Unknown error, click the button again");
                return;
            }
            if (resultCode == RESULT_OK) {
                Log.i(TAG, "Retrying");
                //TODO
                show("need to retry");
                return;
            }
            if (resultCode == RESULT_CANCELED) {
                show("User rejected authorization.");
                return;
            }
            show("Unknown error, click the button again");
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
	
	private void loadAccountList() {
		accountNames = getAccountNames();
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, accountNames));
	}
	
    public void showErrorDialog(final int code) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
              Dialog d = GooglePlayServicesUtil.getErrorDialog(
                  code,
                  SignIn.this,
                  REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
              d.show();
            }
        });
    }
    
    public void show(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
            	Toast.makeText(SignIn.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String[] getAccountNames() {
    	accountManager = AccountManager.get(this);
        Account[] accounts = accountManager.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
        String[] names = new String[accounts.length];
        for (int i = 0; i < names.length; i++) {
            names[i] = accounts[i].name;
        }
        return names;
    }

	@Override
	protected void onPause() {
		super.onPause();
	}
}
