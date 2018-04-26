package de.tinf15b4.quizduell;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import de.tinf15b4.quizduell.db.Answer;
import de.tinf15b4.quizduell.db.Points;
import de.tinf15b4.quizduell.db.Question;
import de.tinf15b4.quizduell.db.QuestionDTO;

import java.net.URI;
import java.util.UUID;

public class RestInterface {

	String restServiceUrl;
	Client client;

	private long userID;
	private UUID gameUUID;

	public RestInterface(String restServiceUrl) {
		this.restServiceUrl = restServiceUrl;
		ClientConfig cc = new DefaultClientConfig();
		cc.getClasses().add(JacksonJsonProvider.class);
		this.client = Client.create(cc);	}

	public boolean postAnswer(Answer answer) {
		WebResource answers = client.resource(
				UriBuilder.fromUri(restServiceUrl).path("answer").path("" + gameUUID).path("" + userID).build());
		return answers.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(boolean.class, answer);
	}

	public void postReady() {
		WebResource ready = client.resource(UriBuilder.fromUri(restServiceUrl).path("ready").build());
		gameUUID = ready.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(UUID.class, String.valueOf(userID));
	}

	public void createUser(String username) {
		URI uri = UriBuilder.fromUri(restServiceUrl).path("user").build();
		WebResource user = client.resource(uri);
		userID = Long.valueOf(user.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(String.class, username));
	}

	public HandleQuestion getQuestion() {
		WebResource questions = client.resource(
				UriBuilder.fromUri(restServiceUrl).path("question").path("" + gameUUID).path("" + userID).build());
		ClientResponse response = questions.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		HandleQuestion handleQuestion = new HandleQuestion(response.getEntity(QuestionDTO.class), response.getStatus());
		return handleQuestion;
	}

	public Points getPoints() {
		WebResource points = client.resource(
				UriBuilder.fromUri(restServiceUrl).path("points").path("" + gameUUID).path("" + userID).build());
		return points.accept(MediaType.APPLICATION_JSON).get(Points.class);
	}
}
