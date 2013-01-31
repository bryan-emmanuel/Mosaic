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
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.expressme.openid.OpenIdException;

public class Nonce {

	public static final String Table = "nonces";
	
	public static enum Columns {
		id, nonce;
	}
	
	private static final long ONE_HOUR = 3600000L;
	
	private Nonce() {
	}
	
	protected static void storeNonce(String nonce, DatabaseManager databaseManager) throws OpenIdException {
		long nonceTime = getNonceTime(nonce);
		if (isExpired(nonceTime))
			throw new OpenIdException("authentication attempt expired");
		Connection connection;
		try {
			connection = databaseManager.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new OpenIdException("authentication failure");
		}
		try {
			Statement statement = connection.createStatement();
			statement.execute("INSERT INTO " + Table + " VALUES ('" + nonce + "');");
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
	
	private static long getNonceTime(String nonce) throws OpenIdException {
		try {
			return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
			.parse(nonce.substring(0, 19) + "+0000")
			.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			throw new OpenIdException(e.getMessage());
		}
	}
	
	private static boolean isExpired(long nonce) {
		return Math.abs(System.currentTimeMillis() - nonce) > ONE_HOUR;
	}

}
