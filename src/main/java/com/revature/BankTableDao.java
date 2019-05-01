package com.revature;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class BankTableDao implements BankDao{

	
	//Potentially needed line
	//resSet.getString("firstname")+"\t\t"+resSet.getString("lastname")+"\t\t"+resSet.getInt("id")+"\t"+resSet.getString("accounttype")+"\t"+resSet.getDouble("balance")+"\t"+resSet.getString("accountstatus");


	public BankAccount getAccount(int id) {
		String sql="SELECT * FROM public.bankaccounts WHERE id="+id;	
		try (
				Connection connection = ConnectionFactory.getConnection();
				Statement statement = connection.createStatement();
				) { 
			ResultSet resSet = statement.executeQuery(sql);
			CheckingAccount ca = null;
			JointAccount ja = null;
			resSet.next();
			if(resSet.getString("accounttype").equals("Checking")) {
				ca = new CheckingAccount(resSet.getInt("id"),resSet.getDouble("balance"),resSet.getString("accounttype"),resSet.getInt("accountstatus"));
			}else {
				ja = new JointAccount(resSet.getInt("id"),resSet.getDouble("balance"),resSet.getString("accounttype"),resSet.getInt("accountstatus"));
			}
			connection.close();
			if(ca != null) {
				return ca;
			}else {
				return ja;
			}
		} catch (SQLException ex) {
			System.out.println("DB did not work couldn't load bank accounts!");
			System.out.println(ex.getMessage());
			return null;
		}
	}

	public ArrayList<BankAccount> getAllAccounts() {

		ArrayList<BankAccount> accounts = new ArrayList<BankAccount>();
		String sql="SELECT * FROM public.bankaccounts";	
		try (
				Connection connection = ConnectionFactory.getConnection();
				Statement statement = connection.createStatement();
				) {
			ResultSet resSet = statement.executeQuery(sql);
			CheckingAccount ca = null;
			JointAccount ja = null;
			while(resSet.next()) {
				if(resSet.getString("accounttype").equals("Checking")) {
					ca = new CheckingAccount(resSet.getInt("id"),resSet.getDouble("balance"),resSet.getString("accounttype"),resSet.getInt("accountstatus"));
					accounts.add(ca);
					ca=null;
				}else {
					ja = new JointAccount(resSet.getInt("id"),resSet.getDouble("balance"),resSet.getString("accounttype"),resSet.getInt("accountstatus"));
					accounts.add(ja);
					ja=null;
				}
			}
			resSet.close();
			return accounts;

		} catch (SQLException ex) {
			System.out.println("DB did not work couldn't load bank accounts!");
			System.out.println(ex.getMessage());
			return accounts;
		}
	}

	public boolean insertAccount(Users currentUser, int row,BankAccount ba) {
		try (
				Connection connection = ConnectionFactory.getConnection();
				Statement statement = connection.createStatement();
				) { 
			String sql = "INSERT INTO public.bankaccounts (id, accounttype, balance, accountstatus) VALUES ("+row+", '"+ba.getType()+"', "+ba.getBalance()+", "+ba.getAccountstatus()+")";
			//System.out.println(sql);
			statement.executeUpdate(sql);
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

	public int returnRowCount() {	
		try (
				Connection connection = ConnectionFactory.getConnection();
				Statement statement = connection.createStatement();
				) {
			ResultSet resSet = statement.executeQuery("SELECT id FROM bankaccounts WHERE id=(SELECT max(id) FROM bankaccounts)");
			resSet.next();
			int count = resSet.getInt("id");
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

		ArrayList<BankAccount> accounts = new ArrayList<BankAccount>();
		try (
				Connection connection = ConnectionFactory.getConnection();
				Statement statement = connection.createStatement();
				) { 
			String sql = "WITH joinusersbank as(SELECT bankaccountid FROM joinusersbank WHERE userid="+currentUser.getId()+") SELECT * FROM joinusersbank INNER JOIN bankaccounts ON joinusersbank.bankaccountid = bankaccounts.id";
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

	public boolean updateAccount(BankAccount ba) {
		try (
				Connection connection = ConnectionFactory.getConnection();
				Statement statement = connection.createStatement();
			) { 
				String sql = "UPDATE public.bankaccounts SET id="+ba.getId()+", accounttype='"+ba.getType()+"', balance="+ba.getBalance()+", accountstatus="+ba.getAccountstatus()+" WHERE id="+ba.getId();
				statement.executeUpdate(sql);
				connection.close();
				//System.out.println("Transaction Saved!");
				System.out.println("Finishing up the process...");
				return true;
				
				} catch (SQLException ex) {
					System.out.println("DB did not work in saving transaction!");
					System.out.println(ex.getMessage());
					return false;
			}
	}

	public ArrayList<BankAccount> getPendingBankAccounts() {

		ArrayList<BankAccount> accounts = new ArrayList<BankAccount>();
		try (
				Connection connection = ConnectionFactory.getConnection();
				Statement statement = connection.createStatement();
			) { 
		String sql = "SELECT * FROM public.bankaccounts WHERE accountstatus=2";
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
		return accounts;
	} catch (SQLException ex) {
		System.out.println("DB did not work couldn't retreve pending bank accounts!");
		System.out.println(ex.getMessage());
	}
		return accounts;
	}

	public ArrayList<BankAccount> getActiveBankAccounts() {
		ArrayList<BankAccount> accounts = new ArrayList<BankAccount>();
		try (
				Connection connection = ConnectionFactory.getConnection();
				Statement statement = connection.createStatement();
			) { 
		String sql = "SELECT * FROM public.bankaccounts WHERE accountstatus=1";
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
		return accounts;
	} catch (SQLException ex) {
		System.out.println("DB did not work initializing bank accounts!");
		System.out.println(ex.getMessage());
	}
		return accounts;
	}

	public ArrayList<BankAccount> getCanceledDeniedBankAccounts() {
		ArrayList<BankAccount> accounts = new ArrayList<BankAccount>();
		try (
				Connection connection = ConnectionFactory.getConnection();
				Statement statement = connection.createStatement();
			) { 
		String sql = "SELECT * FROM public.bankaccounts WHERE accountstatus>2";
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
		return accounts;
	} catch (SQLException ex) {
		System.out.println("DB did not work initializing bank accounts!");
		System.out.println(ex.getMessage());
	}
		return accounts;
	}

	public boolean TransferBetweenAccounts(ArrayList<BankAccount> accounts, double amount) {
		double senderBal = accounts.get(0).getBalance()-amount;
		double recieverBal = accounts.get(1).getBalance()+amount;
		if(this.validateBankAccount(accounts.get(1).getId())) {
			try (
					Connection connection = ConnectionFactory.getConnection();
					Statement statement = connection.createStatement();
			) { 
			String sql = "UPDATE public.bankaccounts SET id="+accounts.get(0).getId()+", accounttype='"+accounts.get(0).getType()+"', balance="+senderBal+", accountstatus="+accounts.get(0).getAccountstatus()+" WHERE id="+accounts.get(0).getId()+";"+"UPDATE public.bankaccounts SET id="+accounts.get(1).getId()+", accounttype='"+accounts.get(1).getType()+"', balance="+recieverBal+", accountstatus="+accounts.get(1).getAccountstatus()+" WHERE id="+accounts.get(1).getId()+";";
			//System.out.println(sql);
			statement.executeUpdate(sql);
			connection.close();
			return true;
			} catch (SQLException ex) {
				System.out.println("DB did not work in saving transaction!");
				System.out.println(ex.getMessage());
				return false;
			}
			
		}else {
			System.out.println("Account not activated yet cannot transfer funds to it!");
			return false;
		}
	}

	public boolean validateBankAccount(int id) {
		if(this.getAccount(id).getAccountstatus() <2) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public void printPendingBankAccounts() {
		try (
				Connection connection = ConnectionFactory.getConnection();
				Statement statement = connection.createStatement();
				) { 
			String sql = "SELECT * FROM public.bankaccounts WHERE accountstatus=2";
			ResultSet resSet = statement.executeQuery(sql);
			System.out.println("ID\tAccountType\t\tBalance\t\tAccountStatus");
			while(resSet.next()) {
				if(resSet.getString("accounttype").equals("Joint")) {
					System.out.println(resSet.getInt("id")+"\t"+resSet.getString("accounttype")+"\t\t\t"+resSet.getDouble("balance")+"\t\t\t"+resSet.getInt("accountstatus"));
				}else {
					System.out.println(resSet.getInt("id")+"\t"+resSet.getString("accounttype")+"\t\t"+resSet.getDouble("balance")+"\t\t\t"+resSet.getInt("accountstatus"));
				}
			}
			connection.close();
		} catch (SQLException ex) {
			System.out.println("DB did not work initializing bank accounts!");
			System.out.println(ex.getMessage());
		}
		
	}

	@Override
	public void printAllBankAccounts() {
		try (
				Connection connection = ConnectionFactory.getConnection();
				Statement statement = connection.createStatement();
				) { 
			String sql = "SELECT * FROM public.bankaccounts";
			ResultSet resSet = statement.executeQuery(sql);
			System.out.println("ID\tAccountType\t\tBalance\tAccountStatus");
			while(resSet.next()) {
				if(resSet.getString("accounttype").equals("Joint")) {
					System.out.println(resSet.getInt("id")+"\t"+resSet.getString("accounttype")+"\t\t\t$"+String.format("%.2f",resSet.getDouble("balance"))+"\t\t\t"+resSet.getInt("accountstatus"));
				}else {
					System.out.println(resSet.getInt("id")+"\t"+resSet.getString("accounttype")+"\t\t$"+String.format("%.2f",resSet.getDouble("balance"))+"\t\t\t"+resSet.getInt("accountstatus"));
				}
			}
			connection.close();
		} catch (SQLException ex) {
			System.out.println("DB did not work initializing bank accounts!");
			System.out.println(ex.getMessage());
		}
	}

	@Override
	public void printAllBankAccountsWithNamesView() {
		try (
				Connection connection = ConnectionFactory.getConnection();
				Statement statement = connection.createStatement();
				) { 
			String sql="SELECT * FROM joinusersbank LEFT JOIN users ON (users.id = joinusersbank.userid) RIGHT JOIN bankaccounts ON (bankaccounts.id = joinusersbank.bankaccountid)";
			ResultSet resSet = statement.executeQuery(sql);
			System.out.println("FirstName\tLastName\tID\tAccountType\t\tBalance\t\t\tAccountStatus");
			while(resSet.next()) {
				if(resSet.getString("accounttype").equals("Joint")) {
					System.out.println(resSet.getString("FirstName")+"\t\t"+resSet.getString("lastname")+"\t\t"+resSet.getInt("bankaccountid")+"\t"+resSet.getString("accounttype")+"\t\t\t$"+String.format("%.2f",resSet.getDouble("balance"))+"\t\t\t"+resSet.getInt("accountstatus"));
				}else {
					System.out.println(resSet.getString("FirstName")+"\t\t"+resSet.getString("lastname")+"\t\t"+resSet.getInt("bankaccountid")+"\t"+resSet.getString("accounttype")+"\t\t$"+String.format("%.2f",resSet.getDouble("balance"))+"\t\t\t"+resSet.getInt("accountstatus"));
				}
			}
			connection.close();
		} catch (SQLException ex) {
			System.out.println("DB did not work initializing bank accounts!");
			System.out.println(ex.getMessage());
		}
		
	}

}


