package com.revature;

import java.util.Set;

public interface UserDao {
    Users getUser(String username, String password);
    Set<Users> getAllUsers();
    boolean getIfUserExists(String username, String password);
    boolean insertUser();
    boolean updateUser();
    boolean deleteUser();
    boolean authUser(String username, String password);
}
