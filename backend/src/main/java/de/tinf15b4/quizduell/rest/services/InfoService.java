package de.tinf15b4.quizduell.rest.services;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.tinf15b4.quizduell.db.PersistenceBean;
import de.tinf15b4.quizduell.db.User;

@Path("/info")
public class InfoService {

	@Inject
	private PersistenceBean bean;

	@GET
	@Path("/html")
	@Produces(MediaType.TEXT_HTML)
	public Response htmlSummary() {
		StringBuilder response = new StringBuilder();

		response.append("<!DOCTYPE html>");
		response.append("<meta http-equiv=Refresh content=30>");
		response.append("<title>Quizduell test</title>");

		response.append("Hello World!");

		List<User> list = bean.getUsers();
		String string = list.stream()//
				.map(User::getUsername)//
				.collect(Collectors.joining(", "));
		System.out.println(string);

		return Response.ok(response.toString()).build();
	}
}
