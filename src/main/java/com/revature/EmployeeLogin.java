package com.revature;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class EmployeeLogin {
	
	Employees employee;
	
	EmployeeLogin(users currentUser){
		this.employee = (Employees) currentUser;
	}

	public void welcome() {
		boolean run = true;
		System.out.println("Welcome Employee "+employee.firstname+" "+employee.lastname);
		Scanner input = new Scanner(System.in);
		while(run) {
			System.out.println("What would you like to do?");
			System.out.println("Press 0 to log out.");
			System.out.println("Press 1 to see all user accounts.");
			System.out.println("Press 2 to see all bank accounts.");
			System.out.println("Press 3 to see all pending bank accounts.");
			System.out.println("Press 4 change status of bank accounts.");
			System.out.print("Please pick option: ");
			int choice = ensureScannerInt(input, 5, 0);
			if(choice == 0) {
				run=false;
				break;
			}else if(choice == 1) {
				System.out.println("All Users:");
				this.viewAllUserAccounts();
				System.out.println("==============");
			}else if(choice == 2) {
				System.out.println("All Bank Accounts:");
				this.viewAllBankAccounts();
				System.out.println("==============");
			}else if(choice == 3) {
				System.out.println("All Pending Bank Accounts:");
				this.viewAllPendingBankAccounts();
				System.out.println("==============");
			}else{
				this.editBankAccountStatus();
				System.out.println("==============");
			}
		}
	
	}
	
	private void viewAllUserAccounts() {

		String url  = "jdbc:postgresql://127.0.0.1:8001/postgres";
		String dbusername = "postgres";
		String dbpassword = "test";

		try (
				Connection connection = DriverManager.getConnection(url,dbusername,dbpassword);
				Statement statement = connection.createStatement();
				) { 
			String sql = "SELECT * FROM public.users";
			ResultSet resSet = statement.executeQuery(sql);
			System.out.println("ID\tFirstName\tLastName\tUsername");
			while(resSet.next()) {
				if(resSet.getInt("authtype") != 3) {
					System.out.println(resSet.getInt("id")+"\t"+resSet.getString("firstname")+"\t\t"+resSet.getString("lastname")+"\t\t"+resSet.getString("username"));
				}
			}
		} catch (SQLException ex) {
			System.out.println("DB did not work initializing bank accounts!");
			System.out.println(ex.getMessage());
		}
	}
	
	private void viewAllBankAccounts() {
		String url  = "jdbc:postgresql://127.0.0.1:8001/postgres";
		String dbusername = "postgres";
		String dbpassword = "test";

		try (
				Connection connection = DriverManager.getConnection(url,dbusername,dbpassword);
				Statement statement = connection.createStatement();
				) { 
			String sql = "SELECT * FROM public.bankaccounts";
			ResultSet resSet = statement.executeQuery(sql);
			System.out.println("ID\tAccountType\t\tBalance\tAccountStatus");
			while(resSet.next()) {
				if(resSet.getString("accounttype").equals("Joint")) {
					System.out.println(resSet.getInt("id")+"\t"+resSet.getString("accounttype")+"\t\t\t"+resSet.getDouble("balance")+"\t\t"+resSet.getInt("accountstatus"));
				}else {
					System.out.println(resSet.getInt("id")+"\t"+resSet.getString("accounttype")+"\t\t"+resSet.getDouble("balance")+"\t\t"+resSet.getInt("accountstatus"));
				}
			}
		} catch (SQLException ex) {
			System.out.println("DB did not work initializing bank accounts!");
			System.out.println(ex.getMessage());
		}
		
	}
	
	private void viewAllPendingBankAccounts() {
		String url  = "jdbc:postgresql://127.0.0.1:8001/postgres";
		String dbusername = "postgres";
		String dbpassword = "test";

		try (
				Connection connection = DriverManager.getConnection(url,dbusername,dbpassword);
				Statement statement = connection.createStatement();
				) { 
			String sql = "SELECT * FROM public.bankaccounts WHERE accountstatus=2";
			ResultSet resSet = statement.executeQuery(sql);
			System.out.println("ID\tAccountType\t\tBalance\t\tAccountStatus");
			while(resSet.next()) {
				if(resSet.getString("accounttype").equals("Joint")) {
					System.out.println(resSet.getInt("id")+"\t"+resSet.getString("accounttype")+"\t\t\t"+resSet.getDouble("balance")+"\t\t"+resSet.getInt("accountstatus"));
				}else {
					System.out.println(resSet.getInt("id")+"\t"+resSet.getString("accounttype")+"\t\t"+resSet.getDouble("balance")+"\t\t"+resSet.getInt("accountstatus"));
				}
			}
		} catch (SQLException ex) {
			System.out.println("DB did not work initializing bank accounts!");
			System.out.println(ex.getMessage());
		}
		
	}
	
	
	public ArrayList<BankAccount> returnPendingBankAccounts() {
		
		String url  = "jdbc:postgresql://127.0.0.1:8001/postgres";
		String dbusername = "postgres";
		String dbpassword = "test";
		
		ArrayList<BankAccount> accounts = new ArrayList();
		try (
				Connection connection = DriverManager.getConnection(url,dbusername,dbpassword);
				Statement statement = connection.createStatement();
			) { 
		String sql = "SELECT * FROM public.bankaccounts WHERE accountstatus=2";
		ResultSet resSet = statement.executeQuery(sql);
		while(resSet.next()) {
			if(resSet.getString("accounttype").equals("Checking")) {
				CheckingAccount chaccount = new CheckingAccount(resSet.getInt("id"), resSet.getDouble("balance"), resSet.getString("accounttype"), resSet.getInt("accountstatus"));
				accounts.add(chaccount);
			}else {
				JointAccount joiaccount = new JointAccount(resSet.getInt("id"), resSet.getDouble("balance"), resSet.getString("accounttype"), resSet.getInt("accountstatus"));
				accounts.add(joiaccount);
			}
		}
		return accounts;
	} catch (SQLException ex) {
		System.out.println("DB did not work initializing bank accounts!");
		System.out.println(ex.getMessage());
	}
		return accounts;
	}
	
	
	private void editBankAccountStatus() {
		ArrayList<BankAccount> accounts = new ArrayList();
		accounts = returnPendingBankAccounts();
		Scanner input = new Scanner(System.in);

		int i=0;
		while(i<accounts.size()) {
			System.out.println(accounts.get(i).getId()+"\t"+accounts.get(i).getBalance()+"\t"+accounts.get(i).getId()+"\t"+accounts.get(i).getAccountstatus());
			i++;
		}
		boolean run = true;
		while(run) {
			System.out.println("Select account to update by their id... press 0 to back out");
			int choice = ensureScannerInt(input, 100000, 0);
			while(i<accounts.size()) {
				if(accounts.get(i).getId() == choice) {

				}
				i++;
			}
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
                System.out.println("Sorry wrong input");
                choice=-1;
            }
        }
		return choice;
	}
	
	
	
	
	private void saveTranstion(BankAccount ba) {
		CheckingAccount account = new CheckingAccount(ba.getId(), ba.getBalance(), ba.getType(),ba.getAccountstatus());
		
		//account status 1 approved
		//account status 2 pending
		//account status 3 canceled
		//account status 4 denied
		String url  = "jdbc:postgresql://127.0.0.1:8001/postgres";
		String dbusername = "postgres";
		String dbpassword = "test";

		try (
			Connection connection = DriverManager.getConnection(url,dbusername,dbpassword);
			Statement statement = connection.createStatement();
		) { 
			String sql = "UPDATE public.bankaccounts SET id="+account.getId()+", accounttype='"+account.getType()+"', balance="+account.getBalance()+", accountstatus="+account.accountstatus+" WHERE id="+account.getId();
			//System.out.println(sql);
			int resSet = statement.executeUpdate(sql);
			System.out.println("Transaction Saved!");
			
			} catch (SQLException ex) {
				System.out.println("DB did not work in saving transaction!");
				System.out.println(ex.getMessage());
		}
	} 
	
	
	
	
}
