package controller;

import java.net.URL;
import java.util.ResourceBundle;

import data.Status;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ControllerWelcome extends Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label name;

    @FXML
    void initialize() {
        assert name != null : "fx:id=\"name\" was not injected: check your FXML file 'welcome.fxml'.";
    }

	@Override
	public void initData() {
		Status status = (Status) super.getStage().getUserData();
		name.setText("Benvenuto " + status.getUserData().getUsername() + "!!!");
	}

}
