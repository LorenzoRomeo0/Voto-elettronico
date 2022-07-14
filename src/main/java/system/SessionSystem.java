package system;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SessionSystem {
	private static SessionSystem session;
	private Stage stage;
	private VBox content;
	
	private SessionSystem() {
	}

	public static SessionSystem getInstance() {
		if (null == session)
			session = new SessionSystem();
		return session;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public Stage getStage() {
		return this.stage;
	}

	public VBox getContent() {
		return content;
	}

	public void setContent(VBox content) {
		this.content = content;
	}
	
	public static String date_formatter(LocalDate data) {
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    	return data.format(formatter);
    }
}
