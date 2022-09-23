package controllerImpiegato;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logger.FileLogger;
import system.SessionSystem;

public class Controller_log {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private VBox content;

    @FXML
    private Text txt_error;

    @FXML
    private TextArea txt_log;

    @FXML
    void btn_ricarica_log(ActionEvent event) {
    	FileLogger logger = FileLogger.getInstance();
    	txt_log.setText(logger.retrieveAll());
    }

    @FXML
    void btn_svuota_log(ActionEvent event) {
    	FileLogger logger = FileLogger.getInstance();
    	logger.makeEmpty();
    	txt_log.setText(logger.retrieveAll());
    }

    @FXML
    void initialize() {
        assert content != null : "fx:id=\"content\" was not injected: check your FXML file 'visualizza_log.fxml'.";
        assert txt_error != null : "fx:id=\"txt_error\" was not injected: check your FXML file 'visualizza_log.fxml'.";
        assert txt_log != null : "fx:id=\"txt_log\" was not injected: check your FXML file 'visualizza_log.fxml'.";
        
        SessionSystem ss = SessionSystem.getInstance();
		VBox container_content = ss.getContent();
		container_content.widthProperty().addListener((observable, oldValue, newValue) -> {
			adatta_contenuto(newValue.doubleValue());
		});

		Stage stage = ss.getStage();
		stage.resizableProperty().addListener((observable, oldValue, newValue) -> {
			adatta_contenuto(container_content.getWidth());
		});
		
		FileLogger logger = FileLogger.getInstance();
    	txt_log.setText(logger.retrieveAll());
        
    }
    
    private void adatta_contenuto(double larghezza) {
		content.setMinWidth(larghezza);
		content.setMaxWidth(larghezza);
	}

}
