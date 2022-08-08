package system;

public class VotoReferendum implements Voto {
	private Referendum referendum;
	
	public VotoReferendum(Referendum referendum) {
		this.referendum = referendum;
	}

	@Override
	public Referendum getValue() {
		return referendum;
	}

	@Override
	public String toString() {
		return referendum.toString();
	}
	
	
	
}
