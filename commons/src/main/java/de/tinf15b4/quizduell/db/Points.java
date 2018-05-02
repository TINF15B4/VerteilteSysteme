package de.tinf15b4.quizduell.db;

public class Points {

	private int myPoints;
	private int otherPoints;

	public Points() { /* JSON ONLY */ }

	public Points(int myPoints, int otherPoints) {
		this.myPoints = myPoints;
		this.otherPoints = otherPoints;
	}

	public int getMyPoints() {
		return myPoints;
	}

	public int getOtherPoints() {
		return otherPoints;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + myPoints;
		result = prime * result + otherPoints;
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
		Points other = (Points) obj;
		if (myPoints != other.myPoints)
			return false;
		if (otherPoints != other.otherPoints)
			return false;
		return true;
	}

}
