package com.revature;

public class Users {
	
	
	public int id;
	public String firstname;
	public String lastname;
	public String username;
	public String password;
	public int authtype;
	
	Users(int id, String firstname, String lastname, String username, String password, int authtype){
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.username = username;
		this.password = password;
		this.authtype = authtype;
	}
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getAuthtype() {
		return authtype;
	}

	public void setAuthtype(int authtype) {
		this.authtype = authtype;
	}

}
