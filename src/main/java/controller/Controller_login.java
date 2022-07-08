package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import dao.UtenteDTO;
import dao.SistemaVotazioniDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import system.Session;

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
		assert in_codiceFiscale != null : "fx:id=\"in_codiceFiscale\" was not injected: check your FXML file 'login.fxml'.";
		assert in_password != null : "fx:id=\"in_password\" was not injected: check your FXML file 'login.fxml'.";
		assert txt_error != null : "fx:id=\"txt_error\" was not injected: check your FXML file 'login.fxml'.";
	}

	private void login() {
		SistemaVotazioniDAO login = new SistemaVotazioniDAO();
		UtenteDTO dati = login.login(in_codiceFiscale.getText(), in_password.getText());
		/*if (null == dati) {
			txt_error.setVisible(true);
		} else {
			txt_error.setVisible(false);*/
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("../main/main.fxml"));
				Parent root = loader.load();
				Session session = Session.getInstance();
				Stage stage = session.getStage();
				session.setDatiUtente(dati);
				stage.setScene(new Scene(root));
				stage.show();;
			} catch (IOException e) {
				System.out.println("---> caricamento main fallito.");
				e.printStackTrace();
			}
		//}
	}

}
