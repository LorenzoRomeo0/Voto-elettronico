package system.voto;

import java.util.ArrayList;
import system.schede.Scheda;
import system.votabili.Votabile;

public class VotoCategoricoConPreferenza extends Voto {
	
	private ArrayList<Votabile> voto;
	private int lista;

	public VotoCategoricoConPreferenza(Scheda scheda, Integer id_lista, ArrayList<Votabile> voto) {
		super(scheda);
		this.voto = voto;
		this.lista = id_lista;
	}

	public ArrayList<Votabile> getVoto() {
		return voto;
	}

	public int getLista() {
		return lista;
	}

}
