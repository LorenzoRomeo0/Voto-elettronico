package dao;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import system.SessionSystem;

public class SistemaVotazioniDAO {

	final private int id_referendum = 1;
	final private String default_salt = "fish and chips!!";

	final private String db_url = "jdbc:mysql://localhost:3306/sistema_votazioni";
	final private String db_username = "INGSW";
	final private String db_password = "ProgettoINGSW";
	protected Connection conn;

	private void connetti() {
		try {
			System.out.println("Connesione al database...");
			conn = DriverManager.getConnection(db_url, db_username, db_password);
			System.out.println("Connesso al database!!!");
		} catch (Exception e) {
			System.out.println("errore durante la connnessione al database.");
		}
	}

	private void disconnetti() {
		try {
			System.out.println("Chiusura connesione al database...");
			conn.close();
			System.out.println("Connessione al database chiusa!!!");
		} catch (Exception e) {
			System.out.println("errore durante la chiusura della connnessione al database.");
		}
	}

	public UtenteDTO login(String codiceFiscale, String password) {
		System.out.println("\n---> inizio login...");
		String salt = get_salt(codiceFiscale);
		String id = null;
		UtenteDTO dati = null;
		if (null != salt) {
			id = get_ID(codiceFiscale, salt.concat(password));
		}
		if (null != id) {
			dati = get_dati_utente(id, codiceFiscale);
		}
		System.out.println("---X fine login!!!");
		return dati;
	}

