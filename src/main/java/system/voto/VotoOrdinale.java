package system.voto;

import system.schede.Scheda;
import system.votabili.Votabile;

public class VotoOrdinale extends Voto {
	
	private Votabile voto;
	private Integer posizione;
	
	public VotoOrdinale(Scheda scheda, Votabile voto, Integer posizione) {
		super(scheda);
		this.voto = voto;
		this.posizione = posizione;
	}

	public Votabile getVoto() {
		return voto;
	}
	
	public void setVoto(Votabile voto) {
		this.voto = voto;
	}
	
	public int getPosizione() {
		return posizione;
	}
	
	public void setPosizione(Integer posizione) {
		this.posizione = posizione;
	}

}
