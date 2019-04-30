package com.revature;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {


    public static final String URL = "jdbc:postgresql://127.0.0.1:8001/postgres";
    public static final String USER = "postgres";
    public static final String PASS = "test";
    

    public static Connection getConnection(){
		try {
			 	Connection connection = DriverManager.getConnection(URL,USER,PASS);
				return connection;
      } catch (SQLException ex) {
          throw new RuntimeException("Error connecting to the database", ex);
      }
    }
	
}
