package de.tinf15b4.quizduell.rest.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.tinf15b4.quizduell.db.Answer;
import de.tinf15b4.quizduell.db.Question;
import de.tinf15b4.quizduell.rest.api.IQuizduellService;

@Path("/api")
public class QuizduellService implements IQuizduellService {

	@Override
	@GET
	@Path("/question")
	@Produces(MediaType.APPLICATION_JSON)
	public Question getQuestion() {
		return null;
	}

	@Override
	@PUT
	@Path("/answer")
	@Consumes(MediaType.APPLICATION_JSON)
	public void getQuestion(Answer answer) {

	}

	@Override
	@POST
	@Path("/ready")
	public void ready() {

	}

	@Override
	@GET
	@Path("/points")
	@Produces(MediaType.TEXT_PLAIN)
	public int getPoints() {
		return 0;
	}

}
