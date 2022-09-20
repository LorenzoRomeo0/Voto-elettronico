package system.schede;

import java.time.LocalDate;
import java.util.ArrayList;

import dao.SchedaDTO;
import data.Stato;
import data.Esito;
import data.TipoScheda;
import system.voto.Voto;

public abstract class Scheda {

	protected int id;
	protected String nome;
	protected LocalDate dataAvvio;
	protected LocalDate dataTermine;

	protected Stato stato;
	protected TipoScheda tipoScheda;
	protected Esito esito;

	protected ArrayList<Voto> voti;

	public Scheda(SchedaDTO scheda) {
		this.id = scheda.getId();
		this.nome = scheda.getNome();
		this.dataAvvio = scheda.getDataAvvio();
		this.dataTermine = scheda.getDataTermine();
		this.stato = Stato.valueOf(scheda.getStato());
		this.tipoScheda = TipoScheda.valueOf(scheda.getTipoScheda());
		this.esito = Esito.valueOf(scheda.getEsito());
	}

	public Scheda(int id, String nome, LocalDate dataAvvio, LocalDate dataTermine, Stato stato, TipoScheda tipoScheda,
			Esito tipoRisultato) {
		super();
		this.id = id;
		this.nome = nome;
		this.dataAvvio = dataAvvio;
		this.dataTermine = dataTermine;
		this.stato = stato;
		this.tipoScheda = tipoScheda;
		this.esito = tipoRisultato;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		if (obj instanceof String) {
			String other = (String) obj;
			return other.equals(nome);
		}
		Scheda other = (Scheda) obj;
		return id == other.id || nome == other.nome;
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

	public Stato getStato() {
		return stato;
	}

	public TipoScheda getTipoScheda() {
		return tipoScheda;
	}

	public Esito getEsito() {
		return esito;
	}

	@Override
	public String toString() {
		return "Scheda [id=" + id + ", nome=" + nome + ", dataAvvio=" + dataAvvio + ", dataTermine=" + dataTermine
				+ ", stato=" + stato + ", tipoScheda=" + tipoScheda + ", esito=" + esito + "]";
	}
	
	public ArrayList<Voto> getVoti() {
		return voti;
	}

	public void setVoti(ArrayList<Voto> voti) {
		this.voti = voti;
	}

}
