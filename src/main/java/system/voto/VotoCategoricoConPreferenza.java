package system.voto;

import java.util.ArrayList;
import system.schede.Scheda;
import system.votabili.Lista;
import system.votabili.Votabile;

public class VotoCategoricoConPreferenza extends Voto {
	
	private ArrayList<Votabile> voto;
	private Lista lista;

	public VotoCategoricoConPreferenza(Scheda scheda, Lista id_lista, ArrayList<Votabile> voto) {
		super(scheda);
		this.voto = voto;
		this.lista = id_lista;
	}

	public ArrayList<Votabile> getVoto() {
		return voto;
	}

	public Lista getLista() {
		return lista;
	}

}
