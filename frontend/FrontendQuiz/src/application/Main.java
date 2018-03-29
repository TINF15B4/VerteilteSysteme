package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("Test.fxml"));

		Scene scene = new Scene(root, 600, 600);

		stage.setTitle("Welcome");
		stage.setScene(scene);
		stage.show();
	}

	// @Override
	// public void start(Stage primaryStage) {
	// try {
	// BorderPane root = new BorderPane();
	// Scene scene = new Scene(root,400,400);
	// scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
	// primaryStage.setScene(scene);
	// primaryStage.show();
	// } catch(Exception e) {
	// e.printStackTrace();
	// }
	// }

	public static void main(String[] args) {
		Application.launch(Main.class, args);
	}
}
