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
