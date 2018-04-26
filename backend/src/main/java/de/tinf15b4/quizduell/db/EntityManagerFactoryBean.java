package de.tinf15b4.quizduell.db;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import de.tinf15b4.quizduell.main.ShutdownController;

@Singleton
public class EntityManagerFactoryBean {
    private EntityManagerFactory emf;

    @Inject
    private ShutdownController shutdownController;

    @PostConstruct
    private void init() {
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

        emf = Persistence.createEntityManagerFactory(persistenceUnit, overrides);
        shutdownController.setDb(emf);
    }

    @PreDestroy
    private void shutdown() {
        emf.close();
    }

    public EntityManager createEntityManager() {
        return emf.createEntityManager();
    }
}
