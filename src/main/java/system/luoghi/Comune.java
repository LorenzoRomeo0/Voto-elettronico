package system.luoghi;

import dao.ComuneDTO;

public class Comune {

	private int id;
	private String nome;
	private Provincia provincia;

	public Comune (ComuneDTO comune, Provincia provincia) {
		this.id = comune.getId();
		this.nome = comune.getNome();
		this.provincia = provincia;
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

	public Provincia getProvincia() {
		return provincia;
	}

	public void setProvincia(Provincia provincia) {
		this.provincia = provincia;
	}

	@Override
	public String toString() {
		return nome;
	}
	
	
}