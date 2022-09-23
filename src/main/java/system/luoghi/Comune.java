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

	public /*@pure@*/ int getId() {
		return id;
	}

	/*@require id != null @*/
	public void setId(int id) {
		this.id = id;
	}

	public /*@pure@*/ String getNome() {
		return nome;
	}
	
	/*@require nome != null @*/
	public void setNome(String nome) {
		this.nome = nome;
	}

	public /*@pure@*/ Provincia getProvincia() {
		return provincia;
	}
	
	/*@require provincia != null @*/
	public void setProvincia(Provincia provincia) {
		this.provincia = provincia;
	}

	@Override
	public /*@ pure @*/ String toString() {
		return nome;
	}
	
	
}