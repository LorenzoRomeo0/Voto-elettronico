package system.schede;

import java.time.LocalDate;
import java.util.ArrayList;

import dao.SchedaDTO;
import data.Esito;
import data.Stato;
import data.TipoScheda;
import system.votabili.Lista;

public class SchedaCategoricaConPreferenze extends Scheda {
	
	private ArrayList<Lista> liste;
	
	public SchedaCategoricaConPreferenze(SchedaDTO scheda, ArrayList<Lista> liste) {
		super(scheda);
		this.liste = liste;
	}

	public SchedaCategoricaConPreferenze(int id, String nome, LocalDate dataAvvio, LocalDate dataTermine, Stato stato,
			TipoScheda tipoScheda, Esito tipoRisultato, ArrayList<Lista> liste) {
		super(id, nome, dataAvvio, dataTermine, stato, tipoScheda, tipoRisultato);
		this.liste = liste;
	}

	public ArrayList<Lista> getListe() {
		return liste;
	}

	@Override
	public String toString() {
		return nome;
	}
	
	
}
