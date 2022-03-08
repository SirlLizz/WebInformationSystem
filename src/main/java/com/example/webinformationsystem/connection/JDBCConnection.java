package com.example.webinformationsystem.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCConnection {
	
	private static String url = "jdbc:oracle:thin:@//localhost:1521/WebSystem";
	private static String username = "SYS as SYSDBA";
	private static String password = "123";
	
	public static Connection get() {
		try {
			return DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
