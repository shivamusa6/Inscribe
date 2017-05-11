package com.inscript.Call;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class Database {
	//private static Connection con = null;
	private static DataSource ds = null;

	private static void Connect(){
		try{
			System.out.print("\ngettinng required data for establishing connection... ");
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:comp/env");
			ds = (DataSource) envContext.lookup("jdbc/development");
			//Class.forName("com.mysql.jdbc.Driver").newInstance();
			System.out.println("DONE");
		}catch(Exception e){
			ds = null;
			System.out.println("FAILED");
			e.printStackTrace();
		}
	}


	static
	{
		Connect();
	}

	public static Connection getConnection() throws Exception{
		if(ds!=null){
			System.out.println("\nestablishing connection... ");
			return ds.getConnection();
		}else
			throw new Exception("Can't establish connection with NULL DataSource.");
	}

	public static void closeConnection(Connection con)
	{
		try {
			if(con!=null){
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
