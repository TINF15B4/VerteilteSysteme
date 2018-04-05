package de.tinf15b4.quizduell.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Answer {
	public Answer() {
	}

	@Id
	@GeneratedValue
	private long id;

	@Column
	private String answerString;

	public Answer(String answerString) {
		this.answerString = answerString;
	}

	public long getId() {
		return id;
	}

	public String getAnswerString() {
		return answerString;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((answerString == null) ? 0 : answerString.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
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
		Answer other = (Answer) obj;
		if (answerString == null) {
			if (other.answerString != null)
				return false;
		} else if (!answerString.equals(other.answerString))
			return false;
		if (id != other.id)
			return false;
		return true;
	}

}
