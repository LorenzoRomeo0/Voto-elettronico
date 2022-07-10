package system;

import dao.UtenteDTO;

public class SessionUser {
	private static SessionUser session;
	private UtenteDTO Utente;

	private SessionUser() {
	}

	public static SessionUser getInstance() {
		if (null == session)
			session = new SessionUser();
		return session;
	}

	public UtenteDTO getUtente() {
		return Utente;
	}

	public void setUtente(UtenteDTO Utente) {
		this.Utente = Utente;
	}
}
