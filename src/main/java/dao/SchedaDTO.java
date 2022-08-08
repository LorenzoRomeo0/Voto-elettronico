package dao;

public class SchedaDTO extends ValoreSempliceDTO{
	
	private String tipo_scheda;
	
	public SchedaDTO (int id, String nome, String tipo_scheda) {
		super(id, nome);
		this.tipo_scheda = tipo_scheda;
	}
	
	@Override
	public String toString() {
		return super.getNome();
	}

	public String getTipoScheda() {
		return tipo_scheda;
	}

	public void setTipoScheda(String tipo_scheda) {
		this.tipo_scheda = tipo_scheda;
	}
}
