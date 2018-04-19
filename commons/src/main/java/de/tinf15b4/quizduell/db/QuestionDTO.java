package de.tinf15b4.quizduell.db;

import java.util.Set;

public class QuestionDTO {

	private long id;
	private String questionString;
	private Set<Answer> answers;

	public QuestionDTO() {
	}

	public QuestionDTO(long id, String questionString, Set<Answer> answers, Answer correctAnswer) {
		this.id = id;
		this.questionString = questionString;
		this.answers = answers;
		answers.add(correctAnswer);
	}

	public String getQuestionString() {
		return questionString;
	}

	public Set<Answer> getAnswers() {
		return answers;
	}

	public long getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((answers == null) ? 0 : answers.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((questionString == null) ? 0 : questionString.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QuestionDTO other = (QuestionDTO) obj;
		if (answers == null) {
			if (other.answers != null)
				return false;
		} else if (!answers.equals(other.answers))
			return false;
		if (id != other.id)
			return false;
		if (questionString == null) {
			if (other.questionString != null)
				return false;
		} else if (!questionString.equals(other.questionString))
			return false;
		return true;
	}

}
