package controllerImpiegato.creaSchede;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import dao.SistemaVotazioniDAO;
import data.Esito;
import data.Stato;
import data.TipoScheda;
import data.TipoUtente;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import system.SessionSystem;
import system.Valore;
import system.utenti.Utente;

public class ControllerCreaSchedaReferendum {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Valore> cb_esito;

    @FXML
    private ComboBox<Valore> cb_stato;

    @FXML
    private DatePicker dpk_avvio;

    @FXML
    private DatePicker dpk_termine;

    @FXML
    private Label txt_error;

    @FXML
    private TextField txt_referendum;

    @FXML
    private TextField txt_titolo;

    @FXML
    void aggiungi_scheda(ActionEvent event) {
    	String titolo = txt_titolo.getText();
		String referendum = txt_referendum.getText();
		LocalDate avvio = dpk_avvio.getValue();
		LocalDate termine = dpk_termine.getValue();
		SessionSystem ss = SessionSystem.getInstance();
		Utente u = ss.getUtente();
		
		txt_error.setVisible(false);
		txt_error.setTextFill(Paint.valueOf("red"));
		if (titolo.isEmpty()) {
			txt_error.setText("Errore!!! Titolo non può essere vuoto.");
			txt_error.setVisible(true);
		} else if (referendum.isEmpty()) {
			txt_error.setText("Errore!!! Referendum non puo essere vuoto.");
			txt_error.setVisible(true);
		} else if (!u.getTipo().equals(TipoUtente.IMPIEGATO)) {
			txt_error.setText("Errore!!! Non sei autorizzato.");
			txt_error.setVisible(true);
		} else if (null == cb_stato.getValue()){
			txt_error.setText("Errore!!! Inserire stato scheda.");
			txt_error.setVisible(true);
		} else if (null == cb_esito.getValue()){
			txt_error.setText("Errore!!! Inserire tipo di esito scheda.");
			txt_error.setVisible(true);
		} else {
			Stato stato = (Stato) cb_stato.getValue().getObj();
			Esito esito = (Esito) cb_esito.getValue().getObj();
			SistemaVotazioniDAO dao =  SistemaVotazioniDAO.getInstance();
			boolean result = dao.insertSchedaReferendum(avvio, termine, u.getId(), stato, esito, titolo, referendum);
			if (result) {
				txt_error.setTextFill(Paint.valueOf("black"));
				txt_error.setText("Scheda aggiunta con successo!!!");
				txt_error.setVisible(true);
			} else {
				txt_error.setTextFill(Paint.valueOf("red"));
				txt_error.setText("Errore con inserimento scheda!!! Prova a cambiarle il nome.");
				txt_error.setVisible(true);
			}
		}		
    }

    @FXML
    void initialize() {
        assert cb_esito != null : "fx:id=\"cb_esito\" was not injected: check your FXML file 'crea_scheda_referendum.fxml'.";
        assert cb_stato != null : "fx:id=\"cb_stato\" was not injected: check your FXML file 'crea_scheda_referendum.fxml'.";
        assert dpk_avvio != null : "fx:id=\"dpk_avvio\" was not injected: check your FXML file 'crea_scheda_referendum.fxml'.";
        assert dpk_termine != null : "fx:id=\"dpk_termine\" was not injected: check your FXML file 'crea_scheda_referendum.fxml'.";
        assert txt_error != null : "fx:id=\"txt_error\" was not injected: check your FXML file 'crea_scheda_referendum.fxml'.";
        assert txt_referendum != null : "fx:id=\"txt_referendum\" was not injected: check your FXML file 'crea_scheda_referendum.fxml'.";
        assert txt_titolo != null : "fx:id=\"txt_titolo\" was not injected: check your FXML file 'crea_scheda_referendum.fxml'.";
        
        ArrayList<Valore> stati_scheda = Valore.toArray(Stato.values());
        cb_stato.setItems(FXCollections.observableArrayList(stati_scheda));
        
        ArrayList<Valore> esiti_scheda = Valore.toArray(Esito.getValues(TipoScheda.REFERENDUM));
        cb_esito.setItems(FXCollections.observableArrayList(esiti_scheda));
        
        LocalDate domani = LocalDate.now().plusDays(1);

		dpk_avvio.setDayCellFactory(param -> new DateCell() {
			@Override
			public void updateItem(LocalDate date, boolean empty) {
				super.updateItem(date, empty);
				setDisable(empty || date.compareTo(domani) < 0);
			}
		});

		dpk_avvio.setValue(domani);
		dpk_termine.setValue(domani);
		
		dpk_avvio.valueProperty().addListener((observable, oldValue, newValue) -> {
			if (dpk_termine.getValue().isBefore(newValue)) {
				dpk_termine.setValue(newValue);
			}
			dpk_termine.setDayCellFactory(param -> new DateCell() {
				@Override
				public void updateItem(LocalDate date, boolean empty) {
					super.updateItem(date, empty);
					setDisable(empty || date.compareTo(newValue) < 0);
				}
			});
		});
    }

}
