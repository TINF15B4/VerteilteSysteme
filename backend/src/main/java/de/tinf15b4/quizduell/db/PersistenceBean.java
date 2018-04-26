package de.tinf15b4.quizduell.db;

import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 * Use this classes methods for read operations only. Move everything else to
 * the {@link Transaction} class, so we can do multiple changes in one
 * transaction.
 */

@RequestScoped
public class PersistenceBean {
	@Inject
	private EntityManagerFactoryBean emf;

	private EntityManager em;

	@PostConstruct
	private void postInit() {
		em = emf.createEntityManager();
	}

	@PreDestroy
	private void cleanup() {
		em.close();
	}

	public Transaction transaction() {
		return new Transaction(em);
	}

	public <T> List<T> findAll(Class<T> clazz) {
		TypedQuery<T> query = em.createQuery("SELECT x from " + clazz.getName() + " x", clazz);
		return query.getResultList();
	}

	public <T> T findById(Class<T> clazz, Object id) {
		return em.find(clazz, id);
	}

	public Question getRandomQuestion() {
		Random rand = new Random();
		TypedQuery<Long> query = em.createQuery("SELECT count(*) FROM Question", Long.class);

		Long count = query.getSingleResult();
		int randomNumber = rand.nextInt(Math.toIntExact(count));

		TypedQuery<Question> selectQuery = em.createQuery("SELECT q FROM Question q", Question.class);
		selectQuery.setFirstResult(randomNumber);
		selectQuery.setMaxResults(1);
		List<Question> resultList = selectQuery.getResultList();
		return resultList.get(0);
	}

	public PendingGame findAndConsumePendingGame(User user) {
		PendingGame game = null;

		try {
			em.getTransaction().begin();
			TypedQuery<PendingGame> query = em.createQuery(
					"SELECT x from PendingGame x WHERE x.waitingUser.id <> " + user.getId(), PendingGame.class);
			query.setMaxResults(1);
			List<PendingGame> queryResult = query.getResultList();
			if (!queryResult.isEmpty()) {
				game = queryResult.get(0);
				em.remove(game);
			}

			em.getTransaction().commit();
			return game;
		} catch (Throwable t) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();

			throw t;
		}
	}

}