	private UtenteDTO get_dati_utente(String id, String codiceFiscale) {
		System.out.println("\n---> prendi dati utenti...");
		UtenteDTO dati = null;
		connetti();
		String sql = "SELECT u.nome, u.cognome, YEAR(u.dataDinascita) as anno, MONTH(u.dataDinascita) as mese, "
				+ "DAY(u.dataDinascita) as giorno, t.nome as tipologia, p.nome as nazionalita, s.nome as sesso, "
				+ "c.nome as comune, r.nome as regione, pr.nome as provincia, u.id FROM utenti AS u JOIN tipi_utente AS "
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
						result.getString(8), result.getString(9), result.getString(10), result.getString(11),
						result.getInt(12));
			}
		} catch (SQLException e) {
			System.out.println("---! prendi dati utenti fallito.");
		}
		disconnetti();
		System.out.println("---X fine prendi dati utenti!!!");
		return dati;
	}

	private String get_ID(String codiceFiscale, String saltedPassword) {
		System.out.println("\n---> prendi id...");
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
				System.out.println("---! prendi id fallito.");
			}
			disconnetti();
		}
		System.out.println("---X fine prendi id!!!");
		return id;
	}

	private String crypta_password(String saltedPassword) {
		System.out.println("\n---> criptazione password...");
		String encryptedPassword = null;
		try {
			MessageDigest m = MessageDigest.getInstance("md5");
			m.update(saltedPassword.getBytes());
			encryptedPassword = new BigInteger(1, m.digest()).toString(16);
			encryptedPassword = String.format("%32s", encryptedPassword).replace(' ', '0');
			m.reset();
		} catch (NoSuchAlgorithmException e) {
			System.out.println("---! criptazione password fallita.");
		}
		System.out.println("---X fine criptazione password!!!");
		return encryptedPassword;
	}

	private String get_salt(String codicefiscale) {
		System.out.println("\n---> prendi salt...");
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
			System.out.println("---! prendi salt fallito.");
		}
		disconnetti();
		System.out.println("---X fine prendi salt!!!");
		return salt;
	}

	public boolean controllo_nome_scheda(String nome) {
		System.out.println("\n---> controllo nome scheda...");
		connetti();
		boolean risultato = false;
		String sql = "select * from schede where nome = ?;";
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, nome);
			ResultSet result = statement.executeQuery();
			risultato = !result.next();
		} catch (Exception e) {
			System.out.println("---! controllo nome scheda fallito.");
		}
		disconnetti();
		System.out.println("---X fine controllo nome scheda!!!");
		return risultato;
	}

	public void insert_scheda_referendum(LocalDate avvio, LocalDate termine, int creatore, int stato, String nome,
			String referendum) {
		insertScheda(SessionSystem.date_formatter(avvio), SessionSystem.date_formatter(termine), creatore, stato, nome);
		int id = get_scheda_ID_by_nome(nome);
		insert_scheda_referendum(id, referendum);
	}

	private void insertScheda(String avvio, String termine, int creatore, int stato, String nome) {
		System.out.println("\n---> inserisci scheda...");
		connetti();
		String sql = "insert into schede(dataAvvio, dataTermine, creatore, stato, tipologia, nome) "
				+ "values (STR_TO_DATE(?, '%d/%m/%Y'), STR_TO_DATE(?, '%d/%m/%Y'), ?, ?, ?, ?);";
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, avvio);
			statement.setString(2, termine);
			statement.setInt(3, creatore);
			statement.setInt(4, stato);
			statement.setInt(5, id_referendum);
			statement.setString(6, nome);
			statement.executeUpdate();
		} catch (Exception e) {
			System.out.println("---! inserisci scheda fallito.");
		}
		disconnetti();
		System.out.println("---X fine inserisci scheda!!!");
	}

	private Integer get_scheda_ID_by_nome(String nome) {
		System.out.println("\n---> prendi id scheda con nome...");
		connetti();
		Integer risultato = null;
		String sql = "select id from schede where nome = ? limit 1;";
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, nome);
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				risultato = result.getInt(1);
			}
		} catch (Exception e) {
			System.out.println("---! prendi id scheda con nome fallito.");
		}
		disconnetti();
		System.out.println("---X fine prendi id scheda con nome!!!");
		return risultato;
	}

	private void insert_scheda_referendum(int id, String referendum) {
		System.out.println("\n---> inserisci scheda referendum...");
		connetti();
		String sql = "insert into schede_referendum (id, referendum) values (?, ?);";
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, id);
			statement.setString(2, referendum);
			statement.executeUpdate();
		} catch (Exception e) {
			System.out.println("---! inserisci scheda referendum fallito.");
		}
		disconnetti();
		System.out.println("---X fine inserisci scheda referendum!!!");
	}

	public ArrayList<ValoreSempliceDTO> get_nazionalita_all() {
		System.out.println("\n---> prendi nazionalità...");
		String sql = "select id, nome from paesi order by nome asc;";
		ArrayList<ValoreSempliceDTO> risultati = null;
		try {
			risultati = get_valori_semplici(sql);
		} catch (Exception e) {
			System.out.println("---! prendi nazionalità fallito.");
		}
		System.out.println("---X fine prendi nazionalità!!!");
		return risultati;
	}

	public ArrayList<ValoreSempliceDTO> get_tipi_utente_all() {
		System.out.println("\n---> prendi tipologie utente...");
		String sql = "select id, nome from tipi_utente order by nome asc;";
		ArrayList<ValoreSempliceDTO> risultati = null;
		try {
			risultati = get_valori_semplici(sql);
		} catch (Exception e) {
			System.out.println("---! prendi tipologie utente fallito.");
		}
		System.out.println("---X fine prendi tipologie utente!!!");
		return risultati;
	}

	public ArrayList<ValoreSempliceDTO> get_sessi_all() {
		System.out.println("\n---> prendi sessi...");
		String sql = "select id, nome from sessi order by nome asc;";
		ArrayList<ValoreSempliceDTO> risultati = null;
		try {
			risultati = get_valori_semplici(sql);
		} catch (Exception e) {
			System.out.println("---! prendi sessi fallito.");
		}
		System.out.println("---X fine prendi sessi!!!");
		return risultati;
	}

	public ArrayList<ValoreSempliceDTO> get_tipi_scheda_all() {
		System.out.println("\n---> prendi tipi scheda...");
		String sql = "select id, nome from tipi_scheda;";
		ArrayList<ValoreSempliceDTO> risultati = null;
		try {
			risultati = get_valori_semplici(sql);
		} catch (Exception e) {
			System.out.println("---! errore prendi tipi scheda fallito.");
		}
		System.out.println("---X fine prendi tipi scheda!!!");
		return risultati;
	}

	public ArrayList<ValoreSempliceDTO> get_schede_stati_all() {
		System.out.println("\n---> prendi stati scheda...");
		String sql = "select id, nome from stati_scheda;";
		ArrayList<ValoreSempliceDTO> risultati = null;
		try {
			risultati = get_valori_semplici(sql);
		} catch (SQLException e) {
			System.out.println("---! errore prendi stati_scheda fallito.");
		}
		System.out.println("---X fine prendi stati scheda!!!");
		return risultati;
	}

	private ArrayList<ValoreSempliceDTO> get_valori_semplici(String sql) throws SQLException {
		connetti();
		ArrayList<ValoreSempliceDTO> tipi_scheda = new ArrayList<ValoreSempliceDTO>();
		PreparedStatement statement = conn.prepareStatement(sql);
		ResultSet result = statement.executeQuery();
		while (result.next()) {
			tipi_scheda.add(new ValoreSempliceDTO(result.getInt("id"), result.getString("nome")));
		}
		disconnetti();
		if (tipi_scheda.size() == 0) {
			tipi_scheda = null;
		}
		return tipi_scheda;
	}

	public ArrayList<RegioneDTO> get_regioni_all() {
		System.out.println("\n---> prendi regioni...");
		connetti();
		ArrayList<RegioneDTO> risultato = new ArrayList<RegioneDTO>();
		String sql = "select id, nome from regioni order by nome asc;";
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				risultato.add(new RegioneDTO(result.getInt("id"), result.getString("nome")));
			}
		} catch (Exception e) {
			System.out.println("---! prendi regioni fallito.");
		}
		disconnetti();
		if (risultato.size() == 0) {
			risultato = null;
		}
		System.out.println("---X fine prendi nazionalità!!!");
		return risultato;
	}

	public ArrayList<ProvinciaDTO> get_province_all() {
		System.out.println("\n---> prendi province...");
		connetti();
		ArrayList<ProvinciaDTO> risultato = new ArrayList<ProvinciaDTO>();
		String sql = "select id, nome, regione from province order by nome asc;";
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				risultato
						.add(new ProvinciaDTO(result.getInt("id"), result.getString("nome"), result.getInt("regione")));
			}
		} catch (Exception e) {
			System.out.println("---! prendi province fallito.");
		}
		disconnetti();
		if (risultato.size() == 0) {
			risultato = null;
		}
		System.out.println("---X fine prendi province!!!");
		return risultato;
	}

	public ArrayList<ComuneDTO> get_comune_all() {
		System.out.println("\n---> prendi comune...");
		connetti();
		ArrayList<ComuneDTO> risultato = new ArrayList<ComuneDTO>();
		String sql = "select id, nome, regione, provincia from comuni order by nome asc;";
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				risultato.add(new ComuneDTO(result.getInt("regione"), result.getInt("provincia"), result.getInt("id"),
						result.getString("nome")));
			}
		} catch (Exception e) {
			System.out.println("---! prendi comuni fallito.");
		}
		disconnetti();
		if (risultato.size() == 0) {
			risultato = null;
		}
		System.out.println("---X fine prendi comuni!!!");
		return risultato;
	}

	public boolean insert_utente(String nome, String cognome, String codiceFiscale, String password, LocalDate nascita,
			ComuneDTO residenza, ValoreSempliceDTO nazionalita, ValoreSempliceDTO tipologia, ValoreSempliceDTO sesso) {
		boolean inserted = true;
		if (null != get_salt(codiceFiscale)) {
			inserted = false;
		} else {
			String saltedPassword = default_salt.concat(password);
			insert_credenziali(codiceFiscale, saltedPassword, default_salt);
			String id = get_ID(codiceFiscale, saltedPassword);
			insert_dati_utenti(id, nome, cognome, nascita, residenza, nazionalita, tipologia, sesso);
		}
		return inserted;

	}
	
	private void insert_dati_utenti(String id, String nome, String cognome, LocalDate nascita, ComuneDTO residenza,
			ValoreSempliceDTO nazionalita, ValoreSempliceDTO tipologia, ValoreSempliceDTO sesso) {
		System.out.println("\n---> inserisci dati utente...");
		connetti();
		String sql = "insert into utenti (id, nome, cognome, dataDiNascita, tipologia, "
				+ "nazionalita, residenza, genere) values (?, ?, ?, "
				+ "STR_TO_DATE(?, '%d/%m/%Y'), ?, ?, ?, ?);";
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, id);
			statement.setString(2, nome);
			statement.setString(3, cognome);
			statement.setString(4, SessionSystem.date_formatter(nascita));
			statement.setInt(5, tipologia.getId());
			statement.setInt(6, nazionalita.getId());
			statement.setInt(7, residenza.getId());
			statement.setInt(8, sesso.getId());
			statement.executeUpdate();
		} catch (Exception e) {
			System.out.println("---! inserisci dati utente fallito.");
			e.printStackTrace();
		}
		disconnetti();
		System.out.println("---X fine inserisci dati utente!!!");
		
	}

	private void insert_credenziali(String codiceFiscale, String saltedPassword, String salt) {
		System.out.println("\n---> inserisci credenziali...");
		connetti();
		String sql = "insert into credenziali (codiceFiscale, pwd, salt) values (?, ?, ?);";
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, codiceFiscale);
			statement.setString(2, crypta_password(saltedPassword));
			statement.setString(3, salt);
			statement.executeUpdate();
		} catch (Exception e) {
			System.out.println("---! inserisci credenziali fallito.");
			e.printStackTrace();
		}
		disconnetti();
		System.out.println("---X fine inserisci credenziali!!!");
	}
}