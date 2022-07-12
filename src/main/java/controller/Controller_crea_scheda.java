package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import dao.SistemaVotazioniDAO;
import dao.ValoreSempliceDTO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import system.SessionSystem;

public class Controller_crea_scheda {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private ComboBox<ValoreSempliceDTO> cb_tipo_scheda;

	@FXML
	private VBox content;

	@FXML
	private VBox crea_scheda_content;

	@FXML
	void initialize() {
		assert cb_tipo_scheda != null : "fx:id=\"cb_tipo_scheda\" was not injected: check your FXML file 'crea_scheda.fxml'.";
		assert content != null : "fx:id=\"content\" was not injected: check your FXML file 'crea_scheda.fxml'.";
		assert crea_scheda_content != null : "fx:id=\"crea_scheda_content\" was not injected: check your FXML file 'crea_scheda.fxml'.";

		SessionSystem session = SessionSystem.getInstance();
		VBox container_content = session.getContent();
		container_content.widthProperty().addListener((observable, oldValue, newValue) -> {
			adatta_contenuto(newValue.doubleValue());
		});

		Stage stage = session.getStage();
		stage.resizableProperty().addListener((observable, oldValue, newValue) -> {
			adatta_contenuto(container_content.getWidth());
		});

		SistemaVotazioniDAO dao = new SistemaVotazioniDAO();
		ArrayList<ValoreSempliceDTO> tipi_scheda = dao.get_tipi_scheda_all();
		cb_tipo_scheda.setItems(FXCollections.observableArrayList(tipi_scheda));

		cb_tipo_scheda.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			String path = "/admin/crea_scheda_" + newValue + ".fxml";
			FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
			try {
				Parent parent = loader.load();
				crea_scheda_content.getChildren().clear();
				crea_scheda_content.getChildren().add(parent);
			} catch (IOException e) {
				System.out.println("caricamento crea scheda '" + newValue + "' fallito.");
				e.printStackTrace();
			}
		});
	}

	private void adatta_contenuto(double larghezza) {
		content.setMinWidth(larghezza);
		content.setMaxWidth(larghezza);
		crea_scheda_content.setMinWidth(larghezza);
		crea_scheda_content.setMaxWidth(larghezza);
	}

}
