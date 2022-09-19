package controllerMain;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import dao.UtenteDTO;
import data.TipoUtente;
import dao.SistemaVotazioniDAO;
import system.SessionSystem;
import system.luoghi.Comune;
import system.utenti.Utente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

public class Controller_login {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TextField in_codiceFiscale;

	@FXML
	private PasswordField in_password;

	@FXML
	private Text txt_error;

	@FXML
	void login(ActionEvent event) {
		login();
	}

	@FXML
	void login_by_enter(KeyEvent event) {
		if (event.getCode().equals(KeyCode.ENTER)) {
			login();
		}
	}

	@FXML
	void initialize() {
		assert in_codiceFiscale != null
				: "fx:id=\"in_codiceFiscale\" was not injected: check your FXML file 'login.fxml'.";
		assert in_password != null : "fx:id=\"in_password\" was not injected: check your FXML file 'login.fxml'.";
		assert txt_error != null : "fx:id=\"txt_error\" was not injected: check your FXML file 'login.fxml'.";
	}

	private void login() {
		SistemaVotazioniDAO dao = SistemaVotazioniDAO.getInstance();
		UtenteDTO dati = dao.login(in_codiceFiscale.getText().toUpperCase(), in_password.getText());
		SessionSystem sessionSystem = SessionSystem.getInstance();
		if (null == dati) {
			txt_error.setText("Password o codice fiscale errati !!!");
			txt_error.setVisible(true);
		} else {
			Comune residenza = sessionSystem.getComune(dati.getResidenza());
			if (null == residenza) {
				txt_error.setText("Errore con il caricamento dati!!!");
				txt_error.setVisible(true);
			} else {
				TipoUtente t = TipoUtente.valueOf(dati.getTipo());
				Utente u = null;
				if(t.equals(TipoUtente.IMPIEGATO))
					u = Utente.makeUtente(dati, residenza);
				else {
					ArrayList<Integer> compilate = dao.getSchedeCompilate(dati.getId());
					u = Utente.makeUtente(dati, residenza, sessionSystem.getSchedeCompilate(compilate));
				}
				sessionSystem.setUtente(u);
				txt_error.setVisible(false);
				if (u.getTipo().equals(TipoUtente.IMPIEGATO)) {
					loadScegliTipo();
				} else if (u.getTipo().equals(TipoUtente.ELETTORE)) {
					SessionSystem.getInstance().loadMain();
				} else {
					System.out.println("---> caricamento main fallito.");
				}
			}
		}
	}

	private void loadScegliTipo() {
		try {
			SessionSystem sessionSystem = SessionSystem.getInstance();
			sessionSystem.loadStage("/main/scegli_tipo_visualizzazione.fxml", "Scegli tipo di intefaccia");
		} catch (IOException e) {
			System.out.println("---> caricamento scegli tipo visualizzazione fallito.");
			e.printStackTrace();
		}
	}

}
