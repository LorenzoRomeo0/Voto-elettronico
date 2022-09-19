package data;


public enum Stato {
	AVVIATO(2), CONCLUSO(3), DISABILITATO(1);
	
	final private int id;
	private Stato(int i) {this.id = i;}
	final public int id() {return id;}
}

