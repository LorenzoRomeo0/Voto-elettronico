package system.luoghi;

import dao.NazionalitaDTO;

public class Nazionalita {
	/* context Nazionalità inv:
	 * self.id > 0 &&
	 * self.nome != null &&
	 * self.nome.lenght > 0;
	 */
	
	private int id;
	private String nome;

	/*@require id != null@*/
	public Nazionalita(NazionalitaDTO nazione) {
		this.id = nazione.getId();
		this.nome = nazione.getNome();
	}

	public /*@pure@*/ int getId() {
		return id;
	}

	/*@require id != null@*/
	public void setId(int id) {
		this.id = id;
	}

	public /*@pure@*/ String getNome() {
		return nome;
	}
	
	/*@require id != null@*/
	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public /*@pure@*/ String toString() {
		return  nome;
	}
	
	
}
