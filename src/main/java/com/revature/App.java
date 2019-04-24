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
import java.util.Scanner;

public class App {
	//public int bankAccountLines;
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub		
		int accountsLine = 0;
		boolean run = true;
		App startApp = new App();
		
		startApp.DBTEST();
		//Keeps the program running 
		while(run) {
			//Prompts the user the options available for this program
			System.out.print("Press 1 to login... \nPress 2 to create a new account... \n");
			//Scanner class for input as 
			Scanner input = new Scanner(System.in);
			System.out.print("Your Choice: ");
			int option = input.nextInt();
			if(option == 1) {
				startApp.login(startApp);
			}else if(option == 2) {
				startApp.createAccount(startApp);
			}else {
				System.out.println("Incorrect option!!!!!");
				while(1==1) {
					int retryOption = input.nextInt();
					if(retryOption == 1) {
						startApp.login(startApp);
					}else if(retryOption == 2) {
						startApp.createAccount(startApp);
					}else {
						System.out.println("Incorrect option!!!!!");
					}
				}
			}
			System.out.println("Terminating loop");
			run = false;
			input.close();
		}
	}
	
	public void login(App obj) throws IOException{
		Scanner input = new Scanner(System.in);
//		System.out.println("\n");
		System.out.println("=====================");
		System.out.println("Please enter your login credentials when prompted...");
		System.out.print("Please enter your username: ");
		String username = input.next();
		System.out.println(username);
		System.out.print("Please enter your password: ");
		String password = input.next();
		System.out.println(password);
		System.out.println("Checking Credentials!!!!");
		boolean exists = obj.authLogin(username, password);
		if(exists) {
			System.out.println("USER LOGIN WAS A SUCCESS!");
			obj.succLogin(obj);
		}else {
			System.out.println("COULD NOT VERIFY PLEASE START OVER!");
		}
	}
	
	public void succLogin(App obj) {
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
				obj.singleAccountOptions(obj, checkAcc);
			}else if(option == 2) {
				System.out.println("Applied for new Joint Account.");
				JointAccount jointAcc = new JointAccount();
				jointAcc.checkBalance();
			}
		}else if(option == 2) {
			System.out.println("Pressed 2 These are your accounts.");
		}
	}
	
	public void createAccount(App obj) throws IOException{
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
		obj.login(obj);
	}
	
	public <T extends BankAccount> void singleAccountOptions(App obj, T account) {
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
				obj.succLogin(obj);
				break;
			default:
				System.out.println("Invalid option please try again....");
				break;			
		}
	  }			
	}
	
	public boolean authLogin(String username, String password) throws IOException {
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

	public void DBTEST() {
		try {
			Class.forName("org.postgresql.Driver");
		} catch(java.lang.ClassNotFoundException e) {
			System.out.print(e.getMessage());
		}
		
		String url  = "jdbc:postgresql://127.0.0.1:8082/postgres";
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
				ps.execute(true);
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
	
	//Uneeded Getters and Setter for Thread Problem	
//	public void incrementCount(int num) {
//		this.bankAccountLines = num;
//	}
//	
//	public int retBankAcountLineCount() {
//		return this.bankAccountLines;
//	}

	//Trying to make a new Thread to count the lines for parallel processing
//	class lineReaderThread implements Runnable{
//		public int lineCount;
//		@Override
//		public void run() {
//			lineCount  = 0;
//			try(BufferedReader fileReader = new BufferedReader(new FileReader("textfiles/bankAccounts.txt"));){
//				synchronized(this) {
//				while (fileReader.readLine() != null) lineCount++;
//				notify();
//				}
//				fileReader.close();
//	
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}	
//	}
	
}