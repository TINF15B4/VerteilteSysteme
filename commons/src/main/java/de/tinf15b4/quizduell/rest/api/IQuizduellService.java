package de.tinf15b4.quizduell.rest.api;

import java.util.UUID;

import de.tinf15b4.quizduell.db.Answer;
import de.tinf15b4.quizduell.db.Question;

public interface IQuizduellService {

	Question getQuestion(UUID gameId, long userId);

	boolean postAnswer(Answer answer, UUID gameId, long userId);

	UUID ready(long userid);

	int getPoints(UUID gameId, long userId);

	long createUser(String username);
}
