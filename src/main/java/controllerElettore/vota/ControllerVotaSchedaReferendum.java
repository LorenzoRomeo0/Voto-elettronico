package controllerElettore.vota;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import system.SessionSystem;
import system.schede.SchedaReferendum;
import system.utenti.Elettore;
import system.voto.VotoReferendum;
import data.Referendum;

public class ControllerVotaSchedaReferendum {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Label txt_referendum;

	private Referendum selected;
	
	@FXML
	void favorevole(ActionEvent event) {
		selected = Referendum.FAVOREVOLE;
		ss.setMessage("Hai scelto \'FAVOREVOLE\' come voto. Sei sicuro?");
		confermaVoto();
	}

	@FXML
	void non_favorevole(ActionEvent event) {
		selected = Referendum.NON_FAVOREVOLE;
		ss.setMessage("Hai scelto \'NON FAVOREVOLE\' come voto. Sei sicuro?");
		confermaVoto();
	}

	@FXML
	void nulla(ActionEvent event) {
		selected = Referendum.NULLO;
		ss.setMessage("Hai scelto \'NULLA\' come voto. Sei sicuro?");
		confermaVoto();
	}

	private SchedaReferendum scheda;
	private SessionSystem ss;

	@FXML
	void initialize() {
		assert txt_referendum != null
				: "fx:id=\"txt_referendum\" was not injected: check your FXML file 'vota_scheda_referendum.fxml'.";
		ss = SessionSystem.getInstance();
		scheda = (SchedaReferendum) ss.getScheda();
		txt_referendum.setText(scheda.getReferendum());
		ss = SessionSystem.getInstance();
	}

	private void confermaVoto() {
		try {
			creaPopup();
		} catch (Exception e) {
			System.out.println("---X caricamento popup referendum fallito.");
			e.printStackTrace();
		}
		boolean inserisci = (boolean) ss.getMessage();
		if (inserisci) {
			Elettore elettore = (Elettore) ss.getUtente();
			elettore.vota(new VotoReferendum(selected, scheda));
			SessionSystem.getInstance().loadMain();
		}
	}

	private void creaPopup() throws IOException {
		Stage popup = new Stage();
		ss.setPopup(popup);
		FXMLLoader loader_popup = new FXMLLoader(getClass().getResource("/utente/popup_voto.fxml"));
		Parent rootPopup = loader_popup.load();
		popup.initModality(Modality.APPLICATION_MODAL);
		popup.initStyle(StageStyle.UTILITY);
		popup.setScene(new Scene(rootPopup, 250, 100));
		popup.setTitle("Conferma voto");
		// chiusura con la X del popup
		popup.setOnCloseRequest((observable) -> {
			ss.setMessage(false);
		});
		popup.setMinWidth(250);
		popup.setMinHeight(150);
		popup.setWidth(250);
		popup.setHeight(150);
		popup.showAndWait();
	}
}
