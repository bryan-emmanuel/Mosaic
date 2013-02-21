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
	public List<MosaicMessage> listMosaicMessage(@Named("latitudeE6") int latitudeE6, @Named("longitudeE6") int longitudeE6, User user) throws OAuthRequestException {
		if (user != null) {
			EntityManager mgr = getEntityManager();
			List<MosaicMessage> result = new ArrayList<MosaicMessage>();
			try {
				double lat = GeocellHelper.fromE6(latitudeE6);
				double lon = GeocellHelper.fromE6(longitudeE6);
				List<String> geocells = GeocellHelper.getGeocells(lat, lon);
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
							&& (GeocellHelper.distance(lat, lon, GeocellHelper.fromE6(msg.getLatitudeE6()), GeocellHelper.fromE6(msg.getLongitudeE6())) < msg.getRadius()))
						result.add(msg);
				}
			} finally {
				mgr.close();
			}
			return result;
		} else
			throw new OAuthRequestException("Invalid user.");
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	public MosaicMessage getMosaicMessage(@Named("encodedKey") String encodedKey, User user) throws OAuthRequestException {
		if (user != null) {
			EntityManager mgr = getEntityManager();
			MosaicMessage mosaicmessage = null;
			try {
				mosaicmessage = mgr.find(MosaicMessage.class, KeyFactory.stringToKey(encodedKey));
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
			mosaicmessage.setGeocells(GeocellHelper.getGeocellsWithinRadius(GeocellHelper.fromE6(mosaicmessage.getLatitudeE6()), GeocellHelper.fromE6(mosaicmessage.getLongitudeE6()), mosaicmessage.getRadius()));
			EntityManager mgr = getEntityManager();
			try {
				MosaicUser mosaicuser = (MosaicUser) mgr.createQuery("select from MosaicUser as MosaicUser where email = :email")
						.setParameter("email", user.getEmail())
						.getSingleResult();
				mosaicmessage.setUser(mosaicuser);
				mosaicuser.addMosaicMessage(mosaicmessage);
				mgr.persist(mosaicuser);
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
			mosaicmessage.setGeocells(GeocellHelper.getGeocellsWithinRadius(GeocellHelper.fromE6(mosaicmessage.getLatitudeE6()), GeocellHelper.fromE6(mosaicmessage.getLongitudeE6()), mosaicmessage.getRadius()));
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
	public MosaicMessage removeMosaicMessage(@Named("encodedKey") String encodedKey, User user) throws OAuthRequestException {
		if (user != null) {
			EntityManager mgr = getEntityManager();
			MosaicMessage mosaicmessage = null;
			try {
				mosaicmessage = mgr.find(MosaicMessage.class, KeyFactory.stringToKey(encodedKey));
				mgr.remove(mosaicmessage);
			} finally {
				mgr.close();
			}
			return mosaicmessage;
		} else
			throw new OAuthRequestException("Invalid user.");
	}

	public void reportMosaicMessage(@Named("encodedKey") String encodedKey, User user) throws OAuthRequestException {
		if (user != null) {
			EntityManager mgr = getEntityManager();
			MosaicMessage mosaicmessage = null;
			try {
				mosaicmessage = mgr.find(MosaicMessage.class, KeyFactory.stringToKey(encodedKey));
				mosaicmessage.setReports(mosaicmessage.getReports() + 2);
				mgr.persist(mosaicmessage);
			} finally {
				mgr.close();
			}
		} else
			throw new OAuthRequestException("Invalid user.");
	}

	public void viewMosaicMessage(@Named("encodedKey") String encodedKey, User user) throws OAuthRequestException {
		if (user != null) {
			EntityManager mgr = getEntityManager();
			try {
				MosaicMessage mosaicmessage = mgr.find(MosaicMessage.class, KeyFactory.stringToKey(encodedKey));
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
