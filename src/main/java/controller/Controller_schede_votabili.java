package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import dao.SchedaDTO;
import dao.SistemaVotazioniDAO;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import system.SessionSystem;
import system.SessionUser;

public class Controller_schede_votabili {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private VBox content;

	@FXML
	private ListView<SchedaDTO> lv_schede;

	@FXML
	private Text txt_error;
	
	private SessionUser session_user;
	private SessionSystem session_system;
	private SistemaVotazioniDAO dao;

	@FXML
	void vota(ActionEvent event) {
		txt_error.setVisible(false);
		SchedaDTO value = lv_schede.getSelectionModel().getSelectedItem();
		if (null != value) {
			String carica = null;
			if (value.getTipoScheda().equals("referendum")) {
				carica = "/utente/vota_scheda_referendum.fxml";
			} else if (value.getTipoScheda().equals("categorica")) {
				carica = "/utente/vota_scheda_categorica.fxml";
			}
			if (null != carica) {
				try {
					session_user.setScheda(value);
					FXMLLoader loader = new FXMLLoader(getClass().getResource(carica));
					Parent root = loader.load();
					Stage stage = session_system.getStage();
					stage.setTitle("Vota Referendum");
					stage.setScene(new Scene(root, 450, 350));
				} catch (IOException e) {
					System.out.println("Caricamento vota fallito.");
					e.printStackTrace();
				}
			}
		}
	}

	@FXML
	void ricarica(ActionEvent event) {
		ArrayList<SchedaDTO> values = dao.get_schede_votabili(session_user.getUtente().getId());
		if (null != values) {
			lv_schede.setItems(FXCollections.observableArrayList(values));
		}
	}
	
	@FXML
	void initialize() {
		assert content != null : "fx:id=\"content\" was not injected: check your FXML file 'schede_votabili.fxml'.";
		assert lv_schede != null : "fx:id=\"lv_schede\" was not injected: check your FXML file 'schede_votabili.fxml'.";
		assert txt_error != null : "fx:id=\"txt_error\" was not injected: check your FXML file 'schede_votabili.fxml'.";
		session_user = SessionUser.getInstance();
		session_system = SessionSystem.getInstance();
		VBox container_content = session_system.getContent();
		container_content.widthProperty().addListener((observable, oldValue, newValue) -> {
			adatta_contenuto(newValue.doubleValue());
		});

		Stage stage = session_system.getStage();
		stage.resizableProperty().addListener((observable, oldValue, newValue) -> {
			adatta_contenuto(container_content.getWidth());
		});

		dao = new SistemaVotazioniDAO();
		ArrayList<SchedaDTO> values = dao.get_schede_votabili(session_user.getUtente().getId());
		if (null != values) {
			lv_schede.setItems(FXCollections.observableArrayList(values));
		}
	}

	private void adatta_contenuto(double larghezza) {
		content.setMinWidth(larghezza);
		content.setMaxWidth(larghezza);
	}

}
