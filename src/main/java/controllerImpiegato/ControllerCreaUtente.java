package controllerImpiegato;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import dao.SistemaVotazioniDAO;
import data.Sesso;
import data.TipoUtente;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import system.SessionSystem;
import system.Valore;
import system.luoghi.Comune;
import system.luoghi.Nazionalita;
import system.luoghi.Provincia;
import system.luoghi.Regione;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;

public class ControllerCreaUtente {
	@FXML
	private Label txt_error;

	@FXML
	private PasswordField pwd_password1;

	@FXML
	private PasswordField pwd_password2;

	@FXML
	private TextField txt_codiceFiscale;

	@FXML
	private TextField txt_cognome;

	@FXML
	private TextField txt_nome;

	@FXML
	private VBox content;

	@FXML
	private DatePicker dpk_nascita;

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private ComboBox<Comune> cb_comune;

	@FXML
	private ComboBox<Valore> cb_genere;

	@FXML
	private ComboBox<Nazionalita> cb_nazionalita;

	@FXML
	private ComboBox<Provincia> cb_provincia;

	@FXML
	private ComboBox<Regione> cb_regione;

	@FXML
	private ComboBox<Valore> cb_tipologia;

	private final String chk_codiceFiscale = "^[A-Z]{6}[0-9]{2}[A-Z][0-9]{2}[A-Z][0-9]{3}[A-Z]$";
	private final String chk_password = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@$!%*?&])[A-Za-z0-9@$!%*?&]{8,}$";

	@FXML
	void aggiungi_utente(ActionEvent event) {
		txt_error.setTextFill(Paint.valueOf("red"));
		txt_error.setVisible(false);
		String nome = txt_nome.getText();
		String cognome = txt_cognome.getText();
		String codiceFiscale = txt_codiceFiscale.getText().toUpperCase();
		String password1 = pwd_password1.getText();
		String password2 = pwd_password2.getText();
		LocalDate nascita = dpk_nascita.getValue();
		Comune comune = cb_comune.getValue();
		Nazionalita nazionalita = cb_nazionalita.getValue();
		TipoUtente tipologia = (TipoUtente) cb_tipologia.getValue().getObj();
		Sesso sesso = (Sesso) cb_genere.getValue().getObj();
		if (nome.isBlank()) {
			txt_error.setText("Errore!!! Inserire nome.");
			txt_error.setVisible(true);
		} else if (cognome.isBlank()) {
			txt_error.setText("Errore!!! Inserire cognome.");
			txt_error.setVisible(true);
		} else if (!codiceFiscale.matches(chk_codiceFiscale)) {
			txt_error.setText("Errore!!! Codice fiscale inserito non valido.");
			txt_error.setVisible(true);
		} else if (!password1.matches(chk_password)) {
			txt_error.setText("Errore!!! password non valida. " + "\n - Lunghezza minima 8 "
					+ "\n - Almeno una lettera maiscola " + "\n - Almeno una lettera minuscola"
					+ "\n - Almeno un numero" + "\n - Almeno un carattere speciale @$!%*?&.");
			txt_error.setVisible(true);
		} else if (!password1.equals(password2)) {
			txt_error.setText("Errore!!! Le password non combaciano.");
			txt_error.setVisible(true);
		} else if (nascita.isAfter(LocalDate.now().minusYears(18))) {
			txt_error.setText("Errore!!! Data di nascita non valida.");
			txt_error.setVisible(true);
		} else if (null == tipologia) {
			txt_error.setText("Errore!!! Inserire tipo utente.");
			txt_error.setVisible(true);
		} else if (null == nazionalita) {
			txt_error.setText("Errore!!! Inserire nazionalità.");
			txt_error.setVisible(true);
		} else if (null == sesso) {
			txt_error.setText("Errore!!! Inserire sesso.");
			txt_error.setVisible(true);
		} else if (null == comune) {
			txt_error.setText("Errore!!! Inserire comune di residenza.");
			txt_error.setVisible(true);
		} else {
			SistemaVotazioniDAO dao = SistemaVotazioniDAO.getInstance();
			if (dao.insertUtente(nome, cognome, codiceFiscale, password1, 
					nascita, comune, nazionalita, tipologia, sesso)) {
				txt_error.setTextFill(Paint.valueOf("black"));
				txt_error.setText("Utente aggiunto con successo!!!");
				txt_error.setVisible(true);
			} else {
				txt_error.setText("Errore durante l'inserimento del nuovo utente.");
				txt_error.setVisible(true);
			}
			
		}
	}

	@FXML
	void filter_comune(KeyEvent event) {
		if (event.getCode() != null && cb_comune.isShowing()) {
			@SuppressWarnings("unchecked")
			ArrayList<Comune> clone = (ArrayList<Comune>) comune_all.clone();
			Regione value_regione = cb_regione.getValue();
			Provincia value_provincia = cb_provincia.getValue();
			if (null != value_regione) {
				clone.removeIf(t -> t.getProvincia().getRegione().getId() != value_regione.getId());
			}
			if (null != value_provincia) {
				clone.removeIf(t -> t.getProvincia().getId() != value_provincia.getId());
			}
			KeyCode code = event.getCode();
			if (code.equals(KeyCode.BACK_SPACE)) {
				comune_filter.setLength(0);
			} else if (code.isLetterKey()) {
				comune_filter.append(code);
				clone.removeIf(t -> !t.getNome().toUpperCase().startsWith(comune_filter.toString()));
			}
			cb_comune.setItems(FXCollections.observableArrayList(clone));
		}
	}

	@FXML
	void filter_nazionalita(KeyEvent event) {
		KeyCode code = event.getCode();
		if (code.equals(KeyCode.BACK_SPACE)) {
			nazionalita_filter.setLength(0);
		} else if (code.isLetterKey()) {
			nazionalita_filter.append(code);
		}
		@SuppressWarnings("unchecked")
		ArrayList<Nazionalita> clone = (ArrayList<Nazionalita>) nazionalita_all.clone();
		clone.removeIf(t -> !t.getNome().toUpperCase().startsWith(nazionalita_filter.toString()));
		cb_nazionalita.setItems(FXCollections.observableArrayList(clone));
	}

	@FXML
	void filter_provincia(KeyEvent event) {
		if (event.getCode() != null && cb_provincia.isShowing()) {
			@SuppressWarnings("unchecked")
			ArrayList<Provincia> clone = (ArrayList<Provincia>) provincia_all.clone();
			Regione value_regione = cb_regione.getValue();
			if (null != value_regione) {
				clone.removeIf(t -> t.getRegione().getId() != value_regione.getId());
			}
			KeyCode code = event.getCode();
			if (code.equals(KeyCode.BACK_SPACE)) {
				provincia_filter.setLength(0);
			} else if (code.isLetterKey()) {
				provincia_filter.append(code);
				clone.removeIf(t -> !t.getNome().toUpperCase().startsWith(provincia_filter.toString()));
			}
			cb_provincia.setItems(FXCollections.observableArrayList(clone));
		}
	}

	@FXML
	void filter_regione(KeyEvent event) {
		if (event.getCode() != null && cb_regione.isShowing()) {
			@SuppressWarnings("unchecked")
			ArrayList<Regione> clone = (ArrayList<Regione>) regione_all.clone();
			KeyCode code = event.getCode();
			if (code.equals(KeyCode.BACK_SPACE)) {
				regione_filter.setLength(0);
			} else if (code.isLetterKey()) {
				regione_filter.append(code);
				clone.removeIf(t -> !t.getNome().toUpperCase().startsWith(regione_filter.toString()));
			}
			cb_regione.setItems(FXCollections.observableArrayList(clone));
		}
	}

	@FXML
	void activate_provincia(ActionEvent event) {
		cb_provincia.setDisable(false);
		cb_comune.setDisable(true);
		@SuppressWarnings("unchecked")
		ArrayList<Provincia> clone = (ArrayList<Provincia>) provincia_all.clone();
		Regione value = cb_regione.getValue();
		if (null != value) {
			clone.removeIf(t -> t.getRegione().getId() != value.getId());
		}
		cb_provincia.setItems(FXCollections.observableArrayList(clone));
	}

	@FXML
	void activate_comune(ActionEvent event) {
		cb_comune.setDisable(false);
		@SuppressWarnings("unchecked")
		ArrayList<Comune> clone = (ArrayList<Comune>) comune_all.clone();
		Regione value_regione = cb_regione.getValue();
		Provincia value_provincia = cb_provincia.getValue();
		if (null != value_regione) {
			clone.removeIf(t -> t.getProvincia().getRegione().getId() != value_regione.getId());
		}
		if (null != value_provincia) {
			clone.removeIf(t -> t.getProvincia().getId() != value_provincia.getId());
		}
		cb_comune.setItems(FXCollections.observableArrayList(clone));
	}

	private StringBuilder nazionalita_filter;
	private StringBuilder regione_filter;
	private StringBuilder provincia_filter;
	private StringBuilder comune_filter;

	private ArrayList<Nazionalita> nazionalita_all;
	private ArrayList<Regione> regione_all;
	private ArrayList<Provincia> provincia_all;
	private ArrayList<Comune> comune_all;

	@FXML
	void initialize() {
		assert cb_comune != null : "fx:id=\"cb_comune\" was not injected: check your FXML file 'crea_utente.fxml'.";
		assert cb_genere != null : "fx:id=\"cb_genere\" was not injected: check your FXML file 'crea_utente.fxml'.";
		assert cb_nazionalita != null
				: "fx:id=\"cb_nazionalita\" was not injected: check your FXML file 'crea_utente.fxml'.";
		assert cb_provincia != null
				: "fx:id=\"cb_provincia\" was not injected: check your FXML file 'crea_utente.fxml'.";
		assert cb_regione != null : "fx:id=\"cb_regione\" was not injected: check your FXML file 'crea_utente.fxml'.";
		assert cb_tipologia != null
				: "fx:id=\"cb_tipologia\" was not injected: check your FXML file 'crea_utente.fxml'.";
		assert content != null : "fx:id=\"content\" was not injected: check your FXML file 'crea_utente.fxml'.";
		assert dpk_nascita != null : "fx:id=\"dpk_nascita\" was not injected: check your FXML file 'crea_utente.fxml'.";
		assert pwd_password1 != null
				: "fx:id=\"pwd_password1\" was not injected: check your FXML file 'crea_utente.fxml'.";
		assert pwd_password2 != null
				: "fx:id=\"pwd_password2\" was not injected: check your FXML file 'crea_utente.fxml'.";
		assert txt_codiceFiscale != null
				: "fx:id=\"txt_codiceFiscale\" was not injected: check your FXML file 'crea_utente.fxml'.";
		assert txt_cognome != null : "fx:id=\"txt_cognome\" was not injected: check your FXML file 'crea_utente.fxml'.";
		assert txt_error != null : "fx:id=\"txt_error\" was not injected: check your FXML file 'crea_utente.fxml'.";
		assert txt_nome != null : "fx:id=\"txt_nome\" was not injected: check your FXML file 'crea_utente.fxml'.";

		SessionSystem session = SessionSystem.getInstance();
		VBox container_content = session.getContent();
		container_content.widthProperty().addListener((observable, oldValue, newValue) -> {
			adatta_contenuto(newValue.doubleValue());
		});

		Stage stage = session.getStage();
		stage.resizableProperty().addListener((observable, oldValue, newValue) -> {
			adatta_contenuto(container_content.getWidth());
		});

		LocalDate maggiorenne = LocalDate.now().minusYears(18);
		dpk_nascita.setDayCellFactory(param -> new DateCell() {
			@Override
			public void updateItem(LocalDate date, boolean empty) {
				super.updateItem(date, empty);
				setDisable(empty || date.compareTo(maggiorenne) > 0);
			}
		});
		dpk_nascita.setValue(maggiorenne.minusDays(1));

		nazionalita_filter = new StringBuilder();
		regione_filter = new StringBuilder();
		provincia_filter = new StringBuilder();
		comune_filter = new StringBuilder();

		nazionalita_all = session.getNazionalita();
		regione_all = session.getRegioni();
		provincia_all = session.getProvince();
		comune_all = session.getComuni();
		
		@SuppressWarnings("unchecked")
		ArrayList<Nazionalita> nazionalita = (ArrayList<Nazionalita>) nazionalita_all.clone();
		cb_nazionalita.setItems(FXCollections.observableArrayList(nazionalita));

		@SuppressWarnings("unchecked")
		ArrayList<Regione> regione = (ArrayList<Regione>) regione_all.clone();
		cb_regione.setItems(FXCollections.observableArrayList(regione));
		
		ArrayList<Valore> tipi = Valore.toArray(TipoUtente.values());
		cb_tipologia.setItems(FXCollections.observableArrayList(tipi));
		
		ArrayList<Valore> sessi = Valore.toArray(Sesso.values());
		cb_genere.setItems(FXCollections.observableArrayList(sessi));
	}

	private void adatta_contenuto(double larghezza) {
		content.setMinWidth(larghezza);
		content.setMaxWidth(larghezza);
	}
}
