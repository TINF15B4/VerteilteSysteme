package de.tinf15b4.quizduell.main;

import java.net.URI;

import javax.enterprise.inject.spi.CDI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.jboss.weld.environment.se.Weld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

/**
 * Main class starting up the server
 * 
 * @author herglotz.marco
 *
 */
public class QuizduellApp {

	private static final Logger logger = LoggerFactory.getLogger(QuizduellApp.class);

	@Parameter(names = "-host", description = "Host part of the url this application should be available at")
	private String host = "http://localhost";

	@Parameter(names = "-port", description = "Port this application should be listening to")
	private int port = 8080;

	@Parameter(names = "-path", description = "Base path this application should be available at under the given host and port")
	private String baseUrl = "/quizduell/";

	@Parameter(names = { "-help", "-?" }, description = "Shows help about what commands are available")
	private boolean help = false;

	/**
	 * Starts Grizzly HTTP server exposing JAX-RS resources defined in
	 * de.tinf15b4.ihatestau.services
	 * 
	 * @return Grizzly HTTP server.
	 */
	public HttpServer startServer() throws Exception {
		final ResourceConfig rc = new ResourceConfig()//
				.packages("de.tinf15b4.quizduell.rest.services", "de.tinf15b4.quizduell.rest.exceptions")//
				.register(JacksonFeature.class);

		Weld weld = new Weld();
		weld.initialize();

		HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(getUri()), rc);
		System.setProperty("ihatestau.apiurl", getUri());

		ShutdownController hook = CDI.current().select(ShutdownController.class).get();
		hook.setServer(server);
		hook.setWeld(weld);

		CDI.current().select(QuestionInitializer.class).get().maybeInitializeQuestions();

		return server;
	}

	public String getUri() {
		return String.format("%s:%s%s", host, port, baseUrl);
	}

	public static void main(String[] args) throws Exception {
		QuizduellApp app = new QuizduellApp();
		JCommander commander = JCommander.newBuilder()//
				.addObject(app)//
				.build();
		commander.parse(args);

		if (app.help) {
			commander.usage();
			return;
		}

		app.startServer();
		logger.info("Jersey app started with WADL available at {}application.wadl", app.getUri());
	}

}
