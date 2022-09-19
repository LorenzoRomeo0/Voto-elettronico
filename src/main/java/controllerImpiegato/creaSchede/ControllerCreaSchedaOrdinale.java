package controllerImpiegato.creaSchede;

import java.time.LocalDate;
import java.util.ArrayList;

import dao.SistemaVotazioniDAO;
import data.Esito;
import data.Stato;
import data.TipoUtente;
import data.TipoVotabile;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.paint.Paint;
import system.SessionSystem;
import system.Valore;
import system.utenti.Utente;
import system.votabili.Votabile;

public class ControllerCreaSchedaOrdinale extends ControllerCreaScheda_o_c_p {

	@FXML
	@Override
	void initialize() {
		super.initialize();
		ArrayList<Valore> values = Valore.toArray(TipoVotabile.values());
		cb_tipo.setItems(FXCollections.observableArrayList(values));
	}

	@FXML
	@Override
	void inserisci_scheda(ActionEvent event) {
		SistemaVotazioniDAO dao = SistemaVotazioniDAO.getInstance();
		super.inserisci_scheda(event);

		ArrayList<Votabile> values = new ArrayList<Votabile>();
		values.addAll(tb_partecipanti.getItems());

		String titolo = txt_titolo.getText();
		LocalDate avvio = dpk_avvio.getValue();
		LocalDate termine = dpk_termine.getValue();
		Utente u = SessionSystem.getInstance().getUtente();
		Stato stato = (Stato) cb_stato.getValue().getObj();
		Esito esito = (Esito) cb_esito.getValue().getObj();
		TipoVotabile tipo = (TipoVotabile) cb_tipo.getValue().getObj();

		txt_error.setVisible(false);
		txt_error.setTextFill(Paint.valueOf("red"));
		if (titolo.isEmpty()) {
			txt_error.setText("Errore!!! Titolo non può essere vuoto.");
			txt_error.setVisible(true);
		} else if (!u.getTipo().equals(TipoUtente.IMPIEGATO)) {
			txt_error.setText("Errore!!! Non sei autorizzato.");
			txt_error.setVisible(true);
		} else if (values.size() <= 0) {
			txt_error.setText("Errore!!! Serve almeno un partecipante.");
			txt_error.setVisible(true);
		} else {
			dao.insertSchedaOrdinale(avvio, termine, u.getId(), stato, esito, tipo, titolo, values);
			txt_error.setTextFill(Paint.valueOf("black"));
			txt_error.setText("Scheda aggiunta con successo!!!");
			txt_error.setVisible(true);
		}
	}
}
