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

    private Object message;
    private SessionSystem ss;
    
    @FXML
    void annulla(ActionEvent event) {
    	ss.setMessage(message, false);
    	close();
    }

    @FXML
    void conferma(ActionEvent event) {
    	ss.setMessage(message, true);
    	close();
    }

    @FXML
    void initialize() {
        assert txt_popup != null : "fx:id=\"txt_popup\" was not injected: check your FXML file 'poup_voto.fxml'.";
        ss= SessionSystem.getInstance();
        message = ss.getMessage();
        txt_popup.setText("Hai scelto \'"+ message.toString() +"\' come voto. Sei sicuro?");
    }
    
    private void close() {
    	ss.getPopup().close();
    }
    
    
    
    
}
