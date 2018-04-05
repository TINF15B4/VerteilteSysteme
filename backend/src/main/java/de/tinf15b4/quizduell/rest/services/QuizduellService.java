package de.tinf15b4.quizduell.rest.services;

import java.util.Optional;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.tinf15b4.quizduell.db.Answer;
import de.tinf15b4.quizduell.db.Game;
import de.tinf15b4.quizduell.db.PendingGame;
import de.tinf15b4.quizduell.db.PersistenceBean;
import de.tinf15b4.quizduell.db.PlayingUser;
import de.tinf15b4.quizduell.db.Question;
import de.tinf15b4.quizduell.db.User;
import de.tinf15b4.quizduell.rest.api.IQuizduellService;

@Path("/api")
public class QuizduellService implements IQuizduellService {

	private static final Logger LOGGER = LoggerFactory.getLogger(QuizduellService.class);

	@Inject
	private PersistenceBean persistenceBean;

	@Override
	@GET
	@Path("/question/{gameId}/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Question getQuestion(@PathParam("gameId") UUID gameId, @PathParam("gameId") long userId) {
		// TODO Timecheck
		Game game = persistenceBean.getGameWithId(gameId);
		if (game.getCurrentUser().getId() == userId) {
			// same user still, return same question
			return game.getCurrentQuestion();
		} else {
			// next question
			Question question = game.nextQuestion();
			persistenceBean.transaction().update(game).commit();
			return question;
		}
	}

	@Override
	@POST
	@Path("/answer/{gameId}/{userId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean postAnswer(Answer answer, @PathParam("gameId") UUID gameId, @PathParam("userId") long userId) {
		// TODO timecheck
		Game game = persistenceBean.getGameWithId(gameId);
		for (PlayingUser playingUser : game.getUsers()) {
			if (playingUser.getUser().getId() == userId) {
				// We found the right user in the right game!
				Question currentQuestion = game.getCurrentQuestion();
				if (currentQuestion.getAnswers().contains(answer)) {
					if (currentQuestion.getCorrectAnswer().equals(answer)) {
						playingUser.incrementPoints();
						persistenceBean.transaction().update(playingUser).commit();
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
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/ready")
	public UUID ready(long userid) {
		User u = persistenceBean.findById(User.class, userid);
		if (u == null)
			throw new WebApplicationException(Response.status(400).entity("Unknown user\n").build());

		PendingGame pg = persistenceBean.findAndConsumePendingGame(u);
		if (pg != null) {
			// create game
			Set<PlayingUser> users = new HashSet<>();
			users.add(new PlayingUser(u, 0));
			users.add(new PlayingUser(pg.getWaitingUser(), 0));
			// TODO: add questions
			Game g = new Game(pg.getGameId(), users, null);

			persistenceBean.merge(g);
			return g.getGameId();
		} else {
			// create new pending game

			pg = new PendingGame(u);
			persistenceBean.merge(pg);

			return pg.getGameId();
		}
	}

	@Override
	@GET
	@Path("/points/{gameId}/{userId}")
	@Produces(MediaType.TEXT_PLAIN)
	public int getPoints(@PathParam("gameId") UUID gameId, @PathParam("userId") long userId) {
		Game game = persistenceBean.getGameWithId(gameId);
		Optional<PlayingUser> user = game.getUsers().stream().filter(u -> u.getId() == userId).findFirst();
		if (!user.isPresent()) {
			LOGGER.error("Invalid user id for this game");
			return -1;
			// FIXME throw Exception
		} else {
			return user.get().getPoints();
		}

	}

}
