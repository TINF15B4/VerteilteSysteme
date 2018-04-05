package de.tinf15b4.quizduell.rest.api;

import de.tinf15b4.quizduell.db.Answer;
import de.tinf15b4.quizduell.db.Question;

public interface IQuizduellService {

	Question getQuestion();

	void postAnswer(Answer answer);

	void ready();

	int getPoints();

}
