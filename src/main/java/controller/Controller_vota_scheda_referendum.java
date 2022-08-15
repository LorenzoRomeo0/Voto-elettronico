package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import dao.SchedaDTO;
import dao.SistemaVotazioniDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import system.Referendum;
import system.SessionSystem;
import system.SessionUser;
import system.VotoReferendum;

public class Controller_vota_scheda_referendum {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Label txt_referendum;

	@FXML
	void favorevole(ActionEvent event) {
		session_system.setMessage(new VotoReferendum(Referendum.FAVOREVOLE));
		popup();
	}

	@FXML
	void non_favorevole(ActionEvent event) {
		session_system.setMessage(new VotoReferendum(Referendum.NON_FAVOREVOLE));
		popup();
	}

	@FXML
	void nulla(ActionEvent event) {
		session_system.setMessage(new VotoReferendum(Referendum.NULLO));
		popup();
	}
	
	private SchedaDTO scheda;
	private SessionUser session_user;
	private SessionSystem session_system;
	private SistemaVotazioniDAO dao;

	@FXML
	void initialize() {
		assert txt_referendum != null : "fx:id=\"txt_referendum\" was not injected: check your FXML file 'vota_scheda_referendum.fxml'.";
		dao = new SistemaVotazioniDAO();
		session_user = SessionUser.getInstance();
		scheda = session_user.getScheda();
		txt_referendum.setText(dao.get_referendum(scheda.getId()));
		session_system = SessionSystem.getInstance();
	}

	private void popup() {
		try {
			creaPopup();
		} catch (Exception e) {
			System.out.println("---X caricamento popup referendum fallito.");
			e.printStackTrace();
		}
		boolean inserisci = (boolean) session_system.getMessage(1);
		if (inserisci) {
			VotoReferendum value = (VotoReferendum) session_system.getMessage();
			dao.insert_voto_referendum(scheda.getId(), value.getValue());
        	dao.insert_schede_votate(session_user.getUtente().getId(), scheda.getId());
			try {
				tornaMain();
			} catch (IOException e) {
				System.out.println("---! da referendum a main");
				e.printStackTrace();
			}
		}
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
		//chiusura con la X del popup
		popup.setOnCloseRequest((observable) -> {
			session_system.setMessage(null, false);
		});
		popup.setMinWidth(250);
		popup.setMinHeight(150);
		popup.setWidth(250);
		popup.setHeight(150);
		popup.showAndWait();
	}
	
	private void tornaMain() throws IOException {
		Stage stage = session_system.getStage();
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/main.fxml"));
		Parent root = loader.load();
		stage.setTitle("vota");
		stage.setScene(new Scene(root, 450, 350));
	}

}
