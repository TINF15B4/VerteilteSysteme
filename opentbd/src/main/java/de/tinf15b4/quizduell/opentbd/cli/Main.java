package de.tinf15b4.quizduell.opentbd.cli;

import com.mashape.unirest.http.exceptions.UnirestException;
import de.tinf15b4.quizduell.opentbd.connector.OpenBtdBrowser;

public class Main {
    public static void main(String[] args) throws UnirestException {
        OpenBtdBrowser openBtdBrowser = OpenBtdBrowser.withToken();
        System.out.println("Token: " + openBtdBrowser.getToken());
    }
}
