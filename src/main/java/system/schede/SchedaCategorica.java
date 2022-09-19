package system.schede;

import java.util.ArrayList;

import dao.SchedaDTO;
import system.votabili.Votabile;

public class SchedaCategorica extends Scheda {

	private ArrayList<Votabile> votabili;

	public SchedaCategorica(SchedaDTO scheda, ArrayList<Votabile> votabili) {
		super(scheda);
		this.votabili = votabili;
	}

	public ArrayList<Votabile> getVotabile() {
		return votabili;
	}

	@Override
	public String toString() {
		return nome;
	}
}
