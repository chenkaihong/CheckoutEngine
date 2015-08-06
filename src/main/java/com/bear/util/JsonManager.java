package com.bear.util;

import com.google.gson.Gson;

public class JsonManager {
	
	private static Gson gson = new Gson();

	public static Gson getGson(){
		return gson;
	}
	
}
