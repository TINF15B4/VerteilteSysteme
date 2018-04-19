package de.tinf15b4.quizduell;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import de.tinf15b4.quizduell.db.Answer;
import de.tinf15b4.quizduell.db.Question;

import java.util.UUID;

public class RestInterface {
	private WebResource answers;
	private WebResource questions;
	private WebResource points;
	private WebResource ready;
	
	String restServiceUrl;
	Client client;
	
	private long userID; //TODO get from Rest Service
	private UUID gameUUID; //TODO get from Rest Service
	
	public RestInterface(String restServiceUrl) {
		this.restServiceUrl = restServiceUrl;
		client = Client.create();
		answers = client.resource(UriBuilder.fromUri(restServiceUrl).path("answer").build());
		questions = client.resource(UriBuilder.fromUri(restServiceUrl).path("question").path(""+gameUUID).path(""+userID).build());
		points = client.resource(UriBuilder.fromUri(restServiceUrl).path("points").build());
		ready = client.resource(UriBuilder.fromUri(restServiceUrl).path("ready").build());
	}
	
	public void postAnswer(Answer answer) {
		answers.type(MediaType.APPLICATION_JSON).post(answer);
	}
	
	public void postReady() {
		gameUUID = ready.type(MediaType.APPLICATION_JSON).post(UUID.class, userID);
	}
	
	public void createUser(String username) {
		WebResource user = client.resource(UriBuilder.fromUri(restServiceUrl).path("user").build());
		userID = user.type(MediaType.APPLICATION_JSON).post(long.class, username);
	}
	
	public Question getQuestion() {
		return questions.accept(MediaType.APPLICATION_JSON).get(Question.class);
	}
	
	public int getPoints() {
		return Integer.parseInt(points.get(String.class));
	}
}
