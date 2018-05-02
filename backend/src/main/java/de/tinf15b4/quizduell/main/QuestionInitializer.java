package de.tinf15b4.quizduell.main;

import java.util.List;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.tinf15b4.quizduell.db.Answer;
import de.tinf15b4.quizduell.db.EntityManagerFactoryBean;
import de.tinf15b4.quizduell.db.Question;
import de.tinf15b4.quizduell.opentbd.connector.OpenBtdBrowser;
import de.tinf15b4.quizduell.opentbd.connector.OpenBtdRequests;
import de.tinf15b4.quizduell.opentbd.model.OpenBtdQuestion;

@ApplicationScoped
public class QuestionInitializer {
    private static final Logger LOG = LoggerFactory.getLogger(QuestionInitializer.class);

    @Inject
    private EntityManagerFactoryBean emf;

    public void maybeInitializeQuestions() throws Exception {
        EntityManager em = emf.createEntityManager();
        try {

            TypedQuery<Long> query = em.createQuery("SELECT count(*) FROM Question", Long.class);
            if (query.getSingleResult() > 5) {
                return;
            }

            OpenBtdBrowser openBtdBrowser = new OpenBtdBrowser(new OpenBtdRequests());
            LOG.info("OpenTBDingens Token: " + openBtdBrowser.getToken());

            List<OpenBtdQuestion> questions = openBtdBrowser.requestQuestions(2000);

            for (OpenBtdQuestion q : questions) {

                Set<Answer> incorrect_answers = q.getIncorrect_answers();
                Answer correct_answer = q.getCorrect_answer();

                Question question = new Question(q.getQuestion(), incorrect_answers, correct_answer);
                em.getTransaction().begin();
                try {
                    em.persist(question);
                    em.getTransaction().commit();
                } catch (Throwable t) {
                    em.getTransaction().rollback();
                    throw t;
                }
            }
        } finally {
            em.close();
        }
    }
}
