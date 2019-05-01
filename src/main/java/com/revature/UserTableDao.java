package com.revature;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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
	public ArrayList<Users> getAllUsers() {
		ArrayList<Users> users = new ArrayList<Users>();


		try (
				Connection connection = ConnectionFactory.getConnection();
				Statement statement = connection.createStatement();
				) { 
			//System.out.println("Connection: ");
			//System.out.println(connection);
			String sql ="SELECT * FROM public.users";
			Users user = null;
			ResultSet resSet = statement.executeQuery(sql);
			while(resSet.next()) {
				user = new Users(resSet.getInt("id"), resSet.getString("firstname"), resSet.getString("lastname"), resSet.getString("username"), resSet.getString("password"), resSet.getInt("authtype"));
				users.add(user);
			}


			if(users.size() > 0) {
				return users;
			}else {
				System.out.println("Error no users please restart app");
				return null;
			}


		} catch (SQLException ex) {
			System.out.println("Could not find user!");
			System.out.println(ex.getMessage());
			return null;
		}
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

	public boolean insertUser(int row, Users user) {		
		try (
				Connection connection = ConnectionFactory.getConnection();
				Statement statement = connection.createStatement();
				) { 
			String sql = "INSERT INTO public.users( id, firstname, lastname, username, password, authtype) VALUES ("+row+", '"+user.getFirstname()+"', '"+user.getLastname()+"', '"+user.getUsername()+"', '"+user.getPassword()+"', "+user.getAuthtype()+")";
			statement.executeUpdate(sql);
			connection.close();
			return true;
		} catch (SQLException ex) {
			System.out.println("DB did not work saving user account!");
			System.out.println(ex.getMessage());
			return false;
		}
	}

	@Override
	public boolean updateUser(Users user) {
		try (
				Connection connection = ConnectionFactory.getConnection();
				Statement statement = connection.createStatement();
				) { 
			String sql = "UPDATE public.users SET id="+user.getId()+", firstname='"+user.getFirstname()+"', lastname='"+user.getLastname()+"', username='"+user.getUsername()+"', password='"+user.getPassword()+"', authtype='"+user.getAuthtype()+"' WHERE id="+user.getId();
			int response = 0;
			response = statement.executeUpdate(sql);
			if(response != 0) {
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

	public int returnUserRowCount() {
		try (
				Connection connection = ConnectionFactory.getConnection();
				Statement statement = connection.createStatement();
				) { 
			ResultSet resSet = statement.executeQuery("SELECT id FROM users WHERE id=(SELECT max(id) FROM users)");
			int count = 0;
			if(resSet.next()) {
				count = resSet.getInt("id");
			}
			connection.close();
			if(count == 0) {
				return -1;
			}else {
				return count;
			}

		} catch (SQLException ex) {
			System.out.println("DB did not work!");
			System.out.println(ex.getMessage());
			return 0;
		}
	}

	public void printAllUsers() {
		try (
				Connection connection = ConnectionFactory.getConnection();
				Statement statement = connection.createStatement();
				) { 
			String sql = "SELECT * FROM public.users";
			ResultSet resSet = statement.executeQuery(sql);
			System.out.println("ID\tFirstName\tLastName\tUsername");
			while(resSet.next()) {
				if(resSet.getInt("authtype") != 3) {
					System.out.println(resSet.getInt("id")+"\t"+resSet.getString("firstname")+"\t\t"+resSet.getString("lastname")+"\t\t"+resSet.getString("username"));
				}
			}
			connection.close();
		} catch (SQLException ex) {
			System.out.println("DB did not work initializing bank accounts!");
			System.out.println(ex.getMessage());
		}
	}

	@Override
	public void printAllUsersFullView() {
		try (
				Connection connection = ConnectionFactory.getConnection();
				Statement statement = connection.createStatement();
				) { 
			String sql = "SELECT * FROM public.users";
			ResultSet resSet = statement.executeQuery(sql);
			System.out.println("ID\tFirstName\tLastName\t\tUsername\tPasswords\tAuthType");
			while(resSet.next()) {
					System.out.println(resSet.getInt("id")+"\t"+resSet.getString("firstname")+"\t\t\t"+resSet.getString("lastname")+"\t\t"+resSet.getString("username")+"\t\t"+resSet.getString("password")+"\t\t"+resSet.getString("authtype"));
			}
			connection.close();
		} catch (SQLException ex) {
			System.out.println("DB did not work initializing bank accounts!");
			System.out.println(ex.getMessage());
		}
	}

	@Override
	public boolean deleteUser(Users user) {
		// TODO Auto-generated method stub
		return false;
	}

	public void printOneUser(int id) {
		try (
				Connection connection = ConnectionFactory.getConnection();
				Statement statement = connection.createStatement();
				) { 
			String sql = "SELECT * FROM public.users WHERE id="+id;
			ResultSet resSet = statement.executeQuery(sql);
			System.out.println("ID\tFirstName\tLastName\t\tUsername\tPasswords\tAuthType");
			while(resSet.next()) {
					System.out.println(resSet.getInt("id")+"\t"+resSet.getString("firstname")+"\t\t\t"+resSet.getString("lastname")+"\t\t"+resSet.getString("username")+"\t\t"+resSet.getString("password")+"\t\t"+resSet.getString("authtype"));
			}
			connection.close();
		} catch (SQLException ex) {
			System.out.println("DB did not work initializing bank accounts!");
			System.out.println(ex.getMessage());
		}
	}

	public Users getOneUser(int id) {
		try (
				Connection connection = ConnectionFactory.getConnection();
				Statement statement = connection.createStatement();
				) { 
			String sql = "SELECT * FROM public.users WHERE id="+id;
			ResultSet resSet = statement.executeQuery(sql);
			resSet.next();
			Users user= new Users(resSet.getInt("id"), resSet.getString("firstname"), resSet.getString("lastname"), resSet.getString("username"), resSet.getString("password"), resSet.getInt("authtype"));
			connection.close();
			return user;
		} catch (SQLException ex) {
			System.out.println("DB did not work initializing bank accounts!");
			System.out.println(ex.getMessage());
			return null;
		}
	}
}
