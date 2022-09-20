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
import system.checkboxManagers.ListCheckBox;
import system.checkboxManagers.ListCheckBoxUpdater;
import system.schede.SchedaCategoricaConPreferenze;
import system.utenti.Elettore;
import system.votabili.Lista;
import system.votabili.Votabile;
import system.voto.VotoCategoricoConPreferenza;

public class ControllerVotaSchedaCategoricaConPreferenza {

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
	private final double col_max_width = 500.0;
	private SessionSystem ss;
	private SchedaCategoricaConPreferenze scheda;
	private ArrayList<Lista> liste;
	private Elettore elettore;
	private ListCheckBoxUpdater options;

	@FXML
	void conferma_voto(ActionEvent event) {
		ArrayList<ListCheckBox> values = options.getValues();
		if (null != values) {
			ArrayList<Votabile> votabili = new ArrayList<Votabile>();
			for (ListCheckBox value : values) {
				votabili.add(liste.get(value.getLista()).getCandidati().get(value.getCandidato()));
			}
			Lista l = liste.get(options.getSelectedLista());
			try {
				StringBuilder b = new StringBuilder("Hai scelto dalla lista \'" + l.getNome() + "\':\n");
				for (int i = 0; i < votabili.size(); i++) {
					b.append((i + 1) + ") " + votabili.get(i) + "\n");
				}
				b.append("Confermi?");
				ss.setMessage(b.toString());
				creaPopup();
			} catch (Exception e) {
				System.out.println("---! caricamento popup ordinale fallito.");
				e.printStackTrace();
			}
			boolean inserisci = (boolean) ss.getMessage();
			if (inserisci) {
				VotoCategoricoConPreferenza voto = new VotoCategoricoConPreferenza(scheda, l.getId(), votabili);
				elettore.vota(voto);
				SessionSystem.getInstance().loadMain();
			}
		} else {
			voto_nullo(event);
		}
	}

	@FXML
	void voto_nullo(ActionEvent event) {
		try {
			ss.setMessage("Hai scelto \'NULLO\' come voto. Sei sicuro?");
			creaPopup();
		} catch (Exception e) {
			System.out.println("---! caricamento popup ordinale fallito.");
			e.printStackTrace();
		}
		boolean inserisci = (boolean) ss.getMessage();
		if (inserisci) {
			VotoCategoricoConPreferenza voto = new VotoCategoricoConPreferenza(scheda, null, null);
			elettore.vota(voto);
			SessionSystem.getInstance().loadMain();
		}
	}

	@FXML
	void initialize() {
		assert col1 != null
				: "fx:id=\"col1\" was not injected: check your FXML file 'vota_scheda_categorica_preferenza.fxml'.";
		assert col2 != null
				: "fx:id=\"col2\" was not injected: check your FXML file 'vota_scheda_categorica_preferenza.fxml'.";
		assert contenitore_scheda != null
				: "fx:id=\"contenitore_scheda\" was not injected: check your FXML file 'vota_scheda_categorica_preferenza.fxml'.";
		assert dati_scheda != null
				: "fx:id=\"dati_scheda\" was not injected: check your FXML file 'vota_scheda_categorica_preferenza.fxml'.";
		assert schermata != null
				: "fx:id=\"schermata\" was not injected: check your FXML file 'vota_scheda_categorica_preferenza.fxml'.";

		ss = SessionSystem.getInstance();

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

		ss = SessionSystem.getInstance();
		scheda = (SchedaCategoricaConPreferenze) ss.getScheda();
		liste = scheda.getListe();
		elettore = (Elettore) ss.getUtente();
		options = new ListCheckBoxUpdater();

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
		int rows = liste.size();
		if (col2_visible) {
			rows = Math.round(rows / 2);
		}
		for (int i = 0; i < rows; i++) {
			dati_scheda.addRow(i, new Label());
		}
	}

	private void datiGridpane() {
		int row = 0;
		for (int i = 0; i < liste.size(); i++) {
			try {
				Votabile a = liste.get(i);
				ss.setMessage(a, i, options);
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/visualizza_lista.fxml"));
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
		ss.setPopup(popup);
		FXMLLoader loader_popup = new FXMLLoader(getClass().getResource("/utente/popup_voto.fxml"));
		Parent root_popup = loader_popup.load();
		popup.initModality(Modality.APPLICATION_MODAL);
		popup.initStyle(StageStyle.UTILITY);
		popup.setScene(new Scene(root_popup));
		popup.setTitle("Conferma voto");
		// chiusura con la X del popup
		popup.setOnCloseRequest((observable) -> {
			ss.setMessage(false);
		});
		popup.showAndWait();
	}

}
