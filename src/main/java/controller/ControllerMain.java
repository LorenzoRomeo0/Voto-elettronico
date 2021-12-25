package controller;

import java.net.URL;
import java.util.ResourceBundle;

import data.Page;
import data.Status;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class ControllerMain extends Controller {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private ListView<Page> actions;

	@FXML
	private AnchorPane page;

	@FXML
	private Text userType;

	@FXML
	void initialize() {
		assert actions != null : "fx:id=\"actions\" was not injected: check your FXML file 'main.fxml'.";
		assert page != null : "fx:id=\"page\" was not injected: check your FXML file 'main.fxml'.";
		assert userType != null : "fx:id=\"userType\" was not injected: check your FXML file 'main.fxml'.";
		
	}
	
	@Override
	public void initData() {
		Status st = (Status) getStage().getUserData();
		
		userType.setText(st.getUserData().getUserType());
		ObservableList<Page> items = actions.getItems();
		try {
			Page welcome = new Page("Benvenuto !!!", "../welcome.fxml");
			Controller welcomeController = welcome.getController();
			welcomeController.setStage(getStage());
			welcomeController.initData();;
			items.add(welcome);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			Page myData = new Page("I miei dati", "../myData.fxml");
			Controller myDataController = myData.getController();
			myDataController.setStage(getStage());
			myDataController.initData();
			items.add(myData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			Page vota = new Page("Vota", "../workInProgress.fxml");
			items.add(vota);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (items.size() > 0) {
			actions.getSelectionModel().select(0);
			page.getChildren().add(items.get(0).getContent());
		}
		
		actions.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Page>() {

			@Override
			public void changed(ObservableValue<? extends Page> observable, Page oldValue, Page newValue) {
				page.getChildren().clear();
				if(newValue != null && newValue.getContent() != null) {
					page.getChildren().add(newValue.getContent());
				}
			}
		});
	}
	

}
