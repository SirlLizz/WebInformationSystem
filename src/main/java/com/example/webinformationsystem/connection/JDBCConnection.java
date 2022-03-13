package com.example.webinformationsystem.connection;

public class JDBCConnection {

	private static JDBCConnection instance = new JDBCConnection();
	private JDBCUtils jdbcUtil;

	public static JDBCConnection getInstance() {
		return instance;
	}

	private JDBCUtils prepareJDBCUtils() {
		if (jdbcUtil == null) {
			jdbcUtil = new JDBCUtils();
			jdbcUtil.init("jdbc/ConnectionPool");
		}

		return jdbcUtil;
	}

	public static synchronized JDBCUtils getJDBCUtils() {
		return getInstance().prepareJDBCUtils();
	}
}
