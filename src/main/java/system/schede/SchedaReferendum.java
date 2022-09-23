package system.schede;

import java.time.LocalDate;

import dao.SchedaDTO;
import data.Esito;
import data.Stato;
import data.TipoScheda;

public class SchedaReferendum extends Scheda {
	
	/* context scheda inv:
	 * self.id > 0 &&
	 * self.nome != null &&
	 * self.nome.lenght > 0 &&
	 * self.dataAvvio != null &&
	 * self.dataTermine != null &&
	 * self.stato != null &&
	 * self.tipoScheda != null &&
	 * self.esito != null &&
	 * self.referendum != null &&
	 * self.referendum.lenght > 0;
	 */
	
	private String referendum;
	
	public SchedaReferendum(SchedaDTO scheda, String referendum) {
		super(scheda);
		this.referendum = referendum;
	}

	public SchedaReferendum(int id, String nome, LocalDate dataAvvio, LocalDate dataTermine, Stato stato,
			TipoScheda tipoScheda, Esito tipoRisultato, String referendum) {
		super(id, nome, dataAvvio, dataTermine, stato, tipoScheda, tipoRisultato);
		this.referendum = referendum;
	}

	public String getReferendum() {
		return referendum;
	}

	@Override
	public String toString() {
		return nome;
	}
	
}
