package de.tinf15b4.quizduell.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import com.google.common.collect.Sets;

/**
 * Use this classes methods for read operations only. Move everything else to
 * the {@link Transaction} class, so we can do multiple changes in one transaction.
 */

@ApplicationScoped
public class PersistenceBean {

	private EntityManagerFactory factory;

	public PersistenceBean() {
		String persistenceUnit = System.getenv("DB_UNIT");
		if (persistenceUnit == null || persistenceUnit.length() == 0)
			persistenceUnit = "testing";

		Map<String, String> overrides = new HashMap<>();

		if (System.getenv("DB_URL") != null)
			overrides.put("hibernate.connection.url", System.getenv("DB_URL"));
		if (System.getenv("DB_USERNAME") != null)
			overrides.put("hibernate.connection.username", System.getenv("DB_USERNAME"));
		if (System.getenv("DB_PASSWORD") != null)
			overrides.put("hibernate.connection.password", System.getenv("DB_PASSWORD"));

		factory = Persistence.createEntityManagerFactory(persistenceUnit, overrides);
	}

	public Transaction transaction() {
		return new Transaction(factory.createEntityManager());
	}

	public <T> List<T> findAll(Class<T> clazz) {
		EntityManager manager = factory.createEntityManager();
		manager.getTransaction().begin();
		TypedQuery<T> query = manager.createQuery("SELECT x from " + clazz.getName() + " x", clazz);
		List<T> list = query.getResultList();
		manager.getTransaction().commit();
		return list;
	}

	public <T> T findById(Class<T> clazz, Object id) {
		EntityManager em = factory.createEntityManager();
		return em.find(clazz, id);
	}

	public <T> T merge(T o) {
		EntityManager em = factory.createEntityManager();
		em.getTransaction().begin();
		try {
			o = em.merge(o);
			em.getTransaction().commit();
		} catch (Throwable t) {
			em.getTransaction().rollback();
			throw t;
		}
		return o;
	}

	public <T> T persist(T o) {
		EntityManager em = factory.createEntityManager();
		em.getTransaction().begin();
		try {
			em.persist(o);
			em.getTransaction().commit();
		} catch (Throwable t) {
			em.getTransaction().rollback();
			throw t;
		}
		return o;
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

	public List<Question> getFiveRandomQuestions() {
		Random rand = new Random();
		EntityManager manager = factory.createEntityManager();
		manager.getTransaction().begin();
		TypedQuery<Integer> query = manager
				.createQuery("SELECT count(*) FROM " + "Question", Integer.class);


		List<Integer> list = query.getResultList();
		manager.getTransaction().commit();

		int randomNumber = rand.nextInt(list.get(0) - 5);

		TypedQuery<Question> selectQuery = manager.createQuery("SELECT q FROM Question q", Question.class);
		selectQuery.setFirstResult(randomNumber);
		selectQuery.setMaxResults(5);
		return selectQuery.getResultList();
	}

	public PendingGame findAndConsumePendingGame(User user) {
		PendingGame g = null;

		EntityManager em = factory.createEntityManager();
		em.getTransaction().begin();
		try {
			TypedQuery<PendingGame> query = em.createQuery(
					"SELECT x from PendingGame x WHERE x.waitingUser.id <> " + user.getId(),
					PendingGame.class);
			query.setMaxResults(1);
			List<PendingGame> gl = query.getResultList();
			if (gl.size() > 0) {
				g = gl.get(0);
				em.remove(g);
			}
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
			throw e;
		}

		return g;
	}
}
