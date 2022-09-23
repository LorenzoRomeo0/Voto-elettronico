package system.utenti;

import java.time.LocalDate;
import java.util.ArrayList;

import dao.SistemaVotazioniDAO;
import dao.UtenteDTO;
import data.TipoUtente;
import system.luoghi.Comune;
import system.schede.Scheda;

public abstract class Utente {
	
	/* context scheda inv:
	 * self.id > 0 &&
	 * self.nome != null &&
	 * self.nome.lenght > 0 &&
	 * self.cognome != null &&
	 * self.cognome.lenght > 0 &&
	 * self.dataDiNascita != null &&
	 * self.nazionalita != null &&
	 * self.nazionalita.lenght > 0 &&
	 * self.codiceFiscale != null &&
	 * self.codiceFiscale.lenght = 16 &&
	 * self.tipo != null;
	 */
	
	/*@invariant self.id > 0 && self.nome != null && self.nome.lenght > 0 && self.cognome != null && self.cognome.lenght > 0 && self.dataDiNascita != null && self.nazionalita != null && self.nazionalita.lenght > 0 && self.codiceFiscale != null && self.codiceFiscale.lenght = 16 && self.tipo != null @*/
	
	protected int id;
	protected String nome;
	protected String cognome;
	protected LocalDate dataDinascita;
	protected Comune residenza;
	protected String nazionalita;
	protected String codiceFiscale;
	protected TipoUtente tipo;
	
	protected SistemaVotazioniDAO dao;
	
	protected Utente(int id, String nome, String cognome, LocalDate dataDinascita, Comune comune, String nazionalita,
			String codiceFiscale, String tipo) {
		super();
		this.id = id;
		this.nome = nome;
		this.cognome = cognome;
		this.dataDinascita = dataDinascita;
		this.residenza = comune;
		this.nazionalita = nazionalita;
		this.codiceFiscale = codiceFiscale;
		this.tipo = TipoUtente.valueOf(tipo);
		this.dao = SistemaVotazioniDAO.getInstance();
	}
	
	protected Utente(UtenteDTO utente, Comune residenza) {
		super();
		this.id = utente.getId();
		this.nome = utente.getNome();
		this.cognome = utente.getCognome();
		this.dataDinascita = utente.getDataDiNascita();
		this.residenza = residenza;
		//this.nazionalita = utente.getNazionalità();
		this.codiceFiscale = utente.getCodiceFiscale();
		this.tipo = TipoUtente.valueOf(utente.getTipo().toUpperCase());
		this.dao = SistemaVotazioniDAO.getInstance();
	}
	
	@Override
	public String toString() {
		return "Utente [id=" + id + ", nome=" + nome + ", cognome=" + cognome + ", dataDinascita=" + dataDinascita
				+ ", residenza=" + residenza + ", nazionalita=" + nazionalita + ", codiceFiscale=" + codiceFiscale
				+ ", tipo=" + tipo + "]";
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

	public LocalDate getDataDinascita() {
		return dataDinascita;
	}

	public Comune getResidenza() {
		return residenza;
	}

	public String getNazionalita() {
		return nazionalita;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public TipoUtente getTipo() {
		return tipo;
	}
	
	public void setTipo(TipoUtente tipo) {
		this.tipo = tipo;
	}

	public static Impiegato makeUtente(UtenteDTO utente, Comune residenza) {
		return new Impiegato(utente, residenza); 
	}
	
	public static Elettore makeUtente(UtenteDTO utente, Comune residenza, ArrayList<Scheda> compilate) {
		return new Elettore(utente, residenza, new Libretto(compilate));
	}
}
