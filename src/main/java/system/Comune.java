package system;

public class Comune {

	private String comune;
	private String provincia;
	private String regione;

	public Comune(String comune, String provincia, String regione) {
		super();
		this.comune = comune;
		this.provincia = provincia;
		this.regione = regione;
	}

	public String getComune() {
		return comune;
	}

	public String getProvincia() {
		return provincia;
	}

	public String getRegione() {
		return regione;
	}

	@Override
	public String toString() {
		return "Comune [comune=" + comune + ", provincia=" + provincia + ", regione=" + regione + "]";
	}
}
