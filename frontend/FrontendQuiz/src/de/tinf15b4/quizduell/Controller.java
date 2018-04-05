package de.tinf15b4.quizduell;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;

public class Controller {

	boolean isClicked = false;
	boolean isStarted = false;

	@FXML
	private Label lblQuestion;
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

		btnAnswer1.setDisable(false);
		btnAnswer2.setDisable(false);
		btnAnswer3.setDisable(false);
		btnAnswer4.setDisable(false);
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
				btnAnswer1.setDisable(true);
				btnAnswer2.setDisable(true);
				btnAnswer3.setDisable(true);
				btnAnswer4.setDisable(true);
			}
		};
		progress.start();
	}

}
