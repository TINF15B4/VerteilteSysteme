package de.tinf15b4.quizduell.db;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Game {

	@Id
	private UUID gameId;

	@OneToMany(cascade = CascadeType.ALL)
	private Set<PlayingUser> users;

	@OneToMany(cascade = CascadeType.ALL)
	private List<Question> questions;

	@Column
	private int currentQuestionIndex;

	@ManyToOne
	@JoinColumn(name = "currentUser")
	private User currentUser;

	@Column
	private long timestamp;

	public Game() {
		/* HIBERNATE COMPAT ONLY */ }

	public Game(UUID gameId, Set<PlayingUser> users, List<Question> questions) {
		this.gameId = gameId;
		this.users = users;
		this.questions = questions;
	}

	public UUID getGameId() {
		return gameId;
	}

	public Set<PlayingUser> getUsers() {
		return users;
	}

	public List<Question> getQuestions() {
		return questions;
	}

	public Question getCurrentQuestion() {
		return questions.get(currentQuestionIndex);
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public Question nextQuestion() {
		currentQuestionIndex++;
		return getCurrentQuestion();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + currentQuestionIndex;
		result = prime * result + ((currentUser == null) ? 0 : currentUser.hashCode());
		result = prime * result + ((gameId == null) ? 0 : gameId.hashCode());
		result = prime * result + ((questions == null) ? 0 : questions.hashCode());
		result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
		result = prime * result + ((users == null) ? 0 : users.hashCode());
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
		Game other = (Game) obj;
		if (currentQuestionIndex != other.currentQuestionIndex)
			return false;
		if (currentUser == null) {
			if (other.currentUser != null)
				return false;
		} else if (!currentUser.equals(other.currentUser))
			return false;
		if (gameId == null) {
			if (other.gameId != null)
				return false;
		} else if (!gameId.equals(other.gameId))
			return false;
		if (questions == null) {
			if (other.questions != null)
				return false;
		} else if (!questions.equals(other.questions))
			return false;
		if (timestamp != other.timestamp)
			return false;
		if (users == null) {
			if (other.users != null)
				return false;
		} else if (!users.equals(other.users))
			return false;
		return true;
	}

}
