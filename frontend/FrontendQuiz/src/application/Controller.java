package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class Controller {
	@FXML
	private Label testlabel;
	@FXML
	private Text testtext;

	@FXML
	protected void handleSubmitButtonAction(ActionEvent event) {
		if(testtext==null) {
		System.out.println("isNUll");
		}else {
		testtext.setText("Sign in button pressed");
		}
	}

}
