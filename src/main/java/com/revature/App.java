package com.revature;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class App {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Main startApp = new Main();
		//Keeps the program running 
		while(1==1) {
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
		}
	}
	
	public void login(Main obj) throws IOException{
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
		}else {
			System.out.println("COULD NOT VERIFY PLEASE START OVER!");
		}
		
	}
	
	public void createAccount(Main obj) throws IOException{
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
	
	
	
	
}