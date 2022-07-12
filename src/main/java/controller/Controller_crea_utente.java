package controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import dao.ComuneDTO;
import dao.ProvinciaDTO;
import dao.RegioneDTO;
import dao.SistemaVotazioniDAO;
import dao.ValoreSempliceDTO;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import system.SessionSystem;

public class Controller_crea_utente {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<ComuneDTO> cb_comune;

    @FXML
    private ComboBox<ValoreSempliceDTO> cb_genere;

    @FXML
    private ComboBox<ValoreSempliceDTO> cb_nazionalita;

    @FXML
    private ComboBox<ProvinciaDTO> cb_provincia;

    @FXML
    private ComboBox<RegioneDTO> cb_regione;

    @FXML
    private ComboBox<ValoreSempliceDTO> cb_tipologia;

    @FXML
    private DatePicker dpk_nascita;

    @FXML
    private TextField txt_cognome;

    @FXML
    private TextField txt_nome;
    
    @FXML
    private VBox content;

    @FXML
    void aggiungi_utente(ActionEvent event) {

    }

    @FXML
    void filter_nazionalità(KeyEvent event) {
    	if
    }
    
    @FXML
    void initialize() {
        assert cb_comune != null : "fx:id=\"cb_comune\" was not injected: check your FXML file 'crea_utente.fxml'.";
        assert cb_genere != null : "fx:id=\"cb_genere\" was not injected: check your FXML file 'crea_utente.fxml'.";
        assert cb_nazionalita != null : "fx:id=\"cb_nazionalita\" was not injected: check your FXML file 'crea_utente.fxml'.";
        assert cb_provincia != null : "fx:id=\"cb_provincia\" was not injected: check your FXML file 'crea_utente.fxml'.";
        assert cb_regione != null : "fx:id=\"cb_regione\" was not injected: check your FXML file 'crea_utente.fxml'.";
        assert cb_tipologia != null : "fx:id=\"cb_tipologia\" was not injected: check your FXML file 'crea_utente.fxml'.";
        assert content != null : "fx:id=\"content\" was not injected: check your FXML file 'crea_utente.fxml'.";
        assert dpk_nascita != null : "fx:id=\"dpk_nascita\" was not injected: check your FXML file 'crea_utente.fxml'.";
        assert txt_cognome != null : "fx:id=\"txt_cognome\" was not injected: check your FXML file 'crea_utente.fxml'.";
        assert txt_nome != null : "fx:id=\"txt_nome\" was not injected: check your FXML file 'crea_utente.fxml'.";

		SessionSystem session = SessionSystem.getInstance();
		VBox container_content = session.getContent();
		container_content.widthProperty().addListener((observable, oldValue, newValue) -> {
			adatta_contenuto(newValue.doubleValue());
		});

		Stage stage = session.getStage();
		stage.resizableProperty().addListener((observable, oldValue, newValue) -> {
			adatta_contenuto(container_content.getWidth());
		});
        
        dpk_nascita.setDayCellFactory(param -> new DateCell() {
	        @Override
	        public void updateItem(LocalDate date, boolean empty) {
	            super.updateItem(date, empty);
	            setDisable(empty || date.compareTo(LocalDate.now().minusYears(18)) > 0 );
	        }
	    });
        
        SistemaVotazioniDAO dao = new SistemaVotazioniDAO();
		ArrayList<ValoreSempliceDTO> nazionalita = dao.get_nazionalita_all();
		cb_nazionalita.setItems(FXCollections.observableArrayList(nazionalita));
		
		ArrayList<ValoreSempliceDTO> tipi_utente = dao.get_tipi_utente_all();
		cb_tipologia.setItems(FXCollections.observableArrayList(tipi_utente));
		
		ArrayList<ValoreSempliceDTO> sessi = dao.get_sessi_all();
		cb_genere.setItems(FXCollections.observableArrayList(sessi));
		
		ArrayList<RegioneDTO> regioni = dao.get_regioni_all();
		cb_regione.setItems(FXCollections.observableArrayList(regioni));
    }
    
	private void adatta_contenuto(double larghezza) {
		content.setMinWidth(larghezza);
		content.setMaxWidth(larghezza);
	}

}
