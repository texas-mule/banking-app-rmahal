package com.revature;

abstract class BankAccount implements Withdrawable, Depositable, Transferable{
	public abstract void checkBalance();
}
