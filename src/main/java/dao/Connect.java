package dao;

import java.sql.Connection;
import java.sql.DriverManager;

public abstract class Connect {

	final private String url = "jdbc:mysql://localhost:3306/sistema_votazioni";
	final private String username = "INGSW";
	final private String password = "ProgettoINGSW";
	protected Connection conn;

	protected void connetti() {
		try {
			System.out.println("Connesione al database...");
			conn = DriverManager.getConnection(url, username, password);
			System.out.println("Connesso al database!!!");
		} catch (Exception e) {
			System.out.println("outore durante la connnessione al database.");
		}
	}
	
	protected void disconnetti() {
		try {
			System.out.println("Chiusura connesione al database...");
			conn.close();
			System.out.println("Connessione al database chiusa!!!");
		} catch (Exception e) {
			System.out.println("outore durante la chiusura della connnessione al database.");
		}
	}

}
