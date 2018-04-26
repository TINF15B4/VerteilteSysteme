package de.tinf15b4.quizduell.main;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import javax.persistence.EntityManagerFactory;

import org.glassfish.grizzly.http.server.HttpServer;
import org.jboss.weld.environment.se.Weld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class ShutdownController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ShutdownController.class);

	private Weld weld;
	private HttpServer server;
	private EntityManagerFactory db;

	@PostConstruct
	public void postInit() {
		Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
	}

	public void shutdown() {
		LOGGER.error("Triggered graceful shutdown");
		if (db != null)
			db.close();
		if (server != null)
			server.shutdown();
		if (weld != null)
			weld.shutdown();
	}

	public void setServer(HttpServer server) {
		this.server = server;
	}

	public void setWeld(Weld weld) {
		this.weld = weld;
	}

	public void setDb(EntityManagerFactory db) {
		this.db = db;
	}

}
