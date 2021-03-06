package com.revature;

import java.util.ArrayList;

public class Admin extends Employees{

	private int id;
	private String firstname;
	private String lastname;
	private String username;
	private String password;
	private int authtype;

	Admin(int id, String firstname, String lastname, String username, String password, int authtype){
		super(authtype, password, password, password, password, authtype);
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

	public ArrayList<BankAccount> seeAllBankAccounts() {
		BankTableDao btd = new BankTableDao();
		return btd.getAllAccounts();
	}
	
	public ArrayList<Users> seeAllUserAccounts() {
		UserTableDao utd = new UserTableDao();
		return utd.getAllUsers();
	}
	
	public void accessBankAccount(int id) {
		
	}

}
