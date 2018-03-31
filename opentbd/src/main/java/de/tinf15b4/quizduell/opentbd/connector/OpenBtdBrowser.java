package de.tinf15b4.quizduell.opentbd.connector;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import de.tinf15b4.quizduell.opentbd.model.OpenBtdResults;
import de.tinf15b4.quizduell.opentbd.model.Question;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
        HttpResponse<JsonNode> tokenHttpResponse = btdRequests.receiveToken();

        Gson gson = new Gson();
        Map tokenResponse = gson.fromJson(tokenHttpResponse.getBody().toString(), Map.class);

        if((Double)(tokenResponse.get("response_code")) == 0){
            return tokenResponse.get("token").toString();
        }
        else {
            throw new UnirestException("Invalid response code");
        }
    }

    public List<Question> requestQuestions(int number) throws UnirestException, UnsupportedEncodingException {
        return requestQuestions(null, number);
    }

    public List<Question> requestQuestions(String token, int number) throws UnirestException, UnsupportedEncodingException {
        if(token == null) token = this.Token;
        HttpResponse<JsonNode> questionsHttpResponse = btdRequests.retrieveQuestions(number, token);

        Gson gson = new Gson();
        OpenBtdResults questionsResponse = gson.fromJson(questionsHttpResponse.getBody().toString(), OpenBtdResults.class);
        return (List<Question>) questionsResponse.getResults();
    }

    public String getToken() {
        return Token;
    }
}
