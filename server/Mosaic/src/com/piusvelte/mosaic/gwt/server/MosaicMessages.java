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
import com.google.api.server.spi.config.ApiMethod;
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

	@ApiMethod(
			httpMethod = "GET",
			name = "message.list",
			path = "message/list")
	@SuppressWarnings({ "cast", "unchecked" })
	public List<MosaicMessage> listMosaicMessage(@Named("latE6") int latE6, @Named("lonE6") int lonE6, User user) throws OAuthRequestException {
		if (user != null) {
			EntityManager mgr = getEntityManager();
			List<MosaicMessage> result = new ArrayList<MosaicMessage>();
			try {
				double lat = GeocellHelper.fromE6(latE6);
				double lon = GeocellHelper.fromE6(lonE6);
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
					if ((msg.getReports() <= msg.getViews())
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

	@ApiMethod(
			httpMethod = "GET",
			name = "message.get",
			path = "message/get/{id}")
	public MosaicMessage getMosaicMessage(@Named("id") long id, User user) throws OAuthRequestException {
		if (user != null) {
			EntityManager mgr = getEntityManager();
			MosaicMessage mosaicmessage = null;
			try {
				mosaicmessage = mgr.find(MosaicMessage.class, id);
				mosaicmessage.setUser(mgr.find(MosaicUser.class, mosaicmessage.getUser_id()));
			} finally {
				mgr.close();
			}
			return mosaicmessage;
		} else
			throw new OAuthRequestException("Invalid user.");
	}

	@ApiMethod(
			httpMethod = "POST",
			name = "message.insert",
			path = "message/insert")
	public MosaicMessage insertMosaicMessage(MosaicMessage mosaicmessage, User user) throws OAuthRequestException {
		if (user != null) {
			mosaicmessage.setGeocells(GeocellHelper.getGeocellsWithinRadius(GeocellHelper.fromE6(mosaicmessage.getLatitudeE6()), GeocellHelper.fromE6(mosaicmessage.getLongitudeE6()), mosaicmessage.getRadius()));
			EntityManager mgr = getEntityManager();
			try {
				mgr.persist(mosaicmessage);
				mosaicmessage.setUser(mgr.find(MosaicUser.class, mosaicmessage.getUser_id()));
			} finally {
				mgr.close();
			}
			return mosaicmessage;
		} else
			throw new OAuthRequestException("Invalid user.");
	}

	@ApiMethod(
			httpMethod = "POST",
			name = "message.update",
			path = "message/update")
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

	@ApiMethod(
			httpMethod = "GET",
			name = "message.remove",
			path = "message/remove/{id}")
	public MosaicMessage removeMosaicMessage(@Named("id") long id, User user) throws OAuthRequestException {
		if (user != null) {
			EntityManager mgr = getEntityManager();
			MosaicMessage mosaicmessage = null;
			try {
//				mosaicmessage = mgr.find(MosaicMessage.class, KeyFactory.stringToKey(id));
				mgr.remove(mgr.find(MosaicMessage.class, id));
			} finally {
				mgr.close();
			}
			return mosaicmessage;
		} else
			throw new OAuthRequestException("Invalid user.");
	}

	@ApiMethod(
			httpMethod = "GET",
			name = "message.report",
			path = "message/report/{id}")
	public void reportMosaicMessage(@Named("id") long id, User user) throws OAuthRequestException {
		if (user != null) {
			EntityManager mgr = getEntityManager();
			MosaicMessage mosaicmessage = null;
			try {
				mosaicmessage = mgr.find(MosaicMessage.class, id);
				mosaicmessage.addReport();
				mgr.persist(mosaicmessage);
			} finally {
				mgr.close();
			}
		} else
			throw new OAuthRequestException("Invalid user.");
	}

	@ApiMethod(
			httpMethod = "GET",
			name = "message.view",
			path = "message/view/{id}")
	public void viewMosaicMessage(@Named("id") long id, User user) throws OAuthRequestException {
		if (user != null) {
			EntityManager mgr = getEntityManager();
			try {
				MosaicMessage mosaicmessage = mgr.find(MosaicMessage.class, id);
				mosaicmessage.addView();
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
