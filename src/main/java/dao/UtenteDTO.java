package dao;

import system.Comune;
import java.time.LocalDate;

public class UtenteDTO {
	
	private int id;
	private String nome;
	private String cognome;
	private String codiceFiscale;
	private String tipoUtente;
	private String nazionalità;
	private Comune residenza;
	private String sesso;
	private LocalDate dataDiNascita;

	public UtenteDTO(String codiceFiscale, String nome, String cognome, int annoNascita, int meseNascita,
			int giornoNascita, String tipoUtente, String nazionalità, String sesso, String comune, String provincia,
			String regione, int id) {
		this.id = id;
		this.nome = nome;
		this.cognome = cognome;
		this.codiceFiscale = codiceFiscale;
		this.tipoUtente = tipoUtente;
		this.nazionalità = nazionalità;
		this.residenza = new Comune(comune, provincia, regione);
		this.sesso = sesso;
		this.dataDiNascita = LocalDate.of(annoNascita, meseNascita, giornoNascita);
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

	public String getCognome() {
		return cognome;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public String getTipoUtente() {
		return tipoUtente;
	}

	public String getNazionalità() {
		return nazionalità;
	}

	public Comune getResidenza() {
		return residenza;
	}

	public String getSesso() {
		return sesso;
	}

	public LocalDate getDataDiNascita() {
		return dataDiNascita;
	}

	@Override
	public String toString() {
		return "DatiUtenteDTO [nome=" + nome + ", cognome=" + cognome + ", codiceFiscale=" + codiceFiscale
				+ ", tipoUtente=" + tipoUtente + ", nazionalità=" + nazionalità + ", residenza=" + residenza
				+ ", sesso=" + sesso + ", dataDiNascita=" + dataDiNascita + "]";
	}
}
