package data;


public enum Stato {
	AVVIATO(2), CONCLUSO(3), DISABILITATO(1);
	
	final private int id;
	private Stato(int i) {this.id = i;}
	final public int id() {return id;}
	static final public Stato getById(int id) {
		switch(id){
			case 1 : return Stato.DISABILITATO;
			case 2 : return Stato.AVVIATO;
			case 3 : return Stato.CONCLUSO;
		}
		return null;
	}
}

