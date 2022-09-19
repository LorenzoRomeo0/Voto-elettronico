package system.luoghi;

import dao.NazionalitaDTO;

public class Nazionalita {
	private int id;
	private String nome;

	public Nazionalita(NazionalitaDTO nazione) {
		this.id = nazione.getId();
		this.nome = nazione.getNome();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public String toString() {
		return  nome;
	}
	
	
}
