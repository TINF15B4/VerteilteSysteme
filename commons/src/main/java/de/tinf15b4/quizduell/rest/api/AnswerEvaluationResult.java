package de.tinf15b4.quizduell.rest.api;

import de.tinf15b4.quizduell.db.Answer;

public class AnswerEvaluationResult {
    public static enum Reason { CORRECT, WRONG, TIMEOUT };

    private Reason reason;
    private Answer correctAnswer;

    public AnswerEvaluationResult() { /* JSON ONLY */ }
    public AnswerEvaluationResult(Reason reason, Answer correctAnswer) {
        this.reason = reason;
        this.correctAnswer = correctAnswer;
    }

    public Reason getReason() {
        return reason;
    }

    public void setReason(Reason reason) {
        this.reason = reason;
    }

    public Answer getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(Answer correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}
