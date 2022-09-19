package dao;

import java.time.LocalDate;

public class SchedaDTO {
	
	private int id;
	private String nome;
	private LocalDate dataAvvio;
	private LocalDate dataTermine;
	
	private String stato;
	private String esito;
	private String tipo;
	
	/* private arrayList Voto voti; */

	public SchedaDTO(int id, String nome, LocalDate dataAvvio, LocalDate dataTermine,
			String stato, String esito, String tipo) {
		super();
		this.stato = stato;
		this.esito = esito;
		this.id = id;
		this.dataAvvio = dataAvvio;
		this.dataTermine = dataTermine;
		this.nome = nome;
		this.tipo = tipo;
	}

	public int getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public LocalDate getDataAvvio() {
		return dataAvvio;
	}

	public LocalDate getDataTermine() {
		return dataTermine;
	}

	public String getStato() {
		return stato;
	}

	public String getTipoScheda() {
		return tipo;
	}

	public String getEsito() {
		return esito;
	}

	@Override
	public String toString() {
		return "SchedaDTO [id=" + id + ", nome=" + nome + ", dataAvvio=" + dataAvvio + ", dataTermine=" + dataTermine
				+ ", stato=" + stato + ", esito=" + esito + ", tipoScheda=" + tipo + "]";
	}
	
}
