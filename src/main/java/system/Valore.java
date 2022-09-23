package system;

import java.util.ArrayList;
import java.util.Collection;

public class Valore {
	private String nome;
	private Object obj;
	
	public Valore(String nome, Object obj) {
		super();
		this.nome = nome;
		this.obj = obj;
	}
	
	public /*@pure@*/  String getNome() {
		return nome;
	}
	
	/*@asssignable name;@*/
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public /*@pure@*/ Object getObj() {
		return obj;
	}
	
	/*@asssignable obj;@*/
	public /*@pure@*/void setObj(Object obj) {
		this.obj = obj;
	}

	@Override
	public String /*@pure@*/ toString() {
		return nome;
	}
	
	public static ArrayList<Valore> toArray(Collection<Object> array) {
		ArrayList<Valore> valori = new ArrayList<Valore>();
        for (Object v : array) {
        	valori.add(new Valore(v.toString().replace("_", " "), v));
		}
        return valori;
	}

	public static ArrayList<Valore> toArray(Object[] array) {
		ArrayList<Valore> valori = new ArrayList<Valore>();
        for (Object v : array) {
        	valori.add(new Valore(v.toString().replace("_", " "), v));
		}
        return valori;
	}
	
}
