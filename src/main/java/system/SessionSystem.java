package system;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SessionSystem {
	private static SessionSystem session;
	private Stage stage;
	private Stage popup;
	private Object[] message;
	
	public Object getMessage() {
		return message[0];
	}
	
	public Object getMessage(int i) {
		return message[i];
	}

	public void setMessage() throws Exception{
		Exception e = new Exception("valore non settato");
		throw e;
	}
	
	public void setMessage(Object... message) {
		this.message = message;
	}

	public Stage getPopup() {
		return popup;
	}

	public void setPopup(Stage popup) {
		this.popup = popup;
	}

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
