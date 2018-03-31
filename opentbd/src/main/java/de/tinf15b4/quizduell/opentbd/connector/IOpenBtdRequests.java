package de.tinf15b4.quizduell.opentbd.connector;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;

public interface IOpenBtdRequests {
    HttpResponse<JsonNode> receiveToken() throws UnirestException;
    public HttpResponse<JsonNode> retrieveQuestions(final int number, final String token) throws UnirestException;
}
