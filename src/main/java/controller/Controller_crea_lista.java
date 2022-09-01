package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import dao.CandidatoDTO;
import dao.PartitoDTO;
import dao.SistemaVotazioniDAO;
import dao.ValoreSempliceDTO;
import dao.Votabile;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class Controller_crea_lista {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btn_aggiungi;

    @FXML
    private Button btn_cerca;

    @FXML
    private Button btn_insert;

    @FXML
    private Button btn_rimuovi;

    @FXML
    private ComboBox<ValoreSempliceDTO> cb_tipo;

    @FXML
    private TableView<Votabile> tb_partecipanti;

    @FXML
    private TableView<Votabile> tb_trovati;

    @FXML
    private Label txt_error;

    @FXML
    private TextField txt_filtro;

    @FXML
    private TextField txt_titolo;

    @FXML
    private VBox vb_tipo;

    @FXML
	void aggiungi(ActionEvent event) {
		Votabile votabile = tb_trovati.getSelectionModel().getSelectedItem();
		if (null != votabile) {
			String tipo = cb_tipo.getValue().getNome();
			if (tipo.equals("partito")) {
				PartitoDTO partito = (PartitoDTO) votabile;
				if (tb_partecipanti.getItems().indexOf(partito) == -1) {
					tb_partecipanti.getItems().add(partito);
				}
			} else if (tipo.equals("candidato")) {
				CandidatoDTO candidato = (CandidatoDTO) votabile;
				if (tb_partecipanti.getItems().indexOf(candidato) == -1) {
					tb_partecipanti.getItems().add(candidato);
				}
			}
		}
		tb_partecipanti.refresh();
	}

	@FXML
	void cerca(ActionEvent event) {
		load_values(txt_filtro.getText());
		tb_trovati.refresh();
	}
	
	@FXML
	void rimuovi(ActionEvent event) {
		Votabile votabile = tb_partecipanti.getSelectionModel().getSelectedItem();
		tb_partecipanti.getItems().remove(votabile);
		tb_partecipanti.refresh();
	}
	
	@FXML
	void selezione_tipo(ActionEvent event) {
		svuota();
		aggiungi_colonne(tb_partecipanti);
		aggiungi_colonne(tb_trovati);
		abilita();
		carica_colonne();
		load_values(txt_filtro.getText());
		tb_trovati.refresh();
	}
	
    protected SistemaVotazioniDAO dao;

    @FXML
    void initialize() {
        assert btn_aggiungi != null : "fx:id=\"btn_aggiungi\" was not injected: check your FXML file 'crea_lista.fxml'.";
        assert btn_cerca != null : "fx:id=\"btn_cerca\" was not injected: check your FXML file 'crea_lista.fxml'.";
        assert btn_insert != null : "fx:id=\"btn_insert\" was not injected: check your FXML file 'crea_lista.fxml'.";
        assert btn_rimuovi != null : "fx:id=\"btn_rimuovi\" was not injected: check your FXML file 'crea_lista.fxml'.";
        assert cb_tipo != null : "fx:id=\"cb_tipo\" was not injected: check your FXML file 'crea_lista.fxml'.";
        assert tb_partecipanti != null : "fx:id=\"tb_partecipanti\" was not injected: check your FXML file 'crea_lista.fxml'.";
        assert tb_trovati != null : "fx:id=\"tb_trovati\" was not injected: check your FXML file 'crea_lista.fxml'.";
        assert txt_error != null : "fx:id=\"txt_error\" was not injected: check your FXML file 'crea_lista.fxml'.";
        assert txt_filtro != null : "fx:id=\"txt_filtro\" was not injected: check your FXML file 'crea_lista.fxml'.";
        assert txt_titolo != null : "fx:id=\"txt_titolo\" was not injected: check your FXML file 'crea_lista.fxml'.";
        assert vb_tipo != null : "fx:id=\"vb_tipo\" was not injected: check your FXML file 'crea_lista.fxml'.";

        dao = new SistemaVotazioniDAO();
    }
    
    private void carica_colonne () {
		String tipo = cb_tipo.getValue().getNome();
		if (tipo.equals("candidato") || tipo.equals("partito") || tipo.equals("lista")) {
			@SuppressWarnings("unchecked")
			TableColumn<Votabile, String> col1 = (TableColumn<Votabile, String>) tb_trovati.getColumns().get(0);
			@SuppressWarnings("unchecked")
			TableColumn<Votabile, String> col2 = (TableColumn<Votabile, String>) tb_partecipanti.getColumns().get(0);
			col1.setCellValueFactory(new PropertyValueFactory<Votabile, String>("nome"));
			col2.setCellValueFactory(new PropertyValueFactory<Votabile, String>("nome"));
		}
		if (tipo.equals("candidato")) {
			@SuppressWarnings("unchecked")
			TableColumn<Votabile, String> col1 = (TableColumn<Votabile, String>) tb_trovati.getColumns().get(1);
			@SuppressWarnings("unchecked")
			TableColumn<Votabile, String> col2 = (TableColumn<Votabile, String>) tb_partecipanti.getColumns().get(1);
			col1.setCellValueFactory(new PropertyValueFactory<Votabile, String>("cognome"));
			col2.setCellValueFactory(new PropertyValueFactory<Votabile, String>("cognome"));
		}
	}

	private void aggiungi_colonne(TableView<Votabile> tabella) {
		String tipo = cb_tipo.getValue().getNome();
		tabella.getColumns().clear();
		if (tipo.equals("candidato") || tipo.equals("partito") || tipo.equals("lista")) {
			tabella.getColumns().add(new TableColumn<>("Nome"));
		}
		if (tipo.equals("candidato")) {
			tabella.getColumns().add(new TableColumn<>("Cognome"));
		}
	}
	
	private void svuota () {
		txt_error.setVisible(false);
		tb_partecipanti.getItems().clear();
		tb_partecipanti.refresh();
		tb_trovati.getItems().clear();
		tb_trovati.refresh();
		txt_filtro.clear();
	}
	
	private  void abilita () {
		btn_cerca.setDisable(false);
		btn_aggiungi.setDisable(false);
		btn_rimuovi.setDisable(false);
		btn_insert.setDisable(false);
		txt_filtro.setDisable(false);
	}
	
	private void load_values(String filtro) {
		String tipo = cb_tipo.getValue().getNome();
		if (tipo.equals("partito")) {
			ArrayList<PartitoDTO> values = dao.get_partiti_filtrati(filtro);
			if (null != values) {
				tb_trovati.setItems(FXCollections.observableArrayList(values));
			}
		} else if (tipo.equals("candidato")) {
			ArrayList<CandidatoDTO> values = dao.get_candidati_filtrati(filtro);
			if (null != values) {
				tb_trovati.setItems(FXCollections.observableArrayList(values));
			}
		}
	}

}
