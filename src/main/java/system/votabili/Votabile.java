package system.votabili;

public abstract class Votabile {
	protected int id;
	protected String nome;
	
	public Votabile(int id, String nome) {
		super();
		this.id = id;
		this.nome = nome;
	}

	public int getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}
	
}
