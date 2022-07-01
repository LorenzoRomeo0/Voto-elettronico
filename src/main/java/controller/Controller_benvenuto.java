package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import system.Session;

public class Controller_benvenuto {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private VBox content;

    @FXML
    private Label txt_benvenuto;

    @FXML
    void initialize() {
        assert content != null : "fx:id=\"content\" was not injected: check your FXML file 'benvenuto.fxml'.";
        assert txt_benvenuto != null : "fx:id=\"txt_benvenuto\" was not injected: check your FXML file 'benvenuto.fxml'.";
        
        Session session = Session.getInstance();
        VBox container_content = session.getContent();
        container_content.widthProperty().addListener((observable, oldValue, newValue) -> {
        	double larghezza = newValue.doubleValue();
        	content.setMinWidth(larghezza);
        	content.setMaxWidth(larghezza);
        	System.out.println(oldValue + " -> " + newValue);
        });
        
        Stage stage = session.getStage();
        stage.resizableProperty().addListener((observable, oldValue, newValue) -> {
        	double larghezza = container_content.getWidth();
        	content.setMinWidth(larghezza);
        	content.setMaxWidth(larghezza);
        	System.out.println(oldValue + " -> " + newValue);
        });;
        
        txt_benvenuto.setText("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");
        
    }

}
