package de.tinf15b4.quizduell.opentbd.model;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class OpenBtdResults {
    Double response_code;
    private List<Question> results;

    public List<Question> getResults() throws UnsupportedEncodingException {
        List<Question> decQuestions = new ArrayList<Question>(results.size());
        for (Question q : results) {
            decQuestions.add(q.decode());
        }
        return decQuestions;
    }
}
