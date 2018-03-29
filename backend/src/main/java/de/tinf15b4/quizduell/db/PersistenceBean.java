package de.tinf15b4.quizduell.db;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

@ApplicationScoped
public class PersistenceBean {

	private EntityManagerFactory factory;

	public PersistenceBean() {
		factory = Persistence.createEntityManagerFactory("production");
	}

	public List<User> getUsers() {
		EntityManager manager = factory.createEntityManager();
		manager.getTransaction().begin();
		TypedQuery<User> query = manager.createQuery("SELECT u from User u", User.class);
		List<User> list = query.getResultList();
		manager.getTransaction().commit();
		return list;
	}

}
