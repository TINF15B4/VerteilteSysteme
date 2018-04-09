package de.tinf15b4.quizduell;

import de.tinf15b4.quizduell.db.Answer;
import de.tinf15b4.quizduell.db.Question;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;

import java.util.Set;

public class Controller {

	boolean isClicked = false;
	boolean isStarted = false;
	RestInterface restInterface;
    private Object[] answers;
	
	String playerName = "";

	@FXML
	private Label lblQuestion;
	@FXML
	private Label lblPlayerName;
	@FXML
	private Button btnAnswer1;
	@FXML
	private Button btnAnswer2;
	@FXML
	private Button btnAnswer3;
	@FXML
	private Button btnAnswer4;
	@FXML
	private Button btnStart;
	@FXML
	private ProgressIndicator progressIndicator;

    public void initialize () {
		NameGenerator newName = new NameGenerator();
		playerName = newName.getName();
		lblPlayerName.setText("Hello " + playerName);

		restInterface = new RestInterface("TODO Real URL");
	}
	
	@FXML
	protected void handleSubmitButton1Action(ActionEvent event) {
        handleSubmit(1);
	}

	@FXML
	protected void handleSubmitButton2Action(ActionEvent event) {
        handleSubmit(2);
	}

	@FXML
	protected void handleSubmitButton3Action(ActionEvent event) {
        handleSubmit(3);
	}

	@FXML
	protected void handleSubmitButton4Action(ActionEvent event) {
        handleSubmit(4);
	}

	@FXML
    private void handleSubmit(int answer){
        if (isStarted) {
            restInterface.postAnswer((Answer) this.answers[answer - 1]);
            btnStart.setDisable(false);
            isClicked = true;
            isStarted = false;
            setDisableAllAnswerButtons(true);
        }
    }

    private void setDisableAllAnswerButtons(boolean disable) {
        btnAnswer1.setDisable(disable);
        btnAnswer2.setDisable(disable);
        btnAnswer3.setDisable(disable);
        btnAnswer4.setDisable(disable);
    }

    @FXML
	protected void handleSubmitButtonStartAction(ActionEvent event) {
		handleQuestion();
	}

	@FXML
	public void handleQuestion() {
        Question question = restInterface.getQuestion();
        answers = question.getAnswers().toArray();

        lblQuestion.setText(question.getQuestionString());

        btnAnswer1.setText(((Answer) answers[0]).getAnswerString());
        btnAnswer2.setText(((Answer) answers[1]).getAnswerString());
        btnAnswer3.setText(((Answer) answers[2]).getAnswerString());
        btnAnswer4.setText(((Answer) answers[3]).getAnswerString());
        setDisableAllAnswerButtons(false);

		progressIndicator.setProgress(0.0);

		isStarted = true;

		Thread progress = new Thread() {
			public void run() {
				while (progressIndicator.getProgress() <= 1 && !isClicked) {
					try {
						progressIndicator.setProgress(progressIndicator.getProgress() + 0.1);
						Thread.sleep(1000);

					} catch (InterruptedException v) {
						System.out.println(v);
					}
				}
				isStarted = false;
			}
		};
		progress.start();

		btnStart.setDisable(true);
		btnStart.setText("Next Question");

	}

	@FXML
	public void handleQuestionDummy() {

		progressIndicator.setProgress(0.0);
		lblQuestion.setText("Ist das eine Testfrage?");
		btnAnswer1.setText("Ja");
		btnAnswer2.setText("Nein");
		btnAnswer3.setText("Vielleicht");
		btnAnswer4.setText("Leck mich");

		isStarted = true;

		Thread progress = new Thread() {
			public void run() {
				while (progressIndicator.getProgress() <= 1 && !isClicked) {
					try {
						progressIndicator.setProgress(progressIndicator.getProgress() + 0.1);
						Thread.sleep(1000);

					} catch (InterruptedException v) {
						System.out.println(v);
					}
				}
				isStarted = false;
			}
		};
		progress.start();
	}

}
