package controllerImpiegato.creaSchede;

import java.time.LocalDate;
import java.util.ArrayList;
import dao.SistemaVotazioniDAO;
import data.Esito;
import data.Stato;
import data.TipoUtente;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Paint;
import system.SessionSystem;
import system.utenti.Utente;
import system.votabili.Lista;
import system.votabili.Votabile;

public class ControllerCreaSchedaCategoricaPreferenza extends ControllerCreaScheda_o_c_p {

	@FXML
	@Override
	void initialize() {
		super.initialize();

		tb_partecipanti.getColumns().clear();
		tb_partecipanti.getColumns().add(new TableColumn<>("Nome"));

		tb_trovati.getColumns().clear();
		tb_trovati.getColumns().add(new TableColumn<>("Nome"));

		@SuppressWarnings("unchecked")
		TableColumn<Votabile, String> col1 = (TableColumn<Votabile, String>) tb_trovati.getColumns().get(0);
		@SuppressWarnings("unchecked")
		TableColumn<Votabile, String> col2 = (TableColumn<Votabile, String>) tb_partecipanti.getColumns().get(0);
		col1.setCellValueFactory(new PropertyValueFactory<Votabile, String>("nome"));
		col2.setCellValueFactory(new PropertyValueFactory<Votabile, String>("nome"));

		SessionSystem ss = SessionSystem.getInstance();
		ArrayList<Lista> values = ss.getListe();
		if (null != values) {
			tb_trovati.setItems(FXCollections.observableArrayList(values));
		}

		vb_tipo.setVisible(false);
		abilita();
	}

	@FXML
	void aggiungi(ActionEvent event) {
		Votabile votabile = tb_trovati.getSelectionModel().getSelectedItem();
		if (null != votabile) {
			Lista lista = (Lista) votabile;
			if (tb_partecipanti.getItems().indexOf(lista) == -1) {
				tb_partecipanti.getItems().add(lista);
			}
		}
		tb_partecipanti.refresh();
	}

	@Override
	protected void load_values(String filtro) {
		SessionSystem ss = SessionSystem.getInstance();
		ArrayList<Lista> values = ss.getListe(filtro);
		if (null != values) {
			tb_trovati.setItems(FXCollections.observableArrayList(values));
		}
	}

	@FXML
	@Override
	void inserisci_scheda(ActionEvent event) {
		super.inserisci_scheda(event);
		ArrayList<Votabile> values = new ArrayList<Votabile>();
		values.addAll(tb_partecipanti.getItems());

		String titolo = txt_titolo.getText();
		LocalDate avvio = dpk_avvio.getValue();
		LocalDate termine = dpk_termine.getValue();
		Utente u = SessionSystem.getInstance().getUtente();
		Stato stato = (Stato) cb_stato.getValue().getObj();
		Esito esito = (Esito) cb_esito.getValue().getObj();

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
			SistemaVotazioniDAO dao = SistemaVotazioniDAO.getInstance();
			dao.insertSchedaCategoricaPreferenza(avvio, termine, u.getId(), stato, esito, titolo, values);
			txt_error.setTextFill(Paint.valueOf("black"));
			txt_error.setText("Scheda aggiunta con successo!!!");
			txt_error.setVisible(true);
		}
	}
}
