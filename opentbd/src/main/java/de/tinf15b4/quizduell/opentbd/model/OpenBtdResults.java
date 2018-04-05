package de.tinf15b4.quizduell.opentbd.model;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class OpenBtdResults {
    Double response_code;
    private List<OpenBtdQuestion> results;

    public List<OpenBtdQuestion> getResults() throws UnsupportedEncodingException {
        List<OpenBtdQuestion> decQuestions = new ArrayList<OpenBtdQuestion>(results.size());
        for (OpenBtdQuestion q : results) {
            decQuestions.add(q.decode());
        }
        return decQuestions;
    }
}
