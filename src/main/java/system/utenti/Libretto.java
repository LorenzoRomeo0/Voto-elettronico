package system.utenti;

import java.util.ArrayList;

import system.schede.Scheda;

public class Libretto {
	private ArrayList <Scheda> schedeVotate;
	
	public Libretto(ArrayList<Scheda> compilate) {
		this.schedeVotate = compilate;
	}

	public boolean haVotato(int idScheda) {
		boolean trovato = false;
		for (Scheda scheda : schedeVotate) {
			if (scheda.getId() == idScheda) {
				trovato =  true;
				break;
			}
		}
		return trovato;
	}
	
	public void addScheda(Scheda scheda) {
		this.schedeVotate.add(scheda);
	}
}
