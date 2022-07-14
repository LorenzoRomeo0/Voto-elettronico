package dao;

import java.util.Objects;

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
	public void setId(int id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	@Override
	public String toString() {
		return nome;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		ValoreSempliceDTO other = (ValoreSempliceDTO) obj;
		return id == other.id;
	}	
}
