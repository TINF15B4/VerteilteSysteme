package de.tinf15b4.quizduell.rest.services;

import java.util.ArrayList;
import java.util.List;
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
import de.tinf15b4.quizduell.db.Points;
import de.tinf15b4.quizduell.db.Question;
import de.tinf15b4.quizduell.db.QuestionDTO;
import de.tinf15b4.quizduell.db.User;
import de.tinf15b4.quizduell.rest.api.IQuizduellService;

@Path("/api")
public class QuizduellService implements IQuizduellService {

	private static final int ANSWER_TIMEOUT_MILLIS = 20000;

	private static final Logger LOGGER = LoggerFactory.getLogger(QuizduellService.class);

	@Inject
	private PersistenceBean persistenceBean;

	@Override
	@GET
	@Path("/question/{gameId}/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public QuestionDTO getQuestion(@PathParam("gameId") UUID gameId, @PathParam("userId") long userId) {
		Game game = persistenceBean.findById(Game.class, gameId);
		if (game == null)
			throw new WebApplicationException(Response.status(404).entity("Unknown Game").build());

		if (game.getCurrentQuestion() == null)
			throw new WebApplicationException(204);

		if (game.getCurrentUser().getId() != userId)
			throw new WebApplicationException(423);

		if (game.getTimestamp() == -1) {
			game.setTimestamp(System.currentTimeMillis());
			game = persistenceBean.merge(game);
		}

		return game.getCurrentQuestion().toDTO();
	}

	@Override
	@POST
	@Path("/answer/{gameId}/{userId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void postAnswer(Answer answer, @PathParam("gameId") UUID gameId, @PathParam("userId") long userId) {
		Game game = persistenceBean.findById(Game.class, gameId);
		if (game == null)
			throw new WebApplicationException(Response.status(404).entity("Unknown Game").build());

		if (game.getCurrentUser().getId() != userId)
			throw new WebApplicationException(423);

		Question answeredQuestion = game.getCurrentQuestion();
		long answerTimestamp = game.getTimestamp();
		if (answeredQuestion == null)
			throw new WebApplicationException(Response.status(400).entity("Game finished").build());

		if (game.getCurrentUser().getId() == game.getUser1().getUser().getId()) {
			// user 1 --> switch to user 2
			game.setCurrentUser(game.getUser2().getUser());
		} else {
			// user 2 --> switch to user 1 and advance question
			game.setCurrentUser(game.getUser1().getUser());
			game.nextQuestion();
		}
		game.setTimestamp(-1);
		game = persistenceBean.merge(game);

		// check validity and award points
		if (answeredQuestion.getCorrectAnswer().equals(answer) || answeredQuestion.getAnswers().contains(answer)) {
			long diff = System.currentTimeMillis() - answerTimestamp;
			if (diff > ANSWER_TIMEOUT_MILLIS) {
				throw new WebApplicationException(
						Response.status(406).entity("Answer too late. Timeout has been reached").build());
			}
			if (answeredQuestion.getCorrectAnswer().equals(answer)) {
				game.getCurrentPlayingUser().incrementPoints();
				persistenceBean.transaction().update(game.getCurrentPlayingUser()).commit();
			} else {
				throw new WebApplicationException(Response.status(406).entity("Answer was wrong").build());
			}
		} else {
			// recieved an Answer that does not match the answers of the question
			throw new WebApplicationException(
					Response.status(400).entity("Answer not in question's answer set").build());
		}
	}

	@Override
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/ready")
	public UUID ready(long userid) {
		User u = persistenceBean.findById(User.class, userid);
		if (u == null)
			throw new WebApplicationException(Response.status(400).entity("Unknown user\n").build());

		PendingGame pg = persistenceBean.findAndConsumePendingGame(u);
		if (pg != null) {
			// create game
			List<Question> questionList = new ArrayList<>();
			for (int i = 0; i < 5; i++) {
				questionList.add(persistenceBean.getRandomQuestion());
			}
			Game g = new Game(pg.getGameId(), new PlayingUser(u, 0), new PlayingUser(pg.getWaitingUser(), 0),
					questionList);
			g.setCurrentUser(u);
			g.setTimestamp(-1);

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
	@Produces(MediaType.APPLICATION_JSON)
	public Points getPoints(@PathParam("gameId") UUID gameId, @PathParam("userId") long userId) {
		Game game = persistenceBean.getGameWithId(gameId);
		int myPoints = -1;
		int otherPoints = 0;

		if (game.getUser1().getUser().getId() == userId) {
			myPoints = game.getUser1().getPoints();
			otherPoints = game.getUser2().getPoints();
		} else if (game.getUser2().getId() == userId) {
			myPoints = game.getUser2().getPoints();
			otherPoints = game.getUser2().getPoints();
		}

		if (myPoints == -1)
			throw new WebApplicationException(Response.status(400).entity("Invalid user id").build());
		return new Points(myPoints, otherPoints);
	}

	@Override
	@POST
	@Path("/user")
	@Consumes("application/json")
	@Produces("application/json")
	public long createUser(String username) {
		User u = new User();
		u.setUsername(username);
		persistenceBean.persist(u);
		return u.getId();
	}

}
