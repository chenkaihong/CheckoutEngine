package com.bear.util;

import javax.servlet.http.HttpServletRequest;

public class ServletUtil {
	public static String getURL(HttpServletRequest request, String path){
		if(!path.startsWith("/")){
			path = "/" + path;
		}
		return request.getContextPath() + path;
	}
}