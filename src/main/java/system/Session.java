package system;

import dao.DatiUtenteDTO;
import javafx.stage.Stage;

public class Session {
	private static Session session;
	private Stage stage;
	private DatiUtenteDTO datiUtente;
	
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

	public DatiUtenteDTO getDatiUtente() {
		return datiUtente;
	}

	public void setDatiUtente(DatiUtenteDTO datiUtente) {
		this.datiUtente = datiUtente;
	}
	
	

}
