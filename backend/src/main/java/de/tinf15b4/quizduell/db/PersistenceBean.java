package de.tinf15b4.quizduell.db;

import com.google.common.collect.Sets;

import java.util.List;
import java.util.Random;
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

	public <T> void update(T entity) {
		EntityManager manager = factory.createEntityManager();
		manager.getTransaction().begin();
		manager.merge(entity);
		manager.getTransaction().commit();
	}

	public Question getRandomQuestion(){
		Random rand = new Random();
		EntityManager manager = factory.createEntityManager();
		manager.getTransaction().begin();
		TypedQuery<Question> query = manager.createQuery("SELECT x from " + "Question x WHERE id = " + rand.nextInt(1800), Question.class);
		List<Question> list = query.getResultList();
		manager.getTransaction().commit();
		try {
			return list.get(0);
		}catch (Exception e){
			Answer A1 = new Answer("Ja!");
			Answer A2 = new Answer("Nein!");
			Answer A3 = new Answer("Vielleicht");
			Answer A4 = new Answer("Ihr seit ja echt scheiße!");
			return new Question("Ist uns ein Fehler unterlaufen?", Sets.newHashSet(A1, A2, A3, A4), A1);

		}
	}

}
