package de.tinf15b4.quizduell.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 * Use this classes methods for read operations only. Move everything else to
 * the {@link Transaction} class, so we can do multiple changes in one
 * transaction.
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
		TypedQuery<T> query = manager.createQuery("SELECT x from " + clazz.getName() + " x", clazz);
		return query.getResultList();
	}

	public <T> T findById(Class<T> clazz, Object id) {
		EntityManager manager = factory.createEntityManager();
		return manager.find(clazz, id);
	}

	public Question getRandomQuestion() {
		Random rand = new Random();
		EntityManager manager = factory.createEntityManager();
		TypedQuery<Long> query = manager.createQuery("SELECT count(*) FROM Question", Long.class);

		Long count = query.getSingleResult();
		int randomNumber = rand.nextInt(Math.toIntExact(count));

		TypedQuery<Question> selectQuery = manager.createQuery("SELECT q FROM Question q", Question.class);
		selectQuery.setFirstResult(randomNumber);
		selectQuery.setMaxResults(1);
		List<Question> resultList = selectQuery.getResultList();
		return resultList.get(0);
	}

	public PendingGame findAndConsumePendingGame(User user) {
		PendingGame game = null;

		EntityManager em = factory.createEntityManager();
		TypedQuery<PendingGame> query = em.createQuery(
				"SELECT x from PendingGame x WHERE x.waitingUser.id <> " + user.getId(), PendingGame.class);
		query.setMaxResults(1);
		List<PendingGame> queryResult = query.getResultList();
		if (!queryResult.isEmpty()) {
			game = queryResult.get(0);
			transaction().delete(game).commit();
		}
		return game;
	}
}
