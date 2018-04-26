package de.tinf15b4.quizduell.db;

import javax.persistence.EntityManager;

public class Transaction {

	private EntityManager manager;

	Transaction(EntityManager manager) {
		this.manager = manager;
		manager.getTransaction().begin();
	}

	public <T> Transaction update(T entity) {
		manager.merge(entity);
		return this;
	}

	public <T> Transaction persist(T entity) {
		manager.persist(entity);
		return this;
	}

	public <T> Transaction delete(T entity) {
		manager.remove(entity);
		return this;
	}

	public void commit() {
		manager.getTransaction().commit();
		manager.close();
	}

}
