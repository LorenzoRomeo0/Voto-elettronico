package controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import dao.CandidatoDTO;
import dao.ListaDTO;
import dao.PartitoDTO;
import dao.SistemaVotazioniDAO;
import dao.ValoreSempliceDTO;
import dao.Votabile;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class Controller_crea_scheda_o_c_p {

    @FXML
    protected ResourceBundle resources;

    @FXML
    protected URL location;

    @FXML
    protected Button btn_aggiungi;

    @FXML
    protected Button btn_cerca;

    @FXML
    protected Button btn_insert;

    @FXML
    protected Button btn_rimuovi;

    @FXML
    protected ComboBox<ValoreSempliceDTO> cb_stato;

	@FXML
	protected ComboBox<ValoreSempliceDTO> cb_tipo;

    @FXML
    protected DatePicker dpk_avvio;

    @FXML
    protected DatePicker dpk_termine;

	@FXML
	protected TableView<Votabile> tb_partecipanti;

	@FXML
	protected TableView<Votabile> tb_trovati;

    @FXML
    protected Label txt_error;

    @FXML
    protected TextField txt_filtro;

    @FXML
    protected TextField txt_titolo;
    
    @FXML
    protected VBox vb_tipo;

    @FXML
	void inserisci_scheda(ActionEvent event) {}

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

	protected void load_values(String filtro) {
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
	
	@FXML
	void rimuovi(ActionEvent event) {
		Votabile votabile = tb_partecipanti.getSelectionModel().getSelectedItem();
		tb_partecipanti.getItems().remove(votabile);
		tb_partecipanti.refresh();
	}

	private void svuota () {
		txt_error.setVisible(false);
		tb_partecipanti.getItems().clear();
		tb_partecipanti.refresh();
		tb_trovati.getItems().clear();
		tb_trovati.refresh();
		txt_filtro.clear();
	}
	
	protected  void abilita () {
		btn_cerca.setDisable(false);
		btn_aggiungi.setDisable(false);
		btn_rimuovi.setDisable(false);
		btn_insert.setDisable(false);
		txt_filtro.setDisable(false);
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

    protected SistemaVotazioniDAO dao;
    
    @FXML
    void initialize() {
        assert btn_aggiungi != null : "fx:id=\"btn_aggiungi\" was not injected: check your FXML file 'crea_scheda_o_c_p.fxml'.";
        assert btn_cerca != null : "fx:id=\"btn_cerca\" was not injected: check your FXML file 'crea_scheda_o_c_p.fxml'.";
        assert btn_insert != null : "fx:id=\"btn_insert\" was not injected: check your FXML file 'crea_scheda_o_c_p.fxml'.";
        assert btn_rimuovi != null : "fx:id=\"btn_rimuovi\" was not injected: check your FXML file 'crea_scheda_o_c_p.fxml'.";
        assert cb_stato != null : "fx:id=\"cb_stato\" was not injected: check your FXML file 'crea_scheda_o_c_p.fxml'.";
        assert cb_tipo != null : "fx:id=\"cb_tipo\" was not injected: check your FXML file 'crea_scheda_o_c_p.fxml'.";
        assert dpk_avvio != null : "fx:id=\"dpk_avvio\" was not injected: check your FXML file 'crea_scheda_o_c_p.fxml'.";
        assert dpk_termine != null : "fx:id=\"dpk_termine\" was not injected: check your FXML file 'crea_scheda_o_c_p.fxml'.";
        assert tb_partecipanti != null : "fx:id=\"tb_partecipanti\" was not injected: check your FXML file 'crea_scheda_o_c_p.fxml'.";
        assert tb_trovati != null : "fx:id=\"tb_trovati\" was not injected: check your FXML file 'crea_scheda_o_c_p.fxml'.";
        assert txt_error != null : "fx:id=\"txt_error\" was not injected: check your FXML file 'crea_scheda_o_c_p.fxml'.";
        assert txt_filtro != null : "fx:id=\"txt_filtro\" was not injected: check your FXML file 'crea_scheda_o_c_p.fxml'.";
        assert txt_titolo != null : "fx:id=\"txt_titolo\" was not injected: check your FXML file 'crea_scheda_o_c_p.fxml'.";
        assert vb_tipo != null : "fx:id=\"vb_tipo\" was not injected: check your FXML file 'crea_scheda_o_c_p.fxml'.";
        
        dao = new SistemaVotazioniDAO();
        
        ArrayList<ValoreSempliceDTO> stati_scheda = dao.get_schede_stati_all();
		cb_stato.setItems(FXCollections.observableArrayList(stati_scheda));
		cb_stato.setValue(stati_scheda.get(0));
		
		LocalDate domani = LocalDate.now().plusDays(1);
		
		dpk_avvio.setDayCellFactory(param -> new DateCell() {
	        @Override
	        public void updateItem(LocalDate date, boolean empty) {
	            super.updateItem(date, empty);
	            setDisable(empty || date.compareTo(domani) < 0 );
	        }
	    });
		
		dpk_avvio.setValue(domani);
		dpk_termine.setValue(domani);
		
		dpk_avvio.valueProperty().addListener((observable, oldValue, newValue) -> {
			if(dpk_termine.getValue().isBefore(newValue)) {
				dpk_termine.setValue(newValue);
			}
			dpk_termine.setDayCellFactory(param -> new DateCell() {
		        @Override
		        public void updateItem(LocalDate date, boolean empty) {
		            super.updateItem(date, empty);
		            setDisable(empty || date.compareTo(newValue) < 0 );
		        }
		    });
		});
        
    }

}
