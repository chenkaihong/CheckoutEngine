package com.bear.util;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface PackupSelf<T>{
	T packup(ResultSet rs) throws SQLException;
	String getSql();
	String[] getParm();
}

