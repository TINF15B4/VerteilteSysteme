package de.tinf15b4.quizduell.rest.api;

import java.util.UUID;

import de.tinf15b4.quizduell.db.Answer;
import de.tinf15b4.quizduell.db.Points;
import de.tinf15b4.quizduell.db.QuestionDTO;

public interface IQuizduellService {

	QuestionDTO getQuestion(UUID gameId, long userId);

	void postAnswer(Answer answer, UUID gameId, long userId);

	UUID ready(long userid);

	Points getPoints(UUID gameId, long userId);

	long createUser(String username);
}
