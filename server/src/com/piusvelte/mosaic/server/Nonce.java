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
