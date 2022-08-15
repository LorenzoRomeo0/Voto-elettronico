package controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import dao.ListaDTO;
import dao.UtenteDTO;
import dao.Votabile;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Paint;
import system.SessionUser;

public class Controller_crea_scheda_categorica_preferenza extends Controller_crea_scheda_o_c_p {

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

		ArrayList<ListaDTO> values = dao.get_liste_filtrati("");
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
			ListaDTO lista = (ListaDTO) votabile;
			if (tb_partecipanti.getItems().indexOf(lista) == -1) {
				tb_partecipanti.getItems().add(lista);
			}
		}
		tb_partecipanti.refresh();
	}

	@Override
	protected void load_values(String filtro) {
		ArrayList<ListaDTO> values = dao.get_liste_filtrati(filtro);
		if (null != values) {
			tb_trovati.setItems(FXCollections.observableArrayList(values));
		}
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
			dao.insert_scheda_categorica_preferenza(avvio, termine, u.getId(), stato, titolo, values);
	    	txt_error.setTextFill(Paint.valueOf("black"));
			txt_error.setText("Scheda aggiunta con successo!!!");
			txt_error.setVisible(true);
		}
	}
}
