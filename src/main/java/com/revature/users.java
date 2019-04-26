package com.revature;

public class users {
	public int id;
	public String firstname;
	public String lastname;
	public String username;
	public String password;
	public int authtype;
	
	users(int id, String firstname, String lastname, String username, String password, int authtype){
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.username = username;
		this.password = password;
		this.authtype = authtype;
	}
	
}
