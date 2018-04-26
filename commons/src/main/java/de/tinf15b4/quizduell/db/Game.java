package de.tinf15b4.quizduell.db;

import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Game {

	@Id
	private UUID gameId;

	@OneToOne(cascade = CascadeType.ALL)
	private PlayingUser user1;

	@OneToOne(cascade = CascadeType.ALL)
	private PlayingUser user2;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
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

	public Game(UUID gameId, PlayingUser user1, PlayingUser user2, List<Question> questions) {
		this.gameId = gameId;
		this.user1 = user1;
		this.user2 = user2;
		this.questions = questions;
	}

	public UUID getGameId() {
		return gameId;
	}

	public PlayingUser getUser1() {
		return user1;
	}

	public PlayingUser getUser2() {
		return user2;
	}

	public PlayingUser getCurrentPlayingUser() {
		if (user1.getUser().getId() == currentUser.getId())
			return user1;
		else
			return user2;
	}

	public List<Question> getQuestions() {
		return questions;
	}

	public Question getCurrentQuestion() {
		if (currentQuestionIndex >= questions.size()) {
			return null;
		} else {
			return questions.get(currentQuestionIndex);
		}
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
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
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Game))
			return false;

		Game game = (Game) o;

		if (currentQuestionIndex != game.currentQuestionIndex)
			return false;
		if (timestamp != game.timestamp)
			return false;
		if (gameId != null ? !gameId.equals(game.gameId) : game.gameId != null)
			return false;
		if (user1 != null ? !user1.equals(game.user1) : game.user1 != null)
			return false;
		if (user2 != null ? !user2.equals(game.user2) : game.user2 != null)
			return false;
		if (questions != null ? !questions.equals(game.questions) : game.questions != null)
			return false;
		return currentUser != null ? currentUser.equals(game.currentUser) : game.currentUser == null;
	}

	@Override
	public int hashCode() {
		int result = gameId != null ? gameId.hashCode() : 0;
		result = 31 * result + (user1 != null ? user1.hashCode() : 0);
		result = 31 * result + (user2 != null ? user2.hashCode() : 0);
		result = 31 * result + (questions != null ? questions.hashCode() : 0);
		result = 31 * result + currentQuestionIndex;
		result = 31 * result + (currentUser != null ? currentUser.hashCode() : 0);
		result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
		return result;
	}
}
