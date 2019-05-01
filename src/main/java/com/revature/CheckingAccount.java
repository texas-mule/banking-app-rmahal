package com.revature;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CheckingAccount extends BankAccount implements Withdrawable, Depositable, Transferable{


	public int id;
	public String type;
	public double balance;
	public int accountstatus;

	//Constructor
	CheckingAccount(){
		this.balance = 0;
		this.type = "Checking";
	}

	CheckingAccount(int id, double balance, String type, int accountstatus){
		this.id = id;
		this.balance = balance;
		this.type = "Checking";
		this.accountstatus = accountstatus;	
	}


	public int getId() {
		return id;
	}

	public int setId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	//Method to check balance of account
	@Override
	public void checkBalance() {
		DecimalFormat df = new DecimalFormat("0.00");
		System.out.println("Your balance is: $"+df.format(this.balance));
	}


	//Method to withdraw money from their bank account
	public void Withdraw(double amount) {
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
	public void Deposit(double amount) {
		if(amount > 0) {
			this.balance = this.balance+amount;
			saveTranstion();
			System.out.println("Your current balance is $"+this.balance);
		}else {
			System.out.println("Invalid amount being deposited please try again later!");
		}
	}

	//Method to transfer money from account to account
	public void Transfer(int transfereeID, double amount) {
		if(this.id == transfereeID) {
			System.out.println("Cannot transfer funds to the same account!");
			System.out.println();
			return;
		}else {
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
	}

	private BankAccount retrieveAccount(int id) {
		BankTableDao btd = new BankTableDao();
		return btd.getAccount(id);
	}


	private void readyForTransfer(ArrayList<BankAccount> accounts, double amount) {
		BankTableDao btd = new BankTableDao();
		boolean success = btd.TransferBetweenAccounts(accounts, amount);
		if(success) {
			System.out.println("Transfering funds from account #"+accounts.get(0).getId()+" to account #"+accounts.get(1)+" was completed." );
		}else {
			System.out.println("There was an error trying to transfer please try again later!");
		}
	}



	private void saveTranstion() {
		CheckingAccount account = new CheckingAccount(this.id, this.balance, this.type,this.accountstatus);
		BankTableDao btd = new BankTableDao();
		boolean success = btd.updateAccount(account);
		if(success) {
			System.out.println("Transaction success!");
		}else {
			System.out.println("There was an error with the transaction please try again later.");
		}
	} 






}
