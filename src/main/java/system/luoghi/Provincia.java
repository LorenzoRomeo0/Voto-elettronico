package system.luoghi;

import dao.ProvinciaDTO;

public class Provincia {

	private int id;
	private String nome;
	private Regione regione;

	public Provincia(ProvinciaDTO provincia, Regione regione) {
		super();
		this.id = provincia.getId();
		this.nome = provincia.getNome();
		this.regione = regione;
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

	public Regione getRegione() {
		return regione;
	}

	public void setRegione(Regione regione) {
		this.regione = regione;
	}

	@Override
	public String toString() {
		return nome;
	}
	
	
}