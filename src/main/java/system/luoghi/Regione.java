package system.luoghi;

import dao.RegioneDTO;

public class Regione {
	
	/* context Regione inv:
	 * self.id > 0 &&
	 * self.nome != null &&
	 * self.nome.lenght > 0;
	 */

	private int id;
	private String nome;

	public Regione(RegioneDTO regione) {
		this.id = regione.getId();
		this.nome = regione.getNome();
	}

	public /*@ pure @*/int getId() {
		return id;
	}

	/*@requires id != null && id > 0; @*/
	public void setId(int id) {
		this.id = id;
	}

	public /*@ pure @*/  String getNome() {
		return nome;
	}
	
	/*@requires nome != null && nome > 0; @*/
	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public /*@ pure @*/  String toString() {
		return nome;
	}
	
	
}
