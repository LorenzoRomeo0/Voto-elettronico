package system.voto;

import system.schede.Scheda;
import system.votabili.Votabile;

public class VotoCategorico extends Voto {
	
	private Votabile voto;

	public VotoCategorico(Scheda scheda, Votabile voto) {
		super(scheda);
		this.voto = voto;
	}

	public Votabile getVoto() {
		return voto;
	}
	
}
