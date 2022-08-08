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

public class Controller_vota_referendum {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Label txt_referendum;

	@FXML
	void favorevole(ActionEvent event) {
		SessionSystem.getInstance().setPopup_Value(new VotoReferendum(Referendum.FAVOREVOLE));
		popup();
	}

	@FXML
	void non_favorevole(ActionEvent event) {
		SessionSystem.getInstance().setPopup_Value(new VotoReferendum(Referendum.NON_FAVOREVOLE));
		popup();
	}

	@FXML
	void nulla(ActionEvent event) {
		SessionSystem.getInstance().setPopup_Value(new VotoReferendum(Referendum.NULLO));
		popup();
	}

	private SessionUser session_user;
	private SchedaDTO scheda;
	private SistemaVotazioniDAO dao;
	
	@FXML
	void initialize() {
		assert txt_referendum != null
				: "fx:id=\"txt_referendum\" was not injected: check your FXML file 'vota_scheda_referendum.fxml'.";
		dao = new SistemaVotazioniDAO();
		session_user = SessionUser.getInstance();
		scheda = session_user.getScheda();
		txt_referendum.setText(dao.get_referendum(scheda.getId()));
	}

	private void popup() {
		try {
			SessionSystem system = SessionSystem.getInstance();
			Stage popup = new Stage();
			system.setPopup(popup);
			popup.initModality(Modality.APPLICATION_MODAL);
			FXMLLoader loader_popup = new FXMLLoader(getClass().getResource("/utente/popup_voto.fxml"));
			Parent root_popup = loader_popup.load();
			popup.setTitle("Conferma voto");
			popup.initStyle(StageStyle.UTILITY);
			
			popup.setScene(new Scene(root_popup, 250, 100));
			popup.setOnCloseRequest(event -> {
				system.setPopup_Value(null);
			});
			popup.setMinWidth(250);
			popup.setMinHeight(150);
			popup.setWidth(250);
			popup.setHeight(150);
			popup.showAndWait();
			
	        VotoReferendum value = (VotoReferendum) system.getPopup_Value();
	        if (null != value) {
	        	dao.insert_voto_referendum(scheda.getId(), value.getValue());
	        	dao.insert_schede_votate(session_user.getUtente().getId(), scheda.getId());
	        	
	        	try {
	        	Stage stage = system.getStage();
	        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/main.fxml"));
	    		Parent root = loader.load();
	    		stage.setTitle("vota");
	    		stage.setScene(new Scene(root, 450, 350));
	        	} catch (Exception e) {
	        		System.out.println("--> da referendum ricarica voto");
					e.printStackTrace();
				}
	        	
	        } else {
	        	System.out.println("non inserire");
	        }
		} catch (IOException e) {
			System.out.println("---> caricamento popup voto fallito.");
			e.printStackTrace();
		}
	}

}
