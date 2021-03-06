package de.tinf15b4.quizduell;

import de.tinf15b4.quizduell.db.Answer;
import de.tinf15b4.quizduell.db.Points;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;

public class Controller {
	boolean isTimerStarted = false;
	boolean gameRunning = false;
	RestInterface restInterface;
	private Object[] answers;
	private Thread progressThread;

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

	public void initialize() {
		NameGenerator newName = new NameGenerator();
		playerName = newName.getName();
		lblPlayerName.setText("Hello " + playerName);
		restInterface = new RestInterface("http://18.221.91.246:8080/quizduell/api/"); // TODO
		setDisableAllAnswerButtons(true);
		try {
			restInterface.createUser(playerName);
		} catch (Exception e) {
			lblQuestion.setText(
					"Es konnte keine Verbindung zum Server hergestellt werden. Bitte pruefen Sie ihre Internetverbindung und starten Sie die Anwendung neu.");
			btnStart.setDisable(true);
		}
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
	private void handleSubmit(int answer) {
		boolean isCorrect = restInterface.postAnswer((Answer) this.answers[answer - 1]);
		String output = "Your answer was " + (isCorrect ? "correct" : isTimerStarted ? "wrong" : "too late");
		lblQuestion.setText(output);
		btnStart.setDisable(false);
		isTimerStarted = false;
		if (progressThread != null)
			progressThread.interrupt();
		setDisableAllAnswerButtons(true);
	}

	private void setDisableAllAnswerButtons(boolean disable) {
		btnAnswer1.setDisable(disable);
		btnAnswer2.setDisable(disable);
		btnAnswer3.setDisable(disable);
		btnAnswer4.setDisable(disable);
	}

	@FXML
	protected void handleSubmitButtonStartAction(ActionEvent event) {
		if (gameRunning) {
			handleQuestion();
		} else {
			gameRunning = true;
			restInterface.postReady();
			lblQuestion.setText("Spiel erstellt!");
			System.err.println("DEBUG: Spiel-ID " + restInterface.getGameUUID());
			btnStart.setText("next question");
		}
	}

	@FXML
	public void handleQuestion() {
		HandleQuestion handleQuestion = restInterface.getQuestion();
		if (handleQuestion.statuscode == 423) {
			lblQuestion.setText("Der Gegenspieler ist noch am Zug. Bitte zu einem späteren Zeitpunkt erneut versuchen");
			return;
		} else if (handleQuestion.statuscode == 204) {
			gameRunning = false;
			selectWinner();
			return;
		} else if (handleQuestion.statuscode == 404) {
			lblQuestion.setText("Noch kein Mitspieler gefunden!");
			return;
		}

		answers = handleQuestion.question.getAnswers().toArray();

		lblQuestion.setText(handleQuestion.question.getQuestionString());

		btnAnswer1.setText(((Answer) answers[0]).getAnswerString());
		btnAnswer2.setText(((Answer) answers[1]).getAnswerString());
		btnAnswer3.setText(((Answer) answers[2]).getAnswerString());
		btnAnswer4.setText(((Answer) answers[3]).getAnswerString());
		setDisableAllAnswerButtons(false);

		if (progressThread != null)
			progressThread.interrupt();

		progressThread = new Thread() {
			public void run() {
				progressIndicator.setProgress(0.0);
				while (progressIndicator.getProgress() <= 1) {
					try {
						progressIndicator.setProgress(progressIndicator.getProgress() + 0.05);
						Thread.sleep(1000);

					} catch (InterruptedException v) {
						return;
					}
				}
				isTimerStarted = false;
			}
		};
		isTimerStarted = true;
		progressThread.start();

		btnStart.setDisable(true);
		// btnStart.setText("Next Question");
	}

	@FXML
	public void selectWinner() {
		Points p = restInterface.getPoints();
		String output;
		if (p.getMyPoints() > p.getOtherPoints()) {
			output = "You have won!";
		} else if (p.getMyPoints() < p.getOtherPoints()) {
			output = "You have lost!";
		} else {
			output = "It is a draw!";
		}

		output += " " + p.getMyPoints() + " : " + p.getOtherPoints();
		lblQuestion.setText(output);
		btnStart.setText("Start new game");

		btnAnswer1.setText("Answer 1");
		btnAnswer2.setText("Answer 2");
		btnAnswer3.setText("Answer 3");
		btnAnswer4.setText("Answer 4");
	}

	@FXML
	public void handleQuestionDummy() {

		progressIndicator.setProgress(0.0);
		lblQuestion.setText("Ist das eine Testfrage?");
		btnAnswer1.setText("Ja");
		btnAnswer2.setText("Nein");
		btnAnswer3.setText("Vielleicht");
		btnAnswer4.setText("Leck mich");

		isTimerStarted = true;

		Thread progress = new Thread() {
			public void run() {
				while (progressIndicator.getProgress() <= 1) {
					try {
						progressIndicator.setProgress(progressIndicator.getProgress() + 0.1);
						Thread.sleep(2000);

					} catch (InterruptedException v) {
						return;
					}
				}
				isTimerStarted = false;
			}
		};
		progress.start();
	}

}
