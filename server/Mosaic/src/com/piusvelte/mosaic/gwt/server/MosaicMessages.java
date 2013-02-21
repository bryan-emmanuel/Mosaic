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
package com.piusvelte.mosaic.gwt.server;

import com.piusvelte.mosaic.gwt.server.EMF;

import com.google.api.server.spi.config.Api;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.users.User;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@Api(name = "mosaicmessages",
clientIds = {Ids.WEB_CLIENT_ID,
		Ids.ANDROID_CLIENT_ID},
		audiences = {Ids.ANDROID_AUDIENCE}
		)
public class MosaicMessages {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method.
	 *
	 * @return List of all entities persisted.
	 * @throws OAuthRequestException 
	 */
	@SuppressWarnings({ "cast", "unchecked" })
	public List<MosaicMessage> listMosaicMessage(@Named("latitude") int lat1E6, @Named("longitude") int lon1E6, User user) throws OAuthRequestException {
		if (user != null) {
			EntityManager mgr = getEntityManager();
			List<MosaicMessage> result = new ArrayList<MosaicMessage>();
			try {
				float lat = coordinateFrom1E6(lat1E6);
				float lon = coordinateFrom1E6(lon1E6);
				List<String> geocells = getGeocells(lat, lon);
				StringBuffer queryStr = new StringBuffer("select from MosaicMessage as MosaicMessage where geocells in (");
				boolean first = true;
				for (String geocell : geocells) {
					if (first)
						first = false;
					else
						queryStr.append(",");
					queryStr.append("\'" + geocell + "\'");
				}
				queryStr.append(")");
				Query query = mgr
						.createQuery(queryStr.toString());
				List<MosaicMessage> msgs = query.getResultList();
				for (MosaicMessage msg : msgs) {
					if ((msg.getReports() <= msg.getVisits())
							&& (distance(lat, lon, msg.getLatitude(), msg.getLongitude()) < msg.getRadius()))
						result.add(msg);
				}
			} finally {
				mgr.close();
			}
			return result;
		} else
			throw new OAuthRequestException("Invalid user.");
	}

	private static int distanceLatitude(float north, float south) {
		return (int) Math.ceil((Math.toRadians(Math.abs(north - south)) * EARTHS_RADIUS));
	}

	private static int distanceLongitude(float latitude, float east, float west) {
		return (int) Math.ceil((Math.toRadians(Math.abs(east - west)) * Math.cos(Math.toRadians(latitude)) * EARTHS_RADIUS));
	}
	
	private static float coordinateFrom1E6(int coordinate) {
		return (float) (coordinate / 1E6);
	}

	private static final int EARTHS_RADIUS = 6378135;
	private static final int MAX_RESOLUTION = 13;
	private static final float MIN_LONGITUDE = -180.0f;
	private static final float MAX_LONGITUDE = 180.0f;
	private static final float MIN_LATITUDE = -90.0f;
	private static final float MAX_LATITUDE = 90.0f;
	private static final int GEOCELL_GRID_SIZE = 4;
	private static final String GEOCELL_ALPHABET = "0123456789abcdef";

	private static char geocellChar(int[] pos) {
		return GEOCELL_ALPHABET.charAt(
				(pos[1] & 2) << 2 |
				(pos[0] & 2) << 1 |
				(pos[1] & 1) << 1 |
				(pos[0] & 1) << 0);
	}
	
	private static String getGeocell(float latitude, float longitude, int resolution) {
		float north = MAX_LATITUDE;
		float south = MIN_LATITUDE;
		float east = MAX_LONGITUDE;
		float west = MIN_LONGITUDE;
		StringBuilder geocell = new StringBuilder();
		while (geocell.length() < resolution) {
			float subcellLonSpan = (east - west) / GEOCELL_GRID_SIZE;
			float subcellLatSpan = (north - south) / GEOCELL_GRID_SIZE;
			int x = Math.min((int) (GEOCELL_GRID_SIZE * (latitude - west) / (east - west)),
					GEOCELL_GRID_SIZE - 1);
			int y = Math.min((int) (GEOCELL_GRID_SIZE * (latitude - south) / (north - south)),
					GEOCELL_GRID_SIZE - 1);
			int pos[] = {x, y};
			geocell.append(geocellChar(pos));
			south +=  subcellLatSpan * y;
			north = south + subcellLatSpan;
			west += subcellLonSpan * x;
			east = west + subcellLonSpan;
		}
		return geocell.toString();
	}
	
