package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import system.Pagina;

public class Controller_main {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane container;

    @FXML
    private AnchorPane container_content;

    @FXML
    private AnchorPane container_menu;

    @FXML
    private VBox content;

    @FXML
    private VBox lst_menu;

    @FXML
    private ScrollPane scroll_content;

    @FXML
    private ScrollPane scroll_menu;
    
    private final double min_larghezza_menu = 150;
	private final double max_larghezza_menu = 300;
	private boolean barra_menu = true;
	private boolean barra_content = true;

    @FXML
    void initialize() {
        assert container != null : "fx:id=\"container\" was not injected: check your FXML file 'main.fxml'.";
        assert container_content != null : "fx:id=\"container_content\" was not injected: check your FXML file 'main.fxml'.";
        assert container_menu != null : "fx:id=\"container_menu\" was not injected: check your FXML file 'main.fxml'.";
        assert content != null : "fx:id=\"content\" was not injected: check your FXML file 'main.fxml'.";
        assert lst_menu != null : "fx:id=\"lst_menu\" was not injected: check your FXML file 'main.fxml'.";
        assert scroll_content != null : "fx:id=\"scroll_content\" was not injected: check your FXML file 'main.fxml'.";
        assert scroll_menu != null : "fx:id=\"scroll_menu\" was not injected: check your FXML file 'main.fxml'.";

    }

	public void init() {
		Pagina pagina = new Pagina("Benvenuto !!!", "../utente/benvenuto.fxml", "../main/button.fxml", content);
		for (int i = 0; i < 20; i++) {
			lst_menu.getChildren().add(pagina.caricaBottone());
		}
		/*for (int i = 0; i < 50; i++) {
			content.getChildren().add(pagina.caricaBottone());
		}*/

		ScrollBar bar_menu = (ScrollBar) scroll_menu.lookup(".scroll-bar:vertical");
		bar_menu.visibleProperty().addListener((observable, oldValue, newValue) -> {
			double larghezza_menu = larghezza_menu(container.getWidth());
			if (newValue) {
				larghezza_menu -= 15;
				barra_menu = true;
			} else {
				larghezza_menu -= 2;
				barra_menu = false;
			}
			lst_menu.setMinWidth(larghezza_menu);
			lst_menu.setMaxWidth(larghezza_menu);
		});

		ScrollBar bar_content = (ScrollBar) scroll_content.lookup(".scroll-bar:vertical");
		bar_content.visibleProperty().addListener((observable, oldValue, newValue) -> {
			double larghezza_content = container_content.getWidth();
			if (newValue) {
				larghezza_content -= 15;
				barra_content = true;
			} else {
				larghezza_content -= 2;
				barra_content = false;
			}
			content.setMinWidth(larghezza_content);
			content.setMaxWidth(larghezza_content);
		});

		container.widthProperty().addListener((observable, oldValue, newValue) -> {
			double larghezzaTotale = newValue.doubleValue();
			double larghezza_menu = larghezza_menu(larghezzaTotale);
			double larghezza_content = larghezzaTotale - larghezza_menu;
			
			container_menu.setMinWidth(larghezza_menu);
			container_menu.setMaxWidth(larghezza_menu);
			
			if (barra_menu) {
				larghezza_menu -= 15;
			} else {
				larghezza_menu -= 2;
			}
			lst_menu.setMinWidth(larghezza_menu);
			lst_menu.setMaxWidth(larghezza_menu);

			container_content.setMinWidth(larghezza_content);
			container_content.setMinWidth(larghezza_content);
			content.setMinWidth(larghezza_content - 2);
			content.setMaxWidth(larghezza_content - 2);
			
			if (barra_content) {
				larghezza_content -= 15;
			} else {
				larghezza_content -= 2;
			}
			content.setMinWidth(larghezza_content);
			content.setMaxWidth(larghezza_content);
		});
	}

	private double larghezza_menu(double larghezza) {
		double new_larghezza_menu = Double.valueOf(String.format("%.0f", larghezza / 3.5));
		double larghezza_menu = min_larghezza_menu;
		if (new_larghezza_menu < min_larghezza_menu) {
			larghezza_menu = min_larghezza_menu;
		} else if (new_larghezza_menu > max_larghezza_menu) {
			larghezza_menu = max_larghezza_menu;
		} else {
			larghezza_menu = new_larghezza_menu;
		}
		return larghezza_menu;
	}
}