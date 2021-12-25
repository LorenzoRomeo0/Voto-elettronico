package data;

import dao.UserDTO;

public class Status {

	UserDTO userData;

	public Status(UserDTO userData) {
		this.userData = userData;
	}
	
	public UserDTO getUserData() {
		return userData;
	}
}