	private static String getGeocellWithinRadius(float latitude, float longitude, int resolution, int radius) {
		float north = MAX_LATITUDE;
		float south = MIN_LATITUDE;
		float east = MAX_LONGITUDE;
		float west = MIN_LONGITUDE;
		StringBuilder geocell = new StringBuilder();
		boolean radiusMet = false;
		while ((geocell.length() < resolution) && !radiusMet) {
			if ((distanceLatitude(latitude, north) > radius)
					&& (distanceLatitude(latitude, south) > radius)
					&& (distanceLongitude(latitude, longitude, east) > radius)
					&& (distanceLongitude(latitude, longitude, west) > radius))
				radiusMet = true;
			float subcellLonSpan = (east - west) / GEOCELL_GRID_SIZE;
			float subcellLatSpan = (north - south) / GEOCELL_GRID_SIZE;
			int x = Math.min((int) (GEOCELL_GRID_SIZE * (latitude - west) / (east - west)),
					GEOCELL_GRID_SIZE - 1);
			int y = Math.min((int) (GEOCELL_GRID_SIZE * (latitude - south) / (north - south)),
					GEOCELL_GRID_SIZE - 1);
			int pos[] = {x, y};
			geocell.append(geocellChar(pos));
			south +=  subcellLatSpan * y;
			north = south + subcellLatSpan;
			west += subcellLonSpan * x;
			east = west + subcellLonSpan;
		}
		return geocell.toString();
	}
	
	private static List<String> getGeocells(float latitude, float longitude) {
		List<String> geocells = new ArrayList<String>();
		String lastGeocell = "";
		for (int resolution = MAX_RESOLUTION; resolution > 0; resolution--) {
			String nextGeocell = getGeocell(latitude, longitude, resolution);
			if (!nextGeocell.equals(lastGeocell))
				geocells.add(nextGeocell);
			else
				break;
		}
		return geocells;
	}
	
	private static List<String> getGeocellsWithinRadius(float latitude, float longitude, int radius) {
		List<String> geocells = new ArrayList<String>();
		String lastGeocell = "";
		for (int resolution = MAX_RESOLUTION; resolution > 0; resolution--) {
			String nextGeocell = getGeocellWithinRadius(latitude, longitude, resolution, radius);
			if (!nextGeocell.equals(lastGeocell))
				geocells.add(nextGeocell);
			else
				break;
			lastGeocell = nextGeocell;
		}
		return geocells;
	}

	public static double distance(double lat1, double lon1, double lat2, double lon2) {
		double p1lat = Math.toRadians(lat1);
		double p1lon = Math.toRadians(lon1);
		double p2lat = Math.toRadians(lat2);
		double p2lon = Math.toRadians(lon2);
		return EARTHS_RADIUS
				* Math.acos(makeDoubleInRange(Math.sin(p1lat) * Math.sin(p2lat)
						+ Math.cos(p1lat) * Math.cos(p2lat)
						* Math.cos(p2lon - p1lon)));
	}

