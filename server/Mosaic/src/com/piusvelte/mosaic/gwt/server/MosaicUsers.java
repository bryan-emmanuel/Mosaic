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

@Api(name = "mosaicusers",
clientIds = {Ids.WEB_CLIENT_ID,
Ids.ANDROID_CLIENT_ID},
audiences = {Ids.ANDROID_AUDIENCE}
)
public class MosaicUsers {

	@ApiMethod(
			httpMethod = "GET",
			name = "user.list",
			path = "user/list")
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

	@ApiMethod(
			httpMethod = "GET",
			name = "user.get",
			path = "user/get/{id}")
	public MosaicUser getMosaicUser(@Named("id") long id, User user) throws OAuthRequestException {
		if (user != null) {
			EntityManager mgr = getEntityManager();
			MosaicUser mosaicuser = null;
			try {
				mosaicuser = mgr.find(MosaicUser.class, id);
			} finally {
				mgr.close();
			}
			return mosaicuser;
		} else
			throw new OAuthRequestException("Invalid user.");
	}

	@ApiMethod(
			httpMethod = "POST",
			name = "user.insert",
			path = "user/insert")
	public MosaicUser insertMosaicUser(User user) throws OAuthRequestException {
		if (user != null) {
			MosaicUser mosaicuser = new MosaicUser();
			EntityManager mgr = getEntityManager();
			try {
				mosaicuser.setEmail(user.getEmail());
				mosaicuser.setNickname(user.getNickname());
				mgr.persist(mosaicuser);
			} finally {
				mgr.close();
			}
			return mosaicuser;
		} else
			throw new OAuthRequestException("Invalid user.");
	}

	@ApiMethod(
			httpMethod = "POST",
			name = "user.update",
			path = "user/update")
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

	@ApiMethod(
			httpMethod = "GET",
			name = "user.remove",
			path = "user/remove/{id}")
	@SuppressWarnings({ "cast", "unchecked" })
	public MosaicUser removeMosaicUser(@Named("id") long id, User user) throws OAuthRequestException {
		if (user != null) {
			EntityManager mgr = getEntityManager();
			MosaicUser mosaicuser = null;
			try {
				mosaicuser = mgr.find(MosaicUser.class, id);
				Query query = mgr.createQuery("select from MosaicMessage as MosaicMessage where user_id = :user_id")
						.setParameter("user_id", id);
				for (Object obj : (List<Object>) query.getResultList())
					mgr.remove(obj);
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
