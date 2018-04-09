package de.tinf15b4.quizduell.db;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Question {

	public Question() {
	}

	public Question(String questionString, Set<Answer> answers, Answer correctAnswer) {
		this.questionString = questionString;
		this.answers = answers;
		this.correctAnswer = correctAnswer;
	}

	@Id
	@GeneratedValue
	private long id;

	@Column
	private String questionString;

	@OneToMany(cascade = CascadeType.ALL)
	private Set<Answer> answers;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "answer_id")
	private Answer correctAnswer;

	public String getQuestionString() {
		return questionString;
	}

	public Set<Answer> getAnswers() {
		return answers;
	}

	public Answer getCorrectAnswer() {
		return correctAnswer;
	}

	public long getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((answers == null) ? 0 : answers.hashCode());
		result = prime * result + ((correctAnswer == null) ? 0 : correctAnswer.hashCode());
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
		Question other = (Question) obj;
		if (answers == null) {
			if (other.answers != null)
				return false;
		} else if (!answers.equals(other.answers))
			return false;
		if (correctAnswer == null) {
			if (other.correctAnswer != null)
				return false;
		} else if (!correctAnswer.equals(other.correctAnswer))
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
