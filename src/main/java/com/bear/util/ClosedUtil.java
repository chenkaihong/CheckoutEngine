package com.bear.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ClosedUtil {
	public static void closedConnection(Connection connection){
		try{
			if(connection != null){
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			connection = null;
		}
	}
	public static void closedPreparedStatement(PreparedStatement ps){
		try{
			if(ps != null){
				ps.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			ps = null;
		}
	}
	public static void closedResultSet(ResultSet rs){
		try{
			if(rs != null){
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			rs = null;
		}
	}
	public static void closedInputStream(InputStream in){
		try{
			if(in != null){
				in.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			in = null;
		}
	}
	public static void closedOutputStream(OutputStream out){
		try{
			if(out != null){
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			out = null;
		}
	}
	public static void closedReader(Reader reader){
		try{
			if(reader != null){
				reader.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			reader = null;
		}
	}
	public static void closedWriter(Writer writer){
		try{
			if(writer != null){
				writer.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			writer = null;
		}
	}
}
