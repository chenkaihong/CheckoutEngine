package com.bear.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.logicalcobwebs.proxool.ProxoolException;
import org.logicalcobwebs.proxool.configuration.PropertyConfigurator;

import com.bear.connection.ServerNode.Server;

public class ConnectionPool {
	
	private static ConnectionPool pool = new ConnectionPool();
	private final String databaseUrl="jdbc:mysql://%s:%s/%s?characterEncoding=UTF-8&user=%s&password=%s";
	
	private String house_keeping_test_sql="select CURRENT_DATE";
	private String verbose="true";
	private String statistics="10s,1m,1d";
	private String statistics_log_level="ERROR";
	private String pre="s";
	private String cname="proxool."+pre;
//	private String platform="proxool.platform";
	private String maximum_active_time="1800000";
	private Set<Integer> set = new HashSet<Integer>();
	private Set<Integer> failConnectionList = new HashSet<Integer>();
	 
	public static ConnectionPool getInstance(){
		if(pool == null){
			synchronized (ConnectionPool.class) {
				if(pool == null){
					pool = new ConnectionPool();
				}
			}
		}
		return pool;
	}
	
	private ConnectionPool(){}
	
	private void addConnection(int sid,Server server) {
		if(!set.contains(sid)){
			String url = String.format(databaseUrl, server.getHost(), server.getPort(), server.getDb(), server.getUser(), server.getPassword());
			Properties properties=new Properties();
			properties.put("jdbc-0.proxool.alias", pre+sid);
			properties.put("jdbc-0.proxool.driver-url", url);
			properties.put("jdbc-0.proxool.driver-class","com.mysql.jdbc.Driver");
			properties.put("jdbc-0.proxool.maximum-connection-count","5");
			properties.put("jdbc-0.proxool.prototype-count","1");
			
			properties.put("jdbc-0.proxool.house-keeping-test-sql", house_keeping_test_sql);
			properties.put("jdbc-0.proxool.verbose", verbose);
			properties.put("jdbc-0.proxool.statistics", statistics);
			properties.put("jdbc-0.proxool.statistics-log-level", statistics_log_level);
			properties.put("jdbc-0.proxool.maximum-active-time", maximum_active_time);
			
			try {PropertyConfigurator.configure(properties);} catch (ProxoolException pe) {pe.printStackTrace();}
			set.add(sid);
			System.out.println("成功添加游戏服["+sid+"]数据库连接池url["+url+"]...............");
		}
	}
	

	/**
	 * 获取数据库连接
	 * @param sid 服务器id
	 * @return
	 */
	public synchronized Connection getConnection(Integer sid,Server server) {
		Connection conn = null;
		try {
			if(failConnectionList.contains(sid)){
				return null;
			}
			if(set.contains(sid)){
				conn = DriverManager.getConnection(cname+sid);
			}else{
				addConnection(sid, server);
				conn = DriverManager.getConnection(cname+sid);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			failConnectionList.add(sid);
			return null;
		}
		return conn;
	}
}
