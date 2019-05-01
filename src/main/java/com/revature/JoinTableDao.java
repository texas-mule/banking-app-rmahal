package com.revature;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class JoinTableDao implements JoinDao{

	@Override
	public boolean insertJoin(Users currentUser, int row) {
		try (
			Connection connection = ConnectionFactory.getConnection();
			Statement statement = connection.createStatement();
		) { 
			int id = currentUser.getId();
			//System.out.println("UserID: "+id+" RowID: "+row);
			String sql = "INSERT INTO public.joinusersbank (userid, bankaccountid) VALUES ("+id+","+row+")";
			@SuppressWarnings("unused")
			int resSet = statement.executeUpdate(sql);
			//System.out.println(resSet);
			connection.close();
			return true;
			} catch (SQLException ex) {
				System.out.println("DB did not work in saving new bank account and user into join table!");
				System.out.println(ex.getMessage());
				return false;
			}
		}
	public boolean addUserToJoint(int userid,int rowid) {
		try (
				Connection connection = ConnectionFactory.getConnection();
				Statement statement = connection.createStatement();
			) { 
				String sql = "INSERT INTO public.joinusersbank(userid, bankaccountid) VALUES ("+userid+", "+rowid+");";
				statement.executeUpdate(sql);
				System.out.println("Account has been added to this join account!");
				connection.close();
				return true;
				} catch (SQLException ex) {
					System.out.println("DB did not work in adding user to join account!");
					System.out.println(ex.getMessage());
					return false;
			}
	} 
}
