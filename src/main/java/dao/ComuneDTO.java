package dao;

public class ComuneDTO extends ProvinciaDTO {

	private int provincia;
	
	public ComuneDTO(int regione, int provincia, int id, String nome) {
		super(regione, nome, id);
		this.provincia = provincia;
	}

	public int getProvincia() {
		return provincia;
	}

	public void setProvincia(int provincia) {
		this.provincia = provincia;
	}
}
