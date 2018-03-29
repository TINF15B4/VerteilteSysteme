package de.tinf15b4.quizduell.rest.api;

public interface IQuizduellService {

	Question getQuestion();

	void getQuestion(Answer answer);

	void ready();

	int getPoints();

}
