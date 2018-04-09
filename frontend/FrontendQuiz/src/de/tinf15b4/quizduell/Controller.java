package de.tinf15b4.quizduell;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;

public class Controller {

	boolean isClicked = false;
	boolean isStarted = false;
	RestInterface restInterface;
	
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
	private ProgressIndicator progressIndicator;

	public void initialize () {
		NameGenerator newName = new NameGenerator();
		playerName = newName.getName();
		lblPlayerName.setText("Hello " + playerName);

		restInterface = new RestInterface("TODO Real URL");
	}
	
	@FXML
	protected void handleSubmitButton1Action(ActionEvent event) {
		if (isStarted) {
			isClicked = true;
		}
	}

	@FXML
	protected void handleSubmitButton2Action(ActionEvent event) {
		if (isStarted) {
			isClicked = true;
		}
	}

	@FXML
	protected void handleSubmitButton3Action(ActionEvent event) {
		if (isStarted) {
			isClicked = true;
		}
	}

	@FXML
	protected void handleSubmitButton4Action(ActionEvent event) {
		if (isStarted) {
			isClicked = true;
		}
	}

	@FXML
	protected void handleSubmitButtonStartAction(ActionEvent event) {
		handleQuestion();
	}

	@FXML
	public void handleQuestion() {

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
