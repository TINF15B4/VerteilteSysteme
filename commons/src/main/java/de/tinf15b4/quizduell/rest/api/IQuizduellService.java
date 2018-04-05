package de.tinf15b4.quizduell.rest.api;

import java.util.UUID;

import de.tinf15b4.quizduell.db.Answer;
import de.tinf15b4.quizduell.db.Question;

public interface IQuizduellService {

	Question getQuestion(UUID gameId);

	boolean postAnswer(Answer answer, UUID gameId, long userId);

	UUID ready();

	int getPoints(UUID gameId, long userId);
}
