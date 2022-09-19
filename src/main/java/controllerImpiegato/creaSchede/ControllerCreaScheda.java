package controllerImpiegato.creaSchede;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import data.TipoScheda;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import system.SessionSystem;
import system.Valore;

public class ControllerCreaScheda {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private ComboBox<Valore> cb_tipo_scheda;

	@FXML
	private VBox content;

	@FXML
	private VBox crea_scheda_content;

	@FXML
	void selected(ActionEvent event) {
		TipoScheda value = (TipoScheda) cb_tipo_scheda.getValue().getObj();
		try {
			String controller_path = null;
			if (value.equals(TipoScheda.ORDINALE) || value.equals(TipoScheda.CATEGORICA)
					|| value.equals(TipoScheda.CATEGORICA_CON_PREFERENZE)) {
				controller_path = "o_c_p";
			}else if (value.equals(TipoScheda.REFERENDUM)) {
				controller_path = "referendum";
			} else
				throw new Exception();
			String path = "/admin/crea_scheda_" + controller_path + ".fxml";
			FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
			if (!value.equals(TipoScheda.REFERENDUM)) {
				if (value.equals(TipoScheda.ORDINALE)) {
					loader.setController(new ControllerCreaSchedaOrdinale());
				} else if (value.equals(TipoScheda.CATEGORICA)) {
					loader.setController(new ControllerCreaSchedaCategorica());
				} else if (value.equals(TipoScheda.CATEGORICA_CON_PREFERENZE)) {
					loader.setController(new ControllerCreaSchedaCategoricaPreferenza());
				}
			}
			Parent parent = loader.load();
			crea_scheda_content.getChildren().clear();
			crea_scheda_content.getChildren().add(parent);
		} catch (Exception e) {
			System.out.println("---! caricamento crea scheda '" + value + "' fallito.");
			e.printStackTrace();
		}
	}

	@FXML
	void initialize() {
		assert cb_tipo_scheda != null : "fx:id=\"cb_tipo_scheda\" was not injected: check your FXML file 'crea_scheda.fxml'.";
		assert content != null : "fx:id=\"content\" was not injected: check your FXML file 'crea_scheda.fxml'.";
		assert crea_scheda_content != null : "fx:id=\"crea_scheda_content\" was not injected: check your FXML file 'crea_scheda.fxml'.";

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
		for (TipoScheda v : TipoScheda.values()) {
			valori.add(new Valore(v.toString().replace("_", " "), v));
		}
		cb_tipo_scheda.setItems(FXCollections.observableArrayList(valori));
	}

	private void adatta_contenuto(double larghezza) {
		content.setMinWidth(larghezza);
		content.setMaxWidth(larghezza);
		crea_scheda_content.setMinWidth(larghezza);
		crea_scheda_content.setMaxWidth(larghezza);
	}

}
