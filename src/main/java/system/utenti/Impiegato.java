package system.utenti;

import java.time.LocalDate;
import java.util.ArrayList;
import dao.UtenteDTO;
import data.Esito;
import data.Referendum;
import data.Stato;
import data.TipoScheda;
import data.TipoVotabile;
import system.luoghi.Comune;
import system.schede.Scheda;
import system.schede.SchedaCategorica;
import system.schede.SchedaCategoricaConPreferenze;
import system.schede.SchedaOrdinale;
import system.schede.SchedaReferendum;
import system.votabili.Lista;
import system.votabili.Votabile;
import system.voto.Voto;
import system.voto.VotoCategorico;
import system.voto.VotoCategoricoConPreferenza;
import system.voto.VotoOrdinale;
import system.voto.VotoReferendum;

public class Impiegato extends Utente {

	public Impiegato(int id, String nome, String cognome, LocalDate dataDinascita, Comune comune, String nazionalita,
			String codiceFiscale, String tipo) {
		super(id, nome, cognome, dataDinascita, comune, nazionalita, codiceFiscale, tipo);
	}

	public Impiegato(UtenteDTO utente, Comune residenza) {
		super(utente, residenza);
	}

	public void creaSchedaCategorica(LocalDate avvio, LocalDate termine, Stato stato, Esito esito, TipoVotabile tipo,
			String nome, ArrayList<Votabile> aspiranti) {
		dao.insertSchedaCategorica(avvio, termine, this.id, stato, esito, tipo, nome, aspiranti);
	}

	public void creaSchedaCategoricaConPreferenza(LocalDate avvio, LocalDate termine, Stato stato, Esito esito,
			String nome, ArrayList<Votabile> aspiranti) {
		dao.insertSchedaCategoricaPreferenza(avvio, termine, this.id, stato, esito, nome, aspiranti);
	}

	public void creaSchedaOrdinale(LocalDate avvio, LocalDate termine, Stato stato, Esito esito, TipoVotabile tipo,
			String nome, ArrayList<Votabile> partecipanti) {
		dao.insertSchedaOrdinale(avvio, termine, this.id, stato, esito, tipo, nome, partecipanti);
	}

	public void creaSchedaReferendum(LocalDate avvio, LocalDate termine, Stato stato, Esito esito, String nome,
			String referendum) {
		dao.insertSchedaReferendum(avvio, termine, this.id, stato, esito, nome, referendum);
	}

