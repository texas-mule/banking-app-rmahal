package com.revature;

public class checkingAccount extends bankAccount implements Withdrawable, Depositable{

	private int balance;
	private String type;
	protected boolean active;
	
	checkingAccount(){
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
	}

	public void Deposit(int amount) {
		// TODO Auto-generated method stub
		this.balance = this.balance+amount;
	}

	public String getType() {
		return type;
	}
	
}
