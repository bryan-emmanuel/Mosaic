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
