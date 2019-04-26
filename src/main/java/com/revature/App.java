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
				login();
			}else if(option == 2) {
				createAccount();
			}else if(option == 0){
				run = false;
				break;
			}else{
				System.out.println("Incorrect option!!!!!");
				while(1==1) {
					int retryOption = input.nextInt();
					if(retryOption == 1) {
						login();
					}else if(retryOption == 2) {
						createAccount();
					}else {
						System.out.println("Incorrect option again closing program!!!!!");
						break;
					}
				}
			}
			System.out.println("Terminating loop!");
			run = false;
			input.close();
		}
		System.out.println("Terminating program!");
	}
	
	public static void login() throws IOException{
		Scanner input = new Scanner(System.in);
		System.out.println("\n");
		System.out.println("=====================");
		System.out.println("Please enter your login credentials when prompted...");
		System.out.print("Please enter your username: ");
		String username = input.next();
		System.out.println(username);
		System.out.print("Please enter your password: ");
		String password = input.next();
		System.out.println(password);
		System.out.println("Checking Credentials!!!!");
		boolean exists = authLogin(username, password);
		if(exists) {
			System.out.println("USER LOGIN WAS A SUCCESS!");
			succLogin();
		}else {
			System.out.println("COULD NOT VERIFY PLEASE START OVER!");
		}
	}
	
	public static void succLogin() {
		Scanner input = new Scanner(System.in);
		System.out.println("Press 1 to create a new bank account.");
		System.out.println("Press 2 to see current bank accounts.");
		System.out.print("Please pick option: ");
		int option = input.nextInt();
		if(option == 1) {
			System.out.println("What kind of account?");
			System.out.println("Press 1 to create a new Checkings account.");
			System.out.println("Press 2 to create a new Joint account.");
			System.out.print("Please pick an option: ");
			option = input.nextInt();
			if(option == 1) {
				System.out.println("Applied for new Checkings Account.");
				CheckingAccount checkAcc = new CheckingAccount();
				checkAcc.checkBalance();
				singleAccountOptions(checkAcc);
			}else if(option == 2) {
				System.out.println("Applied for new Joint Account.");
				JointAccount jointAcc = new JointAccount();
				jointAcc.checkBalance();
			}
		}else if(option == 2) {
			System.out.println("Pressed 2 These are your accounts.");
		}
	}
	
	public static void createAccount() throws IOException{
		Scanner input = new Scanner(System.in);
		System.out.println("=====================");
		System.out.println("Please enter your information for account creation when promped...");
		System.out.print("Please enter your username: ");
		String username = input.next();
		System.out.println(username);
		System.out.print("Please enter your password: ");
		String password = input.next();
		System.out.println(password);
		try(FileWriter fw = new FileWriter("textfiles/mockUsersDB.txt", true);BufferedWriter bw = new BufferedWriter(fw);PrintWriter out = new PrintWriter(bw)){
			out.println(username);
			out.println(password);
			out.close();
		}
		System.out.print("Account created successfully!");
		System.out.print("Please Try Logging In.....");
		login();
	}
	
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
				succLogin();
				break;
			default:
				System.out.println("Invalid option please try again....");
				break;			
		}
	  }			
	}
	
	public static boolean authLogin(String username, String password) throws IOException {
		BufferedReader bufferedReader;
		boolean userExists = false;
		try {
			bufferedReader = new BufferedReader(new FileReader("textfiles/mockUsersDB.txt"));
			String line;
			while((line = bufferedReader.readLine()) != null) {
				if(line.equals(username)) {
					line = bufferedReader.readLine();
					if(line.equals(password)) {
						userExists = true;
						break;
					}
				}
			}
		}catch (FileNotFoundException e){
			e.printStackTrace();
		}
		return userExists;
	}

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
		) {   // executeUpdate() returns the number of rows affected for DML
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
	
	
	
	public static int returnUserRowCount() {
		String url  = "jdbc:postgresql://127.0.0.1:8001/postgres";
		String username = "postgres";
		String password = "test";
		
		try (
			Connection connection = DriverManager.getConnection(url,username,password);
			Statement statement = connection.createStatement();
		) {   // executeUpdate() returns the number of rows affected for DML
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
	
	
	
	//Ensures scanner gets proper input
	public static int ensureScannerInt(Scanner input, int max, int attempts) {
		int attempt = attempts; 
//		System.out.println("ATTEMPT #"+attempt);
		int options = max-1;
		
		if(attempt == 3) {
			System.out.println("Too Many Failed Attempts Please Restart Application.");
			return 0;
		}else {
			try {
				System.out.println("choose between 0 and "+options);
				System.out.print("Your Choice: ");
				int choice = input.nextInt();
				if(choice > options || choice < 0) {
					attempt+=1;
					ensureScannerInt(input, max, attempt);					
				}else {
					return choice;
				}
			}catch(InputMismatchException e) {
				System.out.println("Mismatch hit");
				if(attempt > 0) {
					System.out.println("Invalid option please restart program");
				}
				attempt+=1;
			}
//			ensureScannerInt(input, max, attempt);
		}
		
		
		return 0;
	}
	
	

	
}