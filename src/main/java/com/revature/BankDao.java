package com.revature;

import java.util.ArrayList;
import java.util.Set;

public interface BankDao {

	BankAccount getAccount(String username, String password);
    ArrayList<BankAccount> getAllAccounts();
    ArrayList<BankAccount> getUserBankAccounts(Users currentUser);
    boolean insertAccount(Users currentUser, int row,BankAccount ba);
    boolean updateAccountr();
    boolean deleteAccount();
	int returnRowCount();
	
}
