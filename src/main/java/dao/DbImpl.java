package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbImpl implements Db{

		public Connection connect() {
			String url = "jdbc:mysql://localhost:3306/voto_elettronico";
		    String username = "INGSW";
		    String password = "ProgettoINGSW";
		    java.sql.Connection connection = null;
		    try {
		    	System.out.println("Connecting database...");
		        connection = DriverManager.getConnection(url, username, password);
		        System.out.println("Database connected!");
		    } catch (SQLException e) {
		           System.err.println("Cannot connect the database!");
		           e.printStackTrace();
		       }
		       return connection;
		}
	}

}
