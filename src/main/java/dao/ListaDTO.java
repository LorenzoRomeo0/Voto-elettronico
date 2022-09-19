package dao;

import java.util.ArrayList;

public class ListaDTO {
	private int id;
	private String nome;
	private String tipoLista;
	private ArrayList<Integer> candidati;
	
	
	
	public ListaDTO(int id, String nome, String tipoLista, ArrayList<Integer> candidati) {
		super();
		this.id = id;
		this.nome = nome;
		this.tipoLista = tipoLista;
		this.candidati = candidati;
	}

	public int getId() {
		return id;
	}
	
	public String getNome() {
		return nome;
	}
	
	public ArrayList<Integer> getCandidati() {
		return candidati;
	}
	
	public String getTipoLista() {
		return tipoLista;
	}
	
	public void addCandidato(int candidato) {
		candidati.add(candidato);
	}
}
