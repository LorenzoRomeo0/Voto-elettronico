import controller.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
		Parent root = loader.load();

		Controller controller = loader.getController();
		controller.setStage(primaryStage);
		
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
	}

}
