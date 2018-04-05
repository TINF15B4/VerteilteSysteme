package de.tinf15b4.quizduell.rest.services;

import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.tinf15b4.quizduell.db.Answer;
import de.tinf15b4.quizduell.db.Game;
import de.tinf15b4.quizduell.db.PersistenceBean;
import de.tinf15b4.quizduell.db.PlayingUser;
import de.tinf15b4.quizduell.db.Question;
import de.tinf15b4.quizduell.rest.api.IQuizduellService;

@Path("/api")
public class QuizduellService implements IQuizduellService {

	private static final Logger LOGGER = LoggerFactory.getLogger(QuizduellService.class);

	@Inject
	private PersistenceBean persistenceBean;

	@Override
	@GET
	@Path("/question/{gameId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Question getQuestion(@PathParam("gameId") UUID gameId) {
		return null;
	}

	@Override
	@POST
	@Path("/answer/{gameId}/{userId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean postAnswer(Answer answer, @PathParam("gameId") UUID gameId, @PathParam("userId") long userId) {
		Game game = persistenceBean.getGameWithId(gameId);
		for (PlayingUser playingUser : game.getUsers()) {
			if (playingUser.getUser().getId() == userId) {
				// We found the right user in the right game!
				Question currentQuestion = game.getCurrentQuestion();
				if (currentQuestion.getAnswers().contains(answer)) {
					if (currentQuestion.getCorrectAnswer().equals(answer)) {
						playingUser.incrementPoints();
						persistenceBean.updateUser(playingUser);
						return true;
					}
				} else {
					// TODO
					// recieved an Answer that does not match the answers of the question
					LOGGER.error("answer not in question answer set");
				}
			}
		}
		return false;
	}

	@Override
	@POST
	@Path("/ready")
	public UUID ready() {
		return UUID.randomUUID(); // gameId
	}

	@Override
	@GET
	@Path("/points/{gameId}/{userId}")
	@Produces(MediaType.TEXT_PLAIN)
	public int getPoints(@PathParam("gameId") UUID gameId, @PathParam("userId") long userId) {
		return 0;
	}

}
