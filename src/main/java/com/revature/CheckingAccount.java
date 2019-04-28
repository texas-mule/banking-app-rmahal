package com.revature;

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
		// TODO Auto-generated method stub
		System.out.println("Your balance is: $"+this.balance);
	}
	
	
	//Method to withdraw money from their bank account
	public void Withdraw(int amount) {
		// TODO Auto-generated method stub
		if(amount < balance && amount > 0) {
		this.balance = this.balance-amount;
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
	}
	
	//Method to transfer money from account to account
	public void Transfer(int transferee, int amount) {
		System.out.print("I NEED A BETTER IMPLEMENTATION");
	}
	
	
	
	private void saveAccountToDB() {
		CheckingAccount account = new CheckingAccount(this.i,this.balance. this.accountstatus);
	}
	
	
	
	
	

}
