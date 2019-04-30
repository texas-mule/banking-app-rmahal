package com.revature;

import java.util.ArrayList;

public interface BankDao {

	BankAccount getAccount(int id);
    ArrayList<BankAccount> getAllAccounts();
    ArrayList<BankAccount> getUserBankAccounts(Users currentUser);
    ArrayList<BankAccount> getActiveBankAccounts();
    ArrayList<BankAccount> getPendingBankAccounts();
    void printAllBankAccounts();
    void printPendingBankAccounts();
    ArrayList<BankAccount> getCanceledDeniedBankAccounts();
    boolean TransferBetweenAccounts(ArrayList<BankAccount> accounts, double amount);
    boolean insertAccount(Users currentUser, int row,BankAccount ba);
    boolean updateAccount(BankAccount ba);
    boolean validateBankAccount(int id);
	int returnRowCount();
	
}
