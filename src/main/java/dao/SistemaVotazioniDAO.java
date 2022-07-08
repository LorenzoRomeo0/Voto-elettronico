package dao;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SistemaVotazioniDAO {
	
	final private String url = "jdbc:mysql://localhost:3306/sistema_votazioni";
	final private String username = "INGSW";
	final private String password = "ProgettoINGSW";
	protected Connection conn;

	private void connetti() {
		try {
			System.out.println("Connesione al database...");
			conn = DriverManager.getConnection(url, username, password);
			System.out.println("Connesso al database!!!");
		} catch (Exception e) {
			System.out.println("outore durante la connnessione al database.");
		}
	}
	
	private void disconnetti() {
		try {
			System.out.println("Chiusura connesione al database...");
			conn.close();
			System.out.println("Connessione al database chiusa!!!");
		} catch (Exception e) {
			System.out.println("outore durante la chiusura della connnessione al database.");
		}
	}

	public UtenteDTO login(String codiceFiscale, String password) {
		System.out.println("---> login...");
		String codiceFiscaleUpper = codiceFiscale.toUpperCase();
		String salt = getSalt(codiceFiscaleUpper);
		String id = null;
		UtenteDTO dati = null;
		if (null != salt) {
			id = getID(codiceFiscaleUpper, salt.concat(password));
		}
		if (null != id) {
			dati = getDatiUtente(id, codiceFiscaleUpper);
		}
		System.out.println("---> fine login");
		return dati;
	}

	private UtenteDTO getDatiUtente(String id, String codiceFiscale) {
		System.out.println("---> get dati utenti...");
		UtenteDTO dati = null;
		connetti();
		String sql = "SELECT u.nome, u.cognome, YEAR(u.dataDinascita) as anno, MONTH(u.dataDinascita) as mese, "
				+ "DAY(u.dataDinascita) as giorno, t.nome as tipologia, p.nome as nazionalita, s.nome as sesso, "
				+ "c.nome as comune, r.nome as regione, pr.nome as provincia FROM utenti AS u JOIN tipi_utente AS "
				+ "t ON u.tipologia = t.id JOIN paesi AS p ON u.nazionalita = p.id JOIN comuni AS c ON "
				+ "u.residenza = c.id JOIN sessi AS s ON u.genere = s.id JOIN regioni AS r ON c.regione = r.id "
				+ "JOIN province AS pr ON c.provincia = pr.id WHERE u.id = ? LIMIT 1;";
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, id);
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				dati = new UtenteDTO(codiceFiscale, result.getString(1), result.getString(2), result.getInt(3),
						result.getInt(4), result.getInt(5), result.getString(6), result.getString(7),
						result.getString(8), result.getString(9), result.getString(10), result.getString(11));
			}
		} catch (SQLException e) {
			System.out.println("---> get dati utenti fallito.");
		}
		disconnetti();
		System.out.println("---> fine get dati utenti");
		return dati;
	}

	private String getID(String codiceFiscale, String saltedPassword) {
		System.out.println("---> get id...");
		String encryptedPassword = crypta_password(saltedPassword);
		String id = null;
		if (null != encryptedPassword) {
			connetti();
			String sql = "SELECT id FROM credenziali WHERE codiceFiscale = ? AND pwd = ? LIMIT 1;";
			try {
				PreparedStatement statement = conn.prepareStatement(sql);
				statement.setString(1, codiceFiscale);
				statement.setString(2, encryptedPassword);
				ResultSet result = statement.executeQuery();
				if (result.next()) {
					id = result.getString(1);
				}
			} catch (SQLException e) {
				System.out.println("---> get id fallito.");
				e.printStackTrace();
			}
			disconnetti();
		}
		System.out.println("---> fine get id");
		return id;
	}

	private String crypta_password(String saltedPassword) {
		System.out.println("---> criptazione password...");
		String encryptedPassword = null;
		try {
			MessageDigest m = MessageDigest.getInstance("md5");
			m.update(saltedPassword.getBytes());
			encryptedPassword = new BigInteger(1, m.digest()).toString(16);
			encryptedPassword = String.format("%32s", encryptedPassword).replace(' ', '0');
			m.reset();
		} catch (NoSuchAlgorithmException e) {
			System.out.println("---> criptazione password fallita.");
		}
		System.out.println("---> fine criptazione password");
		return encryptedPassword;
	}

	private String getSalt(String codicefiscale) {
		System.out.println("---> prendi salt...");
		connetti();
		String salt = null;
		String sql = "SELECT salt FROM credenziali WHERE codiceFiscale = ? LIMIT 1;";
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, codicefiscale);
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				salt = result.getString(1);
			}
		} catch (Exception e) {
			System.out.println("---> prendi salt fallito.");
		}
		disconnetti();
		System.out.println("---> fine prendi salt");
		return salt;
	}
}
