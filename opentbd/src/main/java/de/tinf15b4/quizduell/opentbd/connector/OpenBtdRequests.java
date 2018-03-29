package de.tinf15b4.quizduell.opentbd.connector;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class OpenBtdRequests {
    public static HttpResponse<JsonNode> receiveToken() throws UnirestException {
        return Unirest.get("https://opentdb.com/api_token.php")
                .queryString("command", "request")
                .asJson();
    }
}
