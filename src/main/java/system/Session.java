package system;

import dao.UtenteDTO;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Session {
	private static Session session;
	private Stage stage;
	private UtenteDTO Utente;
	private VBox content;
	
	private Session() {
	}

	public static Session getInstance() {
		if (null == session)
			session = new Session();
		return session;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public Stage getStage() {
		return this.stage;
	}

	public UtenteDTO getDatiUtente() {
		return Utente;
	}

	public void setDatiUtente(UtenteDTO Utente) {
		this.Utente = Utente;
	}

	public VBox getContent() {
		return content;
	}

	public void setContent(VBox content) {
		this.content = content;
	}
}
