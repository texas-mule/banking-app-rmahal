package com.revature;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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
		saveTranstion();
		System.out.println("Your current balance is $"+this.balance);
		}else {
			System.out.println("Invalid amount being deposited please try again later!");
		}
	}
	
	//Method to transfer money from account to account
	public void Transfer(int transfereeID, int amount) {
		CheckingAccount account = new CheckingAccount(this.id, this.balance, this.type,this.accountstatus);
		
		//account status 1 approved
		//account status 2 pending
		//account status 3 canceled
		//account status 4 denied
		String url  = "jdbc:postgresql://127.0.0.1:8001/postgres";
		String dbusername = "postgres";
		String dbpassword = "test";

		try (
			Connection connection = DriverManager.getConnection(url,dbusername,dbpassword);
			Statement statement = connection.createStatement();
		) { 
			String sql = "UPDATE public.bankaccounts SET id="+account.getId()+", accounttype='"+account.getType()+"', balance="+account.getBalance()+", accountstatus="+account.accountstatus+" WHERE id="+account.getId();
			//System.out.println(sql);
			int resSet = statement.executeUpdate(sql);
			System.out.println("Transaction Saved!");
			
			} catch (SQLException ex) {
				System.out.println("DB did not work in saving transaction!");
				System.out.println(ex.getMessage());
		}
		
	}
	
	
	private void saveTranstion() {
		CheckingAccount account = new CheckingAccount(this.id, this.balance, this.type,this.accountstatus);
		
		//account status 1 approved
		//account status 2 pending
		//account status 3 canceled
		//account status 4 denied
		String url  = "jdbc:postgresql://127.0.0.1:8001/postgres";
		String dbusername = "postgres";
		String dbpassword = "test";

		try (
			Connection connection = DriverManager.getConnection(url,dbusername,dbpassword);
			Statement statement = connection.createStatement();
		) { 
			String sql = "UPDATE public.bankaccounts SET id="+account.getId()+", accounttype='"+account.getType()+"', balance="+account.getBalance()+", accountstatus="+account.accountstatus+" WHERE id="+account.getId();
			//System.out.println(sql);
			int resSet = statement.executeUpdate(sql);
			System.out.println("Transaction Saved!");
			
			} catch (SQLException ex) {
				System.out.println("DB did not work in saving transaction!");
				System.out.println(ex.getMessage());
		}
	} 
	
	
	
	
	

}
