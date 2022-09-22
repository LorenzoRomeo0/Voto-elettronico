package system.votabili;

import java.util.Arrays;
import dao.PartitoDTO;

public class Partito extends Votabile {
	
	private byte[] logo;

	public Partito(PartitoDTO partito) {
		super(partito.getId(), partito.getNome());
		logo = partito.getLogo();
	}

	public byte[] getLogo() {
		return logo;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(logo);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		if (obj instanceof Integer) {
			Integer other = (Integer) obj;
			return other == this.id;
		}
		Partito other = (Partito) obj;
		return other.id == this.id;
	}

	@Override
	public String toString() {
		return nome;
	}
	
}
