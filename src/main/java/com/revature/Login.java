package com.revature;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class Login {
	Login(){
	}

	public void welcome(boolean running, int userRow, int bankRow) throws IOException {
		boolean run = running;
		int userRowCounts = userRow;
		int bankRowCounts = bankRow;
		while (run) {
			System.out.println("Type 0 at any point to end the program!");
			System.out.println("Press 1 to login...");
			System.out.println("Press 2 to create a new account...");
			Scanner input = new Scanner(System.in);
			int option = ensureScannerInt(input, 3, 0);
			if(option == 1) {
				
				login(bankRowCounts);
				
			}else if(option == 2) {
				
				createAccount(userRowCounts);
				
			}else if(option == 0){
				run = false;
				break;
			}else{
				System.out.println("Incorrect option!");
				break;
			}
			run = false;
			input.close();
		}
	}

	public void createAccount(int row) throws IOException {
		Scanner input = new Scanner(System.in);
		System.out.println("=====================");
		System.out.println("Please enter your information for account creation when promped...");
		System.out.print("Please enter your First Name: ");
		String fname = ensureScannerString(input);	
		System.out.print("Please enter your Last Name: ");
		String lname = ensureScannerString(input);
		System.out.print("Please enter your username: ");
		String username = ensureScannerString(input);
		System.out.println(username);
		System.out.print("Please enter your password: ");
		String password = ensureScannerString(input);
		int authtype = 1;
		saveAccount(row, fname, lname, username, password, authtype);
		System.out.println("Account created successfully!");
		System.out.println("Please Try Logging In.....");
		row++;
		login(row);
	}

	public void saveAccount(int row, String fname, String lname, String username, String password, int authtype) {
		// TODO Auto-generated method stub
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

	public void login(int bankRowCounts) throws IOException{
		Scanner input = new Scanner(System.in);
		System.out.println("=====================");
		System.out.println("Please enter your login credentials when prompted...");
		System.out.print("Please enter your username: ");
		String username = ensureScannerString(input);
		System.out.print("Please enter your password: ");
		String password = ensureScannerString(input);
		System.out.println("Checking Credentials!!!!");
		boolean exists = authLogin(username, password);//PINNED
		if(exists) {
			users currentUser = returnLoggedInUser(username,password);
			System.out.println("AuthType");
			System.out.println(currentUser.authtype);
			System.out.println("USER LOGIN WAS A SUCCESS!");
			successLogin(currentUser, bankRowCounts);
		}else {
			System.out.println("COULD NOT VERIFY PLEASE START OVER!");
		}
	}

	public boolean authLogin(String username, String password) {
		String url  = "jdbc:postgresql://127.0.0.1:8001/postgres";
		String dbusername = "postgres";
		String dbpassword = "test";
		
		try (
			Connection connection = DriverManager.getConnection(url,dbusername,dbpassword);
			Statement statement = connection.createStatement();
		) { 
			String sql ="SELECT * FROM public.users WHERE username='"+username+"' AND password='"+password+"'";
	
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

	public void successLogin(users currentUser, int bankRowCounts) {
		if (currentUser.authtype == 3) {
			
		}else if(currentUser.authtype == 2) {
			EmployeeLogin emplogin = new EmployeeLogin(currentUser);
			emplogin.welcome();
			
		}else {
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
				System.out.print("Please pick option: ");
				int option = ensureScannerInt(input, 4, 0);
				if(option == 1) {
					System.out.println("These are your accounts.");
					int i = 0;
					System.out.println("ID\tTYPE\t\tBALANCE\tSTATUS");
					while(i<accounts.size()) {
						System.out.println(accounts.get(i).getId()+"\t"+accounts.get(i).getType()+"\t"+accounts.get(i).getBalance()+"\t"+accounts.get(i).getAccountstatus());
						i++;
					}

				}else if(option == 2) {
					System.out.println("What kind of account would you like to apply for?");
					System.out.println("Press 0 to go back.");
					System.out.println("Press 1 to create a new Checkings account.");
					System.out.println("Press 2 to create a new Joint account.");
					System.out.print("Please pick an option, ");
					option = ensureScannerInt(input, 3, 0);
					if(option == 0) {
						System.out.println("Going back!");
					}else if(option == 1) {
						saveNewBankAccount(currentUser, row, "Checking", 0, 2);	
						System.out.println("Applied for new Checkings Account.");
						System.out.println("Account created pending approval!........");
						row++;
						System.out.println("\n");
					}else if(option == 2) {
						System.out.println("Applied for new Joint Account.");
						saveNewBankAccount(currentUser, row, "Joint", 0, 2);
						System.out.println("Account created pending approval!");
						row++;
						System.out.println("NEED TO SET UP OTHER USER FOR JOINT ACCOUNT");
					}	
				}else if(option == 3) {
					System.out.println("Please enter id of account you wish to access");
					System.out.print("Your choice: ");
					int choice = ensureScannerInt(input, bankRowCounts,1);
					int i = 0;
					int pick = 0;
					CheckingAccount ckaccount = null;
					JointAccount joiaccount = null;

					while(i<accounts.size()){
						System.out.println("Choice Was:"+choice);
						System.out.println("Id rn is: "+accounts.get(i).getId());
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
					}else if(pick == 1) {
						//System.out.println(ckaccount.getType());
						singleAccountOptions(ckaccount, row);
					}else {
						singleAccountOptions(joiaccount, row);
					}

				}else {
					break;
				}
			}
		}
	}

	
	private void singleAccountOptions(BankAccount account, int row) {
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
					account.Transfer(where, amount);
				}else {
					run = false;
					break;
				}
			}
		}else {
			//Joint account
			while(run) {
				System.out.println("Press 0 to return...");
				System.out.println("What transaction would you like to do?");
				System.out.println("Press 1 to Deposit...");
				System.out.println("Press 2 to Withdraw...");
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
					
				}else {
					run = false;
					break;
				}
			}	
		}
		
	}
	
	private void addAnotherUserToJointAccount(int id) {
		
	}
	
	private void saveNewBankAccount(users currentUser, int row, String accounttype, int balance, int accountstatus) {
		//account status 1 approved
		//account status 2 pending
		//account status 3 denied
		//account status 4 canceled
		String url  = "jdbc:postgresql://127.0.0.1:8001/postgres";
		String dbusername = "postgres";
		String dbpassword = "test";
		
		System.out.println("UserID: "+currentUser.id+" RowID: "+row);
		
		try (
			Connection connection = DriverManager.getConnection(url,dbusername,dbpassword);
			Statement statement = connection.createStatement();
		) { 
			String sql = "INSERT INTO public.bankaccounts (id, accounttype, balance, accountstatus) VALUES ("+row+", '"+accounttype+"', "+balance+", "+accountstatus+")";
			System.out.println(sql);
			int resSet = statement.executeUpdate(sql);
			System.out.println(resSet);
			joinBankUser(currentUser, row);
			} catch (SQLException ex) {
				System.out.println("DB did not work in saving new bank account!");
				System.out.println(ex.getMessage());
		}
		
	}
	
	private void joinBankUser(users currentUser, int row) {
		String url  = "jdbc:postgresql://127.0.0.1:8001/postgres";
		String dbusername = "postgres";
		String dbpassword = "test";
		
		try (
			Connection connection = DriverManager.getConnection(url,dbusername,dbpassword);
			Statement statement = connection.createStatement();
		) { 
			int id = currentUser.id;
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

	public users returnLoggedInUser(String inpusername, String inppassword) {
		
		String url  = "jdbc:postgresql://127.0.0.1:8001/postgres";
		String dbusername = "postgres";
		String dbpassword = "test";
		
		try (
			Connection connection = DriverManager.getConnection(url,dbusername,dbpassword);
			Statement statement = connection.createStatement();
		) { 
			String sql ="SELECT * FROM public.users WHERE username='"+inpusername+"' AND password='"+inppassword+"'";

			ResultSet resSet = statement.executeQuery(sql);
			resSet.next();
			int id = resSet.getInt("id");
			String fname = resSet.getString("firstname");
			String lname = resSet.getString("lastname");
			String username = resSet.getString("username");
			String password = resSet.getString("password");
			int authtype = resSet.getInt("authtype");
			
			if(authtype == 3) {
				Admin currentUser = new Admin(id,fname,lname,username,password,authtype);
				return currentUser;
			}else if (authtype ==2){
				Employees currentUser = new Employees(id,fname,lname,username,password,authtype);
				return currentUser;
			}else {
				users currentUser = new users(id,fname,lname,username,password,authtype);
				return currentUser;
			}
			} catch (SQLException ex) {
				System.out.println("DB did not work!");
				System.out.println(ex.getMessage());
			}
		return null;
	}

	public ArrayList<BankAccount> returnBankAccounts(users currentUser) {
		
		String url  = "jdbc:postgresql://127.0.0.1:8001/postgres";
		String dbusername = "postgres";
		String dbpassword = "test";
		
		ArrayList<BankAccount> accounts = new ArrayList();
		try (
				Connection connection = DriverManager.getConnection(url,dbusername,dbpassword);
				Statement statement = connection.createStatement();
			) { 
		String sql = "WITH joinusersbank as(SELECT bankaccountid FROM joinusersbank WHERE userid="+currentUser.id+") SELECT * FROM joinusersbank INNER JOIN bankaccounts ON joinusersbank.bankaccountid = bankaccounts.id";
		ResultSet resSet = statement.executeQuery(sql);
		while(resSet.next()) {
			//System.out.println(resSet.getString("accounttype"));
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
	
	
	public static int ensureScannerInt(Scanner input, int max, int min) {
		int choice = -1 ; 
		int choiceMax = max-1;
		System.out.println("MAX: "+max+" Choice Max:"+choiceMax);
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
	
	public static String ensureScannerString(Scanner input) {
		String choice = ""; 
		while(choice.equals("")) {
            try {         
 	           choice = input.nextLine();
	           if(choice.contains(";") || choice.contains("*") || choice.equals("\n") || choice.equals("") || choice.contains(" ")){
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
	
	
	
	public static int ensureBalanceScannerInt(Scanner input, int max, int min) {
		int choice = -1 ; 
		int choiceMax = max-1;
		while(choice==-1) {
            try {         
               System.out.print("Your choice:");
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
                System.out.println("Invalid input");
                choice=-1;
            }
        }
		return choice;
	}
	
	
	
	
	
		
}