	public String calcolaRisutato(Scheda scheda) {
		if (scheda.getTipoScheda().equals(TipoScheda.CATEGORICA)) {
			if (null != scheda.getVoti()) {
				SchedaCategorica sc = (SchedaCategorica) scheda;
				scheda.setVoti(datiVotiCategorici(sc, dao.getVotiCategorici(scheda)));
			}
		} else if (scheda.getTipoScheda().equals(TipoScheda.CATEGORICA_CON_PREFERENZE)) {
			if (null != scheda.getVoti()) {
				SchedaCategoricaConPreferenze scc = (SchedaCategoricaConPreferenze) scheda;
				scheda.setVoti(datiVotiCategoriciConPreferenze(scc, dao.getVotiCategoriciConPreferenze(scheda)));
			}
		} else if (scheda.getTipoScheda().equals(TipoScheda.ORDINALE)) {
			if (null != scheda.getVoti()) {
				SchedaOrdinale scc = (SchedaOrdinale) scheda;
				scheda.setVoti(datiVotiOrdinali(scc, dao.getVotiOrdinale(scheda)));
			}
		} else if (scheda.getTipoScheda().equals(TipoScheda.REFERENDUM)) {
			if (null != scheda.getVoti()) {
				SchedaReferendum sr = (SchedaReferendum) scheda;
				scheda.setVoti(datiVotiReferendum(sr, dao.getVotiReferendum(scheda)));
			}
		} else {
			return "Errore !!!";
		}

		if (scheda.getEsito().equals(Esito.MAGGIORANZA_QUALIFICATA)
				|| scheda.getEsito().equals(Esito.REFERENDUM_CON_QUORUM)) {
			return "Calcolo non supportato !!!";
		} else if (scheda.getEsito().equals(Esito.MAGGIORANZA_SEMPLICE)) {
			if (scheda.getTipoScheda().equals(TipoScheda.CATEGORICA)) {

			} else if (scheda.getTipoScheda().equals(TipoScheda.CATEGORICA_CON_PREFERENZE)) {

			} else if (scheda.getTipoScheda().equals(TipoScheda.ORDINALE)) {

			} else {
				return "Errore !!!";
			}
		} else if (scheda.getEsito().equals(Esito.MAGGIORANZA_RELATIVA)) {
			if (scheda.getTipoScheda().equals(TipoScheda.CATEGORICA)) {

			} else if (scheda.getTipoScheda().equals(TipoScheda.CATEGORICA_CON_PREFERENZE)) {

			} else if (scheda.getTipoScheda().equals(TipoScheda.ORDINALE)) {

			} else {
				return "Errore !!!";
			}
		} else if (scheda.getEsito().equals(Esito.MAGGIORANZA_ASSOLUTA)) {
			if (scheda.getTipoScheda().equals(TipoScheda.CATEGORICA)) {

			} else if (scheda.getTipoScheda().equals(TipoScheda.CATEGORICA_CON_PREFERENZE)) {

			} else if (scheda.getTipoScheda().equals(TipoScheda.ORDINALE)) {

			} else {
				return "Errore !!!";
			}
		} else if (scheda.getEsito().equals(Esito.REFERENDUM_SENZA_QUORUM)) {
			if (scheda.getTipoScheda().equals(TipoScheda.REFERENDUM)) {

			} else {
				return "Errore !!!";
			}
		} else {
			return "Errore !!!";
		}
	}

	private ArrayList<Voto> datiVotiCategorici(SchedaCategorica scheda, ArrayList<Integer> voti) {
		ArrayList<Voto> datiVoti = new ArrayList<Voto>();
		for (Integer voto : voti) {
			for (Votabile a : scheda.getVotabile()) {
				if (voto == a.getId()) {
					datiVoti.add(new VotoCategorico(scheda, a));
					break;
				}
			}
		}
		return datiVoti;
	}

	private ArrayList<Voto> datiVotiCategoriciConPreferenze(SchedaCategoricaConPreferenze scheda,
			ArrayList<Integer[]> voti) {
		ArrayList<Voto> datiVoti = new ArrayList<Voto>();
		for (Integer[] voto : voti) {
			ArrayList<Votabile> selezionati = new ArrayList<Votabile>();
			for (Lista l : scheda.getListe()) {
				if (l.getId() == voto[0]) {
					for (Votabile a : l.getCandidati()) {
						if (a.getId() == voto[1]) {
							selezionati.add(a);
							break;
						}
					}
				}
				datiVoti.add(new VotoCategoricoConPreferenza(scheda, l, selezionati));
				break;
			}
		}
		return datiVoti;
	}

	private ArrayList<Voto> datiVotiOrdinali(SchedaOrdinale scheda, ArrayList<Integer[]> voti) {
		ArrayList<Voto> datiVoti = new ArrayList<Voto>();
		for (Integer[] voto : voti) {
			for (Votabile a : scheda.getVotabile()) {
				if (voto[0] == a.getId()) {
					datiVoti.add(new VotoOrdinale(scheda, a, voto[1]));
					break;
				}
			}
		}
		return datiVoti;
	}

	private ArrayList<Voto> datiVotiReferendum(SchedaReferendum scheda, ArrayList<Integer> voti) {
		ArrayList<Voto> datiVoti = new ArrayList<Voto>();
		for (Integer voto : voti) {
			Referendum r = null;
			if (voto == 0) {
				r = Referendum.FAVOREVOLE;
			} else if (voto == 1) {
				r = Referendum.NON_FAVOREVOLE;
			} else {
				r = Referendum.NULLO;
			}
			datiVoti.add(new VotoReferendum(r, scheda));
		}
		return datiVoti;
	}
}
