package de.tinf15b4.quizduell.opentbd.model;

import java.util.List;

public class OpenBtdResults {
    Double response_code;
    private List<Question> results;

    public List<Question> getResults() {
        return results;
    }

    public void setResults(List<Question> results) {
        this.results = results;
    }
}
