package com.revature;

import java.util.ArrayList;
import java.util.Scanner;

public class EmployeeLogin {

	Employees employee;

	EmployeeLogin(Users currentUser){
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

	protected void viewAllUserAccounts() {
		UserTableDao utd = new UserTableDao();
		utd.printAllUsers();
	}

	protected void viewAllBankAccounts() {
		BankTableDao btd = new BankTableDao();
		btd.printAllBankAccounts();
	}

	protected void viewAllPendingBankAccounts() {
		BankTableDao btd = new BankTableDao();
		btd.printPendingBankAccounts();
	}

	protected ArrayList<BankAccount> returnPendingBankAccounts() {
		BankTableDao btd = new BankTableDao();
		return btd.getPendingBankAccounts();
	}

	protected void editBankAccountStatus() {
		ArrayList<BankAccount> accounts = new ArrayList<BankAccount>();
		accounts = returnPendingBankAccounts();
		Scanner input = new Scanner(System.in);

		int i=0;
		while(i<accounts.size()) {
			System.out.println(accounts.get(i).getId()+"\t"+accounts.get(i).getBalance()+"\t"+accounts.get(i).getType()+"\t"+accounts.get(i).getAccountstatus());
			i++;
		}
		boolean run = true;
		CheckingAccount ca = null;
		JointAccount ja = null;
		while(run) {
			accounts = returnPendingBankAccounts();
			System.out.println("Select account to update by their id... press 0 to back out");
			int max =returnBankAccountRowCount();
			int choice = ensureScannerInt(input, max+1, 0);
			if(choice == 0) {
				run = false;
				break;
			}else {
				i=0;
				while(i<accounts.size()) {
					if(accounts.get(i).getId() == choice) {
						if(accounts.get(i).getType().equals("Checking")){
							ca = new CheckingAccount(accounts.get(i).getId(),accounts.get(i).getBalance(), accounts.get(i).getType(), accounts.get(i).getAccountstatus());
							break;
						}else {
							ja = new JointAccount(accounts.get(i).getId(),accounts.get(i).getBalance(), accounts.get(i).getType(), accounts.get(i).getAccountstatus());
							break;
						}
					}
					i++;
				}
				if(ja == null && ca == null) {
					System.out.println("Invalid choice backing out.");
					break;
				}else {
					System.out.println("Select 1 to approve account.");
					System.out.println("Select 2 to deny account.");
					int status = ensureScannerInt(input, 3, 1);
					if(status == 1) {
						if(ca != null) {
							ca.setAccountstatus(1);
							saveTranstion(ca);
							break;
						}else {
							ja.setAccountstatus(1);
							saveTranstion(ja);
							break;
						}
					}else if(status == 2) {
						if(ca != null) {
							ca.setAccountstatus(3);
							saveTranstion(ca);
							break;
						}else {
							ja.setAccountstatus(3);
							saveTranstion(ja);
							break;
						}
					}
				}
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

	protected void saveTranstion(BankAccount ba) {
		BankTableDao btd = new BankTableDao();
		boolean success = btd.updateAccount(ba);
		if(success) {
			System.out.println("Transaction Complete!");
		}else {
			System.out.println("Transaction failed please try again!");
		}
	} 

	public static int returnBankAccountRowCount() {
		BankTableDao btd = new BankTableDao();
		return btd.returnRowCount();
	}

}
