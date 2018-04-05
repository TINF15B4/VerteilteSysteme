package de.tinf15b4.quizduell.opentbd.connector;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.HashMap;

public class OpenBtdRequests implements IOpenBtdRequests {
    public String receiveToken() throws UnirestException {
        return Unirest.get("https://opentdb.com/api_token.php")
                .queryString("command", "request")
                .asJson()
                .getBody()
                .toString();
    }

    public String retrieveQuestions(final int number, final String token) throws UnirestException {
        return Unirest.get("https://opentdb.com/api.php")
                .queryString(new HashMap<String, Object>(){
                    {
                        put("amount", number);
                        put("token", token);
                        put("encode", "url3986");
                        put("type", "multiple");
                    }
                })
                .asJson()
                .getBody()
                .toString();
    }
}
