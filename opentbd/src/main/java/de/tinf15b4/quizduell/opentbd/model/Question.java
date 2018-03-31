package de.tinf15b4.quizduell.opentbd.model;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
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

    public Question decode() throws UnsupportedEncodingException {
        Question question = new Question();
        question.category = URLDecoder.decode(category, "UTF-8");
        question.type = URLDecoder.decode(type, "UTF-8");
        question.difficulty = URLDecoder.decode(difficulty, "UTF-8");
        question.question = URLDecoder.decode(this.question, "UTF-8");
        question.correct_answer = URLDecoder.decode(correct_answer, "UTF-8");
        question.incorrect_answers = new ArrayList<String>(incorrect_answers.size());
        for (String incorrect_answer : incorrect_answers){
            question.incorrect_answers.add(URLDecoder.decode(incorrect_answer, "UTF-8"));
        }
        return question;
    }
}
