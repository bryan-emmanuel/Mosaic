package com.piusvelte.mosaic.gwt.server;

import com.piusvelte.mosaic.gwt.server.EMF;

import com.google.api.server.spi.config.Api;
import com.google.appengine.api.users.User;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@Api(name = "mosaicuserendpoint",
clientIds = {"147772138122.apps.googleusercontent.com"},
audiences = {"mosaic-messaging.appspot.com"})
public class MosaicUserEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method.
	 *
	 * @return List of all entities persisted.
	 */
	@SuppressWarnings({ "cast", "unchecked" })
	public List<MosaicUser> listMosaicUser(User user) {
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
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	public MosaicUser getMosaicUser(@Named("id") String id, User user) {
		EntityManager mgr = getEntityManager();
		MosaicUser mosaicuser = null;
		try {
			mosaicuser = (MosaicUser) mgr.createQuery("Select From MosaicUser Where email = :email").setParameter("email", user.getEmail()).getSingleResult();
			if (mosaicuser == null) {
				mosaicuser = new MosaicUser();
				mosaicuser.setEmail(user.getEmail());
				mosaicuser.setNickname(user.getNickname());
				mosaicuser.setUserid(user.getUserId());
				mgr.persist(mosaicuser);
			}
		} finally {
			mgr.close();
		}
		return mosaicuser;
	}

	/**
	 * This inserts the entity into App Engine datastore.
	 * It uses HTTP POST method.
	 *
	 * @param mosaicuser the entity to be inserted.
	 * @return The inserted entity.
	 */
	public MosaicUser insertMosaicUser(MosaicUser mosaicuser, User user) {
		EntityManager mgr = getEntityManager();
		try {
			mgr.persist(mosaicuser);
		} finally {
			mgr.close();
		}
		return mosaicuser;
	}

	/**
	 * This method is used for updating a entity.
	 * It uses HTTP PUT method.
	 *
	 * @param mosaicuser the entity to be updated.
	 * @return The updated entity.
	 */
	public MosaicUser updateMosaicUser(MosaicUser mosaicuser, User user) {
		EntityManager mgr = getEntityManager();
		try {
			mgr.persist(mosaicuser);
		} finally {
			mgr.close();
		}
		return mosaicuser;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 * @return The deleted entity.
	 */
	public MosaicUser removeMosaicUser(@Named("id") String id, User user) {
		EntityManager mgr = getEntityManager();
		MosaicUser mosaicuser = null;
		try {
			mosaicuser = mgr.find(MosaicUser.class, id);
			mgr.remove(mosaicuser);
		} finally {
			mgr.close();
		}
		return mosaicuser;
	}

	private static EntityManager getEntityManager() {
		return EMF.get().createEntityManager();
	}

}
