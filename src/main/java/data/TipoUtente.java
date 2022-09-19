package data;

public enum TipoUtente {
	IMPIEGATO(1), ELETTORE(2);
	
	final private int id;
	private  TipoUtente(int i) {this.id = i;}
	final public int id() {return id;}
}
