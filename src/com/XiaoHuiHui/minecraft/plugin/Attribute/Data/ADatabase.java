package com.XiaoHuiHui.minecraft.plugin.Attribute.Data;

import java.sql.Connection;
import java.sql.DriverManager;
//import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.List;

public class ADatabase {
	private ADatabase(){
		throw new IllegalArgumentException("Goodbye!you fucking shit!");
	}
	
	private static int port;
	private static String host;
	private static String username;
	private static String password;
	
	private static AData data;
	private static boolean isInit=false;
	
	private static Connection conn;
	private static Statement stmt;
	
	public static int getPort() {
		return port;
	}

	private static void setPort(int port) {
		ADatabase.port = port;
	}

	public static String getHost() {
		return host;
	}

	private static void setHost(String host) {
		ADatabase.host = host;
	}

	public static String getUsername() {
		return username;
	}

	private static void setUsername(String username) {
		ADatabase.username = username;
	}

	public static String getPassword() {
		return password;
	}

	private static void setPassword(String password) {
		ADatabase.password = password;
	}

	public static AData getData() {
		return data;
	}

	private static void setData(AData data) {
		ADatabase.data = data;
	}

	public static Connection getConn() {
		return conn;
	}

	public static void setConn(Connection conn) {
		ADatabase.conn = conn;
	}

	public static Statement getStmt() {
		return stmt;
	}

	public static void setStmt(Statement stmt) {
		ADatabase.stmt = stmt;
	}

	public static void init(){
		setData(AData.getInstance());
		try{
			Class.forName("com.mysql.jdbc.Driver");
			getData().outputInfo("成功加载数据库驱动！");
		}catch(ClassNotFoundException e1){
			getData().outputError("数据库驱动加载失败！");
		}
		isInit=true;
	}
	
	public static void load(){
		if(!isInit){
			throw new AssertionError();
		}
		setPort(getData().getPort());
		setHost(getData().getHost());
		setUsername(getData().getUsername());
		setPassword(getData().getPassword());
		String url="jdbc:mysql://"+getHost()+":"+getPort()
					+"/attribute?useSSL=true&useUnicode=true&characterEncoding=UTF8";
		try {
			setConn(DriverManager.getConnection(url,getUsername(),getPassword()));
			setStmt(getConn().createStatement());
		} catch (SQLException e){
			getData().outputError("数据库操作异常！ 错误代码: "+e.getErrorCode()
											+" 错误信息: "+e.getMessage());
			return;
		}
		getData().outputInfo("数据库加载成功！");
	}
	
	public static boolean createTable(String name){
		if(!isInit){
			throw new AssertionError();
		}
		String str="CREATE TABLE IF NOT EXISTS \'"+name+"\' (";
		AAttr aa[]=AAttr.values();
		for(int i=0;i<aa.length;++i){
			str+=aa[i].name();
			str+=" INT(10) DEFAULT 0";
			if(i==aa.length-1){
				str+=")";
			}else{
				str+=",";
			}
		}
		try {
			getStmt().executeQuery(str);
		} catch (SQLException e) {
			getData().outputError("数据库操作异常！");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static boolean updateData(String name,AAttr attr,int value){
		if(!isInit){
			throw new AssertionError();
		}
		boolean flag=createTable(name);
		if(!flag)return false;
		String str="UPDATE \'"+name+"\' SET "+attr.name()+"="+value;
		try {
			getStmt().executeQuery(str);
		} catch (SQLException e) {
			getData().outputError("数据库操作异常！");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static void shutdown(){
		if(!isInit){
			return;
		}
		try {
			if(getStmt()!=null)getStmt().close();
			if(getConn()!=null)getConn().close();
		} catch (SQLException e) {
			getData().outputError("数据库关闭时发生异常！");
			e.printStackTrace();
		}
	}
	
//	public static int getData(String name){
//		if(!isInit){
//			throw new AssertionError();
//		}
//		String str="SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'attribute'";
//		List<String> list=new ArrayList<String>();
//		try {//TODO
//			ResultSet rs = getStmt().executeQuery(str);
//		} catch (SQLException e) {
//			getData().outputError("数据库操作异常！");
//			e.printStackTrace();
//			return 0;
//		}
//		return 0;
//	}
}
