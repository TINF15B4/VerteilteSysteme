package de.tinf15b4.quizduell.db;

import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Game {

	@Id
	private UUID gameId;

	@OneToMany
	private Set<PlayingUser> users;

	@OneToMany
	private List<Question> questions;

	public Game(UUID gameId, Set<PlayingUser> users, List<Question> questions) {
		assertNotNull(gameId);
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((gameId == null) ? 0 : gameId.hashCode());
		result = prime * result + ((questions == null) ? 0 : questions.hashCode());
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
		if (users == null) {
			if (other.users != null)
				return false;
		} else if (!users.equals(other.users))
			return false;
		return true;
	}

}
