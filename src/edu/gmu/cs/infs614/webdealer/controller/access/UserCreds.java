package edu.gmu.cs.infs614.webdealer.controller.access;

public class UserCreds {

	private static String login;
	
	public UserCreds(String login) {
		UserCreds.login = login;
	}
	
	// getters
	public static String getLogin() {
		return login;
	}
	
}
