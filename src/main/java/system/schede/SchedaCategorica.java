package system.schede;

import java.util.ArrayList;
import dao.SchedaDTO;
import system.votabili.Votabile;

public class SchedaCategorica extends Scheda {
	
	/* context scheda inv:
	 * self.id > 0 &&
	 * self.nome != null &&
	 * self.nome.lenght > 0 &&
	 * self.dataAvvio != null &&
	 * self.dataTermine != null &&
	 * self.stato != null &&
	 * self.tipoScheda != null &&
	 * self.esito != null &&
	 * self.votabili != null;
	 */

	private ArrayList<Votabile> votabili;

	public SchedaCategorica(SchedaDTO scheda, ArrayList<Votabile> votabili) {
		super(scheda);
		this.votabili = votabili;
	}

	public ArrayList<Votabile> getVotabile() {
		return votabili;
	}

	@Override
	public String toString() {
		return nome;
	}
}
