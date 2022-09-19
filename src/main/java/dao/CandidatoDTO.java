package dao;

public class CandidatoDTO {
	private int id;
	private String nome;
	private String cognome;
	private int sesso;
	private int partito;

	public CandidatoDTO(int id, String nome, String cognome, int sesso, int partito) {
		super();
		this.id = id;
		this.nome = nome;
		this.cognome = cognome;
		this.sesso = sesso;
		this.partito = partito;
	}

	public int getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public String getCognome() {
		return cognome;
	}

	public int getSesso() {
		return sesso;
	}

	public int getPartito() {
		return partito;
	}
	
}
