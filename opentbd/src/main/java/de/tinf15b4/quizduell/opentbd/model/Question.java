package de.tinf15b4.quizduell.opentbd.model;

import java.util.List;

public class Question {
    String category;
    String type;
    String difficulty;
    String question;
    String correct_answer;
    List<String> incorrect_answers;

    public String getQuestion() {
        return question;
    }
}
