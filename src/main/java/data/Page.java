package data;

import controller.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class Page {
	private String name;
	private AnchorPane content;
	private Controller controller;

	public Page(String name, String path) throws Exception {
		this.name = name;
		FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
		this.content = loader.load();
		this.controller = loader.getController();
	}

	@Override
	public String toString() {
		return name;
	}

	public AnchorPane getContent() {
		return content;
	}

	public Controller getController() {
		return controller;
	}

	public String getName() {
		return name;
	}

}
