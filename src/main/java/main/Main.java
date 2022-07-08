package main;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import system.Session;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Session session = Session.getInstance();
		session.setStage(primaryStage);
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/login.fxml"));
		Parent root = loader.load();
		
		primaryStage.setTitle("Login");
		primaryStage.setScene(new Scene(root, 450, 350));
		primaryStage.show();
		
		primaryStage.setMinWidth(450);
		primaryStage.setMinHeight(350);
		primaryStage.setWidth(450);
		primaryStage.setHeight(350); 
	}

	public static void main(String[] args) {
		launch(args);
		return;
	}

}