package de.tinf15b4.quizduell.rest.services;

import java.util.List;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.tinf15b4.quizduell.db.Answer;
import de.tinf15b4.quizduell.db.Game;
import de.tinf15b4.quizduell.db.PersistenceBean;
import de.tinf15b4.quizduell.db.PlayingUser;
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
	@POST
	@Path("/answer/{gameId}/{userId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean postAnswer(Answer answer, @PathParam("gameId") UUID gameId, @PathParam("userId") long userId) {
		PersistenceBean dbBean = new PersistenceBean();
		List<Game> gameList = dbBean.getGameWithId(gameId);
		if(gameList.size()!=1) {
			//TODO: react on error
			System.err.println("to much/less games: "+gameList.size());
		}else {
			Game currentGame = gameList.get(0);
			for(PlayingUser playingUser : currentGame.getUsers()) {
				if(playingUser.getUser().getId() == userId) {
					//We found the right user in the right game!
					Question currentQuestion = currentGame.getCurrentQuestion();
					if(currentQuestion.getAnswers().contains(answer)) {
						if(currentQuestion.getCorrectAnswer().equals(answer)) {
							playingUser.incrementPoints();
							return true;
						}
					}else {
						//TODO
						//recieved an Answer that does not match the answers of the question
						System.err.println("answer not in question answer set");
					}
				}
			}
		}	
		return false;
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
