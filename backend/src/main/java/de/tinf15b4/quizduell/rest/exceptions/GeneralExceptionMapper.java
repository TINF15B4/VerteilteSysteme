package de.tinf15b4.quizduell.rest.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class GeneralExceptionMapper implements ExceptionMapper<Throwable> {

	private static final Logger logger = LoggerFactory.getLogger(GeneralExceptionMapper.class);

	@Override
	public Response toResponse(Throwable e) {
		logger.error("", e);
		if (e instanceof WebApplicationException) {
			return ((WebApplicationException)e).getResponse();
		} else {
			return Response.status(500)//
					.entity("The server did something wrong. See server log for additional information")//
					.type(MediaType.TEXT_PLAIN)//
					.build();
		}
	}

}
