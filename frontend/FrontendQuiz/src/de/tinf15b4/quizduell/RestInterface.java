package de.tinf15b4.quizduell;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import de.tinf15b4.quizduell.db.Answer;
import de.tinf15b4.quizduell.db.Question;

public class RestInterface {
	private WebResource answers;
	private WebResource questions;
	private WebResource points;
	private WebResource ready;
	
	public RestInterface(String restServiceUrl) {
		Client client = Client.create();
		answers = client.resource(UriBuilder.fromUri(restServiceUrl).path("answer").build());
		questions = client.resource(UriBuilder.fromUri(restServiceUrl).path("question").build());
		points = client.resource(UriBuilder.fromUri(restServiceUrl).path("points").build());
		ready = client.resource(UriBuilder.fromUri(restServiceUrl).path("ready").build());
	}
	
	public void postAnswer(Answer answer) {
		answers.type(MediaType.APPLICATION_JSON).post(answer);
	}
	
	public void postReady() {
		ready.post();
	}
	
	public Question getQuestion() {
		return questions.accept(MediaType.APPLICATION_JSON).get(Question.class);
	}
	
	public int getPoints() {
		return Integer.parseInt(points.get(String.class));
	}

}
