package system.votabili;

import dao.CandidatoDTO;
import data.Sesso;

public class Candidato extends Votabile {

	private String cognome;
	private Sesso sesso;
	private Partito partito;

	public Candidato(CandidatoDTO candidato, Partito partito) {
		super(candidato.getId(), candidato.getNome());
		this.cognome = candidato.getCognome();
		this.sesso = Sesso.sesso(candidato.getSesso());
		this.partito = partito;
	}

	public String getCognome() {
		return cognome;
	}

	public Sesso getSesso() {
		return sesso;
	}

	public Partito getPartito() {
		return partito;
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
			return other == id;
		}
		Candidato other = (Candidato) obj;
		return other.id == this.id;
	}
	
	@Override
	public String toString() {
		return toTitleCase(cognome + " " + nome);
	}
	
	private String toTitleCase(String string) {
	    String[] arr = string.split(" ");
	    StringBuffer sb = new StringBuffer();

	    for (int i = 0; i < arr.length; i++) {
	        sb.append(Character.toUpperCase(arr[i].charAt(0)))
	            .append(arr[i].substring(1)).append(" ");
	    }          
	    return sb.toString().trim();
	}
  
}
