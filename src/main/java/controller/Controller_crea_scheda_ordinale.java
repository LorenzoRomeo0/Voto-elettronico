package controller;

import java.time.LocalDate;
import java.util.Collection;

import dao.UtenteDTO;
import dao.Votabile;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.paint.Paint;
import system.SessionUser;

public class Controller_crea_scheda_ordinale extends Controller_crea_scheda_o_c_p {
	
	@FXML
	@Override
	void initialize() {
		super.initialize();
		cb_tipo.setItems(FXCollections.observableArrayList(dao.get_tipi_votabili_all()));
	}
	
	@FXML
	@Override
	void inserisci_scheda(ActionEvent event) {
		super.inserisci_scheda(event);
		Collection<Votabile> values = tb_partecipanti.getItems();
		
		String titolo = txt_titolo.getText();
    	LocalDate avvio = dpk_avvio.getValue();
    	LocalDate termine = dpk_termine.getValue();
    	int stato = cb_stato.getValue().getId();
    	UtenteDTO u =  SessionUser.getInstance().getUtente();
    	
    	txt_error.setVisible(false);
		txt_error.setTextFill(Paint.valueOf("red"));
    	if (titolo.isEmpty()) {
    		txt_error.setText("Errore!!! Titolo non può essere vuoto.");
    		txt_error.setVisible(true);
    	}else if (!dao.controllo_nome_scheda(titolo)) {
    		txt_error.setText("Errore!!! Titolo non disponibile.");
    		txt_error.setVisible(true);
    	}else if (!u.getTipoUtente().equals("admin")) {
    		txt_error.setText("Errore!!! Non sei autorizzato.");
    		txt_error.setVisible(true);
    	} else if(values.size() <= 0) {
			txt_error.setText("Errore!!! Serve almeno un partecipante.");
			txt_error.setVisible(true);
		} else {
			dao.insert_scheda_ordinale(avvio, termine, u.getId(), stato, titolo, values);
	    	txt_error.setTextFill(Paint.valueOf("black"));
			txt_error.setText("Scheda aggiunta con sucesso!!!");
			txt_error.setVisible(true);
		}
	}
}
