package controllerElettore.vota;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import system.SessionSystem;
import system.schede.SchedaOrdinale;
import system.utenti.Elettore;
import system.votabili.Votabile;
import system.voto.VotoOrdinale;

public class ControllerVotaSchedaOrdinale {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private ListView<Votabile> candidati;

	@FXML
	private GridPane candidati_container;

	@FXML
	private VBox container;

	@FXML
	void conferma_voto(ActionEvent event) {
		try {
			StringBuilder b = new StringBuilder("Hai scelto: \n");
			for (int i = 0; i < aspiranti.size(); i++) {
				b.append((i + 1) + ") " + aspiranti.get(i) + "\n");
			}
			b.append("come ordine. Confermi?");
			ss.setMessage(b.toString());
			creaPopup();
		} catch (Exception e) {
			System.out.println("---! caricamento popup ordinale fallito.");
			e.printStackTrace();
		}
		boolean inserisci = (boolean) ss.getMessage();
		if (inserisci) {
			VotoOrdinale voto = new VotoOrdinale(scheda, aspiranti);
			elettore.vota(voto);
			SessionSystem.getInstance().loadMain();
		}
	}

	@FXML
	void down(ActionEvent event) {
		int pos = candidati.getSelectionModel().getSelectedIndex();
		if (pos < aspiranti.size() - 1) {
			Votabile sw1 = aspiranti.get(pos + 1);
			Votabile sw2 = aspiranti.get(pos);
			aspiranti.set(pos, sw1);
			aspiranti.set(pos + 1, sw2);
			refresh();
		}
	}

	@FXML
	void up(ActionEvent event) {
		int pos = candidati.getSelectionModel().getSelectedIndex();
		if (pos > 0) {
			Votabile sw1 = aspiranti.get(pos - 1);
			Votabile sw2 = aspiranti.get(pos);
			aspiranti.set(pos, sw1);
			aspiranti.set(pos - 1, sw2);
			refresh();
		}
	}

	@FXML
	void voto_nullo(ActionEvent event) {
		ss.setMessage("Hai scelto \'NULLO\' come voto. Sei sicuro?");
		try {
			creaPopup();
		} catch (Exception e) {
			System.out.println("---! caricamento popup categorico fallito.");
			e.printStackTrace();
		}
		boolean inserisci = (boolean) ss.getMessage();
		if (inserisci) {
			VotoOrdinale voto = new VotoOrdinale(scheda, null);
			elettore.vota(voto);
			SessionSystem.getInstance().loadMain();
		}
	}

	private SessionSystem ss;
	private SchedaOrdinale scheda;
	private ArrayList<Votabile> aspiranti;
	private Elettore elettore;

	@FXML
	void initialize() {
		assert candidati != null
				: "fx:id=\"candidati\" was not injected: check your FXML file 'vota_scheda_ordinale.fxml'.";
		assert candidati_container != null
				: "fx:id=\"candidati_container\" was not injected: check your FXML file 'vota_scheda_ordinale.fxml'.";
		assert container != null
				: "fx:id=\"container\" was not injected: check your FXML file 'vota_scheda_ordinale.fxml'.";

		container.heightProperty().addListener((observable, oldValue, newValue) -> {
			double value = newValue.doubleValue();
			if (value < candidati_container.getMaxHeight()) {
				candidati_container.setMinHeight(value);
			}
		});

		container.widthProperty().addListener((observable, oldValue, newValue) -> {
			double value = newValue.doubleValue();
			if (value < candidati_container.getMaxWidth()) {
				candidati_container.setMinWidth(value);
			}
		});

		ss = SessionSystem.getInstance();
		scheda = (SchedaOrdinale) ss.getScheda();
		aspiranti = scheda.getVotabile();
		elettore = (Elettore) ss.getUtente();
		
		Collections.shuffle(aspiranti);
		refresh();
	}

	private void refresh() {
		candidati.setItems(FXCollections.observableArrayList(aspiranti));
		candidati.refresh();
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