package main;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene; 
import javafx.stage.Stage;
import system.SessionSystem;

public class Main extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		try {
			SessionSystem session = SessionSystem.getInstance();
			session.setStage(stage);
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/login.fxml"));
			Parent root = loader.load();
			stage.setTitle("Login");
			stage.setScene(new Scene(root, SessionSystem.stageMinW, SessionSystem.stageMinH));
			stage.show();
			stage.setMinHeight(stage.getHeight());
			stage.setMinWidth(stage.getWidth());
		} catch (IOException e) {
			System.out.println("---> caricamento login fallito.");
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
