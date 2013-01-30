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

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Mosaic
 */
public class Mosaic extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final String openidRealm = "http://";
	private static final String openidReturnto = "http:///openid";
	
	private DatabaseManager databaseManager;
	
    public Mosaic() {
        super();
    }
    
    private static final String propertiesFile = "/WEB-INF/mosaic.properties";
    private enum Props {

    	databasehost(""), databaseport("0"), databasename(""), databaseuser(""), databasepass(""), databasepool("0");
    	
    	public String value;
    	
    	private Props(String value) {
    		this.value = value;
    	}
    	
    }
    
    @Override
    public void init() throws ServletException {
    	super.init();
    	Properties properties = new Properties();
    	try {
			properties.load(getServletContext().getResourceAsStream(propertiesFile));
			for (Props prop : Props.values()) {
				if (properties.containsKey(prop.name()));
					prop.value = properties.getProperty(prop.name(), "");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    	databaseManager = new DatabaseManager(Props.databasehost.value, Integer.parseInt(Props.databaseport.value), Props.databasename.value, Props.databaseuser.value, Props.databasepass.value, Integer.parseInt(Props.databasepool.value));
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		OpenIdAuthenticator authenticator = new OpenIdAuthenticator(databaseManager, openidRealm, openidReturnto);
		authenticator.doGet(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
