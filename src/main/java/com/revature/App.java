package com.revature;

import java.io.IOException;

public class App {

	public static void main(String[] args) throws IOException {
		boolean run = true;
		int userRowCount =returnUserRowCount();
		userRowCount += 1;
		int bankRowCount =returnBankAccountRowCount();
		bankRowCount += 1;
		Login login = new Login();
		login.welcome(run, userRowCount, bankRowCount);
		System.out.println("Terminating program!");
	}

	//Method to know which is next available row for user counts
	public static int returnUserRowCount() {
		UserTableDao utd = new UserTableDao();
		return utd.returnUserRowCount();
	}

	//Method to know which is next available row for bank accounts
	public static int returnBankAccountRowCount() {
		BankTableDao btd = new BankTableDao();
		return btd.returnRowCount();
	}

}