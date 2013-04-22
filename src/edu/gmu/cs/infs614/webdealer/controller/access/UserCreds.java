package edu.gmu.cs.infs614.webdealer.controller.access;

public class UserCreds {

	private final String login;
	
	public UserCreds(String login) {
		this.login = login;
	}
	
	// getters
	public String getLogin() {
		return login;
	}
	
}
