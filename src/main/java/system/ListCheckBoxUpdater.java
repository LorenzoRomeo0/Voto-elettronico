package system;

import java.util.ArrayList;
import javafx.scene.control.CheckBox;

public class ListCheckBoxUpdater extends CheckBoxUpdater{
	private ArrayList<ListCheckbox> chks;
	private ArrayList<Integer> selectedChecks;
	private int selected;
	private int lista;
	
	public ListCheckBoxUpdater() {
		this.selected = -1;
		this.lista = -1;
		chks = new ArrayList<>();
		selectedChecks = new ArrayList<>();
	}
	
	@Override
	public void add(CheckBox chk) {
		this.chks.add(new ListCheckbox(lista, chk));
	}

	@Override
	public void clear() {
		chks.clear();
	}
	
	@Override
	public void selected(int pos) {
		System.out.println(chks);
		ListCheckbox sel = chks.get(pos);
		selected = sel.getLista();
		selectedChecks.clear();
		for (int i = 0; i < chks.size(); i++) {
			ListCheckbox a = chks.get(i);
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
	
	public ArrayList<Integer> isOneTrue() {
		
	}
}

class ListCheckbox {
	private int lista;
	private CheckBox checkbox;
	
	public ListCheckbox(int lista, CheckBox checkbox) {
		super();
		this.lista = lista;
		this.checkbox = checkbox;
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
		return "ListCheckbox [lista=" + lista + ", checkbox=" + checkbox.getText() + "]";
	}

	
}
