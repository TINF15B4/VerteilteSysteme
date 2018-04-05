package de.tinf15b4.quizduell.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class PlayingUser {

	@Id
	@GeneratedValue
	private long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Column
	private int points;

	public PlayingUser(User user, int points) {
		this.user = user;
		this.points = points;
	}

	public long getId() {
		return id;
	}

	public int getPoints() {
		return points;
	}

	public User getUser() {
		return user;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + points;
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		PlayingUser other = (PlayingUser) obj;
		if (id != other.id)
			return false;
		if (points != other.points)
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

}
