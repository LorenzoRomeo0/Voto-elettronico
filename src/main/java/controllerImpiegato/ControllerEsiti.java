package controllerImpiegato;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import system.SessionSystem;
import system.schede.Scheda;
import system.utenti.Impiegato;

public class ControllerEsiti {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private VBox content;

    @FXML
    private ListView<Scheda> lv_schede;

    @FXML
    private Text txt_error;
    
    private ArrayList<Scheda> values;

    @FXML
    void calcola_esito(ActionEvent event) {
    	Impiegato imp = (Impiegato) SessionSystem.getInstance().getUtente();
    	Scheda sc = lv_schede.getSelectionModel().getSelectedItem();
    	String res = imp.calcolaRisutato(sc);
    	txt_error.setText(res);
    }

    @FXML
    void ricarica(ActionEvent event) {
    	System.out.println("ricarica!");
    	SessionSystem ss = SessionSystem.getInstance();
		ArrayList<Scheda> values = ss.getSchedeConcluse();
		if (null != values) {
			lv_schede.setItems(FXCollections.observableArrayList(values));
		}
    }

    @FXML
    void initialize() {
        assert content != null : "fx:id=\"content\" was not injected: check your FXML file 'esito_scheda.fxml'.";
        assert lv_schede != null : "fx:id=\"lv_schede\" was not injected: check your FXML file 'esito_scheda.fxml'.";
        assert txt_error != null : "fx:id=\"txt_error\" was not injected: check your FXML file 'esito_scheda.fxml'.";
        
        SessionSystem ss = SessionSystem.getInstance();
		VBox container_content = ss.getContent();
		container_content.widthProperty().addListener((observable, oldValue, newValue) -> {
			adatta_contenuto(newValue.doubleValue());
		});

		Stage stage = ss.getStage();
		stage.resizableProperty().addListener((observable, oldValue, newValue) -> {
			adatta_contenuto(container_content.getWidth());
		});
		
		values = ss.getSchedeConcluse();
		if (null != values) {
			lv_schede.setItems(FXCollections.observableArrayList(values));
		}
    }
    
	private void adatta_contenuto(double larghezza) {
		content.setMinWidth(larghezza);
		content.setMaxWidth(larghezza);
	}

}