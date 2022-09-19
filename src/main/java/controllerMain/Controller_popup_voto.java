package controllerMain;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import system.SessionSystem;

public class Controller_popup_voto {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private VBox container;

    @FXML
    private Label txt_popup;


	private SessionSystem ss;
    
	@FXML
	void annulla(ActionEvent event) {
		ss.setMessage(false);
		close();
	}

	@FXML
	void conferma(ActionEvent event) {
		ss.setMessage(true);
		close();
	}

    @FXML
    void initialize() {
        assert container != null : "fx:id=\"container\" was not injected: check your FXML file 'popup_voto.fxml'.";
        assert txt_popup != null : "fx:id=\"txt_popup\" was not injected: check your FXML file 'popup_voto.fxml'.";
        ss = SessionSystem.getInstance();
        String print = ss.getMessage().toString();
        txt_popup.setText(print);
        int height = (int) txt_popup.getFont().getSize();
        int rows = (int) print.lines().count();
        int containerH = rows * (height + 10);
        container.setMinHeight(containerH);
        container.setPrefHeight(containerH);
        ss.getPopup().setMinHeight(containerH+45+35);
    }
    
    private void close() {
		ss.getPopup().close();
	}

}
