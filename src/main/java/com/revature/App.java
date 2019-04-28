package com.revature;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
		String url  = "jdbc:postgresql://127.0.0.1:8001/postgres";
		String username = "postgres";
		String password = "test";
		
		try (
			Connection connection = DriverManager.getConnection(url,username,password);
			Statement statement = connection.createStatement();
		) { 
			ResultSet resSet = statement.executeQuery("SELECT COUNT(*) AS rowcount FROM users");
			resSet.next();
			int count = resSet.getInt("rowcount");
			resSet.close();
			return count;
				
			} catch (SQLException ex) {
				System.out.println("DB did not work!");
				System.out.println(ex.getMessage());
				return 0;
			}
	}
	
	//Method to know which is next available row for bank accounts
	public static int returnBankAccountRowCount() {
		String url  = "jdbc:postgresql://127.0.0.1:8001/postgres";
		String username = "postgres";
		String password = "test";	
		try (
			Connection connection = DriverManager.getConnection(url,username,password);
			Statement statement = connection.createStatement();
		) {   // executeUpdate() returns the number of rows affected for DML
			ResultSet resSet = statement.executeQuery("SELECT COUNT(*) AS rowcount FROM bankaccounts");
			resSet.next();
			int count = resSet.getInt("rowcount");
			resSet.close();
			return count;
				
			} catch (SQLException ex) {
				System.out.println("DB did not work!");
				System.out.println(ex.getMessage());
				return 0;
			}
	}

}