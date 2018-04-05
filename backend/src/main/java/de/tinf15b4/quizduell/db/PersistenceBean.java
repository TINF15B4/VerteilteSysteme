package de.tinf15b4.quizduell.db;

import java.util.List;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

@ApplicationScoped
public class PersistenceBean {

	private EntityManagerFactory factory;

	public PersistenceBean() {
		factory = Persistence.createEntityManagerFactory("testing");
	}

	public <T> List<T> findAll(Class<T> clazz) {
		EntityManager manager = factory.createEntityManager();
		manager.getTransaction().begin();
		TypedQuery<T> query = manager.createQuery("SELECT x from " + clazz.getName() + " x", clazz);
		List<T> list = query.getResultList();
		manager.getTransaction().commit();
		return list;
	}

	public Game getGameWithId(UUID gameId) {
		EntityManager manager = factory.createEntityManager();
		manager.getTransaction().begin();
		TypedQuery<Game> query = manager.createQuery(
				"SELECT x from Game x where gameId = '" + gameId.toString().replaceAll("-", "") + "'", Game.class);
		Game game = query.getSingleResult();
		manager.getTransaction().commit();
		return game;
	}

	public void updateUser(PlayingUser playingUser) {
		EntityManager manager = factory.createEntityManager();
		manager.getTransaction().begin();
		manager.merge(playingUser);
		manager.getTransaction().commit();
	}

}
