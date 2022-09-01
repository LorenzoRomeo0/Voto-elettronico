package dao;

import java.sql.Blob;
import java.sql.SQLException;

public class PartitoDTO implements Votabile{

	private int id;
	private String nome;
	private byte[] logo;

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

	public byte[] getLogo() {
		return logo;
	}

	public void setLogo(byte[] logo) {
		this.logo = logo;
	}

	public PartitoDTO(int id, String nome, Blob logo) {
		super();
		this.id = id;
		this.nome = nome;
		try {
			this.logo = logo.getBytes(1, (int) logo.length());
		} catch (SQLException e) {
			System.out.println("---! caricamento logo fallito.");
		}
	}

	@Override
	public String toString() {
		return nome;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PartitoDTO other = (PartitoDTO) obj;
		return id == other.id;
	}

}
