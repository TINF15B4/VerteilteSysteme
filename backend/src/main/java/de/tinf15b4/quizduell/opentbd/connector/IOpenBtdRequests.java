package de.tinf15b4.quizduell.opentbd.connector;

import com.mashape.unirest.http.exceptions.UnirestException;

public interface IOpenBtdRequests {
    String receiveToken() throws UnirestException;
    public String retrieveQuestions(final int number, final String token) throws UnirestException;
}
