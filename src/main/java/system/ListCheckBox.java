package system;

import javafx.scene.control.CheckBox;

public class ListCheckBox {
	
	private int lista;
	private int candidato;
	private CheckBox checkbox;
	
	public ListCheckBox(int lista, int candidato, CheckBox checkbox) {
		super();
		this.lista = lista;
		this.candidato = candidato;
		this.checkbox = checkbox;
	}

	public int getCandidato() {
		return candidato;
	}

	public void setCandidato(int candidato) {
		this.candidato = candidato;
	}

	public int getLista() {
		return lista;
	}

	public void setLista(int lista) {
		this.lista = lista;
	}

	public CheckBox getCheckbox() {
		return checkbox;
	}

	public void setCheckbox(CheckBox checkbox) {
		this.checkbox = checkbox;
	}

	@Override
	public String toString() {
		return "ListCheckbox [lista=" + lista + ", candidato=" + candidato + ", checkbox=" + checkbox + "]\n";
	}
}