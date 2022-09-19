package controllerElettore;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import data.TipoScheda;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import system.SessionSystem;
import system.schede.Scheda;

public class ControllerSchedeVotabili {

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
	void vota(ActionEvent event) {
		txt_error.setVisible(false);
		Scheda value = lv_schede.getSelectionModel().getSelectedItem();
		if (null != value) {
			SessionSystem.getInstance().setScheda(value);
			String carica = null;
			if (value.getTipoScheda().equals(TipoScheda.REFERENDUM)) {
				carica = "/utente/vota_scheda_referendum.fxml";
			} else if (value.getTipoScheda().equals(TipoScheda.CATEGORICA)) {
				carica = "/utente/vota_scheda_categorica.fxml";
			}else if (value.getTipoScheda().equals(TipoScheda.CATEGORICA_CON_PREFERENZE)) {
				carica = "/utente/vota_scheda_categorica_preferenza.fxml";
			}  else if (value.getTipoScheda().equals(TipoScheda.ORDINALE)) {
				carica = "/utente/vota_scheda_ordinale.fxml";
			}
			if (null != carica) {
				try {
					SessionSystem.getInstance().loadStage(carica, "Vota");
				} catch (IOException e) {
					System.out.println("Caricamento vota fallito.");
					e.printStackTrace();
				}
			}
		}
	}

	@FXML
	void ricarica(ActionEvent event) {
		SessionSystem ss = SessionSystem.getInstance();
		ArrayList<Scheda> values = ss.getSchedeNonCompilate();
		if (null != values) {
			lv_schede.setItems(FXCollections.observableArrayList(values));
		}
	}
	
	@FXML
	void initialize() {
		assert content != null : "fx:id=\"content\" was not injected: check your FXML file 'schede_votabili.fxml'.";
		assert lv_schede != null : "fx:id=\"lv_schede\" was not injected: check your FXML file 'schede_votabili.fxml'.";
		assert txt_error != null : "fx:id=\"txt_error\" was not injected: check your FXML file 'schede_votabili.fxml'.";
		SessionSystem ss = SessionSystem.getInstance();
		VBox container_content = ss.getContent();
		container_content.widthProperty().addListener((observable, oldValue, newValue) -> {
			adatta_contenuto(newValue.doubleValue());
		});

		Stage stage = ss.getStage();
		stage.resizableProperty().addListener((observable, oldValue, newValue) -> {
			adatta_contenuto(container_content.getWidth());
		});

		values = ss.getSchedeNonCompilate();
		if (null != values) {
			lv_schede.setItems(FXCollections.observableArrayList(values));
		}
	}

	private void adatta_contenuto(double larghezza) {
		content.setMinWidth(larghezza);
		content.setMaxWidth(larghezza);
	}

}
