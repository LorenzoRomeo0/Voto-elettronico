package dao;

public class ValoreSempliceDTO {
	private int id;
	private String nome;
	
	public ValoreSempliceDTO (int id, String nome) {
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
