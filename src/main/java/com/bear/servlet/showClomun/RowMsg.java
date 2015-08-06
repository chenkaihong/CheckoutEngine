package com.bear.servlet.showClomun;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bear.util.DataModel;

public class RowMsg extends DataModel<List<RowMsg>>{
	
	public final Object[] contents;
	
	public RowMsg(String sql, String ...parm){
		super(sql,parm);
		contents = null;
	}
	private RowMsg(Object[] contents){
		this.contents = contents;
	}
	
	@Override
	public List<RowMsg> packup(ResultSet rs) throws SQLException {
		List<RowMsg> msgList = new ArrayList<RowMsg>();
		ResultSetMetaData rsMeta = rs.getMetaData();
		int columnNum = rsMeta.getColumnCount();
		while(rs.next()){
			Object[] msgArray = new Object[columnNum];
			for(int i = 0;i < columnNum;i++){
				msgArray[i] = rs.getObject(i+1);
			}
			msgList.add(new RowMsg(msgArray));
		}
		return msgList;
	}
}
