package com.piusvelte.mosaic.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseManager {

	private String host = "";
	private int port = 0;
	private String name = "";
	private String user = "";
	private String pass = "";
	private int pool = 0;

	private ArrayList<Connection> connections = new ArrayList<Connection>();

	public DatabaseManager(String host, int port, String name, String user, String pass, int poolsize) {
		this.host = host;
		this.port = port;
		this.name = name;
		this.user = user;
		this.pass = pass;
		this.pool = poolsize;
	}

	protected Connection getConnection() throws SQLException {
		if (connections.size() < pool) {
			StringBuilder connectionString = new StringBuilder();
			connectionString.append("jdbc:postgresql:");
			connectionString.append(host);
			connectionString.append(":");
			connectionString.append(port);
			connectionString.append("/");
			connectionString.append(name);
			Connection connection =  DriverManager.getConnection(connectionString.toString(), user, pass);
			connections.add(connection);
			return connection;
		} else
			throw new SQLException("no available connections");
	}

	protected void releaseConnection() {
		for (int connectionsIndex = 0, connectionsSize = connections.size(); connectionsIndex < connectionsSize; connectionsIndex++) {
			boolean isClosed = true;
			try {
				isClosed = connections.get(connectionsIndex).isClosed();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (isClosed) {
				connections.remove(connectionsIndex);
				connectionsIndex--;
				connectionsSize--;
			}
		}
	}

}
