package de.tinf15b4.quizduell.rest.api;

import java.util.UUID;

import de.tinf15b4.quizduell.db.Answer;
import de.tinf15b4.quizduell.db.Question;

public interface IQuizduellService {

	Question getQuestion();

	boolean postAnswer(Answer answer, UUID gameId, long userId);

	void ready();

	int getPoints();
}
