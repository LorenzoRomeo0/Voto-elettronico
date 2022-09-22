package dao;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import data.Esito;
import data.Sesso;
import data.Stato;
import data.TipoScheda;
import data.TipoUtente;
import data.TipoVotabile;
import data.Referendum;
import system.luoghi.Comune;
import system.luoghi.Nazionalita;
import system.schede.Scheda;
import system.votabili.Votabile;
import system.voto.VotoOrdinale;

public class SistemaVotazioniDAO {

	private static SistemaVotazioniDAO dao;

	final private String default_salt = "fish and chips!!";

	final private String db_name = "sistema_votazioni2";
	final private String db_url = "jdbc:mysql://localhost:3306/" + db_name;
	final private String db_username = "INGSW";
	final private String db_password = "ProgettoINGSW";
	protected Connection conn;

	private SistemaVotazioniDAO() {
	}

	public static SistemaVotazioniDAO getInstance() {
		if (null == dao)
			dao = new SistemaVotazioniDAO();
		return dao;
	}

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
		String salt = getSalt(codiceFiscale);
		UtenteDTO dati = null;
		if (null != salt) {
			String saltedPassword = salt.concat(password);
			String encryptedPassword = cryptaPassword(saltedPassword);
			if (null != encryptedPassword) {
				String id = getID(codiceFiscale, encryptedPassword);
				if (null != id) {
					dati = getDatiUtente(id, codiceFiscale);
				}
			}
		}
		System.out.println("---X fine login!!!");
		return dati;
	}

	private String getSalt(String codicefiscale) {
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
			e.printStackTrace();
		}
		disconnetti();
		System.out.println("---X fine prendi salt!!!");
		return salt;
	}

	private String cryptaPassword(String saltedPassword) {
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

	private String getID(String codiceFiscale, String encryptedPassword) {
		System.out.println("\n---> prendi id...");
		String id = null;
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
		System.out.println("---X fine prendi id!!!");
		return id;
	}

	private UtenteDTO getDatiUtente(String id, String codiceFiscale) {
		System.out.println("\n---> prendi dati utenti...");
		UtenteDTO dati = null;
		connetti();
		String sql = "SELECT u.nome, u.cognome, YEAR(u.dataDinascita) as anno, MONTH(u.dataDinascita) as mese, "
				+ "DAY(u.dataDinascita) as giorno, t.nome as tipo, nazionalita, s.nome as sesso, "
				+ "u.id , u.residenza FROM utenti AS u JOIN tipi_utente AS t ON u.tipo = t.id "
				+ "JOIN sessi AS s ON u.sesso = s.id WHERE u.id = ? LIMIT 1;";
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, id);
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				dati = new UtenteDTO(codiceFiscale, result.getString(1), result.getString(2), result.getInt(3),
						result.getInt(4), result.getInt(5), result.getString(6), result.getInt(7), result.getString(8),
						result.getInt(9), result.getInt(10));
			}
		} catch (SQLException e) {
			System.out.println("---! prendi dati utenti fallito.");
			e.printStackTrace();
		}
		disconnetti();
		System.out.println("---X fine prendi dati utenti!!!");
		return dati;
	}

	public void insertSchedaReferendum(LocalDate avvio, LocalDate termine, int creatore, Stato stato, Esito esito,
			String nome, String referendum) {
		Integer id = insertScheda(avvio, termine, creatore, stato.id(), esito.id(), nome, TipoScheda.REFERENDUM.id());
		if (null != id) {
			insertSchedaReferendum(id, referendum);
		}
	}

	private Integer insertScheda(LocalDate avvio, LocalDate termine, int creatore, int stato, int esito, String nome,
			int tipologia) {
		System.out.println(avvio + " - " + termine + " - " + creatore + " - " + stato + " - " + esito + " - " + nome
				+ " - " + tipologia);
		System.out.println("\n---> inserisci scheda...");
		connetti();
		String sql = "insert into schede(dataAvvio, dataTermine, creatore, stato, esito, tipo, nome) "
				+ "values (STR_TO_DATE(?, '%d/%m/%Y'), STR_TO_DATE(?, '%d/%m/%Y'), ?, ?, ?, ?, ?);";
		Integer key = null;
		try {
			PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, dateFormatter(avvio));
			statement.setString(2, dateFormatter(termine));
			statement.setInt(3, creatore);
			statement.setInt(4, stato);
			statement.setInt(5, esito);
			statement.setInt(6, tipologia);
			statement.setString(7, nome);
			statement.executeUpdate();
			ResultSet rs = statement.getGeneratedKeys();
			if (rs.next()) {
				key = Integer.valueOf(rs.getInt(1));
			}
		} catch (Exception e) {
			System.out.println("---! inserisci scheda fallito.");
			e.printStackTrace();
		}
		disconnetti();
		System.out.println("---X fine inserisci scheda!!!");
		return key;
	}

	public static String dateFormatter(LocalDate data) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return data.format(formatter);
	}

	private void insertSchedaReferendum(int id, String referendum) {
		System.out.println("\n---> inserisci scheda referendum...");
		connetti();
		String sql = "insert ignore into schede_referendum (id, referendum) values (?, ?);";
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

	public ArrayList<PartitoDTO> getAllPartiti() {
		System.out.println("\n---> prendi partiti filtrati...");
		connetti();
		ArrayList<PartitoDTO> risultato = new ArrayList<PartitoDTO>();
		String sql = "select id, nome, logo from partiti";
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				risultato.add(new PartitoDTO(result.getInt("id"), result.getString("nome"), result.getBlob("logo")));
			}
		} catch (Exception e) {
			System.out.println("---! prendi partiti filtrati fallito.");
			e.printStackTrace();
		}
		disconnetti();
		if (risultato.size() == 0) {
			risultato = null;
		}
		System.out.println("---X fine prendi partiti filtrati!!!");
		return risultato;
	}

	public ArrayList<CandidatoDTO> getAllCandidati() {
		System.out.println("\n---> prendi candidati filtrati...");
		connetti();
		ArrayList<CandidatoDTO> risultato = new ArrayList<CandidatoDTO>();
		String sql = "select id, sostiene, sesso, cognome, nome from candidati";
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				risultato.add(new CandidatoDTO(result.getInt("id"), result.getString("nome"),
						result.getString("cognome"), result.getInt("sesso"), result.getInt("sostiene")));
			}
		} catch (Exception e) {
			System.out.println("---! prendi candidati filtrati fallito.");
			e.printStackTrace();
		}
		disconnetti();
		if (risultato.size() == 0) {
			risultato = null;
		}
		System.out.println("---X fine prendi candidati filtrati!!!");
		return risultato;
	}

	public ArrayList<SchedaDTO> getAllSchede() {
		System.out.println("\n---> get all schede...");
		connetti();
		String sql = "select s.id, s.nome, ts.nome as tipo, creatore, ss.nome as stato, te.nome as esito, year(dataAvvio) as anno_avvio, "
				+ "month(dataAvvio) as mese_avvio, dayofmonth(dataAvvio) as giorno_avvio, year(dataTermine) as anno_termine, "
				+ "month(dataTermine) as mese_termine, dayofmonth(dataTermine) as giorno_termine from schede as s "
				+ "join tipi_esito as te on te.id = s.esito join stati_scheda as ss on s.stato = ss.id "
				+ "join tipi_scheda as ts on ts.id = s.tipo;";
		ArrayList<SchedaDTO> risultato = new ArrayList<SchedaDTO>();
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet r = statement.executeQuery();
			while (r.next()) {
				LocalDate avvio = LocalDate.of(r.getInt("anno_avvio"), r.getInt("mese_avvio"),
						r.getInt("giorno_avvio"));
				LocalDate termine = LocalDate.of(r.getInt("anno_termine"), r.getInt("mese_termine"),
						r.getInt("giorno_termine"));
				risultato.add(new SchedaDTO(r.getInt("id"), r.getString("nome"), avvio, termine, r.getString("stato"),
						r.getString("esito"), r.getString("tipo")));
			}
		} catch (Exception e) {
			System.out.println("---! errore get all schede fallito.");
			e.printStackTrace();
		}
		disconnetti();
		System.out.println("---X fine get all schede!!!");
		return risultato;
	}

	public ArrayList<ListaDTO> getAllListe() {
		System.out.println("\n---> get all liste...");
		connetti();
		String sql = "select l.id as id, tv.nome as tipo, l.nome as nome from liste as l "
				+ "join tipi_votabile as tv on l.tipo_candidato = tv.id;";
		ArrayList<ListaDTO> risultato = new ArrayList<ListaDTO>();
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet r = statement.executeQuery();
			while (r.next()) {
				int id = r.getInt("id");
				risultato.add(new ListaDTO(id, r.getString("nome"), r.getString("tipo"), getMembri(id)));
			}
		} catch (Exception e) {
			System.out.println("---! errore get all liste fallito.");
			e.printStackTrace();
		}
		disconnetti();
		System.out.println("---X fine get all liste!!!");
		return risultato;
	}

	private ArrayList<Integer> getMembri(int idLista) {
		System.out.println("\n---> get membri...");
		connetti();
		String sql = "select id_candidato as c from membri where id_lista = ?;";
		ArrayList<Integer> risultato = new ArrayList<Integer>();
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, idLista);
			ResultSet r = statement.executeQuery();
			while (r.next()) {
				risultato.add(r.getInt("c"));
			}
		} catch (Exception e) {
			System.out.println("---! errore membri fallito.");
			e.printStackTrace();
		}
		disconnetti();
		System.out.println("---X fine get membri!!!");
		return risultato;
	}

	public String getReferendum(int idScheda) {
		System.out.println("\n---> prendi scheda referendum...");
		connetti();
		String sql = "select referendum from schede as s join schede_referendum as sr on s.id = sr.id where sr.id = ?;";
		String value = null;
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, idScheda);
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				value = result.getString("referendum");
			}
		} catch (SQLException e) {
			System.out.println("---! errore prendi scheda referendum fallito.");
			e.printStackTrace();
		}
		disconnetti();
		System.out.println("---X fine prendi scheda referendum!!!");
		return value;
	}

	public String getTipoCandidatiSchedaCategorica(int idScheda) {
		System.out.println("\n---> prendi tipo candidati scheda categorica...");
		connetti();
		String sql = "select tv.nome as tipo from schede_categoriche as sc "
				+ "join tipi_votabile as tv on tv.id = sc.tipo_candidati where sc.id = ? limit 1;";
		String value = "";
		try {
			PreparedStatement statement_tipo = conn.prepareStatement(sql);
			statement_tipo.setInt(1, idScheda);
			ResultSet r = statement_tipo.executeQuery();
			if (r.next()) {
				System.out.println("CHIAVE?? "+ r.toString());
				value = r.getString(1);
			}
		} catch (Exception e) {
			System.out.println("---! errore prendi tipo candidati scheda categorica fallito.");
			e.printStackTrace();
		}
		disconnetti();
		System.out.println("---X fine prendi tipo candidati scheda categorica!!!");
		System.out.println("++++++++++++NULLLE DAO "+ value + " ----");
		return value;
	}

	public String getTipoCandidatiSchedaOrdinale(int idScheda) {
		System.out.println("\n---> prendi tipo candidati scheda ordinale...");
		connetti();
		String sql = "select tv.nome as tipo_candidati from schede_ordinali as so "
				+ "join tipi_votabile as tv on tv.id = so.tipo_candidati where so.id = ? limit 1";
		String value = null;
		try {
			PreparedStatement statement_tipo = conn.prepareStatement(sql);
			statement_tipo.setInt(1, idScheda);
			ResultSet r = statement_tipo.executeQuery();
			if (r.next()) {
				value = r.getString(1);
			}
		} catch (Exception e) {
			System.out.println("---! errore prendi tipo candidati scheda ordinale fallito.");
			e.printStackTrace();
		}
		disconnetti();
		System.out.println("---X fine prendi tipo candidati scheda ordinale!!!");
		return value;
	}

	public ArrayList<Integer> getAspiranti(int idScheda) {
		System.out.println("\n---> prendi aspiranti...");
		connetti();
		String sql = "select id_candidato from aspiranti where id_scheda = ?";
		ArrayList<Integer> value = new ArrayList<Integer>();
		try {
			PreparedStatement statement_tipo = conn.prepareStatement(sql);
			statement_tipo.setInt(1, idScheda);
			ResultSet r = statement_tipo.executeQuery();
			while (r.next()) {
				value.add(r.getInt(1));
			}
		} catch (Exception e) {
			System.out.println("---! errore prendi aspiranti fallito.");
			e.printStackTrace();
		}
		disconnetti();
		System.out.println("---X fine prendi aspiranti!!!");
		return value;
	}

	public ArrayList<Integer> getConcorrenti(int idScheda) {
		System.out.println("\n---> prendi concorrenti...");
		connetti();
		String sql = "select id_candidato from concorrenti where id_scheda = ?";
		ArrayList<Integer> value = new ArrayList<Integer>();
		try {
			PreparedStatement statement_tipo = conn.prepareStatement(sql);
			statement_tipo.setInt(1, idScheda);
			ResultSet r = statement_tipo.executeQuery();
			while (r.next()) {
				value.add(r.getInt(1));
			}
		} catch (Exception e) {
			System.out.println("---! errore prendi concorrenti fallito.");
			e.printStackTrace();
		}
		disconnetti();
		System.out.println("---X fine prendi concorrenti!!!");
		return value;
	}

	public void insertSchedaCategorica(LocalDate avvio, LocalDate termine, int creatore, Stato stato, Esito esito,
			TipoVotabile tipo, String nome, ArrayList<Votabile> aspiranti) {
		System.out.println("\n---> inserisci scheda categorica...");
		Integer id = insertScheda(avvio, termine, creatore, stato.id(), esito.id(), nome, TipoScheda.CATEGORICA.id());
		connetti();
		String sql_scheda = "insert ignore into schede_categoriche (id, tipo_candidati) values (?, ?);";
		StringBuilder sql_aspiranti = new StringBuilder();
		sql_aspiranti.append("insert ignore into aspiranti (id_scheda, id_candidato) values ");
		try {
			PreparedStatement statement_scheda = conn.prepareStatement(sql_scheda);
			statement_scheda.setInt(1, id);
			statement_scheda.setInt(2, tipo.id());
			statement_scheda.executeUpdate();

			for (int i = 0; i < aspiranti.size(); i++) {
				sql_aspiranti.append("(?, ?),");
			}

			sql_aspiranti.deleteCharAt(sql_aspiranti.length() - 1);
			sql_aspiranti.append(";");

			int i = 0;
			PreparedStatement statement_aspiranti = conn.prepareStatement(sql_aspiranti.toString());
			for (Votabile votabile : aspiranti) {
				statement_aspiranti.setInt(++i, id);
				statement_aspiranti.setInt(++i, votabile.getId());
			}
			statement_aspiranti.executeUpdate();
		} catch (Exception e) {
			System.out.println("---! inserisci elementi scheda categorica fallita.");
			e.printStackTrace();
		}
		disconnetti();
		System.out.println("---X fine inserisci elementi scheda categorica!!!");
	}

	public void insertSchedaOrdinale(LocalDate avvio, LocalDate termine, int creatore, Stato stato, Esito esito,
			TipoVotabile tipo, String nome, ArrayList<Votabile> partecipanti) {
		System.out.println("\n---> inserisci scheda ordinale...");
		System.out.println(avvio + " - " + termine + " - " + creatore + " - " + stato + " - " + esito + " - " + tipo
				+ " - " + nome + " - " + partecipanti);
		int id = insertScheda(avvio, termine, creatore, stato.id(), esito.id(), nome, TipoScheda.ORDINALE.id());
		connetti();
		String sql_scheda = "insert ignore into schede_ordinali (id, tipo_candidati) values (?, ?);";
		StringBuilder sql_concorrenti = new StringBuilder();
		sql_concorrenti.append("insert ignore into concorrenti (id_scheda, id_candidato) values ");
		try {
			PreparedStatement statement_scheda = conn.prepareStatement(sql_scheda);
			statement_scheda.setInt(1, id);
			statement_scheda.setInt(2, tipo.id());
			statement_scheda.executeUpdate();

			for (int i = 0; i < partecipanti.size(); i++) {
				sql_concorrenti.append("(?, ?),");
			}

			sql_concorrenti.deleteCharAt(sql_concorrenti.length() - 1);
			sql_concorrenti.append(";");

			int i = 0;
			PreparedStatement statement_aspiranti = conn.prepareStatement(sql_concorrenti.toString());
			for (Votabile votabile : partecipanti) {
				statement_aspiranti.setInt(++i, id);
				statement_aspiranti.setInt(++i, votabile.getId());
			}
			statement_aspiranti.executeUpdate();
		} catch (Exception e) {
			System.out.println("---! inserisci elementi scheda ordinali fallita.");
			e.printStackTrace();
		}
		disconnetti();
		System.out.println("---X fine inserisci elementi scheda ordinali!!!");
	}

	public void insertSchedaCategoricaPreferenza(LocalDate avvio, LocalDate termine, int creatore, Stato stato,
			Esito esito, String nome, ArrayList<Votabile> aspiranti) {
		System.out.println("\n---> inserisci scheda categorica con preferenza...");
		int id = insertScheda(avvio, termine, creatore, stato.id(), esito.id(), nome,
				TipoScheda.CATEGORICA_CON_PREFERENZE.id());
		connetti();
		String sql_scheda = "insert ignore into schede_categoriche_con_preferenze (id) values (?);";
		String sql_aspiranti = "insert ignore into partecipanti (id_scheda, id_lista) values (?, ?);";
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

	public ArrayList<RegioneDTO> getAllRegioni() {
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
		System.out.println("---X fine prendi nazionalità!!!");
		return risultato;
	}

	public ArrayList<ProvinciaDTO> getAllProvince() {
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
		System.out.println("---X fine prendi province!!!");
		return risultato;
	}

	public ArrayList<ComuneDTO> getAllComune() {
		System.out.println("\n---> prendi comune...");
		connetti();
		ArrayList<ComuneDTO> risultato = new ArrayList<ComuneDTO>();
		String sql = "select c.id, c.nome as comune, regione, c.provincia from comuni as c "
				+ "join province as p on c.provincia = p.id order by c.nome asc;";
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				risultato.add(new ComuneDTO(result.getInt("regione"), result.getInt("provincia"), result.getInt("id"),
						result.getString("comune")));
			}
		} catch (Exception e) {
			System.out.println("---! prendi comuni fallito.");
			e.printStackTrace();
		}
		disconnetti();
		System.out.println("---X fine prendi comuni!!!");
		return risultato;
	}

	public ArrayList<NazionalitaDTO> getAllNazionalita() {
		System.out.println("\n---> prendi nazionalità...");
		connetti();
		ArrayList<NazionalitaDTO> risultato = new ArrayList<NazionalitaDTO>();
		String sql = "select id, nome from paesi order by nome asc;";
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				risultato.add(new NazionalitaDTO(result.getInt("id"), result.getString("nome")));
			}
		} catch (Exception e) {
			System.out.println("---! prendi nazionalità fallito.");
			e.printStackTrace();
		}
		disconnetti();
		System.out.println("---X fine nazionalità comuni!!!");
		return risultato;
	}

	public boolean insertUtente(String nome, String cognome, String codiceFiscale, String password, LocalDate nascita,
			Comune residenza, Nazionalita nazionalita, TipoUtente tipologia, Sesso sesso) {
		System.out.println("\n---> inserisci utente...");
		boolean inserted = true;
		if (null != getSalt(codiceFiscale)) {
			inserted = false;
		} else {
			String saltedPassword = default_salt.concat(password);
			String cryptatedPassword = cryptaPassword(saltedPassword);
			System.out.println(cryptatedPassword);
			connetti();
			String sql = "insert ignore into utenti (nome, cognome, dataDiNascita, tipo, nazionalita, residenza,"
					+ "sesso, codiceFiscale, pwd, salt) values (?, ?, STR_TO_DATE(?, '%d/%m/%Y'),?, ?, ?, ?, ?, ?, ?);";
			try {
				PreparedStatement statement = conn.prepareStatement(sql);
				statement.setString(1, nome);
				statement.setString(2, cognome);
				statement.setString(3, dateFormatter(nascita));
				statement.setInt(4, tipologia.id());
				statement.setInt(5, nazionalita.getId());
				statement.setInt(6, residenza.getId());
				statement.setInt(7, sesso.id());
				statement.setString(8, codiceFiscale);
				statement.setString(9, cryptatedPassword);
				statement.setString(10, default_salt);
				statement.executeUpdate();
			} catch (Exception e) {
				System.out.println("---! inserisci utente fallito.");
				e.printStackTrace();
			}
			disconnetti();
			System.out.println("---X fine inserisci utente!!!");
		}
		return inserted;
	}

	public ArrayList<Integer> getSchedeCompilate(int idUtente) {
		System.out.println("\n---> prendi schede compilate...");
		connetti();
		String sql = "select id_scheda from schede_compilate where id_utente = ?;";
		ArrayList<Integer> value = new ArrayList<Integer>();
		try {
			PreparedStatement statement_tipo = conn.prepareStatement(sql);
			statement_tipo.setInt(1, idUtente);
			ResultSet r = statement_tipo.executeQuery();
			while (r.next()) {
				value.add(r.getInt(1));
			}
		} catch (Exception e) {
			System.out.println("---! errore prendi schede compilate fallito.");
			e.printStackTrace();
		}
		disconnetti();
		System.out.println("---X fine prendi schede compilate!!!");
		return value;
	}

	public ArrayList<Integer> getPartecipanti(int idScheda) {
		System.out.println("\n---> prendi partecipanti...");
		connetti();
		String sql = "select id_lista from partecipanti where id_scheda = ?;";
		ArrayList<Integer> value = new ArrayList<Integer>();
		try {
			PreparedStatement statement_tipo = conn.prepareStatement(sql);
			statement_tipo.setInt(1, idScheda);
			ResultSet r = statement_tipo.executeQuery();
			while (r.next()) {
				value.add(r.getInt(1));
			}
		} catch (Exception e) {
			System.out.println("---! errore prendi partecipanti fallito.");
			e.printStackTrace();
		}
		disconnetti();
		System.out.println("---X fine prendi partecipanti!!!");
		return value;
	}

	public void insertVotoReferendum(int id_scheda, Referendum voto) {
		System.out.println("\n---> inserisci voto referendum...");
		connetti();
		String sql = "insert ignore into voti_scheda_referendum(id_scheda, voto) values (?, ?);";
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, id_scheda);
			switch (voto) {
			case FAVOREVOLE:
				statement.setByte(2, (byte) 1);
				break;
			case NON_FAVOREVOLE:
				statement.setByte(2, (byte) 0);
				break;
			case NULLO:
				statement.setNull(2, Types.BIT);
				break;
			default:
				Exception e = new Exception("valore voto referendum non valido");
				throw e;
			}
			statement.executeUpdate();
		} catch (Exception e) {
			System.out.println("---! errore inserisci voto referendum fallito.");
			e.printStackTrace();
		}
		disconnetti();
		System.out.println("---X fine inserisci voto referendum!!!");
	}

	public void insertSchedeCompilate(int id_utente, int id_scheda) {
		System.out.println("\n---> inserisci scheda votata...");
		connetti();
		String sql = "insert ignore into schede_compilate (id_utente, id_scheda) values (?, ?);";
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, id_utente);
			statement.setInt(2, id_scheda);
			statement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("---! errore prendi schede votabili fallito.");
			e.printStackTrace();
		}
		disconnetti();
		System.out.println("---X fine inserisci scheda votata!!!");
	}

	public void insertVotoCategorico(int id_scheda, Votabile voto) {
		System.out.println("\n---> inserisci voto categorico...");
		connetti();
		String sql = "insert voti_scheda_categorica(id_scheda, id_candidato) values (?,?)";
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, id_scheda);
			if (null != voto) {
				statement.setInt(2, voto.getId());
			} else {
				statement.setNull(2, Types.INTEGER);
			}
			statement.executeUpdate();
		} catch (Exception e) {
			System.out.println("---! errore inserisci voto categorico fallito.");
			e.printStackTrace();
		}
		disconnetti();
		System.out.println("---X fine inserisci voto categorico!!!");
	}

	public void insertVotoOrdinale(int id_scheda, ArrayList<VotoOrdinale> voto) {
		System.out.println("\n---> inserisci voto ordinale...");
		connetti();
		StringBuilder sql = new StringBuilder(
				"insert voti_scheda_ordinale(id_scheda, id_candidato, posizione) values (?,?,?),");
		if (null != voto) {
			for (int i = 0; i < voto.size() - 1; i++) {
				sql.append("(?,?,?),");
			}
		}
		sql.deleteCharAt(sql.length() - 1).append(";");
		try {
			PreparedStatement statement = conn.prepareStatement(sql.toString());
			if (null != voto) {
				int j = 1;
				for (int i = 0; i < voto.size(); i++) {
					VotoOrdinale valore = voto.get(i);
					statement.setInt(j, id_scheda);
					j++;
					statement.setInt(j, valore.getVoto().getId());
					j++;
					statement.setInt(j, valore.getPosizione());
					j++;

				}
			} else {
				statement.setInt(1, id_scheda);
				statement.setNull(2, Types.INTEGER);
				statement.setNull(3, Types.INTEGER);
			}
			statement.executeUpdate();
		} catch (Exception e) {
			System.out.println("---! errore inserisci voto ordinale fallito.");
			e.printStackTrace();
		}
		disconnetti();
		System.out.println("---X fine inserisci voto ordinale!!!");
	}

	public void insertVotaSchedaCategoricaConPreferenze(int id_scheda, int id_lista, ArrayList<Votabile> voto) {
		System.out.println("\n---> inserisci voto categorico con preferenza...");
		connetti();
		String sql = "insert into voti_scheda_categorica_con_preferenza (id_scheda, id_lista) values (?,?);";
		Integer key = null;
		try {
			PreparedStatement statement = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
			if (null != voto) {
				statement.setInt(1, id_scheda);
				statement.setInt(2, id_lista);
			} else {
				statement.setInt(1, id_scheda);
				statement.setNull(2, Types.INTEGER);
			}
			statement.executeUpdate();
			if (null != voto) {
				ResultSet rs = statement.getGeneratedKeys();
				if (rs.next()) {
					key = Integer.valueOf(rs.getInt(1));
					StringBuilder sql_preferenze = new StringBuilder(
							"insert preferenze(id_voto, id_candidato) values (?,?),");
					for (int i = 0; i < voto.size() - 1; i++) {
						sql_preferenze.append("(?,?),");
					}
					sql_preferenze.deleteCharAt(sql_preferenze.length() - 1).append(";");
					PreparedStatement statement_preferenze = conn.prepareStatement(sql_preferenze.toString());
					int j = 1;
					for (int i = 0; i < voto.size(); i++) {
						statement_preferenze.setInt(j, key);
						j++;
						statement_preferenze.setInt(j, voto.get(i).getId());
						j++;
					}
					statement_preferenze.execute();
				} else {
					throw new Exception();
				}
			}
		} catch (Exception e) {
			System.out.println("---! errore inserisci voto categorico con preferenza fallito.");
			e.printStackTrace();
		}

		disconnetti();
		System.out.println("---X fine inserisci voto categorico con preferenza!!!");
	}
	
	public ArrayList<Integer> getVotiCategorici(Scheda scheda) {
		System.out.println("\n---> prendi voti categorici...");
		connetti();
		String sql = "select id_candidato from voti_scheda_categorica where id_scheda = ?;";
		ArrayList<Integer> value = new ArrayList<Integer>();
		try {
			PreparedStatement statement_tipo = conn.prepareStatement(sql);
			statement_tipo.setInt(1, scheda.getId());
			ResultSet r = statement_tipo.executeQuery();
			while (r.next()) {
				value.add(r.getInt(1));
			}
		} catch (Exception e) {
			System.out.println("---! errore voti categorici fallito.");
			e.printStackTrace();
		}
		disconnetti();
		System.out.println("---X fine voti categorici!!!");
		return value;
	}
	
	public ArrayList<Integer[]> getVotiCategoriciConPreferenze(Scheda scheda) {
		System.out.println("\n---> prendi voti categorici con preferenza...");
		connetti();
		String sql = "select id_lista, id_candidato from voti_scheda_categorica_con_preferenza as vscc "
				+ "join preferenze as p on vscc.id = p.id_voto where id_scheda = ?;";
		ArrayList<Integer[]> values = new ArrayList<Integer[]>();
		try {
			PreparedStatement statement_tipo = conn.prepareStatement(sql);
			statement_tipo.setInt(1, scheda.getId());
			ResultSet r = statement_tipo.executeQuery();
			while (r.next()) {
				Integer[] value = new Integer[2];
				value[0] = r.getInt("id_lista");
				value[1] = r.getInt("id_candidato");
				values.add(value);
			}
		} catch (Exception e) {
			System.out.println("---! errore voti categorici con preferenza fallito.");
			e.printStackTrace();
		}
		disconnetti();
		System.out.println("---X fine voti categorici con preferenza!!!");
		return values;
	}
	
	public ArrayList<Integer[]> getVotiOrdinale(Scheda scheda) {
		System.out.println("\n---> prendi voti ordinali...");
		connetti();
		String sql = "select id_candidato, posizione from voti_scheda_ordinale where id_scheda = ?;";
		ArrayList<Integer[]> values = new ArrayList<Integer[]>();
		try {
			PreparedStatement statement_tipo = conn.prepareStatement(sql);
			statement_tipo.setInt(1, scheda.getId());
			ResultSet r = statement_tipo.executeQuery();
			while (r.next()) {
				Integer[] value = new Integer[2];
				value[0] = r.getInt("id_candidato");
				value[1] = r.getInt("posizione");
				values.add(value);
			}
		} catch (Exception e) {
			System.out.println("---! errore voti ordinali fallito.");
			e.printStackTrace();
		}
		disconnetti();
		System.out.println("---X fine voti ordinali!!!");
		return values;
	}
	
	public ArrayList<Integer> getVotiReferendum(Scheda scheda) {
		System.out.println("\n---> prendi voti referendum...");
		connetti();
		String sql = "select voto from voti_scheda_referendum where id_scheda = ?;";
		ArrayList<Integer> values = new ArrayList<Integer>();
		try {
			PreparedStatement statement_tipo = conn.prepareStatement(sql);
			statement_tipo.setInt(1, scheda.getId());
			ResultSet r = statement_tipo.executeQuery();
			while (r.next()) {
				values.add(r.getInt("voto"));
			}
		} catch (Exception e) {
			System.out.println("---! errore voti referendum fallito.");
			e.printStackTrace();
		}
		disconnetti();
		System.out.println("---X fine voti referendum!!!");
		return values;
	}
	
	public int contaUtenti() {
		System.out.println("\n---> prendi numero utenti...");
		connetti();
		String sql = "select count(id) as c from utenti";
		Integer count = null;
		try {
			PreparedStatement statement_tipo = conn.prepareStatement(sql);
			ResultSet r = statement_tipo.executeQuery();
			if (r.next()) {
				count = Integer.valueOf(r.getInt(1));
			}
		} catch (Exception e) {
			System.out.println("---! errore numero utenti fallito.");
			e.printStackTrace();
		}
		disconnetti();
		System.out.println("---X fine numero utenti!!!");
		return count;
		
	}
}