package system.utenti;

import java.time.LocalDate;
import java.util.ArrayList;
import dao.UtenteDTO;
import data.Esito;
import data.Stato;
import data.TipoScheda;
import data.TipoVotabile;
import system.luoghi.Comune;
import system.schede.Scheda;
import system.votabili.Votabile;

public class Impiegato extends Utente {

	public Impiegato(int id, String nome, String cognome, LocalDate dataDinascita, Comune comune, String nazionalita,
			String codiceFiscale, String tipo) {
		super(id, nome, cognome, dataDinascita, comune, nazionalita, codiceFiscale, tipo);
	}
	
	public Impiegato(UtenteDTO utente, Comune residenza) {
		super(utente, residenza);
	}
	
	public void creaSchedaCategorica(LocalDate avvio, LocalDate termine, Stato stato, Esito esito,
			TipoVotabile tipo, String nome, ArrayList<Votabile> aspiranti) {
		dao.insertSchedaCategorica(avvio, termine, this.id, stato, esito, tipo, nome, aspiranti);
	}
	
	public void creaSchedaCategoricaConPreferenza(LocalDate avvio, LocalDate termine, Stato stato,
			Esito esito, String nome, ArrayList<Votabile> aspiranti) {
		dao.insertSchedaCategoricaPreferenza(avvio, termine, this.id, stato, esito, nome, aspiranti);
	}
	
	public void creaSchedaOrdinale(LocalDate avvio, LocalDate termine, Stato stato, Esito esito,
			TipoVotabile tipo, String nome, ArrayList<Votabile> partecipanti) {
		dao.insertSchedaOrdinale(avvio, termine, this.id, stato, esito, tipo, nome, partecipanti);
	}
	
	public void creaSchedaReferendum(LocalDate avvio, LocalDate termine, Stato stato, Esito esito,
			String nome, String referendum) {
		dao.insertSchedaReferendum(avvio, termine, this.id, stato, esito, nome, referendum);
	}
	
	/*public String calcolaRisutato(Scheda scheda) {
		if(scheda.getEsito().equals(Esito.MAGGIORANZA_QUALIFICATA) || scheda.getEsito().equals(Esito.REFERENDUM_CON_QUORUM)) {
			return "Calcolo non supportato !!!";
		} else if(scheda.getEsito().equals(Esito.MAGGIORANZA_SEMPLICE)) {
			if(scheda.getTipoScheda().equals(TipoScheda.CATEGORICA)) {
				if (null != scheda.getVoti()) {
					
				}
			} else if (scheda.getTipoScheda().equals(TipoScheda.CATEGORICA_CON_PREFERENZE)) {
				
			} else if (scheda.getTipoScheda().equals(TipoScheda.ORDINALE)) {
				
			} else {
				return "Errore !!!";
			}
		} else if(scheda.getEsito().equals(Esito.MAGGIORANZA_RELATIVA)) {
			if(scheda.getTipoScheda().equals(TipoScheda.CATEGORICA)) {
				
			} else if (scheda.getTipoScheda().equals(TipoScheda.CATEGORICA_CON_PREFERENZE)) {
				
			} else if (scheda.getTipoScheda().equals(TipoScheda.ORDINALE)) {
				
			} else {
				return "Errore !!!";
			}	
		} else if(scheda.getEsito().equals(Esito.MAGGIORANZA_ASSOLUTA)) {
			if(scheda.getTipoScheda().equals(TipoScheda.CATEGORICA)) {
				
			} else if (scheda.getTipoScheda().equals(TipoScheda.CATEGORICA_CON_PREFERENZE)) {
				
			} else if (scheda.getTipoScheda().equals(TipoScheda.ORDINALE)) {
				
			} else {
				return "Errore !!!";
			}
		} else if(scheda.getEsito().equals(Esito.REFERENDUM_SENZA_QUORUM)){
			if(scheda.getTipoScheda().equals(TipoScheda.REFERENDUM)) {
				
			} else {
				return "Errore !!!";
			}
		} else {
			return "Errore !!!";
		}
	}
	/* calcola esito */
	
}