	public static double makeDoubleInRange(double d) {
		if (d > 1)
			return 1;
		else if (d < -1)
			return -1;
		return d;
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	public MosaicMessage getMosaicMessage(@Named("id") String id, User user) throws OAuthRequestException {
		if (user != null) {
			EntityManager mgr = getEntityManager();
			MosaicMessage mosaicmessage = null;
			try {
				mosaicmessage = mgr.find(MosaicMessage.class, KeyFactory.stringToKey(id));
			} finally {
				mgr.close();
			}
			return mosaicmessage;
		} else
			throw new OAuthRequestException("Invalid user.");
	}

	/**
	 * This inserts the entity into App Engine datastore.
	 * It uses HTTP POST method.
	 *
	 * @param mosaicmessage the entity to be inserted.
	 * @return The inserted entity.
	 */
	public MosaicMessage insertMosaicMessage(MosaicMessage mosaicmessage, User user) throws OAuthRequestException {
		if (user != null) {
			mosaicmessage.setGeocells(getGeocellsWithinRadius(mosaicmessage.getLatitude(), mosaicmessage.getLongitude(), mosaicmessage.getRadius()));
			EntityManager mgr = getEntityManager();
			try {
				mgr.persist(mosaicmessage);
			} finally {
				mgr.close();
			}
			return mosaicmessage;
		} else
			throw new OAuthRequestException("Invalid user.");
	}

	/**
	 * This method is used for updating a entity.
	 * It uses HTTP PUT method.
	 *
	 * @param mosaicmessage the entity to be updated.
	 * @return The updated entity.
	 */
	public MosaicMessage updateMosaicMessage(MosaicMessage mosaicmessage, User user) throws OAuthRequestException {
		if (user != null) {
			mosaicmessage.setGeocells(getGeocellsWithinRadius(mosaicmessage.getLatitude(), mosaicmessage.getLongitude(), mosaicmessage.getRadius()));
			EntityManager mgr = getEntityManager();
			try {
				mgr.persist(mosaicmessage);
			} finally {
				mgr.close();
			}
			return mosaicmessage;
		} else
			throw new OAuthRequestException("Invalid user.");
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 * @return The deleted entity.
	 */
	public MosaicMessage removeMosaicMessage(@Named("id") String id, User user) throws OAuthRequestException {
		if (user != null) {
			EntityManager mgr = getEntityManager();
			MosaicMessage mosaicmessage = null;
			try {
				mosaicmessage = mgr.find(MosaicMessage.class, KeyFactory.stringToKey(id));
				mgr.remove(mosaicmessage);
			} finally {
				mgr.close();
			}
			return mosaicmessage;
		} else
			throw new OAuthRequestException("Invalid user.");
	}

	public void reportMosaicMessage(@Named("id") String id, User user) throws OAuthRequestException {
		if (user != null) {
			EntityManager mgr = getEntityManager();
			MosaicMessage mosaicmessage = null;
			try {
				mosaicmessage = mgr.find(MosaicMessage.class, KeyFactory.stringToKey(id));
				mosaicmessage.setReports(mosaicmessage.getReports() + 2);
				mgr.persist(mosaicmessage);
			} finally {
				mgr.close();
			}
		} else
			throw new OAuthRequestException("Invalid user.");
	}

	public void viewMosaicMessage(@Named("id") String id, User user) throws OAuthRequestException {
		if (user != null) {
			EntityManager mgr = getEntityManager();
			try {
				MosaicMessage mosaicmessage = mgr.find(MosaicMessage.class, KeyFactory.stringToKey(id));
				List<String> visitors = mosaicmessage.getVisitors();
				MosaicUser mosaicuser = (MosaicUser) mgr.createQuery("select from MosaicUser as MosaicUser where email = :email").setParameter("email", user.getEmail()).getSingleResult();
				if (!visitors.contains(mosaicuser.getEmail())) {
					visitors.add(mosaicuser.getEmail());
					mosaicmessage.setVisitors(visitors);
				}
				mosaicmessage.setVisits(mosaicmessage.getVisits() + 1);
				mgr.persist(mosaicmessage);
			} finally {
				mgr.close();
			}
		} else
			throw new OAuthRequestException("Invalid user.");
	}

	private static EntityManager getEntityManager() {
		return EMF.get().createEntityManager();
	}

}
