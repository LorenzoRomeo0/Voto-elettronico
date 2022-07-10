package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import dao.SistemaVotazioniDAO;
import dao.TipoSchedaDTO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
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
    private ComboBox<TipoSchedaDTO> cb_tipo_scheda;

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
			double larghezza = newValue.doubleValue();
			content.setMinWidth(larghezza);
			content.setMaxWidth(larghezza);
			crea_scheda_content.setMinWidth(larghezza);
			crea_scheda_content.setMaxWidth(larghezza);
		});

		Stage stage = session.getStage();
		stage.resizableProperty().addListener((observable, oldValue, newValue) -> {
			double larghezza = container_content.getWidth();
			content.setMinWidth(larghezza);
			content.setMaxWidth(larghezza);
		});
		
		SistemaVotazioniDAO dao = new SistemaVotazioniDAO();
		ArrayList<TipoSchedaDTO> tipi_scheda = dao.getTipiSchedaAll();
		cb_tipo_scheda.setItems(FXCollections.observableArrayList(tipi_scheda));
		
		cb_tipo_scheda.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			
		});
    }

}
