package de.tinf15b4.quizduell;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Test.fxml"));

		Scene scene = new Scene(root, 600, 600);

		stage.setTitle("Welcome");
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		Application.launch(Main.class, args);
	}
}
