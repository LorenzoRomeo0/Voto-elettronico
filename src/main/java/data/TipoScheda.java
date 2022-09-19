package data;

public enum TipoScheda {
	REFERENDUM(1), ORDINALE(2), CATEGORICA(3), CATEGORICA_CON_PREFERENZE(4);

	final private int id;
	private TipoScheda(int i) {this.id = i;}
	final public int id() {return id;}
}
