package dao;

public class ProvinciaDTO extends RegioneDTO{

	private int regione;
	public ProvinciaDTO(int id, String nome, int regione) {
		super(id, nome);
		this.regione = regione;
	}
	
	public int getRegione() {
		return regione;
	}
	
}
