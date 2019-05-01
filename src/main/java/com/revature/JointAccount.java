package com.revature;

import java.util.ArrayList;

public class JointAccount extends BankAccount implements Withdrawable, Depositable, Transferable{

	public int id;
	private double balance;
	private String type;
	public int accountstatus;
	
	
	//Constructor
	JointAccount(){
		this.balance = 0;
		this.type = "Joint";
	}
	
	JointAccount(int id, double balance, String type, int accountstatus){
		this.id = id;
		this.balance = balance;
		this.type = "Joint\t";
		this.accountstatus = accountstatus;
		
	}
	
	public int getId() {
		return id;
	}
	
	public int setId() {
		return id;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}


	public int getAccountstatus() {
		return accountstatus;
	}


	public void setAccountstatus(int accountstatus) {
		this.accountstatus = accountstatus;
	}


	public void setType(String type) {
		this.type = type;
	}


	//Method to check Balance of the account
	@Override
	public void checkBalance() {
		System.out.println("Your balance is: $"+this.balance);
	}


	
	//Method to withdraw money from their bank account
	public void Withdraw(int amount) {
		if(amount < balance && amount > 0) {
		this.balance = this.balance-amount;
		saveTranstion();
		}else if(amount < 0){
			System.out.println("You tried to take out a negative value, please try again.");
		}else{
			System.out.println("Attempted to take out more than you have plese try again.");
		}
		System.out.println("Your current balance is $"+this.balance);
		
	}

	//Method to deposit money from their bank account
	public void Deposit(int amount) {
		if(amount > 0) {
		this.balance = this.balance+amount;
		System.out.println("Your current balance is $"+this.balance);
		}else {
			System.out.println("Invalid amount being deposited please try again later!");
		}
		saveTranstion();
	}
	
	//Method to transfer money from account to account
	public void Transfer(int transfereeID, double amount) {
		if(amount < 0 || amount >this.balance) {
			System.out.println("Invalid funds for transaction or invalid inpout please try again!");
			return;
		}else {
			ArrayList<BankAccount> accounts = new ArrayList<BankAccount>();
			accounts.add(retrieveAccount(this.id));
			accounts.add(retrieveAccount(transfereeID));
			this.readyForTransfer(accounts, amount);
		}
	}
	
	
	private BankAccount retrieveAccount(int id) {
		BankTableDao btd = new BankTableDao();
		return btd.getAccount(id);
	}
	
	
	private void readyForTransfer(ArrayList<BankAccount> accounts, double amount) {
		BankTableDao btd = new BankTableDao();
		boolean success = btd.TransferBetweenAccounts(accounts, amount);
		if(success) {
			System.out.println("Transfering funds from account #"+accounts.get(0).getId()+" to account #"+accounts.get(1).getId()+" was completed." );
		}else {
			System.out.println("There was an error trying to transfer please try again later!");
		}
	}
	
	
	//Method to return the type of account
	public String getType() {
		return type;
	}
	
	
	private void saveTranstion() {
		JointAccount account = new JointAccount(this.id, this.balance, this.type,this.accountstatus);
		BankTableDao btd = new BankTableDao();
		boolean success = btd.updateAccount(account);
		if(success) {
			System.out.println("Transaction success!");
		}else {
			System.out.println("There was an error with the transaction please try again later.");
		}
	} 
	
	
	
	@Override
	public boolean addUserToAccount(Users currentUser,int id) {
		boolean success = false;
		if(currentUser.getId() == id) {
			System.out.println("Cannot add yourself to the account please try again.");
			return false;
		}else {
			success = this.addUserToJoint(id);
			return success;
		}
	}
	
	
	private boolean addUserToJoint(int userid) {
		JoinTableDao jtd = new JoinTableDao();
		return jtd.addUserToJoint(userid, this.id);
		
	} 
	
	
	
}
