package de.tinf15b4.quizduell.rest.exceptions;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.tinf15b4.quizduell.main.ShutdownController;

@Provider
public class GeneralExceptionMapper implements ExceptionMapper<Throwable> {

	private static final Logger logger = LoggerFactory.getLogger(GeneralExceptionMapper.class);

	@Inject
	private ShutdownController shutdown;

	@Override
	public Response toResponse(Throwable e) {
		logger.error("", e);
		if (e instanceof WebApplicationException) {
			Response.fromResponse(((WebApplicationException) e).getResponse()).header("content-type",
					MediaType.TEXT_PLAIN);
			return ((WebApplicationException) e).getResponse();
		} else {
			logger.error(
					"Cought undefined exception. In order to prevent inconsistent state this server is shutting down");
			shutdown.shutdown();
			return Response.status(500)//
					.entity("The server did something wrong. See server log for additional information")//
					.type(MediaType.TEXT_PLAIN)//
					.build();
		}
	}

}
