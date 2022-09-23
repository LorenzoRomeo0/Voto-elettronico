package controllerImpiegato;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import data.Stato;
import data.TipoScheda;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import system.SessionSystem;
import system.Valore;
import system.schede.Scheda;
import system.utenti.Impiegato;

public class Controller_modifica {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Valore> combo_schede;

    @FXML
    private VBox content;

    @FXML
    private Text txt_error;

    @FXML
    private Text txt_success;

    @FXML
    void btn_elimina(ActionEvent event) {
    	Scheda sc = (Scheda)combo_schede.getSelectionModel().getSelectedItem().getObj();
    	if(sc == null) {
    		txt_error.setText("Selezionare una scheda");
    	}else {
    		txt_success.setText(sc.getNome());
        	SessionSystem ss = SessionSystem.getInstance();
        	Impiegato im = (Impiegato) ss.getUtente();
        	boolean status = im.eliminaScheda(sc);
        	if(status) { 
        		txt_success.setText("Scheda Eliminata.");
        		combo_schede.getItems().remove(combo_schede.getSelectionModel().getSelectedItem());
        	}
        	else txt_error.setText("Operazione non riuscita.");
    	}
    }

    @FXML
    void btn_termina(ActionEvent event) {
    	Scheda sc = (Scheda)combo_schede.getSelectionModel().getSelectedItem().getObj();
    	if(sc == null) {
    		txt_error.setText("Selezionare una scheda");
    	}else {
    		txt_success.setText(sc.getNome());
        	SessionSystem ss = SessionSystem.getInstance();
        	Impiegato im = (Impiegato) ss.getUtente();
        	boolean status = im.modificaScheda(sc, Stato.valueOf("CONCLUSO"));
        	if(status) txt_success.setText("Operazione completata.");
        	else txt_error.setText("Operazione non riuscita.");
    	}
    }

    @FXML
    void selected(ActionEvent event) {
    	
    }
    
    @FXML
    void btn_visualizza_stato(ActionEvent event) {
    	Scheda sc = (Scheda)combo_schede.getSelectionModel().getSelectedItem().getObj();
    	if(sc == null) {
    		txt_error.setText("Selezionare una scheda");
    	}else {
    		txt_success.setText(sc.getNome());
        	SessionSystem ss = SessionSystem.getInstance();
        	Impiegato im = (Impiegato) ss.getUtente();
        	String status = im.getStatoScheda(sc);
        	txt_success.setText("Scheda "+sc.getNome()+" - Stato: "+status);
        }
    }

    @FXML
    void initialize() {
        assert combo_schede != null : "fx:id=\"combo_schede\" was not injected: check your FXML file 'modifica_sessione.fxml'.";
        assert content != null : "fx:id=\"content\" was not injected: check your FXML file 'modifica_sessione.fxml'.";
        assert txt_error != null : "fx:id=\"txt_error\" was not injected: check your FXML file 'modifica_sessione.fxml'.";
        assert txt_success != null : "fx:id=\"txt_success\" was not injected: check your FXML file 'modifica_sessione.fxml'.";
        
        SessionSystem session = SessionSystem.getInstance();
		VBox container_content = session.getContent();
		container_content.widthProperty().addListener((observable, oldValue, newValue) -> {
			adatta_contenuto(newValue.doubleValue());
		});

		Stage stage = session.getStage();
		stage.resizableProperty().addListener((observable, oldValue, newValue) -> {
			adatta_contenuto(container_content.getWidth());
		});
        
        ArrayList <Valore> valori =  new ArrayList<Valore>();
        SessionSystem ss = SessionSystem.getInstance();
        ArrayList<Scheda> schede = ss.getSchede();
		for (Scheda s : schede) {
			valori.add(new Valore(s.toString().replace("_", " "), s));
		}
		combo_schede.setItems(FXCollections.observableArrayList(valori));
    }
    
    private void adatta_contenuto(double larghezza) {
		content.setMinWidth(larghezza);
		content.setMaxWidth(larghezza);
	}

}
