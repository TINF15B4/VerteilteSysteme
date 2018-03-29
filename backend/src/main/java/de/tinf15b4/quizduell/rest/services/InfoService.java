package de.tinf15b4.quizduell.rest.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/info")
public class InfoService {

	@GET
	@Path("/html")
	@Produces(MediaType.TEXT_HTML)
	public Response htmlSummary() {
		StringBuilder response = new StringBuilder();

		response.append("<!DOCTYPE html>");
		response.append("<meta http-equiv=Refresh content=30>");
		response.append("<title>Quizduell test</title>");
		
		response.append("Hello World!");

		return Response.ok(response.toString()).build();
	}
}
