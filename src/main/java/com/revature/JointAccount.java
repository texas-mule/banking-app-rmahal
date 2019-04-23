package com.revature;

public class JointAccount extends BankAccount implements Withdrawable, Depositable, Transferable{


	private int balance;
	private String type;
	protected boolean active;
	public int acountNumber;
	
	
	//Constructor
	JointAccount(){
		this.balance = 0;
		this.type = "Joint";
	}
	
	
	//Method to check Balance of the account
	@Override
	public void checkBalance() {
		// TODO Auto-generated method stub
		System.out.println("Your balance is: $"+this.balance);
	}

	//Method to toggle activity, when employee or admin want to alter
	public void toggleActive() {
		if(this.active == true) {
			this.active = false;
			System.out.println("Account is now deactivated.");
		}else {
			this.active = true;
			System.out.println("Account is now activated!.");
		}
	}
	

	//Method to let user check the status of their account after applying for one
	public void checkStatus() {
		if(this.active == true) {
			System.out.println("Account is currently deactivated.");
		}else {
			this.active = true;
			System.out.println("Account is currently activated!.");
		}
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
	public void Transfer(int transferer, int transferee, int amount) {
		
	}
	
	//Method to return the type of account
	public String getType() {
		return type;
	}	
}
