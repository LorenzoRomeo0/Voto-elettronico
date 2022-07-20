package dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import system.SessionSystem;

public class SistemaVotazioniDAO {

	final private int id_referendum = 1;
	final private int id_ordinale = 2;
	final private int id_categorica = 3;
	final private int id_categorica_preferenza = 4;
	
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
		int id = insert_scheda(avvio, termine, creatore, stato, nome, id_referendum);
		insert_scheda_referendum(id, referendum);
	}

	private Integer insert_scheda(LocalDate avvio, LocalDate termine, int creatore, int stato, String nome, int tipologia) {
		System.out.println("\n---> inserisci scheda...");
		connetti();
		String sql = "insert into schede(dataAvvio, dataTermine, creatore, stato, tipologia, nome) "
				+ "values (STR_TO_DATE(?, '%d/%m/%Y'), STR_TO_DATE(?, '%d/%m/%Y'), ?, ?, ?, ?);";
		Integer key = null;
		try {
			PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, SessionSystem.date_formatter(avvio));
			statement.setString(2, SessionSystem.date_formatter(termine));
			statement.setInt(3, creatore);
			statement.setInt(4, stato);
			statement.setInt(5, tipologia);
			statement.setString(6, nome);
			statement.executeUpdate();
			ResultSet rs = statement.getGeneratedKeys();
			if (rs.next()) {
				key = Integer.valueOf(rs.getInt(1));
			}
		} catch (Exception e) {
			System.out.println("---! inserisci scheda fallito.");
		}
		disconnetti();
		System.out.println("---X fine inserisci scheda!!!");
		return key;
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
			System.out.println("---! errore prendi stati scheda fallito.");
		}
		System.out.println("---X fine prendi stati scheda!!!");
		return risultati;
	}

	public ArrayList<ValoreSempliceDTO> get_tipi_votabili_all() {
		System.out.println("\n---> prendi tipi votabili...");
		String sql = "select id, nome from tipi_votabile;";
		ArrayList<ValoreSempliceDTO> risultati = null;
		try {
			risultati = get_valori_semplici(sql);
		} catch (SQLException e) {
			System.out.println("---! errore prendi tipi votabili fallito.");
		}
		System.out.println("---X fine prendi tipi votabili!!!");
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
				+ "nazionalita, residenza, genere) values (?, ?, ?, " + "STR_TO_DATE(?, '%d/%m/%Y'), ?, ?, ?, ?);";
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

	public ArrayList<PartitoDTO> get_partiti_filtrati(String filtro) {
		System.out.println("\n---> prendi partiti filtrati...");
		connetti();
		ArrayList<PartitoDTO> risultato = new ArrayList<PartitoDTO>();
		String sql = "select id, nome, logo from partiti where nome like ?";
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, filtro + "%");
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				risultato.add(new PartitoDTO(result.getInt("id"), result.getString("nome"), result.getBlob("logo")));
			}
		} catch (Exception e) {
			System.out.println("---! prendi partiti filtrati fallito.");
		}
		disconnetti();
		if (risultato.size() == 0) {
			risultato = null;
		}
		System.out.println("---X fine prendi partiti filtrati!!!");
		return risultato;
	}

	public ArrayList<CandidatoDTO> get_candidati_filtrati(String filtro) {
		System.out.println("\n---> prendi candidati filtrati...");
		connetti();
		ArrayList<CandidatoDTO> risultato = new ArrayList<CandidatoDTO>();
		String sql = "select id, sostiene, genere, cognome, nome from candidati where nome like ? or cognome like ?";
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, filtro + "%");
			statement.setString(2, filtro + "%");
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				risultato.add(new CandidatoDTO(result.getInt("id"), result.getString("nome"),
						result.getString("cognome"), result.getInt("genere"), result.getInt("sostiene")));
			}
		} catch (Exception e) {
			System.out.println("---! prendi candidati filtrati fallito.");
		}
		disconnetti();
		if (risultato.size() == 0) {
			risultato = null;
		}
		System.out.println("---X fine prendi candidati filtrati!!!");
		return risultato;
	}

	private Integer insert_votabile(int tipo_votabile) {
		System.out.println("\n---> inserisci votabili...");
		connetti();
		String sql = "insert into votabili (tipoVotabile) values (?);";
		Integer key = null;
		try {
			PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, tipo_votabile);
			statement.executeUpdate();
			ResultSet rs = statement.getGeneratedKeys();
			if (rs.next()) {
				key = Integer.valueOf(rs.getInt(1));
			}
		} catch (Exception e) {
			System.out.println("---! inserisci votabili.");
			e.printStackTrace();
		}
		disconnetti();
		System.out.println("---X fine inserisci votabili!!!");
		return key;
	}

	private final int tipo_votabile_candidato = 1;
	private final int tipo_votabile_partito = 2;
	private final int tipo_votabile_lista = 3;

	public void insert_partito(String nome, String img_path) {
		System.out.println("\n---> inserisci partito...");
		connetti();
		String sql = "insert into partiti (id, nome, logo) values (?, ?, ?);";
		try {
			InputStream img = new FileInputStream(img_path);
			PreparedStatement statement = conn.prepareStatement(sql);
			Integer id = insert_votabile(tipo_votabile_partito);
			statement.setInt(1, id);
			statement.setString(2, nome);
			statement.setBlob(3, img);
			statement.executeUpdate();
		} catch (FileNotFoundException e) {
			System.out.println("---! inserisci partito impossibile caricare immagine.");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("---! inserisci partito fallito.");
			e.printStackTrace();
		}
		disconnetti();
		System.out.println("---X fine inserisci partito!!!");
	}

	public void insert_candidato(int partito, int sesso, String cognome, String nome) {
		System.out.println("\n---> inserisci candidato...");
		connetti();
		String sql = "insert into candidati (id, sostiene, genere, cognome, nome) values (?,?,?,?,?);";
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			Integer id = insert_votabile(tipo_votabile_candidato);
			statement.setInt(1, id);
			statement.setInt(2, partito);
			statement.setInt(3, sesso);
			statement.setString(4, cognome);
			statement.setString(5, nome);
			statement.executeUpdate();
		} catch (Exception e) {
			System.out.println("---! inserisci candidato fallito.");
			e.printStackTrace();
		}
		disconnetti();
		System.out.println("---X fine inserisci candidato!!!");
	}

	public void insert_lista(ArrayList<CandidatoDTO> candidati, String nome) {
		System.out.println("\n---> inserisci lista...");
		Integer id = insert_votabile(tipo_votabile_lista);
		connetti();
		String sql_lista = "insert into liste (id, nome) values (?,?);";
		String sql_partecipanti = "insert into affilianti (idLista, idCandidato) values (?,?);";
		try {
			PreparedStatement statement_lista = conn.prepareStatement(sql_lista);
			statement_lista.setInt(1, id);
			statement_lista.setString(2, nome);
			statement_lista.executeUpdate();
			
			System.out.println("---> lista inserita");
			
			for (CandidatoDTO candidato : candidati) {
				PreparedStatement statement_partecipanti = conn.prepareStatement(sql_partecipanti);
				statement_partecipanti.setInt(1, id);
				statement_partecipanti.setInt(2, candidato.getId());
				statement_partecipanti.executeUpdate();
			}
		} catch (Exception e) {
			System.out.println("---! inserisci lista fallito.");
			e.printStackTrace();
		}
		disconnetti();
		System.out.println("---X fine inserisci !!!");
	}
	
	public ArrayList<ListaDTO> get_liste_filtrati(String filtro) {
		System.out.println("\n---> prendi liste filtrati...");
		connetti();
		ArrayList<ListaDTO> risultato = new ArrayList<ListaDTO>();
		String sql = "select id, nome from liste where nome like ?";
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, filtro + "%");
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				risultato.add(new ListaDTO(result.getInt("id"), result.getString("nome")));
			}
		} catch (Exception e) {
			System.out.println("---! prendi liste filtrati fallito.");
		}
		disconnetti();
		if (risultato.size() == 0) {
			risultato = null;
		}
		System.out.println("---X fine prendi liste filtrati!!!");
		return risultato;
	}
	
	public void insert_scheda_ordinale (LocalDate avvio, LocalDate termine, int creatore, int stato, String nome, Collection<Votabile> partecipanti) {
		System.out.println("\n---> inserisci scheda ordinale...");
		int id = insert_scheda(avvio, termine, creatore, stato, nome, id_ordinale);
		connetti();
		String sql_scheda = "insert into schede_ordinali (id) values (?);";
		String sql_concorrenti = "insert into concorrenti (idScheda, idCandidato) values (?, ?);";
		try {
			PreparedStatement statement_scheda = conn.prepareStatement(sql_scheda);
			statement_scheda.setInt(1, id);
			statement_scheda.executeUpdate();
			
			for (Votabile votabile : partecipanti) {
				PreparedStatement statement_partecipanti = conn.prepareStatement(sql_concorrenti);
				statement_partecipanti.setInt(1, id);
				statement_partecipanti.setInt(2, votabile.getId());
				statement_partecipanti.executeUpdate();
			}
		} catch (Exception e) {
			System.out.println("---! inserisci elementi scheda ordinali fallita.");
			e.printStackTrace();
		}
		disconnetti();
		System.out.println("---X fine inserisci elementi scheda ordinali!!!");
	}
	
	public void insert_scheda_categorica (LocalDate avvio, LocalDate termine, int creatore, int stato, String nome, Collection<Votabile> aspiranti) {
		System.out.println("\n---> inserisci scheda categorica...");
		int id = insert_scheda(avvio, termine, creatore, stato, nome, id_categorica);
		connetti();
		String sql_scheda = "insert into schede_categoriche (id) values (?);";
		String sql_aspiranti = "insert into aspiranti (idScheda, idCandidato) values (?, ?);";
		try {
			PreparedStatement statement_scheda = conn.prepareStatement(sql_scheda);
			statement_scheda.setInt(1, id);
			statement_scheda.executeUpdate();
			
			for (Votabile votabile : aspiranti) {
				PreparedStatement statement_aspiranti = conn.prepareStatement(sql_aspiranti);
				statement_aspiranti.setInt(1, id);
				statement_aspiranti.setInt(2, votabile.getId());
				statement_aspiranti.executeUpdate();
			}
		} catch (Exception e) {
			System.out.println("---! inserisci elementi scheda categorica fallita.");
			e.printStackTrace();
		}
		disconnetti();
		System.out.println("---X fine inserisci elementi scheda categorica!!!");
	}
	
	public void insert_scheda_categorica_preferenza (LocalDate avvio, LocalDate termine, int creatore, int stato, String nome, Collection<Votabile> aspiranti) {
		System.out.println("\n---> inserisci scheda categorica con preferenza...");
		int id = insert_scheda(avvio, termine, creatore, stato, nome, id_categorica_preferenza);
		connetti();
		String sql_scheda = "insert into schede_categoriche_con_preferenza (id) values (?);";
		String sql_aspiranti = "insert into partecipanti (idScheda, idCandidato) values (?, ?);";
		try {
			PreparedStatement statement_scheda = conn.prepareStatement(sql_scheda);
			statement_scheda.setInt(1, id);
			statement_scheda.executeUpdate();
			
			for (Votabile votabile : aspiranti) {
				PreparedStatement statement_aspiranti = conn.prepareStatement(sql_aspiranti);
				statement_aspiranti.setInt(1, id);
				statement_aspiranti.setInt(2, votabile.getId());
				statement_aspiranti.executeUpdate();
			}
		} catch (Exception e) {
			System.out.println("---! inserisci elementi scheda categorica con preferenza fallita.");
			e.printStackTrace();
		}
		disconnetti();
		System.out.println("---X fine inserisci elementi scheda categorica con preferenza!!!");
	}
}