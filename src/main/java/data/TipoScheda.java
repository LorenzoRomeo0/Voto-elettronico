package data;

public enum TipoScheda {
	REFERENDUM(4), ORDINALE(3), CATEGORICA(1), CATEGORICA_CON_PREFERENZE(2);

	final private int id;
	private TipoScheda(int i) {this.id = i;}
	final public int id() {return id;}
}
