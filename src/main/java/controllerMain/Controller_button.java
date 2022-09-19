package controllerMain;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import system.Pagina;

public class Controller_button {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btn_name;
    
    private Pagina pagina;

    @FXML
    void caricaPagina(ActionEvent event) {
    	pagina.caricaPagina();
    }

	@FXML
    void initialize() {
        assert btn_name != null : "fx:id=\"btn_name\" was not injected: check your FXML file 'button.fxml'.";
        btn_name.setAlignment(Pos.BASELINE_LEFT);
    }

	public void setPagina(Pagina pagina) {
		this.pagina = pagina;
		btn_name.setText(pagina.getNome());
	}
}
