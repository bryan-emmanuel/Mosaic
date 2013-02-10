package com.piusvelte.mosaic.gwt.server;

import com.piusvelte.mosaic.gwt.server.EMF;

import com.google.api.server.spi.config.Api;
import com.google.appengine.api.users.User;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@Api(name = "mosaicmessageendpoint",
clientIds = {"147772138122.apps.googleusercontent.com"},
audiences = {"mosaic-messaging.appspot.com"})
public class MosaicMessageEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method.
	 *
	 * @return List of all entities persisted.
	 */
	@SuppressWarnings({ "cast", "unchecked" })
	public List<MosaicMessage> listMosaicMessage(User user) {
		EntityManager mgr = getEntityManager();
		List<MosaicMessage> result = new ArrayList<MosaicMessage>();
		try {
			Query query = mgr
					.createQuery("select from MosaicMessage as MosaicMessage");
			for (Object obj : (List<Object>) query.getResultList()) {
				result.add(((MosaicMessage) obj));
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
	public MosaicMessage getMosaicMessage(@Named("id") String id, User user) {
		EntityManager mgr = getEntityManager();
		MosaicMessage mosaicmessage = null;
		try {
			mosaicmessage = mgr.find(MosaicMessage.class, id);
		} finally {
			mgr.close();
		}
		return mosaicmessage;
	}

	/**
	 * This inserts the entity into App Engine datastore.
	 * It uses HTTP POST method.
	 *
	 * @param mosaicmessage the entity to be inserted.
	 * @return The inserted entity.
	 */
	public MosaicMessage insertMosaicMessage(MosaicMessage mosaicmessage, User user) {
		EntityManager mgr = getEntityManager();
		try {
			mgr.persist(mosaicmessage);
		} finally {
			mgr.close();
		}
		return mosaicmessage;
	}

	/**
	 * This method is used for updating a entity.
	 * It uses HTTP PUT method.
	 *
	 * @param mosaicmessage the entity to be updated.
	 * @return The updated entity.
	 */
	public MosaicMessage updateMosaicMessage(MosaicMessage mosaicmessage, User user) {
		EntityManager mgr = getEntityManager();
		try {
			mgr.persist(mosaicmessage);
		} finally {
			mgr.close();
		}
		return mosaicmessage;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 * @return The deleted entity.
	 */
	public MosaicMessage removeMosaicMessage(@Named("id") String id, User user) {
		EntityManager mgr = getEntityManager();
		MosaicMessage mosaicmessage = null;
		try {
			mosaicmessage = mgr.find(MosaicMessage.class, id);
			mgr.remove(mosaicmessage);
		} finally {
			mgr.close();
		}
		return mosaicmessage;
	}

	private static EntityManager getEntityManager() {
		return EMF.get().createEntityManager();
	}

}
