package com.bear.servlet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.bear.util.ClosedUtil;
import com.mysql.jdbc.Statement;

public class MainTest {

	public static void main(String[] args) throws SQLException {
		
		System.out.println("PrimaryKey: " + singleCheckOut("INSERT INTO Name_Test(NAME) VALUES (?),(?),(?),(?),(?),(?)", "Jack", "Jack", "Jack", "Jack", "Jack", "Jack"));
		
	}
	
	public static int singleCheckOut(String sql, Object ...objs) throws SQLException{
		int primaryKey = 0;
		
		String databaseUrl="jdbc:mysql://%s:%s/%s?characterEncoding=UTF-8&user=%s&password=%s";
		String url = String.format(databaseUrl, "db.bwzqgame.com", "3306", "condor_houtai", "root", "shediao");
		Connection ct = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ct = DriverManager.getConnection(url);
			ps = ct.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			if(objs != null && objs.length > 0){
				for(int i = 0;i < objs.length;i++){
					ps.setObject(i+1, objs[i]);
				}
			}
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			while(rs.next()){
				primaryKey = rs.getInt(1);
			}
		} catch(SQLException e){
			throw new RuntimeException(e);
		} finally{
			ClosedUtil.closedResultSet(rs);
			ClosedUtil.closedPreparedStatement(ps);
			ClosedUtil.closedConnection(ct);
		}
		return primaryKey;
	}
	
}
