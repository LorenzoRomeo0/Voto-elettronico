package dao;

public class ValoreSempliceDTO {
	private int id;
	private String nome;
	
	public ValoreSempliceDTO (int id, String nome) {
		super();
		this.id = id;
		this.nome = nome;
	}
	
	private ValoreSempliceDTO (String nome) {
		super();
		this.nome = nome;
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
	@Override
	public String toString() {
		return nome;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		if(obj instanceof String) {
			String other = (String) obj;
			return other.equals(nome);
		}
		if(obj instanceof Integer) {
			Integer other = (Integer) obj;
			return id == other.intValue();
		}
		ValoreSempliceDTO other = (ValoreSempliceDTO) obj;
		return id == other.id || nome.contentEquals(other.getNome());
	}
	
	public static ValoreSempliceDTO empty(int id) {
		ValoreSempliceDTO o = new ValoreSempliceDTO(id, null);
		return o;
	}
	
	public static ValoreSempliceDTO empty(String nome) {
		ValoreSempliceDTO o = new ValoreSempliceDTO(nome);
		return o;
	}
	
}
