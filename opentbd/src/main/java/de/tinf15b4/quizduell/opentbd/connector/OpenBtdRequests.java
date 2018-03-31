package de.tinf15b4.quizduell.opentbd.connector;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.HashMap;

public class OpenBtdRequests implements IOpenBtdRequests {
    public HttpResponse<JsonNode> receiveToken() throws UnirestException {
        return Unirest.get("https://opentdb.com/api_token.php")
                .queryString("command", "request")
                .asJson();
    }

    public HttpResponse<JsonNode> retrieveQuestions(final int number, final String token) throws UnirestException {
        return Unirest.get("https://opentdb.com/api.php")
                .queryString(new HashMap<String, Object>(){
                    {
                        put("amount", number);
                        put("token", token);
                        put("encode", "url3986");
                    }
                })
                .asJson();
    }
}
