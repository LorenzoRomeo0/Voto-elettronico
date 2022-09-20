package system;

import java.io.IOException;
import java.util.ArrayList;
import dao.CandidatoDTO;
import dao.ComuneDTO;
import dao.ListaDTO;
import dao.NazionalitaDTO;
import dao.PartitoDTO;
import dao.ProvinciaDTO;
import dao.RegioneDTO;
import dao.SchedaDTO;
import dao.SistemaVotazioniDAO;
import data.Stato;
import data.TipoScheda;
import data.TipoVotabile;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import system.luoghi.Comune;
import system.luoghi.Nazionalita;
import system.luoghi.Provincia;
import system.luoghi.Regione;
import system.schede.Scheda;
import system.schede.SchedaFactory;
import system.utenti.Elettore;
import system.utenti.Utente;
import system.votabili.Candidato;
import system.votabili.Lista;
import system.votabili.Partito;
import system.votabili.Votabile;

public class SessionSystem {
	public static final int stageMinW = 450;
	public static final int stageMinH = 350;
	private static SessionSystem session;

	private Stage stage;

	private VBox content;
	private Utente utente;
	private Scheda scheda;

	private ArrayList<Scheda> schede;

	private ArrayList<Partito> partiti;
	private ArrayList<Candidato> candidati;
	private ArrayList<Lista> liste;

	private ArrayList<Regione> regioni;
	private ArrayList<Provincia> province;
	private ArrayList<Comune> comuni;

	private ArrayList<Nazionalita> nazionalita;

	private Stage popup;
	private SistemaVotazioniDAO dao;

	private Object[] message;

	private SessionSystem() {
		dao = SistemaVotazioniDAO.getInstance();
		this.partiti = convertiPartiti(dao.getAllPartiti());
		this.candidati = convertiCandidati(dao.getAllCandidati());
		this.liste = loadListe();
		this.schede = loadSchede();
		this.regioni = convertiRegione(dao.getAllRegioni());
		this.province = loadProvince();
		this.comuni = loadComuni();
		this.nazionalita = convertiPaesi(dao.getAllNazionalita());
	}

	public Comune getComune(int id) {
		for (Comune comune : comuni) {
			if (comune.getId() == id) {
				return comune;
			}
		}
		return null;
	}

	public static SessionSystem getInstance() {
		if (null == session) {
			session = new SessionSystem();
		}
		return session;
	}

	private ArrayList<Comune> loadComuni() {
		ArrayList<ComuneDTO> comuni_db = dao.getAllComune();
		ArrayList<Comune> comuni = new ArrayList<Comune>();
		for (ComuneDTO comune : comuni_db) {
			try {
				comuni.add(new Comune(comune, getProvincia(comune.getProvincia())));
			} catch (Exception e) {
				System.out.println("Errore con comune '" + comune.getNome() + "'.");
				e.printStackTrace();
			}
		}
		return comuni;
	}

	private Provincia getProvincia(Integer id) throws Exception {
		if (null != id) {
			for (Provincia provincia : province) {
				if (provincia.getId() == id) {
					return provincia;
				}
			}
		}
		throw new Exception("Errore con get provincia!!!");
	}

	private ArrayList<Provincia> loadProvince() {
		ArrayList<ProvinciaDTO> province_db = dao.getAllProvince();
		ArrayList<Provincia> province = new ArrayList<Provincia>();
		for (ProvinciaDTO provincia : province_db) {
			try {
				Regione regione = getRegione(provincia.getRegione());
				province.add(new Provincia(provincia, regione));
			} catch (Exception e) {
				System.out.println("Errore con province '" + provincia.getNome() + "'.");
				e.printStackTrace();
			}
		}
		return province;
	}

	private Regione getRegione(Integer id) throws Exception {
		if (null != id) {
			for (Regione regione : regioni) {
				if (regione.getId() == id) {
					return regione;
				}
			}
		}
		throw new Exception("Errore con get regione!!!");
	}

	private ArrayList<Lista> loadListe() {
		ArrayList<ListaDTO> liste_db = dao.getAllListe();
		ArrayList<Lista> liste = new ArrayList<Lista>();
		for (ListaDTO lista : liste_db) {
			try {
				ArrayList<Votabile> candidati = new ArrayList<Votabile>();
				TipoVotabile tipo = TipoVotabile.valueOf(lista.getTipoLista());
				for (Integer id : lista.getCandidati()) {
					switch (tipo) {
					case CANDIDATO:
						candidati.add(getCandidato(id));
						break;
					case PARTITO:
						candidati.add(getPartito(id));
						break;
					default:
						throw new Exception();
					}
				}
				liste.add(new Lista(lista, candidati));
			} catch (Exception e) {
				System.out.println("Errore con lista '" + lista.getNome() + "'.");
				e.printStackTrace();
			}
		}
		return liste;
	}

