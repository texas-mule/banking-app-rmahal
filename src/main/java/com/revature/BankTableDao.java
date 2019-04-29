package com.revature;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Set;

public class BankTableDao implements BankDao{


	public BankAccount getAccount(String username, String password) {
		// TODO Auto-generated method stub
		return null;
	}


	public ArrayList<BankAccount> getAllAccounts() {
		// TODO Auto-generated method stub

		return null;
	}


	public boolean insertAccount(Users currentUser, int row,BankAccount ba) {
		//account status 1 approved
		//account status 2 pending
		//account status 3 denied
		//account status 4 canceled
		
		try (
			Connection connection = ConnectionFactory.getConnection();
			Statement statement = connection.createStatement();
		) { 
			String sql = "INSERT INTO public.bankaccounts (id, accounttype, balance, accountstatus) VALUES ("+row+", '"+ba.getType()+"', "+ba.getBalance()+", "+ba.getAccountstatus()+")";
			//System.out.println(sql);
			int resSet = statement.executeUpdate(sql);
			//System.out.println(resSet);
			JoinTableDao jtd = new JoinTableDao();
			jtd.insertJoin(currentUser, row);
			return true;
			} catch (SQLException ex) {
				System.out.println("DB did not work in saving new bank account!");
				System.out.println(ex.getMessage());
				return false;
		}
	}


	public boolean updateAccountr() {
		// TODO Auto-generated method stub
		return false;
	}


	public boolean deleteAccount() {
		// TODO Auto-generated method stub
		return false;
	}

	public int returnRowCount() {	
		try (
			Connection connection = ConnectionFactory.getConnection();
			Statement statement = connection.createStatement();
		) {   // executeUpdate() returns the number of rows affected for DML
			ResultSet resSet = statement.executeQuery("SELECT COUNT(*) AS rowcount FROM bankaccounts");
			resSet.next();
			int count = resSet.getInt("rowcount");
			resSet.close();
			connection.close();
			return count;
				
			} catch (SQLException ex) {
				System.out.println("DB did not work!");
				System.out.println(ex.getMessage());
				return 0;
			}
	}


	@Override
	public ArrayList<BankAccount> getUserBankAccounts(Users currentUser) {
		
		ArrayList<BankAccount> accounts = new ArrayList();
		try (
				Connection connection = ConnectionFactory.getConnection();
				Statement statement = connection.createStatement();
			) { 
		String sql = "WITH joinusersbank as(SELECT bankaccountid FROM joinusersbank WHERE userid="+currentUser.id+") SELECT * FROM joinusersbank INNER JOIN bankaccounts ON joinusersbank.bankaccountid = bankaccounts.id";
		ResultSet resSet = statement.executeQuery(sql);
		while(resSet.next()) {
			if(resSet.getString("accounttype").equals("Checking")) {
				CheckingAccount chaccount = new CheckingAccount(resSet.getInt("id"), resSet.getDouble("balance"), resSet.getString("accounttype"), resSet.getInt("accountstatus"));
				accounts.add(chaccount);
			}else {
				JointAccount joiaccount = new JointAccount(resSet.getInt("id"), resSet.getDouble("balance"), resSet.getString("accounttype"), resSet.getInt("accountstatus"));
				accounts.add(joiaccount);
			}
		}
		connection.close();
		return accounts;
	} catch (SQLException ex) {
		System.out.println("DB did not work initializing bank accounts!");
		System.out.println(ex.getMessage());
	}
		return accounts;
	}
	
}

