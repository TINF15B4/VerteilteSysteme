package de.tinf15b4.quizduell.db;

import org.hibernate.annotations.Type;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class PendingGame {

	@Id
	@Type(type="uuid-char")
	private UUID gameId;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User waitingUser;

	public PendingGame() { /* HIBERNATE COMPAT ONLY */ }

	public PendingGame(User waitingUser) {
		this.waitingUser = waitingUser;
		this.gameId = UUID.randomUUID();
	}

	public UUID getGameId() {
		return gameId;
	}

	public User getWaitingUser() {
		return waitingUser;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((gameId == null) ? 0 : gameId.hashCode());
		result = prime * result + ((waitingUser == null) ? 0 : waitingUser.hashCode());
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
		PendingGame other = (PendingGame) obj;
		if (gameId == null) {
			if (other.gameId != null)
				return false;
		} else if (!gameId.equals(other.gameId))
			return false;
		if (waitingUser == null) {
			if (other.waitingUser != null)
				return false;
		} else if (!waitingUser.equals(other.waitingUser))
			return false;
		return true;
	}

}
