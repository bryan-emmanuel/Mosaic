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
	public List<MosaicMessage> listMosaicMessage(@Named("id") String id, @Named("latitude") int latitude, @Named("longitude") int longitude, User user) throws OAuthRequestException {
		if (user != null) {
			EntityManager mgr = getEntityManager();
			List<MosaicMessage> result = new ArrayList<MosaicMessage>();
			try {
				Query query;
				if ((id != null) && (id.length() > 0))
					query = mgr
					.createQuery("select from MosaicMessage as MosaicMessage where mosaicUserId = :id")
					.setParameter("id", id);
				else
					query = mgr
						.createQuery("select from MosaicMessage as MosaicMessage where minLatitude <= :maxLatitude and maxLatitude >= :minLatitude and minLongitude <= :maxLongitude and maxLongitude >= :minLongitude and visits > (reports * 2)")
						.setParameter("minLatitude", offsetLatitude(latitude, -10))
						.setParameter("maxLatitude", offsetLatitude(latitude, 10))
						.setParameter("minLongitude", offsetLongitude(latitude, longitude, -10))
						.setParameter("maxLongitude", offsetLongitude(latitude, longitude, 10));
				for (Object obj : (List<Object>) query.getResultList())
					result.add(((MosaicMessage) obj));
			} finally {
				mgr.close();
			}
			return result;
		} else
			throw new OAuthRequestException("Invalid user.");
	}
	
	private static final int EARTHS_RADIUS = 6378137;
	private static final double DEGREES = 57.2957795131;//180 / Pi
	
	private int offsetLatitude(int latitude, int offset) {
		if (offset == 0)
			return latitude;
		int sign = offset < 0 ? -1 : 1;
		return (int) (latitude + offset * sign / EARTHS_RADIUS * DEGREES * 1E6 * sign); 
	}
	
	private int offsetLongitude(int latitude, int longitude, int offset) {
		if (offset == 0)
			return longitude;
		int sign = offset < 0 ? -1 : 1;
		return (int) (longitude + offset * sign / (EARTHS_RADIUS * Math.cos(Math.PI * (latitude / 1E6) / 180)));
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
				mosaicmessage = mgr.find(MosaicMessage.class, id);
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
			mosaicmessage.setMinLatitude(offsetLatitude(mosaicmessage.getLatitude(), mosaicmessage.getRadius() * -1));
			mosaicmessage.setMaxLatitude(offsetLatitude(mosaicmessage.getLatitude(), mosaicmessage.getRadius()));
			mosaicmessage.setMinLongitude(offsetLongitude(mosaicmessage.getLatitude(), mosaicmessage.getLongitude(), mosaicmessage.getRadius() * -1));
			mosaicmessage.setMaxLongitude(offsetLongitude(mosaicmessage.getLatitude(), mosaicmessage.getLongitude(), mosaicmessage.getRadius()));
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
			mosaicmessage.setMinLatitude(offsetLatitude(mosaicmessage.getLatitude(), mosaicmessage.getRadius() * -1));
			mosaicmessage.setMaxLatitude(offsetLatitude(mosaicmessage.getLatitude(), mosaicmessage.getRadius()));
			mosaicmessage.setMinLongitude(offsetLongitude(mosaicmessage.getLatitude(), mosaicmessage.getLongitude(), mosaicmessage.getRadius() * -1));
			mosaicmessage.setMaxLongitude(offsetLongitude(mosaicmessage.getLatitude(), mosaicmessage.getLongitude(), mosaicmessage.getRadius()));
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
				mosaicmessage = mgr.find(MosaicMessage.class, id);
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
				mosaicmessage = mgr.find(MosaicMessage.class, id);
				mosaicmessage.setReports(mosaicmessage.getReports() + 1);
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
				MosaicMessage mosaicmessage = mgr.find(MosaicMessage.class, id);
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
