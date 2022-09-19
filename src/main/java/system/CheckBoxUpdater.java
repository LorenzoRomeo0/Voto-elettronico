package system;

import java.util.ArrayList;
import javafx.scene.control.CheckBox;

public class CheckBoxUpdater {
	private ArrayList<CheckBox> chks = new ArrayList<>();
	private int selected;

	public CheckBoxUpdater() {
		this.selected = -1;
	}

	public void add(CheckBox chk) {
		this.chks.add(chk);
	}

	public void clear() {
		chks.clear();
	}

	public void selected(int pos) {
		this.selected = pos;
		for (int i = 0; i < chks.size(); i++) {
			if (i != pos) {
				chks.get(i).setSelected(false);
			}
		}
	}

	public int getSelected() {
		return selected;
	}

	public void reselectOld() {
		if (selected != -1) {
			chks.get(selected).setSelected(true);
		}
	}
}