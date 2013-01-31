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
