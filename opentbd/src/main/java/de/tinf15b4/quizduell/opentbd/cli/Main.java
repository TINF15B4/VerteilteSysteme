package de.tinf15b4.quizduell.opentbd.cli;

import com.mashape.unirest.http.exceptions.UnirestException;
import de.tinf15b4.quizduell.opentbd.connector.OpenBtdBrowser;
import de.tinf15b4.quizduell.opentbd.model.Question;

import java.util.List;

public class Main {
    public static void main(String[] args) throws UnirestException {
        OpenBtdBrowser openBtdBrowser = OpenBtdBrowser.withToken();
        System.out.println("Token: " + openBtdBrowser.getToken());

        List<Question> questions = openBtdBrowser.requestQuestions(50);
        for (Question q : questions) {
            System.out.println(q.getQuestion());
        }
    }
}
