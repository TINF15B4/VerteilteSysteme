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
		checkPreconditions(userId, game);

		if (game.getTimestamp() == -1) {
			LOGGER.info("Starting timer");
			game.setTimestamp(System.currentTimeMillis());
			persistenceBean.transaction().update(game).commit();
		}
		LOGGER.info("Returning current question");
		return game.getCurrentQuestion().toDTO();
	}

	private void checkPreconditions(long userId, Game game) {
		if (game == null)
			throw new WebApplicationException(Response.status(404).entity("Unknown Game").build());

		if (game.getCurrentQuestion() == null)
			throw new WebApplicationException(Response.status(204).entity("Game is finished").build());

		if (game.getCurrentUser().getId() != userId)
			throw new WebApplicationException(Response.status(423).entity("It is the other user's turn").build());
	}

	@Override
	@POST
	@Path("/answer/{gameId}/{userId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void postAnswer(Answer answer, @PathParam("gameId") UUID gameId, @PathParam("userId") long userId) {
		Game game = persistenceBean.findById(Game.class, gameId);
		checkPreconditions(userId, game);

		long timestamp = game.getTimestamp();
		Question question = game.getCurrentQuestion();

		updateUsersAndTimestamp(game);
		if (checkAnswer(question, answer, game, timestamp)) {
			LOGGER.info("Correct answer");
			game.getCurrentPlayingUser().incrementPoints();
			persistenceBean.transaction().update(game.getCurrentPlayingUser()).commit();
		}

	}

	private void updateUsersAndTimestamp(Game game) {
		if (game.getCurrentUser().getId() == game.getUser1().getUser().getId()) {
			// user 1 --> switch to user 2
			game.setCurrentUser(game.getUser2().getUser());
		} else {
			// user 2 --> switch to user 1 and advance question
			game.setCurrentUser(game.getUser1().getUser());
			game.nextQuestion();
		}
		game.setTimestamp(-1);
		persistenceBean.transaction().update(game).commit();
	}

	private boolean checkAnswer(Question answeredQuestion, Answer answer, Game game, long timestamp) {
		if (System.currentTimeMillis() - timestamp > ANSWER_TIMEOUT_MILLIS) {
			LOGGER.info("Timeout reached");
			throw new WebApplicationException(
					Response.status(406).entity("Answer too late. Timeout has been reached").build());
		}

		if (answeredQuestion.getCorrectAnswer().equals(answer))
			return true;
		if (!answeredQuestion.getAnswers().contains(answer)) {
			LOGGER.info("Wrong answer - answer not in answer set");
			LOGGER.info("In Answer set:");
			for (Answer a : answeredQuestion.getAnswers()) {
				LOGGER.info("    - " + a.getId() + ": " + a.getAnswerString());
			}
			LOGGER.info("given: " + answer.getId() + ": " + answer.getAnswerString());
			LOGGER.info("correct: " + answeredQuestion.getCorrectAnswer().getId()
					+ ": " + answeredQuestion.getCorrectAnswer().getAnswerString());
			throw new WebApplicationException(
					Response.status(400).entity("Answer not in question's answer set").build());
		}

		LOGGER.info("Wrong answer - Correct would have been "
				+ answeredQuestion.getCorrectAnswer().getId() + ": "
				+ answeredQuestion.getCorrectAnswer().getAnswerString());
		throw new WebApplicationException(Response.status(406).entity("Answer was wrong").build());
	}

	@Override
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/ready")
	public UUID ready(long userid) {
		User user = persistenceBean.findById(User.class, userid);
		if (user == null)
			throw new WebApplicationException(Response.status(400).entity("Unknown user").build());

		PendingGame pendingGame = persistenceBean.findAndConsumePendingGame(user);
		if (pendingGame != null) {
			LOGGER.info("Starting game");
			// create game
			List<Question> questionList = new ArrayList<>();
			for (int i = 0; i < 5; i++) {
				questionList.add(persistenceBean.getRandomQuestion());
			}
			Game game = new Game(pendingGame.getGameId(), new PlayingUser(user, 0),
					new PlayingUser(pendingGame.getWaitingUser(), 0), questionList);
			game.setCurrentUser(user);
			game.setTimestamp(-1);

			persistenceBean.transaction().update(game).commit();
			return game.getGameId();
		} else {
			// create new pending game
			LOGGER.info("Game pending");
			pendingGame = new PendingGame(user);
			persistenceBean.transaction().update(pendingGame).commit();

			return pendingGame.getGameId();
		}
	}

	@Override
	@GET
	@Path("/points/{gameId}/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Points getPoints(@PathParam("gameId") UUID gameId, @PathParam("userId") long userId) {
		Game game = persistenceBean.findById(Game.class, gameId);
		int myPoints = -1;
		int otherPoints = 0;

		if (game.getUser1().getUser().getId() == userId) {
			myPoints = game.getUser1().getPoints();
			otherPoints = game.getUser2().getPoints();
		} else if (game.getUser2().getUser().getId() == userId) {
			myPoints = game.getUser2().getPoints();
			otherPoints = game.getUser1().getPoints();
		}

		if (myPoints == -1)
			throw new WebApplicationException(Response.status(400).entity("Unknown user").build());
		return new Points(myPoints, otherPoints);
	}

	@Override
	@POST
	@Path("/user")
	@Consumes("application/json")
	@Produces("application/json")
	public long createUser(String username) {
		User user = new User();
		user.setUsername(username);
		persistenceBean.transaction().persist(user).commit();
		return user.getId();
	}

}
