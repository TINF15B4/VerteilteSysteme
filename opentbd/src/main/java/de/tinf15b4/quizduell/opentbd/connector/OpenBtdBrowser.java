package de.tinf15b4.quizduell.opentbd.connector;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.Map;

public class OpenBtdBrowser {

    private String Token;

    private String requestToken() throws UnirestException {
        HttpResponse<JsonNode> tokenHttpResponse = OpenBtdRequests.receiveToken();

        Gson gson = new Gson();
        Map tokenResponse = gson.fromJson(tokenHttpResponse.getBody().toString(), Map.class);

        if((Double)(tokenResponse.get("response_code")) == 0){
            return tokenResponse.get("token").toString();
        }
        else {
            throw new UnirestException("Invalid response code");
        }
    }

    public static OpenBtdBrowser withToken() throws UnirestException {
        OpenBtdBrowser openBtdBrowser = new OpenBtdBrowser();
        openBtdBrowser.Token = openBtdBrowser.requestToken();
        return openBtdBrowser;
    }

    public String getToken() {
        return Token;
    }
}
