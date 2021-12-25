package model;


import dao.UserDAO;
import dao.UserDAOImpl;
import dao.UserDTO;

public class ModelLogin {
	private UserDAO userDAO;
	
	public ModelLogin(){
		userDAO = new UserDAOImpl();
	}
	
	public boolean login(String username, String password) {
		return userDAO.checkPassword(username, password);
	}
	
	public UserDTO getUser(String username) {
		return userDAO.getUser(username);
	}
		
}