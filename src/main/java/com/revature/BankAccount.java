package com.revature;

abstract class BankAccount implements Withdrawable, Depositable, Transferable{
	
	
	public abstract void checkBalance();


	public abstract int getId();

	public abstract int setId();

	public abstract double getBalance();


	public abstract void setBalance(double balance);


	public abstract String getType();


	public abstract void setType(String type);


	public abstract int getAccountstatus();


	public abstract void setAccountstatus(int accountstatus);
	
	public void addUserToAccount(int id) {}
}
