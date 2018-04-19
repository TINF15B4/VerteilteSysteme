package de.tinf15b4.quizduell;

import de.tinf15b4.quizduell.db.QuestionDTO;

public class HandleQuestion {

	public QuestionDTO question;
	public int statuscode;

	public HandleQuestion(QuestionDTO question, int statuscode) {
		this.question = question;
		this.statuscode = statuscode;
	}
}
