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
package com.piusvelte.mosaic.server;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.expressme.openid.OpenIdException;

public class Account {
	
	public static final String Table = "accounts";
	
	public static enum Columns {
		id, identity, email, firstname, lastname, gender, language;
	}
	
	private Account() {
	}
	
	protected static void storeAccount(DatabaseManager databaseManager, String identity, String firstname, String lastname, String email, String gender, String language) {
		Connection connection;
		try {
			connection = databaseManager.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new OpenIdException("authentication failure");
		}
		try {
			Statement statement = connection.createStatement();
			statement.execute("INSERT INTO " + Table + " VALUES ('" + identity + "','" + firstname + "','" + lastname + "','" + email + "','" + gender + "','" + language + "');");
			statement.close();
    		try {
    			connection.close();
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}
    		databaseManager.releaseConnection();
		} catch (SQLException e) {
    		try {
    			connection.close();
    		} catch (SQLException e1) {
    			e1.printStackTrace();
    		}
    		databaseManager.releaseConnection();
			e.printStackTrace();
			throw new OpenIdException("invalid authentication");
		}
	}

}
