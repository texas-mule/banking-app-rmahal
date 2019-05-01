package com.revature;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Login {

	public void welcome(boolean running, int userRow, int bankRow) throws IOException {
		boolean run = running;
		int userRowCounts = userRow;
		int bankRowCounts = bankRow;
		while (run) {
			System.out.println("Welcome to banking app!");
			System.out.println("Press 1 to login...");
			System.out.println("Press 2 to create a new account...");
			System.out.println();
			Scanner input = new Scanner(System.in);
			int option = ensureScannerInt(input, 3, 0);
			if(option == 1) {
				login(bankRowCounts);
			}else if(option == 2) {
				createAccount(userRowCounts);
				userRowCounts++;
			}else if(option == 0){
				run = false;
				break;
			}else{
				System.out.println("Incorrect option!");
				break;
			}
		}
	}

	public void createAccount(int row) throws IOException {
		Scanner input = new Scanner(System.in);
		System.out.println();
		System.out.println("=====================");
		System.out.println();
		System.out.println("Please enter your information for account creation when promped...");
		System.out.print("Please enter your First Name: ");
		String fname = ensureScannerString(input);	
		System.out.print("Please enter your Last Name: ");
		String lname = ensureScannerString(input);
		System.out.print("Please enter your username: ");
		String username = ensureScannerString(input);
		//System.out.println(username);
		System.out.print("Please enter your password: ");
		String password = ensureScannerString(input);
		int authtype = 1;
		saveAccount(row, fname, lname, username, password, authtype);
		System.out.println("Please Try Logging In Now.....");
		System.out.println("=================");
		System.out.println("\n");
	}

	public void saveAccount(int row, String fname, String lname, String username, String password, int authtype) {
		Users user = new Users(row, fname, lname,username, password, authtype);
		UserTableDao utd = new UserTableDao();
		boolean success = utd.insertUser(row, user);
		if(success) {
			System.out.println("Account created successfully!");
		}else {
			System.out.println("Error in adding user, please restart app and try again!");
		}
	}

	public void login(int bankRowCounts) throws IOException{
		Scanner input = new Scanner(System.in);
		System.out.println("=====================");
		System.out.println("Please enter your login credentials when prompted...");
		System.out.print("Please enter your username: ");
		String username = ensureScannerString(input);
		System.out.print("Please enter your password: ");
		String password = ensureScannerString(input);
		System.out.println("Checking Credentials!!!!");
		System.out.println();
		UserTableDao utd = new UserTableDao();
		boolean exists = utd.getIfUserExists(username, password);
		if(exists) {
			Users currentUser = returnLoggedInUser(username,password);
			System.out.println("====================");
			successLogin(currentUser, bankRowCounts);
		}else {
			System.out.println("COULD NOT VERIFY PLEASE START OVER!");
		}
	}

	public boolean authLogin(String username, String password) {
		UserTableDao utd = new UserTableDao();
		return utd.authUser(username, password);
	}

	public void successLogin(Users currentUser, int bankRowCounts) {
		if (currentUser.getAuthtype() == 3) {
			AdminLogin adlogin = new AdminLogin(currentUser);
			adlogin.welcome();
		}else if(currentUser.getAuthtype() == 2) {
			EmployeeLogin emplogin = new EmployeeLogin(currentUser);
			emplogin.welcome();
			
		}else if(currentUser.getAuthtype() == 1) {
			System.out.println("Welcome user "+currentUser.getFirstname()+" "+currentUser.getLastname()+"!");
			int row = bankRowCounts;
			ArrayList<BankAccount> accounts = new ArrayList<BankAccount>();
			boolean run = true;
			while(run) {
				accounts = returnBankAccounts(currentUser);
				Scanner input = new Scanner(System.in);
				System.out.println("Press 0 to log out.");
				System.out.println("Press 1 to see current bank accounts.");
				System.out.println("Press 2 to create a new bank account.");
				System.out.println("Press 3 to access an account.");
				//System.out.print("Please pick option: ");
				int option = ensureScannerInt(input, 4, 0);
				if(option == 1) {
					System.out.println("These are your accounts.");
					int i = 0;
					System.out.println("ID\tTYPE\t\tBALANCE\tSTATUS");
					while(i<accounts.size()) {
						System.out.println(accounts.get(i).getId()+"\t"+accounts.get(i).getType()+"\t"+accounts.get(i).getBalance()+"\t"+accounts.get(i).getAccountstatus());
						i++;
					}
					System.out.println();

				}else if(option == 2) {
					int numOfPendAccounts = this.numOfPending(currentUser);
					if(numOfPendAccounts >= 2) {
						System.out.println("Sorry you already have 2 or more accounts pending please wait until they are processed, Thank you!");
						System.out.println();
					}else {
						
					
					System.out.println("What kind of account would you like to apply for?");
					System.out.println("Press 0 to go back.");
					System.out.println("Press 1 to create a new Checkings account.");
					System.out.println("Press 2 to create a new Joint account.");
					//System.out.print("Please pick an option, ");
					option = ensureScannerInt(input, 3, 0);
					if(option == 0) {
						System.out.println("Going back!");
					}else if(option == 1) {	
						saveNewBankAccount(currentUser, row, "Checking", 0, 2);	
						System.out.println("Applied for new Checkings Account.");
						row++;
						System.out.println("\n");
					}else if(option == 2) {
						saveNewBankAccount(currentUser, row, "Joint", 0, 2);
						System.out.println("Applied for new Joint Account.");
						System.out.println();
						row++;
					}
					}
				}else if(option == 3) {
					System.out.println("Please enter id of account you wish to access");
					int choice = ensureScannerInt(input, bankRowCounts,1);
					int i = 0;
					int pick = 0;
					CheckingAccount ckaccount = null;
					JointAccount joiaccount = null;

					while(i<accounts.size()){
						if(accounts.get(i).getId() == choice) {
							if(accounts.get(i).getType().equals("Checking") && accounts.get(i).getAccountstatus() == 1) {
								ckaccount = new CheckingAccount(accounts.get(i).getId(), accounts.get(i).getBalance(),accounts.get(i).getType(),accounts.get(i).getAccountstatus());
								pick = 1;
							}else if(accounts.get(i).getType().equals("Joint\t") && accounts.get(i).getAccountstatus() == 1){
								joiaccount = new JointAccount(accounts.get(i).getId(), accounts.get(i).getBalance(),accounts.get(i).getType(),accounts.get(i).getAccountstatus());
								pick=2;
							}
						}
						i++;
					}

					if(pick == 0) {
						System.out.println("==============");
						System.out.println("Invalid choice for bank account.....");
						System.out.println("==============");
						System.out.println();
					}else if(pick == 1) {
						//System.out.println(ckaccount.getType());
						singleAccountOptions(currentUser, ckaccount, row);
					}else {
						singleAccountOptions(currentUser, joiaccount, row);
					}

				}else {
					break;
				}
			}
		}else {
			
			System.out.println("Invalid account please try again later.");
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
	
	private void saveNewBankAccount(Users currentUser, int row, String accounttype, double balance, int accountstatus) {
		BankTableDao btd = new BankTableDao();
		boolean success = false;
		CheckingAccount ca = null;
		JointAccount ja = null;
		if(accounttype.equals("Checking")) {
			ca = new CheckingAccount(row, balance, accounttype, accountstatus);
			success = btd.insertAccount(currentUser, row, ca);
		}else {
			ja = new JointAccount(row, balance, accounttype, accountstatus);
			success = btd.insertAccount(currentUser, row, ja);
		}
		if(success) {
			System.out.println("Account created sucessfully, pending approval");
		}else {
			System.out.println("Account creation failed please restart application");
		}
	}

	public Users returnLoggedInUser(String inpusername, String inppassword) {
		UserTableDao utd = new UserTableDao();
		Users user = utd.getUser(inpusername, inppassword);
		if(user != null) {
			return user;
		}else {
			return null;
		}
	}

	public ArrayList<BankAccount> returnBankAccounts(Users currentUser) {
		BankTableDao btd = new BankTableDao();
		return btd.getUserBankAccounts(currentUser);
	}
	
	private int numOfPending(Users currentUser) {
		BankTableDao btd = new BankTableDao();
		ArrayList<BankAccount> accounts = btd.getUserBankAccounts(currentUser);
		int count =0;
		int i=0;
		while(i<accounts.size()) {
			if(accounts.get(i).getAccountstatus() == 2) {
				count++;
			}
			i++;
		}
		return count;
	}
	
	public static int ensureScannerInt(Scanner input, int max, int min) {
		int choice = -1 ; 
		int choiceMax = max-1;

		while(choice==-1) {
            try {         
               System.out.print("Your choice: ");
 	           choice = input.nextInt();
        	   System.out.println();
	           if(choice>choiceMax || choice<min){
	                System.out.println("Invalid Input try again,");
	        	   choice=-1;
	           }
            }catch(Exception e) {
                input.next();
                System.out.println("Invalid Input try again,");
                choice=-1;
            }
        }
		return choice;
	}
	
	public static String ensureScannerString(Scanner input) {
		String choice = ""; 
		while(choice.equals("")) {
            try {         
 	           choice = input.nextLine();
        	   System.out.println();
	           if(choice.contains(";") || choice.contains("*") || choice.equals("\n") || choice.equals("") || choice.contains(" ")){
	                System.out.println("Sorry incorrect input, please try again!");
	                System.out.print("Your choice: ");
	        	   choice="";
	        	   System.out.println();
	           }
            }catch(Exception e) {
                input.next();
                System.out.println("Sorry improper input...");
                choice="";
            }
        }
		return choice;
	}
	
	public static int ensureBalanceScannerInt(Scanner input, int max, int min) {
		int choice = -1;
		while(choice==-1) {
            try {         
               System.out.print("Your choice:");
 	           choice = input.nextInt();
        	   System.out.println();
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
}

