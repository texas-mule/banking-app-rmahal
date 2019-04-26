package com.revature;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class App {
	//public int bankAccountLines;
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub		
		boolean run = true;
		int userRowCount =returnUserRowCount();
		userRowCount += 1;
		System.out.println("Next available userid is: "+userRowCount);
		int bankRowCounts =returnBankAccountRowCount();
		bankRowCounts += 1;
		System.out.println("Next available backaccountid is: "+bankRowCounts);
		
		//Keeps the program running 
		while(run) {
			//Prompts the user the options available for this program
			System.out.println("Type 0 at any point to end the program!");
			System.out.println("Press 1 to login...");
			System.out.println("Press 2 to create a new account...");
			//Scanner class for input
			Scanner input = new Scanner(System.in);
			int option = ensureScannerInt(input, 3, 0);
			if(option == 1) {
				login(bankRowCounts);
			}else if(option == 2) {
				createAccount(userRowCount);
			}else if(option == 0){
				run = false;
				break;
			}else{
				System.out.println("Incorrect option!!!!!");
				break;
			}
			System.out.println("Terminating loop!");
			run = false;
			input.close();
		}
		System.out.println("Terminating program!");
	}
	
	public static void login(int bankRowCounts) throws IOException{
		Scanner input = new Scanner(System.in);
		System.out.println("=====================");
		System.out.println("Please enter your login credentials when prompted...");
		System.out.print("Please enter your username: ");
		String username = input.next();
		System.out.print("Please enter your password: ");
		String password = input.next();
		System.out.println("Checking Credentials!!!!");
		boolean exists = authLogin(username, password);
		if(exists) {
			users currentUser = returnLoggedInUser(username,password);
			System.out.println("USER LOGIN WAS A SUCCESS!");
			succLogin(currentUser, bankRowCounts);
		}else {
			System.out.println("COULD NOT VERIFY PLEASE START OVER!");
		}
	}
	
	public static void succLogin(users currentUser, int row) {
		ArrayList< BankAccount> accounts = new ArrayList<BankAccount>();
		CheckingAccount checkAcc = new CheckingAccount();
		checkAcc.Deposit(1000);
		
		accounts.add(checkAcc);
		int i =0;
		while(i<accounts.size()) {
			System.out.println("Account: ");
			accounts.get(i).checkBalance();
			System.out.println("\n ");
			i++;
		}
		System.out.println("DONE");
		Scanner input = new Scanner(System.in);
		System.out.println("Press 0 to log out.");
		System.out.println("Press 1 to see current bank accounts.");
		System.out.println("Press 2 to create a new bank account.");
		System.out.println("Press 3 to access an account.");
		System.out.print("Please pick option: ");
		int option = ensureScannerInt(input, 4, 0);
		if(option == 1) {
			System.out.println("These are your accounts.");
		}else if(option == 2) {
			System.out.println("What kind of account would you like to apply for?");
			System.out.println("Press 1 to create a new Checkings account.");
			System.out.println("Press 2 to create a new Joint account.");
			System.out.print("Please pick an option, ");
			option = ensureScannerInt(input, 2, 1);
			if(option == 1) {
				saveNewBankAccount(currentUser, row, "Checking", 0, 2);	
				System.out.println("Applied for new Checkings Account.");
				System.out.println("Account created pending approval!........");
				System.out.println("\n");
			}else if(option == 2) {
				System.out.println("Applied for new Joint Account.");
				saveNewBankAccount(currentUser, row, "Joint", 0, 2);
				System.out.println("Account created pending approval!");
				System.out.println("NEED TO SET UP OTHER USER FOR JOINT ACCOUNT");
			}
		}
	}
	
	//Method to create an account
	public static void createAccount(int row) throws IOException{
		int idNextRow = row;
		Scanner input = new Scanner(System.in);
		System.out.println("=====================");
		System.out.println("Please enter your information for account creation when promped...");
		System.out.print("Please enter your First Name: ");
		String fname = input.next();	
		System.out.print("Please enter your Last Name: ");
		String lname = input.next();
		System.out.print("Please enter your username: ");
		String username = input.next();
		System.out.println(username);
		System.out.print("Please enter your password: ");
		String password = input.next();
		int authtype = 1;
		saveAccount(row, fname, lname, username, password, authtype);
		System.out.println("Account created successfully!");
		System.out.println("Please Try Logging In.....");
		login(row);
	}
	
	//Allows access to account objects for generics
	public static <T extends BankAccount> void singleAccountOptions(T account) {
		while(1==1) {
		int amount;
		Scanner option = new Scanner(System.in);
		System.out.println("What would you like to do?");
		System.out.println("Press 1 to deposit money.");
		System.out.println("Press 2 to withdaw money.");
		System.out.println("Press 3 to transfer money between accounts.");
		System.out.println("Press 4 to check current Balance.");
		System.out.println("To go back to the previous menu press 0.");
		System.out.print("Please pick an option: ");
		int select = option.nextInt();
		switch(select) {
			case 1:
				System.out.println("How much would you like to add in?");
				System.out.print("$");
				amount = option.nextInt();
				account.Deposit(amount);
				break;
			case 2:
				System.out.println("How much would you like to take out?");
				System.out.print("$");
				amount = option.nextInt();
				account.Withdraw(amount);
				break;
			case 3:
				System.out.println("How much would you like to take out?");
				System.out.print("$");
				amount = option.nextInt();
				break;
			case 4:
				account.checkBalance();
				break;
			case 0:
				//succLogin();
				break;
			default:
				System.out.println("Invalid option please try again....");
				break;			
		}
	  }			
	}
	
	//Authenticates login with database checking
	public static boolean authLogin(String inputusername, String inputpassword) throws IOException {
		String url  = "jdbc:postgresql://127.0.0.1:8001/postgres";
		String dbusername = "postgres";
		String dbpassword = "test";
		
		try (
			Connection connection = DriverManager.getConnection(url,dbusername,dbpassword);
			Statement statement = connection.createStatement();
		) { 
			String sql ="SELECT * FROM public.users WHERE username='"+inputusername+"' AND password='"+inputpassword+"'";

			ResultSet resSet = statement.executeQuery(sql);
			boolean returned = resSet.next();
			if(returned) {
				return true;
			}else {
				return false;
			}
			
			} catch (SQLException ex) {
				System.out.println("DB did not work!");
				System.out.println(ex.getMessage());
				return false;
			}
	}

	//Method to connect to the db
	public static void connect() {
		try {
			Class.forName("org.postgresql.Driver");
		} catch(java.lang.ClassNotFoundException e) {
			System.out.print(e.getMessage());
		}
		
		String url  = "jdbc:postgresql://127.0.0.1:8001/postgres";
		String username = "postgres";
		String password = "test";
		try (
			Connection connection = DriverManager.getConnection(url,username,password);
			Statement statement = connection.createStatement();
		) { 
				System.out.println("I work");
				String sql = "SELECT id, food FROM public.\"foodDB\";";
				String sqlTwp = "INSERT INTO public.\"foodDB\" id, food) VALUES (?, ?);";
				Statement stmt = connection.createStatement();
				PreparedStatement ps = connection.prepareStatement(sqlTwp);
				ps.execute();
				ResultSet rs = stmt.executeQuery(sql);
				System.out.println("DONE WITH DB.");
				while(rs.next()) {
					System.out.print(rs.getString(1)+" ");
					
					System.out.println(rs.getString(2));
				}
				
			} catch (SQLException ex) {
				System.out.println("I DO NOT work");
				System.out.println(ex.getMessage());
			} 
	}
	
	//Saves new user account to db
	public static void saveAccount(int row, String fname, String lname, String username, String password, int authtype) {
		String url  = "jdbc:postgresql://127.0.0.1:8001/postgres";
		String dbusername = "postgres";
		String dbpassword = "test";
		
		try (
			Connection connection = DriverManager.getConnection(url,dbusername,dbpassword);
			Statement statement = connection.createStatement();
		) { 
			String sql = "INSERT INTO public.users( id, firstname, lastname, username, password, authtype) VALUES ("+row+", '"+fname+"', '"+lname+"', '"+username+"', '"+password+"', "+authtype+")";
			ResultSet resSet = statement.executeQuery(sql);
			resSet.close();
				
			} catch (SQLException ex) {
				System.out.println("DB did not work saving user account!");
				System.out.println(ex.getMessage());
			}
	}
	
	//Saves new account to db
	public static void saveNewBankAccount(users currentUser, int bankRow, String accounttype,int balance, int accountstatus) {
		//account status 1 approved
		//account status 2 pending
		//account status 3 canceled/denied
		String url  = "jdbc:postgresql://127.0.0.1:8001/postgres";
		String dbusername = "postgres";
		String dbpassword = "test";
		
		System.out.println("UserID: "+currentUser.id+" RowID: "+bankRow);
		
		try (
			Connection connection = DriverManager.getConnection(url,dbusername,dbpassword);
			Statement statement = connection.createStatement();
		) { 
			String sql = "INSERT INTO public.bankaccounts (id, accounttype, balance, accountstatus) VALUES ("+bankRow+", '"+accounttype+"', "+balance+", "+accountstatus+")";
			System.out.println(sql);
			int resSet = statement.executeUpdate(sql);
			System.out.println(resSet);
			joinBankUser(currentUser, bankRow);
			} catch (SQLException ex) {
				System.out.println("DB did not work in saving new bank account!");
				System.out.println(ex.getMessage());
			}
	}

	//Fills in Join Table for user and new account
	public static void joinBankUser(users currentUser, int bankrow) {
		String url  = "jdbc:postgresql://127.0.0.1:8001/postgres";
		String dbusername = "postgres";
		String dbpassword = "test";
		
		try (
			Connection connection = DriverManager.getConnection(url,dbusername,dbpassword);
			Statement statement = connection.createStatement();
		) { 
			int id = currentUser.id;
			int row = bankrow;
			System.out.println("UserID: "+id+" RowID: "+row);
			String sql = "INSERT INTO public.joinusersbank (userid, bankaccountid) VALUES ("+id+","+row+")";
			int resSet = statement.executeUpdate(sql);
			System.out.println(resSet);
			return;
			} catch (SQLException ex) {
				System.out.println("DB did not work in saving new bank account and user into join table!");
				System.out.println(ex.getMessage());
			}
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
	
	//Returns logged in user data
	public static users returnLoggedInUser(String inputusername, String inputpassword) {
		
		String url  = "jdbc:postgresql://127.0.0.1:8001/postgres";
		String dbusername = "postgres";
		String dbpassword = "test";
		
		try (
			Connection connection = DriverManager.getConnection(url,dbusername,dbpassword);
			Statement statement = connection.createStatement();
		) { 
			String sql ="SELECT * FROM public.users WHERE username='"+inputusername+"' AND password='"+inputpassword+"'";

			ResultSet resSet = statement.executeQuery(sql);
			resSet.next();
			int id = resSet.getInt("id");
			String fname = resSet.getString("firstname");
			String lname = resSet.getString("lastname");
			String username = resSet.getString("username");
			String password = resSet.getString("password");
			int authtype = resSet.getInt("authtype");
			users currentUser = new users(id,fname,lname,username,password,authtype);
			return currentUser;
			} catch (SQLException ex) {
				System.out.println("DB did not work!");
				System.out.println(ex.getMessage());
			}
		return null;
	}
	
	//Method to make sure inputs remain integer
	public static int ensureScannerInt(Scanner input, int max, int min) {
		int choice = -1 ; 
		int choiceMax = max-1;
		while(choice==-1) {
            try {         
               System.out.print("Your choice:");
 	           choice = input.nextInt();
	           if(choice>choiceMax || choice<min){
	                System.out.println("Sorry wrong input");
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
}