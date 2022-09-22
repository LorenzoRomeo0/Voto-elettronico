package system.utenti;

import java.time.LocalDate;
import java.util.ArrayList;

import dao.UtenteDTO;
import system.luoghi.Comune;
import system.schede.Scheda;
import system.voto.Voto;
import system.voto.VotoCategorico;
import system.voto.VotoCategoricoConPreferenza;
import system.voto.VotoOrdinale;
import system.voto.VotoReferendum;

public class Elettore extends Utente {
	
	private Libretto libretto;

	public Elettore(int id, String nome, String cognome, LocalDate dataDinascita, Comune comune, String nazionalita,
			String codiceFiscale, String tipo, Libretto libretto) {
		super(id, nome, cognome, dataDinascita, comune, nazionalita, codiceFiscale, tipo);
		this.libretto = libretto;
	}
	
	public Elettore(UtenteDTO utente, Comune residenza, Libretto libretto) {
		super(utente, residenza);
		this.libretto = libretto;
	}
	
	@Override
	public String toString() {
		return "Elettore [nome=" + nome + ", cognome=" + cognome + ", dataDinascita=" + dataDinascita + ", residenza="
				+ residenza + ", nazionalita=" + nazionalita + ", codiceFiscale=" + codiceFiscale + ", tipo=" + tipo
				+ "]";
	}
	
	public Libretto getLibretto() {
		return libretto;
	}

	public void vota(Voto voto) {
		if (voto instanceof VotoReferendum) {
			VotoReferendum votoReferendum = (VotoReferendum) voto;
			dao.insertVotoReferendum(votoReferendum.getSchedaId(), votoReferendum.getValue());
			libretto.addScheda(voto.getScheda());
		} else if(voto instanceof VotoCategorico) {
			VotoCategorico votoCategorico = (VotoCategorico) voto;
			dao.insertVotoCategorico(voto.getSchedaId(), votoCategorico.getVoto());
			libretto.addScheda(voto.getScheda());
		} else if (voto instanceof VotoCategoricoConPreferenza) {
			VotoCategoricoConPreferenza vccp = (VotoCategoricoConPreferenza) voto;
			dao.insertVotaSchedaCategoricaConPreferenze(vccp.getSchedaId(), vccp.getLista().getId(), vccp.getVoto());
			libretto.addScheda(voto.getScheda());
		} 
		dao.insertSchedeCompilate(id, voto.getSchedaId());
	}

	public void vota(Scheda scheda, ArrayList<VotoOrdinale> voto) {
		dao.insertVotoOrdinale(scheda.getId(), voto);
		libretto.addScheda(scheda);
	} 
	
}
