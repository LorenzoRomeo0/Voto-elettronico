package system.voto;

import system.schede.Scheda;

public abstract class Voto {
	private Scheda scheda;
	
	public Voto(Scheda scheda) {
		this.scheda = scheda;
	}

	public Scheda getScheda() {
		return scheda;
	}
	
	public int getSchedaId() {
		return scheda.getId();
	}
}
