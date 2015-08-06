package com.bear.util;

import com.bear.connection.ServerNode;



public abstract class DataModel<V> implements PackupSelfFactory<V>{
	public final String sql;
	public final String[] parm;
	
	public DataModel(){this.sql = null;this.parm = null;}
	public DataModel(String sql,String ...parm){
		this.sql = sql;
		this.parm = parm;
	}
	
	public String getSql() {
		return sql;
	}
	public String[] getParm() {
		return parm;
	}
	@Override
	public String toString() {
		return "@@@@ TestDataDemo.java ----> sql: " + sql + ", parmArray: " + parm + "\n";
	}
	@Override
	public PackupSelfFactory<V> getModel(ServerNode serverNode) {
		return null;
	}
}