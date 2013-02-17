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

@Api(name = "mosaicusers",
clientIds = {Ids.WEB_CLIENT_ID,
Ids.ANDROID_CLIENT_ID},
audiences = {Ids.ANDROID_AUDIENCE}
)
public class MosaicUsers {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method.
	 *
	 * @return List of all entities persisted.
	 * @throws OAuthRequestException 
	 */
	@SuppressWarnings({ "cast", "unchecked" })
	public List<MosaicUser> listMosaicUser(User user) throws OAuthRequestException {
		if (user != null) {
			EntityManager mgr = getEntityManager();
			List<MosaicUser> result = new ArrayList<MosaicUser>();
			try {
				Query query = mgr
						.createQuery("select from MosaicUser as MosaicUser");
				for (Object obj : (List<Object>) query.getResultList()) {
					result.add(((MosaicUser) obj));
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
	 * @throws OAuthRequestException 
	 */
	public MosaicUser getMosaicUser(@Named("id") String id, User user) throws OAuthRequestException {
		if (user != null) {
			EntityManager mgr = getEntityManager();
			MosaicUser mosaicuser = null;
			try {
				mosaicuser = (MosaicUser) mgr.createQuery("select from MosaicUser as MosaicUser where email = :email").setParameter("email", user.getEmail()).getSingleResult();
			} finally {
				mgr.close();
			}
			return mosaicuser;
		} else
			throw new OAuthRequestException("Invalid user.");
	}

	/**
	 * This inserts the entity into App Engine datastore.
	 * It uses HTTP POST method.
	 *
	 * @param mosaicuser the entity to be inserted.
	 * @return The inserted entity.
	 * @throws OAuthRequestException 
	 */
	public MosaicUser insertMosaicUser(MosaicUser mosaicuser, User user) throws OAuthRequestException {
		if (user != null) {
			EntityManager mgr = getEntityManager();
			try {
				mosaicuser.setEmail(user.getEmail());
				mosaicuser.setNickname(user.getNickname());
				mosaicuser.setUserid(user.getUserId());
				mgr.persist(mosaicuser);
			} finally {
				mgr.close();
			}
			return mosaicuser;
		} else
			throw new OAuthRequestException("Invalid user.");
	}

	/**
	 * This method is used for updating a entity.
	 * It uses HTTP PUT method.
	 *
	 * @param mosaicuser the entity to be updated.
	 * @return The updated entity.
	 * @throws OAuthRequestException 
	 */
	public MosaicUser updateMosaicUser(MosaicUser mosaicuser, User user) throws OAuthRequestException {
		if (user != null) {
			EntityManager mgr = getEntityManager();
			try {
				mgr.persist(mosaicuser);
			} finally {
				mgr.close();
			}
			return mosaicuser;
		} else
			throw new OAuthRequestException("Invalid user.");
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 * @return The deleted entity.
	 * @throws OAuthRequestException 
	 */
	public MosaicUser removeMosaicUser(@Named("id") String id, User user) throws OAuthRequestException {
		if (user != null) {
			EntityManager mgr = getEntityManager();
			MosaicUser mosaicuser = null;
			try {
				mosaicuser = mgr.find(MosaicUser.class, id);
				mgr.remove(mosaicuser);
			} finally {
				mgr.close();
			}
			return mosaicuser;
		} else
			throw new OAuthRequestException("Invalid user.");
	}

	private static EntityManager getEntityManager() {
		return EMF.get().createEntityManager();
	}

}
