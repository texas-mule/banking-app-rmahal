package com.revature;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

public class UserTableDao implements UserDao {

	@Override
	public Users getUser(String username, String password) {

		try (
			Connection connection = ConnectionFactory.getConnection();
			Statement statement = connection.createStatement();
		) { 
			String sql ="SELECT * FROM public.users WHERE username='"+username+"' AND password='"+password+"'";

			ResultSet resSet = statement.executeQuery(sql);
			resSet.next();
			int id = resSet.getInt("id");
			String fname = resSet.getString("firstname");
			String lname = resSet.getString("lastname");
			String uname = resSet.getString("username");
			String passw = resSet.getString("password");
			int authtype = resSet.getInt("authtype");
			
			if(authtype == 3) {
				Admin currentUser = new Admin(id,fname,lname,uname,passw,authtype);
				connection.close();
				return currentUser;
			}else if (authtype ==2){
				Employees currentUser = new Employees(id,fname,lname,uname,passw,authtype);
				connection.close();
				return currentUser;
			}else {
				Users currentUser = new Users(id,fname,lname,uname,passw,authtype);
				connection.close();
				return currentUser;
			}
			} catch (SQLException ex) {
				System.out.println("DB did not work!");
				System.out.println(ex.getMessage());
			}
		return null;
	}

	@Override
	public Set<Users> getAllUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getIfUserExists(String username, String password) {
		try (
			Connection connection = ConnectionFactory.getConnection();
			Statement statement = connection.createStatement();
		) { 
			//System.out.println("Connection: ");
			//System.out.println(connection);
			String sql ="SELECT * FROM public.users WHERE username='"+username+"' AND password='"+password+"'";
	
			ResultSet resSet = statement.executeQuery(sql);
			boolean returned = resSet.next();
			if(returned) {
				connection.close();
				return true;
			}else {
				connection.close();
				return false;
			}
			
			} catch (SQLException ex) {
				System.out.println("Could not find user!");
				System.out.println(ex.getMessage());
				return false;
			}
	}

	@Override
	public boolean insertUser() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateUser() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteUser() {
		// TODO Auto-generated method stub
		return false;
	}


	public boolean authUser(String username, String password) {
		try (
				Connection connection = ConnectionFactory.getConnection();
				Statement statement = connection.createStatement();
			) { 
				String sql ="SELECT * FROM public.users WHERE username='"+username+"' AND password='"+password+"'";
		
				ResultSet resSet = statement.executeQuery(sql);
				boolean returned = resSet.next();
				if(returned) {
					connection.close();
					return true;
				}else {
					connection.close();
					return false;
				}
				
				} catch (SQLException ex) {
					System.out.println("DB did not work!");
					System.out.println(ex.getMessage());
					return false;
				}
	}
	
	
}
