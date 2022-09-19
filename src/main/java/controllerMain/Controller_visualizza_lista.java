package controllerMain;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import system.ListCheckBoxUpdater;
import system.SessionSystem;
import system.votabili.Lista;
import system.votabili.Votabile;

public class Controller_visualizza_lista {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private VBox elementi;

    @FXML
    private Text txt_nome_lista;
    
    private int nLista;
    private Lista lista;

    @FXML
    void initialize() {
        assert elementi != null : "fx:id=\"elementi\" was not injected: check your FXML file 'visualizza_lista.fxml'.";
        assert txt_nome_lista != null : "fx:id=\"txt_nome_lista\" was not injected: check your FXML file 'visualizza_lista.fxml'.";
        SessionSystem ss = SessionSystem.getInstance();
        
        lista = (Lista) ss.getMessage();
        nLista = (int) ss.getMessage(1);
        
        txt_nome_lista.setText(toTitleCase(lista.getNome()));
        ListCheckBoxUpdater m = (ListCheckBoxUpdater) ss.getMessage(2);
        m.setLista(nLista);
        ArrayList<Votabile> aspiranti = lista.getCandidati();
		for (int i = 0; i < aspiranti.size(); i++) {
			try {
				m.setCandidato(i);
				Votabile a = aspiranti.get(i);
				ss.setMessage(a.toString(), m.getNextId() , m);
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/visualizza_elemento_lista.fxml"));
				elementi.getChildren().add(loader.load());
			} catch (Exception e) {
				System.out.println("---! caricamento gridpane fallito.");
				e.printStackTrace();
			}
		}
		
    }
    
	private String toTitleCase(String string) {
	    String[] arr = string.split(" ");
	    StringBuffer sb = new StringBuffer();

	    for (int i = 0; i < arr.length; i++) {
	        sb.append(Character.toUpperCase(arr[i].charAt(0)))
	            .append(arr[i].substring(1)).append(" ");
	    }          
	    return sb.toString().trim();
	}

}
