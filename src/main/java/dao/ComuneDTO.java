package dao;

public class ComuneDTO extends ProvinciaDTO {

	private int provincia;
	
	public ComuneDTO(int regione, int provincia, int id, String nome) {
		super(id, nome, regione);
		this.provincia = provincia;
	}

	public int getProvincia() {
		return provincia;
	}

}
