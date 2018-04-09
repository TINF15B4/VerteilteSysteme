package de.tinf15b4.quizduell.opentbd.cli;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.mashape.unirest.http.exceptions.UnirestException;

import de.tinf15b4.quizduell.db.Answer;
import de.tinf15b4.quizduell.db.Question;
import de.tinf15b4.quizduell.opentbd.connector.OpenBtdBrowser;
import de.tinf15b4.quizduell.opentbd.connector.OpenBtdRequests;
import de.tinf15b4.quizduell.opentbd.model.OpenBtdQuestion;

public class Main {
	public static void main(String[] args) throws UnirestException, UnsupportedEncodingException {
		OpenBtdBrowser openBtdBrowser = new OpenBtdBrowser(new OpenBtdRequests());
		System.out.println("Token: " + openBtdBrowser.getToken());

		List<OpenBtdQuestion> questions = openBtdBrowser.requestQuestions(2000);

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("testing");
		EntityManager entityManager = factory.createEntityManager();
		entityManager.getTransaction().begin();

		for (OpenBtdQuestion q : questions) {

			Set<Answer> incorrect_answers = q.getIncorrect_answers();
			Answer correct_answer = q.getCorrect_answer();

			incorrect_answers.forEach(entityManager::persist);
			entityManager.persist(correct_answer);

			Question question = new Question(q.getQuestion(), incorrect_answers, correct_answer);
			entityManager.persist(question);

		}

		entityManager.getTransaction().commit();
		entityManager.close();
		factory.close();
	}
}
