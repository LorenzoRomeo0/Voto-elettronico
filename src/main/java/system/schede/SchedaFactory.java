package system.schede;
import java.util.ArrayList;

import dao.SchedaDTO;
import system.votabili.Lista;
import system.votabili.Votabile;

public class SchedaFactory {

	public static SchedaReferendum makeSchedaReferendum(SchedaDTO scheda, String referendum) {
		return new SchedaReferendum(scheda, referendum);
	}

	public static SchedaOrdinale makeSchedaOrdinale(SchedaDTO scheda, ArrayList<Votabile> votabili) {
		return new SchedaOrdinale(scheda, votabili);
	}
	
	public static  SchedaCategorica  makeSchedaCategorico(SchedaDTO schede, ArrayList<Votabile> votabile) {
		return new SchedaCategorica(schede, votabile);
	}
	
	public static SchedaCategoricaConPreferenze  makeSchedaCategoricoPreferenze(SchedaDTO schede, ArrayList<Lista> candidati) {
		return new SchedaCategoricaConPreferenze(schede, candidati);
	}
}
