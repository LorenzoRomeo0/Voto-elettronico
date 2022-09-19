package data;

public enum TipoVotabile {
	CANDIDATO(1), PARTITO(2);
	
	final private int id;
	private  TipoVotabile(int i) {this.id = i;}
	final public int id() {return id;}
}