	private ArrayList<Scheda> loadSchede() {
		ArrayList<SchedaDTO> schede_db = dao.getAllSchede();
		ArrayList<Scheda> schede = new ArrayList<Scheda>();

		for (SchedaDTO scheda_db : schede_db) {
			try {
				Scheda nuova = null;
				String tipo_string = null;
				int id_scheda = scheda_db.getId();
				TipoScheda tipo = TipoScheda.valueOf(scheda_db.getTipoScheda());
				if (tipo.equals(TipoScheda.REFERENDUM)) {
					String referendum = dao.getReferendum(id_scheda);
					if (null != referendum) {
						nuova = SchedaFactory.makeSchedaReferendum(scheda_db, referendum);
					}
				} else if (tipo.equals(TipoScheda.CATEGORICA)) {
					tipo_string = dao.getTipoCandidatiSchedaCategorica(id_scheda);
					ArrayList<Votabile> can2 = loadCandidatiCategorica(id_scheda, tipo_string);
					nuova = SchedaFactory.makeSchedaCategorico(scheda_db, can2);
				} else if (tipo.equals(TipoScheda.CATEGORICA_CON_PREFERENZE)) {
					ArrayList<Lista> can3 = loadListe(id_scheda);
					nuova = SchedaFactory.makeSchedaCategoricoPreferenze(scheda_db, can3);
				} else if (tipo.equals(TipoScheda.ORDINALE)) {
					tipo_string = dao.getTipoCandidatiSchedaOrdinale(id_scheda);
					ArrayList<Votabile> can1 = loadCandidatiOrdinale(id_scheda, tipo_string);
					nuova = SchedaFactory.makeSchedaOrdinale(scheda_db, can1);
				} else {
					throw new Exception();
				}
				schede.add(nuova);
			} catch (Exception e) {
				System.out.println("Errore con scheda '" + scheda_db.getNome() + "'.");
				e.printStackTrace();
			}
		}

		return schede;
	}

	private ArrayList<Lista> loadListe(int id_scheda) {
		SistemaVotazioniDAO dao = SistemaVotazioniDAO.getInstance();
		ArrayList<Lista> candidati = new ArrayList<Lista>();
		ArrayList<Integer> id_candidati = dao.getPartecipanti(id_scheda);
		for (Integer id : id_candidati) {
			for (Lista lista : liste) {
				if (lista.getId() == id) {
					candidati.add(lista);
					break;
				}
			}
		}
		return candidati;
	}

	private ArrayList<Votabile> loadCandidatiCategorica(int id_scheda, String tipoString) throws Exception {
		SistemaVotazioniDAO dao = SistemaVotazioniDAO.getInstance();
		TipoVotabile tipo = TipoVotabile.valueOf(tipoString);
		ArrayList<Integer> id_candidati = dao.getAspiranti(id_scheda);
		return loadCandidati(tipo, id_candidati);
	}

	private ArrayList<Votabile> loadCandidatiOrdinale(int id_scheda, String tipoString) throws Exception {
		SistemaVotazioniDAO dao = SistemaVotazioniDAO.getInstance();
		TipoVotabile tipo = TipoVotabile.valueOf(tipoString);
		ArrayList<Integer> id_candidati = dao.getConcorrenti(id_scheda);
		return loadCandidati(tipo, id_candidati);
	}

	private ArrayList<Votabile> loadCandidati(TipoVotabile tipo, ArrayList<Integer> id_candidati) throws Exception {
		ArrayList<Votabile> candidati = new ArrayList<Votabile>();
		for (Integer id : id_candidati) {
			switch (tipo) {
			case CANDIDATO:
				candidati.add(getCandidato(id));
				break;
			case PARTITO:
				candidati.add(getPartito(id));
				break;
			default:
				throw new Exception();
			}
		}
		return candidati;
	}

	private Candidato getCandidato(Integer id) throws Exception {
		if (null != id) {
			for (Candidato candidato : candidati) {
				if (candidato.getId() == id) {
					return candidato;
				}
			}
		}
		throw new Exception("Errore con get partito!!!");
	}

	private Partito getPartito(Integer id) throws Exception {
		if (null != id) {
			for (Partito partito : partiti) {
				if (partito.getId() == id) {
					return partito;
				}
			}
		}
		throw new Exception("Errore con get partito!!!");
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public Stage getStage() {
		return stage;
	}

	public void loadStage(String path, String titolo) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
		Parent root = loader.load();
		stage.setTitle(titolo);
		stage.setScene(new Scene(root, SessionSystem.stageMinW, SessionSystem.stageMinH));
		stage.show();
	}

	public void loadMain() {
		try {
			loadStage("/main/main.fxml", "Interfaccia principale");
		} catch (IOException e) {
			System.out.println("---> caricamento main fallito.");
			e.printStackTrace();
		}
	}

	public VBox getContent() {
		return content;
	}

	public void setContent(VBox content) {
		this.content = content;
	}

	public void setUtente(Utente Utente) {
		this.utente = Utente;
	}

	public Utente getUtente() {
		return utente;
	}

