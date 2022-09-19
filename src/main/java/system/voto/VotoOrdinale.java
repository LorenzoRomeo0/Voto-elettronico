package system.voto;

import java.util.ArrayList;
import system.schede.Scheda;
import system.votabili.Votabile;

public class VotoOrdinale extends Voto {
	
	private ArrayList<Votabile> voto;

	public VotoOrdinale(Scheda scheda, ArrayList<Votabile> voto) {
		super(scheda);
		this.voto = voto;
	}

	public ArrayList<Votabile> getVoto() {
		return voto;
	}
	
}
