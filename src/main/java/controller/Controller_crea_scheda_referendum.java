package controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import dao.SistemaVotazioniDAO;
import dao.UtenteDTO;
import dao.ValoreSempliceDTO;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import system.SessionUser;

public class Controller_crea_scheda_referendum {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private ComboBox<ValoreSempliceDTO> cb_stato;

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

	private SistemaVotazioniDAO dao;

	@FXML
	void aggiungi_scheda(ActionEvent event) {
		String titolo = txt_titolo.getText();
		String referendum = txt_referendum.getText();
		LocalDate avvio = dpk_avvio.getValue();
		LocalDate termine = dpk_termine.getValue();
		int stato = cb_stato.getValue().getId();
		UtenteDTO u = SessionUser.getInstance().getUtente();
		
		txt_error.setVisible(false);
		txt_error.setTextFill(Paint.valueOf("red"));
		if (titolo.isEmpty()) {
			txt_error.setText("Errore!!! Titolo non può essere vuoto.");
			txt_error.setVisible(true);
		} else if (referendum.isEmpty()) {
			txt_error.setText("Errore!!! Referendum non puo essere vuoto.");
			txt_error.setVisible(true);
		} else if (!dao.controllo_nome_scheda(titolo)) {
			txt_error.setText("Errore!!! Titolo non disponibile.");
			txt_error.setVisible(true);
		} else if (!u.getTipoUtente().equals("admin")) {
			txt_error.setText("Errore!!! Non sei autorizzato.");
			txt_error.setVisible(true);
		} else {
			dao.insert_scheda_referendum(avvio, termine, u.getId(), stato, titolo, referendum);
			txt_error.setTextFill(Paint.valueOf("black"));
			txt_error.setText("Scheda aggiunta con sucesso!!!");
			txt_error.setVisible(true);
		}
	}

	@FXML
	void initialize() {
		assert cb_stato != null
				: "fx:id=\"cb_stato\" was not injected: check your FXML file 'crea_scheda_referendum.fxml'.";
		assert dpk_avvio != null
				: "fx:id=\"dpk_avvio\" was not injected: check your FXML file 'crea_scheda_referendum.fxml'.";
		assert dpk_termine != null
				: "fx:id=\"dpk_termine\" was not injected: check your FXML file 'crea_scheda_referendum.fxml'.";
		assert txt_error != null
				: "fx:id=\"txt_error\" was not injected: check your FXML file 'crea_scheda_referendum.fxml'.";
		assert txt_referendum != null
				: "fx:id=\"txt_referendum\" was not injected: check your FXML file 'crea_scheda_referendum.fxml'.";
		assert txt_titolo != null
				: "fx:id=\"txt_titolo\" was not injected: check your FXML file 'crea_scheda_referendum.fxml'.";

		dao = new SistemaVotazioniDAO();
		ArrayList<ValoreSempliceDTO> stati_scheda = dao.get_schede_stati_all();
		cb_stato.setItems(FXCollections.observableArrayList(stati_scheda));
		cb_stato.setValue(stati_scheda.get(0));

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
