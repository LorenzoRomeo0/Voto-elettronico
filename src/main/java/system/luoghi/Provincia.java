package system.luoghi;

import dao.ProvinciaDTO;

public class Provincia {
	
	/* context Provincia inv:
	 * self.id > 0 &&
	 * self.nome != null &&
	 * self.nome.lenght > 0;
	 */

	private int id;
	private String nome;
	private Regione regione;

	public Provincia(ProvinciaDTO provincia, Regione regione) {
		super();
		this.id = provincia.getId();
		this.nome = provincia.getNome();
		this.regione = regione;
	}

	public /*@pure@*/ int getId() {
		return id;
	}
	
	/*@requires id != null && id > 0; @*/
	public void setId(int id) {
		this.id = id;
	}

	public /*@pure@*/ String getNome() {
		return nome;
	}

	/*@requires nome != null@*/
	public void setNome(String nome) {
		this.nome = nome;
	}

	public /*@pure@*/ Regione getRegione() {
		return regione;
	}

	/*@requires regione != null@*/
	public void setRegione(Regione regione) {
		this.regione = regione;
	}

	@Override
	public /*@pure@*/ String toString() {
		return nome;
	}
	
	
}