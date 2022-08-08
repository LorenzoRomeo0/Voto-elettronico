package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import system.SessionSystem;

public class Controller_popup_voto {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label txt_popup;

    @FXML
    void annulla(ActionEvent event) {
    	SessionSystem.getInstance().setPopup_Value(null);
    	close();
    }

    @FXML
    void conferma(ActionEvent event) {
    	close();
    }

    @FXML
    void initialize() {
        assert txt_popup != null : "fx:id=\"txt_popup\" was not injected: check your FXML file 'poup_voto.fxml'.";
        SessionSystem ss = SessionSystem.getInstance();
        Object value = ss.getPopup_Value();
        txt_popup.setText("Hai scelto \'"+ value.toString() +"\' come voto. Sei sicuro?");
    }
    
    private void close() {
    	SessionSystem.getInstance().getPopup().close();
    }
    
    
}
