package data;

public enum Sesso {
	MASCHIO(1), FEMMINA(2);
	
	final private int id;
	private Sesso(int i) {this.id = i;}
	final public int id() {return id;}
	
	public static Sesso sesso(int id) {
		for (Sesso s: Sesso.values()) {
			if(s.id == id)
			return s;
		}
		return null;
		
	}
}
