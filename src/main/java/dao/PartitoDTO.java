package dao;

import java.sql.Blob;
import java.sql.SQLException;

public class PartitoDTO{

	private int id;
	private String nome;
	private byte[] logo;
	
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

	public int getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public byte[] getLogo() {
		return logo;
	}
	
}
