package com.revature;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class AdminLogin {
	
	Admin admin;
	
	AdminLogin(users currentUser){
		this.admin = (Admin) currentUser;
	}
	
	public void welcome() {
		
		//Approve and Deny accoutns
		System.out.println("Welcome Admin "+admin.firstname+" "+admin.lastname);
		Scanner input = new Scanner(System.in);
		boolean run = true;
		while(run) {
			System.out.println("What would you like to do?");
			System.out.println("Press 0 to log out....");
			System.out.println("Press 1 to interact with users and their accounts...");
			System.out.println("Press 2 to interact with bank accounts...");
			System.out.print("Please pick option: ");
			int choice = ensureScannerInt(input, 3, 0);
			if(choice == 0) {
				run = false;
				break;
			}else if(choice == 1) {
				this.userChoice(this.admin);
			}else if(choice == 2){
				this.viewAllBankAccounts(this.admin);
			}else {
				System.out.println("Problems occured shutting down please restart application!");
				run = false;
				break;
			}
			
			
		}
		
	}
	
	
	public void userChoice(users currentUser) {
		boolean run = true;
		Scanner input = new Scanner(System.in);
		while(run) {
			System.out.println("What would you like to do?");
			System.out.println("Press 0 to go back.");
			System.out.println("Press 1 to see all user accounts.");
			System.out.println("Press 2 to edit a users accounts.");
			System.out.print("Please pick option: ");
			int choice = ensureScannerInt(input, 3, 0);
			if(choice == 0) {
				run=false;
				break;
			}else if(choice == 1) {
				System.out.println("All Users:");
				this.viewAllUserAccounts();
				System.out.println("==============");
			}else if(choice == 2) {
				System.out.println("All Bank Accounts:");
				this.editUserAccounts();
				System.out.println("==============");
			}else{
				System.out.println("==============");
			}
		}
	
		
		
		
		
	}
	
	public void accountChoice() {
		
	}
	
	
	private void viewAllUserAccounts() {
		
	}
	
	private void editUserAccounts() {
		
	}
	
	private void viewAllBankAccounts(users currentUser) {
		boolean run = true;
		Scanner input = new Scanner(System.in);
		while(run) {
			System.out.println("What would you like to do?");
			System.out.println("Press 0 to go back.");
			System.out.println("Press 1 to see all bank accounts.");
			System.out.println("Press 2 to access and edit a bank accounts.");
			System.out.println("Press 3 to change status of account.");
			System.out.println("Press 4 cancel an account.");
			System.out.print("Please pick option: ");
			int choice = ensureScannerInt(input, 5, 0);
			if(choice == 0) {
				run=false;
				break;
			}else if(choice == 1) {
				System.out.println("All Accounts:");
				ArrayList<String> accountsString = this.getAllBankAccountsInfo();
				int i =0;
				System.out.println("Firstname\tLastname\tBankID\tType\t\tBalance\tStatus");
				while(i<accountsString.size()) {
					System.out.println(accountsString.get(i));
					i++;
				}
				System.out.println("==============");
			}else if(choice == 2) {
				System.out.println("Pick the account id you wish to access:");
				this.editUserAccounts();
				System.out.println("==============");
			}else if(choice == 3) {
				System.out.println("Change the status:");
				this.editUserAccounts();
				System.out.println("==============");
			}else if(choice == 4) {
				System.out.println("All Bank Accounts:");
				this.editUserAccounts();
				System.out.println("==============");
			}else{
				System.out.println("==============");
			}
		}
	}
	
	private ArrayList getAllBankAccountsInfo() {
		//ArrayList<BankAccounts> ba = new ArrayList<BankAccounts>();
		ArrayList<String> accounts = new ArrayList<String>();
		String sql="SELECT * FROM joinusersbank LEFT JOIN users ON (users.id = joinusersbank.userid) RIGHT JOIN bankaccounts ON (bankaccounts.id = joinusersbank.bankaccountid)";
		String url  = "jdbc:postgresql://127.0.0.1:8001/postgres";
		String username = "postgres";
		String password = "test";	
		try (
			Connection connection = DriverManager.getConnection(url,username,password);
			Statement statement = connection.createStatement();
		) {   // executeUpdate() returns the number of rows affected for DML
			ResultSet resSet = statement.executeQuery(sql);
			String addAccount = "";
			while(resSet.next()) {
				addAccount = resSet.getString("firstname")+"\t\t"+resSet.getString("lastname")+"\t\t"+resSet.getInt("id")+"\t"+resSet.getString("accounttype")+"\t"+resSet.getDouble("balance")+"\t"+resSet.getString("accountstatus");
				accounts.add(addAccount);
			}
			resSet.close();
			return accounts;
				
			} catch (SQLException ex) {
				System.out.println("DB did not work couldn't load bank accounts!");
				System.out.println(ex.getMessage());
				return accounts;
			}
	}
	
	
	public static int ensureScannerInt(Scanner input, int max, int min) {
		int choice = -1 ; 
		int choiceMax = max-1;
		while(choice==-1) {
            try {         
               System.out.print("Your choice:");
 	           choice = input.nextInt();
	           if(choice>choiceMax || choice<min){
	                System.out.println("Invalid Input try again,");
	        	   choice=-1;
	           }
            }catch(Exception e) {
                input.next();
                System.out.println("Invalid input please try again,");
                	System.out.print("Your choice:");
                choice=-1;
            }
        }
		return choice;
	}
	
	
	
	
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
