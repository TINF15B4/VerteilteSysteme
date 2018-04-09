package de.tinf15b4.quizduell.db;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 * Use this classes methods for read operations only. Move everything else to
 * the {@link Transaction} class, so we can do multiple changes in one transaction.
 */
import com.google.common.collect.Sets;

@ApplicationScoped
public class PersistenceBean {

	private EntityManagerFactory factory;

	public PersistenceBean() {
		factory = Persistence.createEntityManagerFactory("testing");
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

	public Question getRandomQuestion() {
		Random rand = new Random();
		EntityManager manager = factory.createEntityManager();
		manager.getTransaction().begin();
		TypedQuery<Question> query = manager
				.createQuery("SELECT x from " + "Question x WHERE id = " + rand.nextInt(1800), Question.class);
		List<Question> list = query.getResultList();
		manager.getTransaction().commit();
		try {
			return list.get(0);
		} catch (Exception e) {
			Answer A1 = new Answer("Ja!");
			Answer A2 = new Answer("Nein!");
			Answer A3 = new Answer("Vielleicht");
			Answer A4 = new Answer("Ihr seit ja echt scheiße!");
			return new Question("Ist uns ein Fehler unterlaufen?", Sets.newHashSet(A1, A2, A3, A4), A1);

		}
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
