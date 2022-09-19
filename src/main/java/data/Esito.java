package data;

import java.util.ArrayList;

public enum Esito {
	MAGGIORANZA_SEMPLICE(1, TipoScheda.CATEGORICA, TipoScheda.CATEGORICA_CON_PREFERENZE, TipoScheda.ORDINALE),
	MAGGIORANZA_RELATIVA(2, TipoScheda.CATEGORICA, TipoScheda.CATEGORICA_CON_PREFERENZE, TipoScheda.ORDINALE),
	MAGGIORANZA_QUALIFICATA(3, TipoScheda.CATEGORICA, TipoScheda.CATEGORICA_CON_PREFERENZE, TipoScheda.ORDINALE),
	MAGGIORANZA_ASSOLUTA(4, TipoScheda.CATEGORICA, TipoScheda.CATEGORICA_CON_PREFERENZE, TipoScheda.ORDINALE),
	REFERENDUM_SENZA_QUORUM(5, TipoScheda.REFERENDUM), REFERENDUM_CON_QUORUM(6, TipoScheda.REFERENDUM);

	final private ArrayList<TipoScheda> schede;
	final private int id;

	private Esito(int i, TipoScheda... schede) {
		this.schede = new ArrayList<TipoScheda>();
		this.id = i;
		for (TipoScheda tipoScheda : schede) {
			this.schede.add(tipoScheda);
		}
	}

	final public int id() {
		return id;
	}

	final public boolean schedaValida(TipoScheda scheda) {
		return schede.indexOf(scheda) != -1;
	}

	final public static Esito[] getValues(TipoScheda tipo) {
		ArrayList<Esito> values = new ArrayList<Esito>();
		for (Esito esito : Esito.values()) {
			if (esito.schedaValida(tipo)) {
				values.add(esito);
			}
		}
		Esito[] arrayValues = new Esito[values.size()];
		for (int i = 0; i < values.size(); i++) {
			arrayValues[i] = values.get(i);
		}
 		return arrayValues;
	}
}
