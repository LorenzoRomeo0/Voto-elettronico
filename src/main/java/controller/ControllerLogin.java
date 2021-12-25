package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import data.Status;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import model.ModelLogin;

public class ControllerLogin extends Controller{

	private ModelLogin model;
	
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label error;

    @FXML
    private PasswordField myPassword;

    @FXML
    private TextField myUsername;

    @FXML
    void onLogin(ActionEvent event) {
    	login();
    }

    private void startMain() {
    	try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../main.fxml"));
			Parent root = loader.load();
			Controller controller = loader.getController();
			
			controller.setStage(getStage());
			controller.initData();		
			
	    	myUsername.getScene().setRoot(root);
	    	
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    @FXML
    void initialize() {
        assert error != null : "fx:id=\"error\" was not injected: check your FXML file 'login.fxml'.";
        assert myPassword != null : "fx:id=\"myPassword\" was not injected: check your FXML file 'login.fxml'.";
        assert myUsername != null : "fx:id=\"myUsername\" was not injected: check your FXML file 'login.fxml'.";
        
       model = new ModelLogin();
    }

	@Override
	public void initData() {}
	
    @FXML
    void onEnter(KeyEvent event) {
    	if (event.getCode().equals(KeyCode.ENTER)) {
			login();
		}
    }
	
	private void login() {
		error.setText("");
    	if(myUsername.getText() == null || myPassword.getText() == null
    			|| myPassword.getText().isBlank() || myUsername.getText().isBlank()) {
    		error.setText("Credenziali invalide!");
    		return;
    	}
    	
    	String username = myUsername.getText();
    	String password = myPassword.getText();
    	
    	if(model.login(username, password)) {
    		stage.setUserData(new Status(model.getUser(username)));

    		startMain();
    	}else error.setText("Credenziali errate!");
	}

}