	public ArrayList<Scheda> getSchede() {
		return schede;
	}

	public ArrayList<Partito> getPartiti() {
		return partiti;
	}

	public ArrayList<Candidato> getCandidati() {
		return candidati;
	}

	public ArrayList<Lista> getListe() {
		return liste;
	}

	public ArrayList<Lista> getListe(String filtro) {
		ArrayList<Lista> values = new ArrayList<Lista>();
		for (Lista lista : liste) {
			if (lista.getNome().toLowerCase().startsWith(filtro)) {
				values.add(lista);
			}
		}
		return values;
	}

	public ArrayList<Candidato> getCandidati(String filtro) {
		ArrayList<Candidato> values = new ArrayList<Candidato>();
		for (Candidato candidato : candidati) {
			if (candidato.getCognome().toLowerCase().startsWith(filtro.toLowerCase())
					|| candidato.getNome().toLowerCase().startsWith(filtro.toLowerCase())) {
				values.add(candidato);
			}
		}
		return values;
	}

	public ArrayList<Partito> getPartiti(String filtro) {
		ArrayList<Partito> values = new ArrayList<Partito>();
		for (Partito partito : partiti) {
			if (partito.getNome().toLowerCase().startsWith(filtro)) {
				values.add(partito);
			}
		}
		return values;
	}

	public ArrayList<Partito> convertiPartiti(ArrayList<PartitoDTO> array) {
		ArrayList<Partito> valori = new ArrayList<Partito>();
		for (PartitoDTO a : array) {
			valori.add(new Partito(a));
		}
		return valori;
	}

	public ArrayList<Candidato> convertiCandidati(ArrayList<CandidatoDTO> array) {
		ArrayList<Candidato> valori = new ArrayList<Candidato>();
		for (CandidatoDTO candidato : array) {
			try {
				valori.add(new Candidato(candidato, getPartito(candidato.getPartito())));
			} catch (Exception e) {
				System.out.println("---! Errore get partito del candidato con id " + candidato.getId() + " fallito.");
				e.printStackTrace();
			}
		}
		return valori;
	}

	public ArrayList<Regione> convertiRegione(ArrayList<RegioneDTO> array) {
		ArrayList<Regione> valori = new ArrayList<Regione>();
		for (RegioneDTO regione : array) {
			try {
				valori.add(new Regione(regione));
			} catch (Exception e) {
				System.out.println("---! Errore get regione con id " + regione.getId() + " fallito.");
				e.printStackTrace();
			}
		}
		return valori;
	}

	public ArrayList<Nazionalita> convertiPaesi(ArrayList<NazionalitaDTO> array) {
		ArrayList<Nazionalita> valori = new ArrayList<Nazionalita>();
		for (NazionalitaDTO nazione : array) {
			try {
				valori.add(new Nazionalita(nazione));
			} catch (Exception e) {
				System.out.println("---! Errore get regione con id " + nazione.getId() + " fallito.");
				e.printStackTrace();
			}
		}
		return valori;
	}

	public ArrayList<Regione> getRegioni() {
		return regioni;
	}

	public ArrayList<Provincia> getProvince() {
		return province;
	}

	public ArrayList<Comune> getComuni() {
		return comuni;
	}

	public ArrayList<Nazionalita> getNazionalita() {
		return nazionalita;
	}

	public Scheda getScheda() {
		return scheda;
	}

	public void setScheda(Scheda scheda) {
		this.scheda = scheda;
	}

	public ArrayList<Scheda> getSchedeNonCompilate() {
		loadSchede();
		@SuppressWarnings("unchecked")
		ArrayList<Scheda> schedeNonCompilate = (ArrayList<Scheda>) schede.clone();
		if (utente instanceof Elettore) {
			Elettore e = (Elettore) utente;
			schedeNonCompilate.removeIf(t -> e.getLibretto().haVotato(t.getId()) || t.getStato().equals(Stato.CONCLUSO));
		}
		return schedeNonCompilate;
	}

	public ArrayList<Scheda> getSchedeCompilate(ArrayList<Integer> idSchede) {
		ArrayList<Scheda> compilate = new ArrayList<Scheda>();
		for (Integer id : idSchede) {
			for (Scheda scheda : schede) {
				if (scheda.getId() == id) {
					compilate.add(scheda);
					break;
				}
			}
		}
		return compilate;
	}
	
	public ArrayList<Scheda> getSchedeConcluse() {
		ArrayList<Scheda> compilate = new ArrayList<Scheda>();
		for (Scheda scheda : schede) {
			if (scheda.getStato().equals(Stato.CONCLUSO)) {
				compilate.add(scheda);
			}
		}
		return compilate;
	}

	public Object getMessage() {
		return message[0];
	}

	public Object getMessage(int i) {
		return message[i];
	}

	public void setMessage(Object... message) {
		this.message = message;
	}

	public Stage getPopup() {
		return popup;
	}

	public void setPopup(Stage popup) {
		this.popup = popup;
	}
}