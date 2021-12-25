package dao;

import java.sql.Statement;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class UserDAOImpl implements UserDAO{
	
	private ArrayList<UserDTO> users;
	private Connection conn;
	
	public UserDAOImpl() {
		conn = connect();
	}
	
	private Connection connect() {
		String url = "jdbc:mysql://localhost:3306/voto_elettronico";
	    String username = "INGSW";
	    String password = "ProgettoINGSW";
	    Connection connection = null;
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
	
	public UserDTO getUser(String username) {
		String sql = "SELECT USERID, username, birthDate, country, comune, codiceFiscale, userType, name, surname FROM users WHERE username = ?";
		try {
			PreparedStatement s = conn.prepareStatement(sql);
			s.setString(1, username);
			ResultSet result = s.executeQuery();
			if(result.next()) {
				BigDecimal USERID = result.getBigDecimal("USERID");
				Date birthDate = result.getDate("birthDate");
				String country = result.getString("country");
				String comune = result.getString("comune");
				String codiceFiscale = result.getString("codiceFiscale");
				String userType = result.getString("userType");
				String name = result.getString("name");
				String surname = result.getString("surname");
				UserDTO userDTO = new UserDTO(USERID, username, name, surname, birthDate, country, comune, codiceFiscale, userType);
				return userDTO;
			}
		}catch(Exception e) {e.printStackTrace();}
		return null;
	}
	
	public ArrayList<UserDTO> getAllUsers(){
		update();
		return users;
	}
	
	public void update() {
		String sql = "SELECT USERID, username, birthDate, country, comune, codiceFiscale, userType, name, surname FROM users";
		users = new ArrayList<UserDTO>();
		try {
			Statement s = conn.createStatement();
			ResultSet result = s.executeQuery(sql);
			while(result.next()) {
				BigDecimal USERID = result.getBigDecimal("USERID");
				String username = result.getString("username");
				Date birthDate = result.getDate("birthDate");
				String country = result.getString("country");
				String comune = result.getString("comune");
				String codiceFiscale = result.getString("codiceFiscale");
				String userType = result.getString("userType");
				String name = result.getString("name");
				String surname = result.getString("surname");
				users.add(new UserDTO(USERID, username, name, surname, birthDate, country, comune, codiceFiscale, userType));
			}
		}catch(Exception e) {e.printStackTrace();}
		
	}
	
	public boolean checkPassword(String username, String insertedPassword){
		String sql = "SELECT username, salt FROM users WHERE username = ? AND password = SHA1(concat(SHA1(?), salt))";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, insertedPassword);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) return true;
			else return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return false;
	}
	
	
}