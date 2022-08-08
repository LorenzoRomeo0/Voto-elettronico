package system;

import dao.SchedaDTO;
import dao.UtenteDTO;

public class SessionUser {
	private static SessionUser session;
	private UtenteDTO utente;
	private SchedaDTO scheda;

	private SessionUser() {
	}

	public static SessionUser getInstance() {
		if (null == session)
			session = new SessionUser();
		return session;
	}

	public UtenteDTO getUtente() {
		return utente;
	}

	public void setUtente(UtenteDTO Utente) {
		this.utente = Utente;
	}
	
	public void setScheda(SchedaDTO scheda) {
		this.scheda = scheda;
	}
	
	public SchedaDTO getScheda() {
		return scheda;
	}
}
