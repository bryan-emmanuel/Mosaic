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

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SignIn extends ListActivity {

	private static final String TAG = "SignIn";
	private AccountManager accountManager;
	private String[] accountNames;
	

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
		getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE)
		.edit()
		.putString(getString(R.string.preference_account_name), accountNames[position])
		.commit();
		finish();
	}
	
	private void loadAccountList() {
		accountNames = getAccountNames();
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, accountNames));
	}
	
	protected void storeEmail(String email) {
	}

    private String[] getAccountNames() {
    	accountManager = AccountManager.get(this);
        Account[] accounts = accountManager.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
        String[] names = new String[accounts.length];
        for (int i = 0; i < names.length; i++)
            names[i] = accounts[i].name;
        return names;
    }
}
