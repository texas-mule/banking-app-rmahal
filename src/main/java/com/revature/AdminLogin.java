package com.revature;

import java.util.ArrayList;
import java.util.Scanner;

public class AdminLogin {

	Admin admin;

	AdminLogin(Users currentUser){
		this.admin = (Admin) currentUser;
	}

	public void welcome() {

		//Approve and Deny accounts
		System.out.println("Welcome Admin "+admin.getFirstname()+" "+admin.getLastname());
		Scanner input = new Scanner(System.in);
		boolean run = true;
		while(run) {
			System.out.println("What would you like to do?");
			System.out.println("Press 0 to log out....");
			System.out.println("Press 1 to interact with users and their accounts...");
			System.out.println("Press 2 to interact with bank accounts...");
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


	public void userChoice(Users currentUser) {
		boolean run = true;
		Scanner input = new Scanner(System.in);
		while(run) {
			System.out.println("What would you like to do?");
			System.out.println("Press 0 to go back.");
			System.out.println("Press 1 to see all user accounts.");
			System.out.println("Press 2 to edit a user accounts.");
			//System.out.print("Please pick option: ");
			int choice = ensureScannerInt(input, 3, 0);
			if(choice == 0) {
				run=false;
				break;
			}else if(choice == 1) {
				System.out.println("All Users:");
				this.viewAllUserAccounts();
				System.out.println("===========================");
			}else if(choice == 2) {
				System.out.println("All Bank Accounts:");
				this.editUserAccount();
				System.out.println("==============");
			}else{
				System.out.println("==============");
			}
		}





	}


	private void viewAllUserAccounts() {
		UserTableDao utd = new UserTableDao();
		utd.printAllUsersFullView();;
	}

	public void editUserAccount() {
		UserTableDao utd = new UserTableDao();
		utd.printAllUsersFullView();
		Scanner input = new Scanner(System.in);
		System.out.println("Select the user you wish to change by id,");
		int choice = ensureScannerInt(input, this.returnUserAccountRowCount()+1, 1);
		utd.printOneUser(choice);
		System.out.print("Please enter the First Name for this user: ");
		String fname = ensureScannerString(input);	
		System.out.print("Please enter the Last Name for this user: ");
		String lname = ensureScannerString(input);
		System.out.print("Please enter the Username for this user: ");
		String username = ensureScannerString(input);
		System.out.println(username);
		System.out.print("Please enter the password  for this user: ");
		String password = ensureScannerString(input);
		System.out.print("Please enter the authtype for this user: ");
		int auth = ensureScannerInt(input, 4, 1);
		
		Users user = new Users(choice,fname,lname,username,password,auth);
		boolean success = utd.updateUser(user);
		if(success) {
			System.out.println("Successfully updated user.");
		}else {
			System.out.println("A problem occured please try again later.");
		}
	}
	
//	private void pushUpdatedUserToDAO(UserTableDao utd,int row, String fname, String lname, String username, String password, int authtype) {
//		Users user = new Users(row, fname, lname,username, password, authtype);
//		boolean success = utd.insertUser(row, user);
//		if(success) {
//			System.out.println("User successfully added!");
//		}else {
//			System.out.println("Error in adding user, please !");
//		}
//	}

	private void viewAllBankAccounts(Users currentUser) {
		boolean run = true;
		Scanner input = new Scanner(System.in);
		while(run) {
			System.out.println("What would you like to do?");
			System.out.println("Press 0 to go back.");
			System.out.println("Press 1 to see all bank accounts.");
			System.out.println("Press 2 to access and edit a bank account.");
			System.out.println("Press 3 to change status of account.");
			System.out.println("Press 4 cancel an account.");
			int choice = ensureScannerInt(input, 5, 0);
			if(choice == 0) {
				run=false;
				break;
			}else if(choice == 1) {
				System.out.println("All Accounts:");
				this.viewAllAccounts();
				System.out.println("==============");
			}else if(choice == 2) {
				int bankRowCounts = this.returnBankAccountRowCount();
				this.accessBankAccount(input, bankRowCounts);
				System.out.println("==============");
			}else if(choice == 3) {
				System.out.println("Change the status:");
				this.editBankStatus();
				System.out.println("==============");
			}else if(choice == 4) {
				System.out.println("Cancel account:");
				this.cancelBankAccount();
				System.out.println("==============");
			}else{
				System.out.println("==============");
			}
		}
	}

	private void cancelBankAccount() {
		ArrayList<BankAccount> accounts = new ArrayList<BankAccount>();
		accounts = returnAllBankAccounts();
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
			accounts = returnAllBankAccounts();
			System.out.println("Select account to cancel by their id... press 0 to back out");
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
					if(ca != null) {
						ca.setAccountstatus(4);
						saveTranstion(ca);
						break;
					}else {
						ja.setAccountstatus(4);
						saveTranstion(ja);
						break;
					}
				}
			}
		}
	}
	
	
	public static String ensureScannerString(Scanner input) {
		String choice = ""; 
		while(choice.equals("") || choice.equals(null)) {
            try {         
 	           choice = input.nextLine();
	           if(choice.contains(";") || choice.contains("*") || choice.equals("\n") || choice.contains(" ") || choice.equals("")){
	                System.out.println("Sorry incorrect input, please try again!");
	                System.out.print("Your choice: ");
	        	   choice="";
	           }
            }catch(Exception e) {
                input.next();
                System.out.println("Sorry improper input...");
                choice="";
            }
        }
		return choice;
	}

	private void accessBankAccount(Scanner input, int bankRowCounts) {
		System.out.println("Please enter id of account you wish to access");
		System.out.print("Your choice: ");
		int choice = ensureScannerInt(input, bankRowCounts+1,1);

		ArrayList<BankAccount> bankaccount = new ArrayList<BankAccount>();
		BankTableDao btd = new BankTableDao();
		bankaccount.add(btd.getAccount(choice));
		CheckingAccount ckaccount = null;
		JointAccount joiaccount = null;
		if(bankaccount.get(0).getType().equals("Checking")) {
			ckaccount = new CheckingAccount(bankaccount.get(0).getId(),bankaccount.get(0).getBalance(),bankaccount.get(0).getType(),bankaccount.get(0).getAccountstatus());
		}else {
			joiaccount = new JointAccount(bankaccount.get(0).getId(),bankaccount.get(0).getBalance(),bankaccount.get(0).getType(),bankaccount.get(0).getAccountstatus());
		}
		if(ckaccount != null) {
			this.singleAccountOptions(this.admin, ckaccount, bankRowCounts);
		}else {
			this.singleAccountOptions(this.admin, joiaccount, bankRowCounts);
		}
	}

	protected ArrayList<BankAccount> returnAllBankAccounts() {
		BankTableDao btd = new BankTableDao();
		return btd.getAllAccounts();
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

	private void editBankStatus() {
		ArrayList<BankAccount> accounts = new ArrayList<BankAccount>();
		accounts = returnAllBankAccounts();
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
			accounts = returnAllBankAccounts();
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
					System.out.println("Select 2 to put account on pending.");
					System.out.println("Select 3 to deny account.");
					int status = ensureScannerInt(input, 4, 1);
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
							ca.setAccountstatus(2);
							saveTranstion(ca);
							break;
						}else {
							ja.setAccountstatus(2);
							saveTranstion(ja);
							break;
						}
					}else if(status == 3) {
						if(ca != null) {
							ca.setAccountstatus(3);
							saveTranstion(ca);
							break;
						}else {
							ja.setAccountstatus(3);
							saveTranstion(ja);
							break;
						}
					}else {
						System.out.println("Invalid option please try again later.");
						break;
					}
				}
			}
		}
	}

	private void singleAccountOptions(Users currentUser,BankAccount account, int row) {
		System.out.println("BANK ACCOUNT # "+account.getId());
		Scanner input = new Scanner(System.in);
		boolean run=true;
		if(account.getType().equals("Checking")) {
			//Check account
			while(run) {
				System.out.println("What transaction would you like to do?");
				System.out.println("Press 0 to return...");
				System.out.println("Press 1 to Withdraw...");
				System.out.println("Press 2 to Deposit...");
				System.out.println("Press 3 to Transfer...");
				int choice = ensureScannerInt(input, 4, 0);
				if(choice == 1) {
					System.out.println("How much would you like to withdraw?");
					int amount = ensureBalanceScannerInt(input, 1000000, 0);
					account.Withdraw(amount);
				}else if(choice == 2) {
					System.out.println("How much would you like to deposit?");
					int amount = ensureBalanceScannerInt(input, 1000000, 0);
					account.Deposit(amount);
				}else if(choice == 3) {
					System.out.println("How much would you like to transfer?");
					int amount = ensureBalanceScannerInt(input, 1000000, 0);
					System.out.println("Where would you like to transfer it too?");
					int where = ensureScannerInt(input, row+1, 1);
					if(where == account.getId()) {
						System.out.println("Nice Try... transferer and transferee cannot be the same account.");
					}else {
						account.Transfer(where, amount);
					}
				}else {
					run = false;
					break;
				}
			}
		}else {
			//Joint account
			while(run) {
				System.out.println("What transaction would you like to do?");
				System.out.println("Press 0 to return...");
				System.out.println("Press 1 to Withdraw...");
				System.out.println("Press 2 to Deposit...");
				System.out.println("Press 3 to Transfer...");
				System.out.println("Press 4 to add another user to the account...");
				int choice = ensureScannerInt(input, 5, 0);
				if(choice == 1) {
					System.out.println("How much would you like to withdraw?");
					int amount = ensureBalanceScannerInt(input, 1000000, 0);
					account.Withdraw(amount);
				}else if(choice == 2) {
					System.out.println("How much would you like to deposit?");
					int amount = ensureBalanceScannerInt(input, 1000000, 0);
					account.Deposit(amount);
				}else if(choice == 3) {
					System.out.println("How much would you like to transfer?");
					int amount = ensureBalanceScannerInt(input, 1000000, 0);
					System.out.println("Where would you like to transfer it too?");
					int where = ensureScannerInt(input, row+1, 1);
					account.Transfer(where, amount);
				}else if(choice == 4) {
					System.out.println("Please enter id of person you wish to add to the account!");
					int where = ensureScannerInt(input, row+1, 1);
					boolean response = account.addUserToAccount(currentUser, where);
					if(response) {
						System.out.println("Successful in adding user!");
					}else {
						System.out.println("Successful in adding user!");
					}
				}else {
					run = false;
					break;
				}
			}	
		}

	}

	private void viewAllAccounts() {
		BankTableDao btd = new BankTableDao();
		btd.printAllBankAccountsWithNamesView();
	}

	private int ensureScannerInt(Scanner input, int max, int min) {
		int choice = -1 ; 
		int choiceMax = max-1;
		while(choice==-1) {
			try {         
				System.out.print("Your choice: ");
				choice = input.nextInt();
				if(choice>choiceMax || choice<min){
					System.out.println("Invalid Input try again,");
					choice=-1;
				}
			}catch(Exception e) {
				input.next();
				System.out.println("Invalid input please try again,");
				System.out.print("Your choice: ");
				choice=-1;
			}
		}
		return choice;
	}

	public static int ensureBalanceScannerInt(Scanner input, int max, int min) {
		int choice = -1;
		while(choice==-1) {
			try {         
				System.out.print("Your choice: ");
				choice = input.nextInt();
				if(choice<min){
					System.out.println("Input too low try again...");
					choice=-1;
				}else if(choice > max) {
					System.out.println("Please be a little more realistic....");
					choice=-1;
				}
			}catch(Exception e) {
				input.next();
				System.out.println("Invalid input please try again,");
				choice=-1;
			}
		}
		return choice;
	}

	private int returnUserAccountRowCount() {
		UserTableDao utd = new UserTableDao();
		return utd.returnUserRowCount();
	}

	private int returnBankAccountRowCount() {
		BankTableDao btd = new BankTableDao();
		return btd.returnRowCount();
	}
}
