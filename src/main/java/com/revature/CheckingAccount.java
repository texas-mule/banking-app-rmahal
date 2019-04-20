package com.revature;

public class CheckingAccount extends BankAccount implements Withdrawable, Depositable{

	private int balance;
	private String type;
	protected boolean active;
	
	CheckingAccount(){
		this.balance = 0;
		this.type = "Checkings";
	}
	
	@Override
	public void checkBalance() {
		// TODO Auto-generated method stub
		System.out.println("Your balance is: $"+this.balance);
	}


//	public void setBalance(int balance) {
//		this.balance = balance;
//	}


	public void toggleActive() {
		if(this.active == true) {
			this.active = false;
			System.out.println("Account is now deactivated.");
		}else {
			this.active = true;
			System.out.println("Account is now activated!.");
		}
	}
	
	public void checkStatus() {
		if(this.active == true) {
			System.out.println("Account is currently deactivated.");
		}else {
			this.active = true;
			System.out.println("Account is currently activated!.");
		}
	}
	
	public void Withdraw(int amount) {
		// TODO Auto-generated method stub
		if(amount < balance) {
		this.balance = this.balance-amount;
		}else {
			System.out.println("Attempted to ");
		}
		System.out.println("Your current balance is $"+this.balance);
	}

	public void Deposit(int amount) {
		// TODO Auto-generated method stub
		if(amount > 0) {
		this.balance = this.balance+amount;
		System.out.println("Your current balance is $"+this.balance);
		}else {
			System.out.println("Invalid amount being deposited please try again later!");
		}
	}

	public String getType() {
		return type;
	}
	
}
