package com.revature;


import java.util.ArrayList;


public interface UserDao {
    Users getUser(String username, String password);
    Users getOneUser(int id);
    ArrayList<Users> getAllUsers();
    void printAllUsers();
    void printAllUsersFullView();
    void printOneUser(int id);
    boolean getIfUserExists(String username, String password);
    boolean insertUser(int row, Users user);
    boolean updateUser(Users user);
    boolean deleteUser(Users user);
    boolean authUser(String username, String password);
    int returnUserRowCount();
}
