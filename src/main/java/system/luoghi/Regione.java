package system.luoghi;

import dao.RegioneDTO;

public class Regione {

	private int id;
	private String nome;

	public Regione(RegioneDTO regione) {
		this.id = regione.getId();
		this.nome = regione.getNome();
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
		return nome;
	}
	
	
}
