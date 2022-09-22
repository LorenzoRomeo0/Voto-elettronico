package system.utenti;

import java.time.LocalDate;
import java.util.ArrayList;
import dao.SistemaVotazioniDAO;
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
		caricaVoti(scheda);
		if (scheda.getEsito().equals(Esito.MAGGIORANZA_QUALIFICATA)
				|| scheda.getEsito().equals(Esito.REFERENDUM_CON_QUORUM)) {
			return "Calcolo non supportato !!!";
		} else if (scheda.getEsito().equals(Esito.MAGGIORANZA_SEMPLICE)) {
			if (scheda.getTipoScheda().equals(TipoScheda.CATEGORICA)) {
				SchedaCategorica sc = (SchedaCategorica) scheda;
				int[] voti = calcolaSC(sc);
				int max = maxValue(voti);
				if (voti[max] > scheda.getVoti().size() / 2) {
					return "Il vincitore è " + sc.getVotabile().get(max) + ".";
				} else {
					return "Non ci sono Vincitori";
				}
			} else if (scheda.getTipoScheda().equals(TipoScheda.CATEGORICA_CON_PREFERENZE)) {
				SchedaCategoricaConPreferenze scc = (SchedaCategoricaConPreferenze) scheda;
				int[] voti = calcolaSCCListe(scc);
				int max = maxValue(voti);
				if (voti[max] > scheda.getVoti().size() / 2) {
					Lista listaVincitrice = scc.getListe().get(max);
					int[] votiCandidati = calcolaSCCVotabili(scc, listaVincitrice);
					int maxCandidati = maxValue(votiCandidati);
					return "La lista vincitrice è " + listaVincitrice.getNome() + ". Il candidato con più voti è "
							+ listaVincitrice.getCandidati().get(maxCandidati) + ".";
				} else {
					return "Non ci sono Vincitori";
				}
			} else if (scheda.getTipoScheda().equals(TipoScheda.ORDINALE)) {
				SchedaOrdinale so = (SchedaOrdinale) scheda;
				double[] voti = calcolaSO(so);
				int max = maxValue(voti);
				if (voti[max] > contaVotiOrdinali(so)
				) {
					return "Il vincitore è " + so.getVotabile().get(max) + ".";
				} else {
					return "Non ci sono Vincitori";
				}
			} else {
				return "Errore !!!";
			}
		} else if (scheda.getEsito().equals(Esito.MAGGIORANZA_RELATIVA)) {
			if (scheda.getTipoScheda().equals(TipoScheda.CATEGORICA)) {
				SchedaCategorica sc = (SchedaCategorica) scheda;
				int[] voti = calcolaSC(sc);
				int max = maxValue(voti);
				if (voti[max] > scheda.getVoti().size() - voti[max]) {
					return "Il vincitore è " + sc.getVotabile().get(max) + ".";
				} else {
					return "Non ci sono Vincitori";
				}
			} else if (scheda.getTipoScheda().equals(TipoScheda.CATEGORICA_CON_PREFERENZE)) {
				SchedaCategoricaConPreferenze scc = (SchedaCategoricaConPreferenze) scheda;
				int[] voti = calcolaSCCListe(scc);
				int max = maxValue(voti);
				if (voti[max] > scheda.getVoti().size() - voti[max]) {
					Lista listaVincitrice = scc.getListe().get(max);
					int[] votiCandidati = calcolaSCCVotabili(scc, listaVincitrice);
					int maxCandidati = maxValue(votiCandidati);
					return "La lista vincitrice è " + listaVincitrice.getNome() + ". Il candidato con più voti è "
							+ listaVincitrice.getCandidati().get(maxCandidati) + ".";
				} else {
					return "Non ci sono Vincitori";
				}
			} else if (scheda.getTipoScheda().equals(TipoScheda.ORDINALE)) {
				SchedaOrdinale so = (SchedaOrdinale) scheda;
				double[] voti = calcolaSO(so);
				int max = maxValue(voti);
				double sommAltri = 0;
				for (int i = 0; i < voti.length; i++) {
					if (i != max) {
						sommAltri += voti[i];
					}
				}
				if (voti[max] > sommAltri) {
					return "Il vincitore è " + so.getVotabile().get(max) + ".";
				} else {
					return "Non ci sono Vincitori";
				}
			} else {
				return "Errore !!!";
			}
		} else if (scheda.getEsito().equals(Esito.MAGGIORANZA_ASSOLUTA)) {
			int userN = SistemaVotazioniDAO.getInstance().contaUtenti();
			if (scheda.getTipoScheda().equals(TipoScheda.CATEGORICA)) {
				SchedaCategorica sc = (SchedaCategorica) scheda;
				int[] voti = calcolaSC(sc);
				int max = maxValue(voti);
				if (voti[max] > (userN / 2) + 1) {
					return "Il vincitore è " + sc.getVotabile().get(max) + ".";
				} else {
					return "Non ci sono Vincitori";
				}
			} else if (scheda.getTipoScheda().equals(TipoScheda.CATEGORICA_CON_PREFERENZE)) {
				SchedaCategoricaConPreferenze scc = (SchedaCategoricaConPreferenze) scheda;
				int[] voti = calcolaSCCListe(scc);
				int max = maxValue(voti);
				if (voti[max] > (userN / 2) + 1) {
					Lista listaVincitrice = scc.getListe().get(max);
					int[] votiCandidati = calcolaSCCVotabili(scc, listaVincitrice);
					int maxCandidati = maxValue(votiCandidati);
					return "La lista vincitrice è " + listaVincitrice.getNome() + ". Il candidato con più voti è "
							+ listaVincitrice.getCandidati().get(maxCandidati) + ".";
				} else {
					return "Non ci sono Vincitori";
				}
			} else if (scheda.getTipoScheda().equals(TipoScheda.ORDINALE)) {
				SchedaOrdinale so = (SchedaOrdinale) scheda;
				double[] voti = calcolaSO(so);
				int max = maxValue(voti);
				if (voti[max] > (userN / 2) + 1) {
					return "Il vincitore è " + so.getVotabile().get(max) + ".";
				} else {
					return "Non ci sono Vincitori";
				}
			} else {
				return "Errore !!!";
			}
		} else if (scheda.getEsito().equals(Esito.REFERENDUM_SENZA_QUORUM)) {
			if (scheda.getTipoScheda().equals(TipoScheda.REFERENDUM)) {
				int fav = 0;
				int nfav = 0;
				for (Voto v : scheda.getVoti()) {
					VotoReferendum vr = (VotoReferendum) v;
					if (vr.getValue().equals(Referendum.FAVOREVOLE)) {
						fav++;
					} else if (vr.getValue().equals(Referendum.NON_FAVOREVOLE)) {
						nfav++;
					}
				}
				if (fav > nfav) {
					return "Ha vinto favorevole!";
				} else if (fav == nfav) {
					return "è stato un pareggio.";
				} else {
					return "Ha vinto non favorevole!";
				}
			} else {
				return "Errore !!!";
			}
		} else {
			return "Errore !!!";
		}
	}

	private void caricaVoti(Scheda scheda) {
		if (scheda.getTipoScheda().equals(TipoScheda.CATEGORICA)) {
			if (null == scheda.getVoti()) {
				SchedaCategorica sc = (SchedaCategorica) scheda;
				scheda.setVoti(datiVotiCategorici(sc, dao.getVotiCategorici(scheda)));
			}
		} else if (scheda.getTipoScheda().equals(TipoScheda.CATEGORICA_CON_PREFERENZE)) {
			if (null == scheda.getVoti()) {
				SchedaCategoricaConPreferenze scc = (SchedaCategoricaConPreferenze) scheda;
				scheda.setVoti(datiVotiCategoriciConPreferenze(scc, dao.getVotiCategoriciConPreferenze(scheda)));
			}
		} else if (scheda.getTipoScheda().equals(TipoScheda.ORDINALE)) {
			if (null == scheda.getVoti()) {
				SchedaOrdinale scc = (SchedaOrdinale) scheda;
				scheda.setVoti(datiVotiOrdinali(scc, dao.getVotiOrdinale(scheda)));
			}
		} else if (scheda.getTipoScheda().equals(TipoScheda.REFERENDUM)) {
			System.out.println("inside referendum --");
			if (null == scheda.getVoti()) {
				SchedaReferendum sr = (SchedaReferendum) scheda;
				scheda.setVoti(datiVotiReferendum(sr, dao.getVotiReferendum(scheda)));
			}
		}
	}

	private int[] calcolaSC(SchedaCategorica sc) {
		int[] calcolo = new int[sc.getVotabile().size()];
		for (Voto v : sc.getVoti()) {
			VotoCategorico vc = (VotoCategorico) v;
			int idVoto = vc.getVoto().getId();
			for (int i = 0; i < sc.getVotabile().size(); i++) {
				int idVotabile = sc.getVotabile().get(i).getId();
				if (idVoto == idVotabile) {
					calcolo[i]++;
					break;
				}
			}
		}
		return calcolo;
	}

	private int[] calcolaSCCListe(SchedaCategoricaConPreferenze scc) {
		int[] calcolo = new int[scc.getListe().size()];
		for (Voto v : scc.getVoti()) {
			VotoCategoricoConPreferenza vc = (VotoCategoricoConPreferenza) v;
			int idListaVoto = vc.getLista().getId();
			for (int i = 0; i < scc.getListe().size(); i++) {
				int idListaScheda = scc.getListe().get(i).getId();
				if (idListaVoto == idListaScheda) {
					calcolo[i]++;
					break;
				}
			}
		}
		return calcolo;
	}

	private int[] calcolaSCCVotabili(SchedaCategoricaConPreferenze scc, Lista l) {
		ArrayList<Votabile> votabili = l.getCandidati();
		int[] calcolo = new int[votabili.size()];
		for (Voto v : scc.getVoti()) {
			VotoCategoricoConPreferenza vc = (VotoCategoricoConPreferenza) v;
			int idListaVoto = vc.getLista().getId();
			if (idListaVoto == l.getId()) {
				ArrayList<Votabile> votabiliVoto = vc.getVoto();
				for (Votabile vt : votabiliVoto) {
					for (int i = 0; i < votabili.size(); i++) {
						int idVotabile = votabili.get(i).getId();
						if (vt.getId() == idVotabile) {
							calcolo[i]++;
							break;
						}
					}
				}
			}
		}
		return calcolo;
	}

	private double[] calcolaSO(SchedaOrdinale so) {
		double[] calcolo = new double[so.getVotabile().size()];
		for (Voto v : so.getVoti()) {
			VotoOrdinale vc = (VotoOrdinale) v;
			int idVoto = vc.getVoto().getId();
			for (int i = 0; i < so.getVotabile().size(); i++) {
				int idVotabile = so.getVotabile().get(i).getId();
				if (idVoto == idVotabile) {
					calcolo[i] += (1 / (vc.getPosizione()+1));
					break;
				}
			}
		}
		return calcolo;
	}

	private int maxValue(int[] array) {
		int maxValue = array[0];
		int maxindex = 0;
		for (int i = 1; i < array.length; i++) {
			if (maxValue < array[i]) {
				maxindex = i;
				maxValue = array[i];
			}
		}
		return maxindex;
	}

	private int maxValue(double[] array) {
		double maxValue = array[0];
		int maxindex = 0;
		for (int i = 1; i < array.length; i++) {
			if (maxValue < array[i]) {
				maxindex = i;
				maxValue = array[i];
			}
		}
		return maxindex;
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

	private int contaVotiOrdinali(SchedaOrdinale scheda) {
		int votiN = 0;
		for (Voto voto : scheda.getVoti()) {
			VotoOrdinale vo = (VotoOrdinale) voto;
			if (vo.getPosizione() == null || vo.getPosizione() == 1) {
				votiN++;
			}
		}
		return votiN;
	}
}
