package controllerMain;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import dao.SistemaVotazioniDAO;
import data.TipoUtente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import system.SessionSystem;
import system.utenti.Elettore;
import system.utenti.Impiegato;
import system.utenti.Libretto;

public class Controller_scegli_tipo_visualizzazione {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    void scegli_elettore(ActionEvent event) {
    	SessionSystem ss = SessionSystem.getInstance();
    	Impiegato imp = (Impiegato) ss.getUtente();
    	SistemaVotazioniDAO dao = SistemaVotazioniDAO.getInstance();
    	ArrayList<Integer> compilate = dao.getSchedeCompilate(imp.getId());
    	Elettore e = new Elettore(imp.getId(), imp.getNome(), imp.getCognome(), imp.getDataDinascita(), imp.getResidenza(),
    			imp.getNazionalita(), imp.getCodiceFiscale(), TipoUtente.ELETTORE.toString(), new Libretto(ss.getSchedeCompilate(compilate)));
    	ss.setUtente(e);
    	SessionSystem.getInstance().loadMain();
    }

    @FXML
    void scegli_impiegato(ActionEvent event) {
    	SessionSystem.getInstance().loadMain();
    }

    @FXML
    void initialize() {
    	
    }


}
