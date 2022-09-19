package system.votabili;

import java.util.ArrayList;

import dao.ListaDTO;
import data.TipoVotabile;

public class Lista extends Votabile{
	
	private TipoVotabile tipoLista;
	private ArrayList<Votabile> candidati;
	
	public Lista(ListaDTO lista, ArrayList<Votabile> candidati) {
		super(lista.getId(), lista.getNome());
		this.tipoLista = TipoVotabile.valueOf(lista.getTipoLista());
		this.candidati = candidati;
	}

	public Lista(int id, String nome, String tipoLista) {
		super(id, nome);
		this.tipoLista = TipoVotabile.valueOf(tipoLista.toUpperCase());
	}
	
	public void addCandidato(Votabile candidato) {
		candidati.add(candidato);
	}

	public TipoVotabile getTipoLista() {
		return tipoLista;
	}

	public ArrayList<Votabile> getCandidati() {
		return candidati;
	}

}
