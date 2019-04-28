package com.revature;

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
		// TODO Auto-generated method stub
		System.out.println("Your balance is: $"+this.balance);
	}


	
	//Method to withdraw money from their bank account
	public void Withdraw(int amount) {
		// TODO Auto-generated method stub
		if(amount < balance) {
		this.balance = this.balance-amount;
		}else {
			System.out.println("Attempted to ");
		}
		System.out.println("Your current balance is $"+this.balance);
	}

	//Method to deposit money from their bank account
	public void Deposit(int amount) {
		// TODO Auto-generated method stub
		if(amount > 0) {
		this.balance = this.balance+amount;
		System.out.println("Your current balance is $"+this.balance);
		}else {
			System.out.println("Invalid amount being deposited please try again later!");
		}
	}
	
	//Method to transfer money from account to account
	public void Transfer(int transferee, int amount) {
		
	}
	
	//Method to return the type of account
	public String getType() {
		return type;
	}
}
