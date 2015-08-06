package com.bear.util;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

import com.bear.connection.ServerNode;

public class PropertiesUtil {
	private static final Properties properties = new Properties();
	static{
		try {
//			properties.load(new FileReader(new File(System.getProperties().getProperty("user.dir") + "/local.properties")));
			properties.load(new FileReader(new File(PropertiesUtil.class.getResource("/").getPath() + "local.properties")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String getProperty(String key){
		String result = properties.getProperty(key);
		if(result == null){
			throw new RuntimeException("properties no find the key! key : " + key);
		}
		return  result;
	}
	public static ServerNode initBackgroud(String local){
		String serverID = 	PropertiesUtil.getProperty(local + "_serverID");
		String area = 		local;
		String serverName = PropertiesUtil.getProperty(local + "_serverName");
		String host =		PropertiesUtil.getProperty(local + "_host");
		String userName = 	PropertiesUtil.getProperty(local + "_userName");
		String password = 	PropertiesUtil.getProperty(local + "_passWord");
		String db = 		PropertiesUtil.getProperty(local + "_db");
		ServerNode background = new ServerNode(serverName, Integer.parseInt(serverID), host, userName, password, db, area);
		return background;
	}
	
	 public static void main(String[] args) {
	        System.out.println(Thread.currentThread().getContextClassLoader().getResource(""));
	        System.out.println(PropertiesUtil.class.getClassLoader().getResource("")); //直接到类路径下
	        System.out.println(ClassLoader.getSystemResource(""));
	        System.out.println(PropertiesUtil.class.getResource(""));
	        System.out.println(PropertiesUtil.class.getResource("../../../")); // Class文件所在路径
	        System.out.println(new File("/").getAbsolutePath());
	        System.out.println(System.getProperty("user.dir"));
	 }
}
