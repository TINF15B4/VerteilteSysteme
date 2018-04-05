package de.tinf15b4.quizduell.opentbd.connector;

import com.google.gson.Gson;
import com.mashape.unirest.http.exceptions.UnirestException;
import de.tinf15b4.quizduell.opentbd.model.OpenBtdResults;
import de.tinf15b4.quizduell.opentbd.model.OpenBtdQuestion;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OpenBtdBrowser {

    private final IOpenBtdRequests btdRequests;
    private String Token;

    public OpenBtdBrowser(IOpenBtdRequests requests) throws UnirestException {
        btdRequests = requests;
        Token = requestToken();
    }

    private String requestToken() throws UnirestException {
        String tokenJsonResponse = btdRequests.receiveToken();

        Gson gson = new Gson();
        Map tokenResponse = gson.fromJson(tokenJsonResponse, Map.class);

        if((Double)(tokenResponse.get("response_code")) == 0){
            return tokenResponse.get("token").toString();
        }
        else {
            throw new UnirestException("Invalid response code");
        }
    }

    public List<OpenBtdQuestion> requestQuestions(int number) throws UnirestException, UnsupportedEncodingException {
        return requestQuestions(null, number);
    }

    public List<OpenBtdQuestion> requestQuestions(String token, int number) throws UnirestException, UnsupportedEncodingException {
        if(token == null) token = this.Token;

        List<OpenBtdQuestion> questions = new ArrayList<OpenBtdQuestion>(number);
        do {
            String questionsJsonResponse = btdRequests.retrieveQuestions(number > 50? 50 : number , token);

            Gson gson = new Gson();
            OpenBtdResults questionsResponse = gson.fromJson(questionsJsonResponse, OpenBtdResults.class);
            questions.addAll(questionsResponse.getResults());

            number -= 50;
        } while(number > 50);

        return questions;
    }

    public String getToken() {
        return Token;
    }
}
