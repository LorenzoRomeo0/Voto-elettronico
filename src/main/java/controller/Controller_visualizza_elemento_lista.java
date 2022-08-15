package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import system.CheckBoxObserver;
import system.SessionSystem;

public class Controller_visualizza_elemento_lista {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private CheckBox chkb_opzione;
    
    private int pos;
    private CheckBoxObserver observer;
    
    @FXML
    void selected(ActionEvent event) {
    	observer.selected(pos);
    }
    
    @FXML
    void initialize() {
        assert chkb_opzione != null : "fx:id=\"chkb_opzione\" was not injected: check your FXML file 'visualizza_elemento_lista.fxml'.";
        SessionSystem ss = SessionSystem.getInstance();
        String string = (String) ss.getMessage();
        pos = (int) ss.getMessage(1);
        observer = (CheckBoxObserver) ss.getMessage(2);
        observer.add(chkb_opzione);
        chkb_opzione.setText(string);
    }
}
