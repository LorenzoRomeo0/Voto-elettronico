package system.checkboxManagers;

import java.util.ArrayList;
import javafx.scene.control.CheckBox;

public class ListCheckBoxUpdater extends CheckBoxUpdater{
	private ArrayList<ListCheckBox> chks;
	private ArrayList<Integer> selectedChecks;
	private int selected;
	
	private int candidato;
	private int lista;
	
	public ListCheckBoxUpdater() {
		this.selected = -1;
		this.lista = -1;
		chks = new ArrayList<>();
		selectedChecks = new ArrayList<>();
	}
	
	@Override
	public void add(CheckBox chk) {
		this.chks.add(new ListCheckBox(lista, candidato,  chk));
	}

	@Override
	public void clear() {
		chks.clear();
	}
	
	@Override
	public void selected(int pos) {
		ListCheckBox sel = chks.get(pos);
		selected = sel.getLista();
		selectedChecks.clear();
		for (int i = 0; i < chks.size(); i++) {
			ListCheckBox a = chks.get(i);
			if(a.getLista() != selected) {
				a.getCheckbox().setSelected(false);
			} else {
				if(a.getCheckbox().isSelected()) {
					selectedChecks.add(i);
				}
			}
		}
	}

	public int getSelectedLista() {
		return selected;
	}
	
	@Override
	public void reselectOld() {
		for (int i = 0; i < chks.size(); i++) {
			chks.get(i).getCheckbox().setSelected(false);
			for (int j = 0; j < selectedChecks.size(); j++) {
				if(i == selectedChecks.get(j)) {
					chks.get(i).getCheckbox().setSelected(true);
				} 
			}
		}
	}
	
	public int getNextId() {
		return chks.size();
	}
	
	public void setLista(int lista) {
		this.lista = lista;
	}

	public int getCandidato() {
		return candidato;
	}

	public void setCandidato(int candidato) {
		this.candidato = candidato;
	}

	public ArrayList<ListCheckBox> getValues() {
		ArrayList<ListCheckBox> values = new ArrayList<ListCheckBox>();
		for (int i = 0; i < chks.size(); i++) {
			for (int j = 0; j < selectedChecks.size(); j++) {
				if(i == selectedChecks.get(j)) {
					values.add(chks.get(i));
				} 
			}
		}
		if(values.size() == 0) {
			values = null;
		}
		return values;
	}
}
