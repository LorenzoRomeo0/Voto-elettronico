package dao;

import java.time.LocalDate;

public class UtenteDTO {
	
	private int id;
	private String nome;
	private String cognome;
	private String codiceFiscale;
	private String tipo;
	private int nazionalità;
	private int residenza;
	private String sesso;
	private LocalDate dataDiNascita;

	public UtenteDTO(String codiceFiscale, String nome, String cognome, int annoNascita, int meseNascita,
			int giornoNascita, String tipo, int nazionalità, String sesso, int id, int residenza) {
		this.id = id;
		this.nome = nome;
		this.cognome = cognome;
		this.codiceFiscale = codiceFiscale;
		this.tipo = tipo;
		this.nazionalità = nazionalità;
		this.residenza = residenza;
		this.sesso = sesso;
		this.dataDiNascita = LocalDate.of(annoNascita, meseNascita, giornoNascita);
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

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public String getTipo() {
		return tipo;
	}

	public int getNazionalità() {
		return nazionalità;
	}

	public int getResidenza() {
		return residenza;
	}

	public String getSesso() {
		return sesso;
	}

	public LocalDate getDataDiNascita() {
		return dataDiNascita;
	}

}
