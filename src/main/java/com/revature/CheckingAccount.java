package com.revature;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
		String url  = "jdbc:postgresql://127.0.0.1:8001/postgres";
		String dbusername = "postgres";
		String dbpassword = "test";
		CheckingAccount ca =null;
		JointAccount ja =null;		
		
		try (
				Connection connection = DriverManager.getConnection(url,dbusername,dbpassword);
				Statement statement = connection.createStatement();
		) { 
		String sql = "SELECT * FROM bankaccounts WHERE id="+id;
		//System.out.println(sql);
		ResultSet resSet = statement.executeQuery(sql);
		resSet.next();
		if(resSet.getString("accounttype").equals("Checking")) {
			ca = new CheckingAccount(resSet.getInt("id"),resSet.getDouble("balance"), resSet.getString("accounttype"), resSet.getInt("accountstatus"));
		}else {
			ja = new JointAccount(resSet.getInt("id"),resSet.getDouble("balance"), resSet.getString("accounttype"), resSet.getInt("accountstatus"));
		}
		
		if(ca != null) {
			return ca;
		}else {
			return ja;
		}
		
		} catch (SQLException ex) {
			System.out.println("DB did not work in saving transaction!");
			System.out.println(ex.getMessage());
	}
		
		return null;
	}
	
	
	private void readyForTransfer(ArrayList<BankAccount> accounts, double amount) {
		String url  = "jdbc:postgresql://127.0.0.1:8001/postgres";
		String dbusername = "postgres";
		String dbpassword = "test";
		double senderBal = accounts.get(0).getBalance()-amount;
		double recieverBal = accounts.get(1).getBalance()+amount;
		
		try (
				Connection connection = DriverManager.getConnection(url,dbusername,dbpassword);
				Statement statement = connection.createStatement();
		) { 
		String sql = "UPDATE public.bankaccounts SET id="+accounts.get(0).getId()+", accounttype='"+accounts.get(0).getType()+"', balance="+senderBal+", accountstatus="+accounts.get(0).getAccountstatus()+" WHERE id="+accounts.get(0).getId()+";"+"UPDATE public.bankaccounts SET id="+accounts.get(1).getId()+", accounttype='"+accounts.get(1).getType()+"', balance="+recieverBal+", accountstatus="+accounts.get(1).getAccountstatus()+" WHERE id="+accounts.get(1).getId()+";";
		//System.out.println(sql);
		int resSet = statement.executeUpdate(sql);
		System.out.println("Transfer Completed!");
		
		} catch (SQLException ex) {
			System.out.println("DB did not work in saving transaction!");
			System.out.println(ex.getMessage());
	}
	}
	
	
	
	private void saveTranstion() {
		CheckingAccount account = new CheckingAccount(this.id, this.balance, this.type,this.accountstatus);

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
