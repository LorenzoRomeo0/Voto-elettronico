package dao;

import java.util.Objects;

public class CandidatoDTO implements Votabile{
	private int id;
	private String nome;
	private String cognome;
	private int genere;
	private int partito;

	public CandidatoDTO(int id, String nome, String cognome, int genere, int partito) {
		super();
		this.id = id;
		this.nome = nome;
		this.cognome = cognome;
		this.genere = genere;
		this.partito = partito;
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

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public int getGenere() {
		return genere;
	}

	public void setGenere(int genere) {
		this.genere = genere;
	}

	public int getPartito() {
		return partito;
	}

	public void setPartito(int partito) {
		this.partito = partito;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CandidatoDTO other = (CandidatoDTO) obj;
		return id == other.id;
	}

}
