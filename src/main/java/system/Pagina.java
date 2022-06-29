package system;

import java.io.IOException;
import controller.Controller_button;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class Pagina {
	private String nome;
	private String content_url;
	private String button_url;
	private VBox parent;

	public Pagina(String nome, String content_url, String button_url, VBox parent) {
		this.nome = nome;
		this.content_url = content_url;
		this.button_url = button_url;
		this.parent = parent;
	}

	public String getNome() {
		return nome;
	}
	
	public AnchorPane caricaBottone() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(button_url));
		AnchorPane bottone = null;
		try {
			bottone = loader.load();
			Controller_button controller = loader.<Controller_button>getController();
			controller.setPagina(this);
		} catch (IOException e) {
			System.out.println("caricamento bottone '" + nome + "' fallito.");
		}
		return bottone;
	}
	
	public void caricaPagina() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(content_url));
		VBox content = null;
		try {
			content = loader.load();
			parent.getChildren().clear();
			parent.getChildren().add(content);
		} catch (IOException e) {
			System.out.println("caricamento content '" + nome + "' fallito.");
			e.printStackTrace();
		}
	}
}
