package system.utenti;

import java.time.LocalDate;

import dao.UtenteDTO;
import system.luoghi.Comune;
import system.schede.Scheda;

public class Impiegato extends Utente {

	public Impiegato(int id, String nome, String cognome, LocalDate dataDinascita, Comune comune, String nazionalita,
			String codiceFiscale, String tipo) {
		super(id, nome, cognome, dataDinascita, comune, nazionalita, codiceFiscale, tipo);
	}
	
	public Impiegato(UtenteDTO utente, Comune residenza) {
		super(utente, residenza);
	}
	
	public void creaScheda(Scheda scheda) {}
	/* manda al db*/
	
	public void /* Esito */  calcolaRisutato(/* ??? */) {}
	/* calcola esito */
	
}
