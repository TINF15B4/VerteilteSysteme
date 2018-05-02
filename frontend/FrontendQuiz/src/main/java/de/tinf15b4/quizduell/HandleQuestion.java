package de.tinf15b4.quizduell;

import com.sun.jersey.api.client.ClientResponse;

import de.tinf15b4.quizduell.db.QuestionDTO;

public class HandleQuestion {

	public QuestionDTO question;
	public int statuscode;

//	public HandleQuestion(QuestionDTO question, int statuscode) {
//		this.question = question;
//		this.statuscode = statuscode;
//	}

	public HandleQuestion(ClientResponse response) {
		this.statuscode = response.getStatus();
		if (statuscode == 200) {
			this.question = response.getEntity(QuestionDTO.class);
		}
	}
}
