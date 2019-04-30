package com.revature;


import java.util.ArrayList;


public interface UserDao {
    Users getUser(String username, String password);
    ArrayList<Users> getAllUsers();
    void printAllUsers();
    boolean getIfUserExists(String username, String password);
    boolean insertUser(int row, Users user);
    boolean updateUser(Users user);
    boolean deleteUser(Users user);
    boolean authUser(String username, String password);
    int returnUserRowCount();
}
