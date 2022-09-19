package system.voto;

import data.Referendum;
import system.schede.Scheda;

public class VotoReferendum extends Voto {

	private Referendum referendum;

	public VotoReferendum(Referendum referendum, Scheda scheda) {
		super(scheda);
		this.referendum = referendum;

	}

	public Referendum getValue() {
		return referendum;
	}

	@Override
	public String toString() {
		return referendum.toString();
	}
	
}
