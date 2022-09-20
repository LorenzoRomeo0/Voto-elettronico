package controllerElettore.vota;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import system.SessionSystem;
import system.checkboxManagers.CheckBoxUpdater;
import system.schede.SchedaCategorica;
import system.utenti.Elettore;
import system.votabili.Votabile;
import system.voto.VotoCategorico;

public class ControllerVotaSchedaCategorica {
	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private ColumnConstraints col1;

	@FXML
	private ColumnConstraints col2;

	@FXML
	private AnchorPane contenitore_scheda;

	@FXML
	private GridPane dati_scheda;

	@FXML
	private VBox schermata;

	private boolean col2_visible;
	private CheckBoxUpdater options;
	private final double col_max_width = 500.0;
	private SessionSystem session_system;
	private SchedaCategorica scheda;
	private ArrayList<Votabile> aspiranti;
	private Elettore elettore;

	@FXML
	void conferma_voto(ActionEvent event) {
		if (options.getSelected() != -1) {
			Votabile selected = aspiranti.get(options.getSelected());
			session_system.setMessage("Hai scelto \'"+ selected +"\' come voto. Sei sicuro?");
			try {
				creaPopup();
			} catch (Exception e) {
				System.out.println("---! caricamento popup categorico fallito.");
				e.printStackTrace();
			}
			boolean inserisci = (boolean) session_system.getMessage();
			if (inserisci) {
				VotoCategorico voto = new VotoCategorico(scheda, selected);
				elettore.vota(voto);
				SessionSystem.getInstance().loadMain();
			}
		} else {
			voto_nullo(event);
		}
	}

	@FXML
	void voto_nullo(ActionEvent event) {
		session_system.setMessage("Hai scelto \'NULLO\' come voto. Sei sicuro?");
		try {
			creaPopup();
		} catch (Exception e) {
			System.out.println("---! caricamento popup categorico fallito.");
			e.printStackTrace();
		}
		boolean inserisci = (boolean) session_system.getMessage();
		if (inserisci) {
			VotoCategorico voto = new VotoCategorico(scheda, null);
			elettore.vota(voto);
			SessionSystem.getInstance().loadMain();
		}
	}

	@FXML
	void initialize() {
		assert col1 != null : "fx:id=\"col1\" was not injected: check your FXML file 'vota_scheda_categorica.fxml'.";
		assert col2 != null : "fx:id=\"col2\" was not injected: check your FXML file 'vota_scheda_categorica.fxml'.";
		assert contenitore_scheda != null : "fx:id=\"contenitore_scheda\" was not injected: check your FXML file 'vota_scheda_categorica.fxml'.";
		assert dati_scheda != null : "fx:id=\"dati_scheda\" was not injected: check your FXML file 'vota_scheda_categorica.fxml'.";
		assert schermata != null : "fx:id=\"schermata\" was not injected: check your FXML file 'vota_scheda_categorica.fxml'.";
		
		session_system = SessionSystem.getInstance();

		schermata.widthProperty().addListener((observable, oldValue, newValue) -> {
			double value = newValue.doubleValue();
			double maxValue = contenitore_scheda.getMaxWidth();
			if (value > maxValue) {
				value = maxValue;
			}
			contenitore_scheda.setMinWidth(value);
		});

		schermata.heightProperty().addListener((observable, oldValue, newValue) -> {
			double value = newValue.doubleValue();
			double maxValue = contenitore_scheda.getMaxHeight();
			if (value > maxValue) {
				value = maxValue;
			}
			contenitore_scheda.setMinHeight(value);
		});

		col2_visible = true;

		contenitore_scheda.widthProperty().addListener((observable, oldValue, newValue) -> {
			double value = newValue.doubleValue() - 2;
			setWidth(dati_scheda, value);
			if (value > col_max_width) {
				double half_value = value / 2;
				setWidth(col1, half_value);
				setWidth(col2, half_value);
				if (!col2_visible) {
					caricaGridpane();
				}
				col2_visible = true;
			} else {
				setWidth(col1, value);
				setWidth(col2, 0);
				if (col2_visible) {
					caricaGridpane();
				}
				col2_visible = false;
			}
		});
		
		session_system = SessionSystem.getInstance();
		scheda = (SchedaCategorica) session_system.getScheda();
		aspiranti = scheda.getVotabile();
		elettore = (Elettore) session_system.getUtente();

		options = new CheckBoxUpdater();
		caricaGridpane();
	}

	private void caricaGridpane() {
		dati_scheda.getChildren().clear();
		options.clear();
		initGridpane();
		datiGridpane();
		options.reselectOld();
	}

	private void initGridpane() {
		int rows = aspiranti.size();
		if (col2_visible) {
			rows = Math.round(rows / 2);
		}
		for (int i = 0; i < rows; i++) {
			dati_scheda.addRow(i, new Label());
		}
	}

	private void datiGridpane() {
		int row = 0;
		for (int i = 0; i < aspiranti.size(); i++) {
			try {
				Votabile a = aspiranti.get(i);
				session_system.setMessage(a.toString(), i, options);
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/visualizza_elemento_lista.fxml"));
				int column = 0;
				if (!col2_visible && i % 2 != 0) {
					column = 1;
					row--;
				}
				dati_scheda.add(loader.load(), column, row);
			} catch (Exception e) {
				System.out.println("---! caricamento gridpane fallito.");
				e.printStackTrace();
			}
			row++;
		}
	}

	private void setWidth(GridPane pane, double value) {
		pane.setMinWidth(value);
		pane.setMaxWidth(value);
	}

	private void setWidth(ColumnConstraints pane, double value) {
		pane.setMinWidth(value);
		pane.setMaxWidth(value);
	}

	private void creaPopup() throws IOException {
		Stage popup = new Stage();
		session_system.setPopup(popup);
		FXMLLoader loader_popup = new FXMLLoader(getClass().getResource("/utente/popup_voto.fxml"));
		Parent root_popup = loader_popup.load();
		popup.initModality(Modality.APPLICATION_MODAL);
		popup.initStyle(StageStyle.UTILITY);
		popup.setScene(new Scene(root_popup, 250, 100));
		popup.setTitle("Conferma voto");
		// chiusura con la X del popup
		popup.setOnCloseRequest((observable) -> {
			session_system.setMessage(null, false);
		});
		popup.setMinWidth(250);
		popup.setMinHeight(150);
		popup.setWidth(250);
		popup.setHeight(150);
		popup.showAndWait();
	}
}