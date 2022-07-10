package controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import dao.UtenteDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import system.SessionSystem;
import system.SessionUser;

public class Controller_benvenuto {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private VBox content;

	@FXML
	private Label txt_benvenuto;

	@FXML
	void initialize() {
		assert content != null : "fx:id=\"content\" was not injected: check your FXML file 'benvenuto.fxml'.";
		assert txt_benvenuto != null : "fx:id=\"txt_benvenuto\" was not injected: check your FXML file 'benvenuto.fxml'.";

		SessionSystem sessionSystem = SessionSystem.getInstance();
		VBox container_content = sessionSystem.getContent();
		container_content.widthProperty().addListener((observable, oldValue, newValue) -> {
			double larghezza = newValue.doubleValue();
			content.setMinWidth(larghezza);
			content.setMaxWidth(larghezza);
		});

		Stage stage = sessionSystem.getStage();
		stage.resizableProperty().addListener((observable, oldValue, newValue) -> {
			double larghezza = container_content.getWidth();
			content.setMinWidth(larghezza);
			content.setMaxWidth(larghezza);
		});
		
		SessionUser sessionUser = SessionUser.getInstance();
		UtenteDTO u = sessionUser.getUtente();
		String nome = prima_lettera_maiuscola(u.getNome());
		String cognome = prima_lettera_maiuscola(u.getCognome());
		txt_benvenuto.setText("Benvenuto " + nome + " " + cognome + " !!!");

	}
	
	private String prima_lettera_maiuscola(String testo) {
		return Pattern.compile("\\b(.)(.*?)\\b").matcher(testo)
				.replaceAll(matche -> matche.group(1).toUpperCase() + matche.group(2));
	}

}
