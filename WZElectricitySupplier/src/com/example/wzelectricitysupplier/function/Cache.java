package com.example.wzelectricitysupplier.function;

import android.content.SharedPreferences;

public class Cache {
	
	// »º´æÎÄ¼þÃû
	public static final String SharedPreferencesFile = "Cache";
	
	public static final String KEY_CODE_USERNAME = "username";
	public static final String KEY_CODE_PASSWORD = "password";
	public static final String KEY_CODE_LOCALMAP = "offline_tiledlayer";
	public static final String KEY_CODE_ICONEXPLAIN = "icon_explain";
	public static final String KEY_CODE_CIRCUITID = "circuit_id";
	
	private SharedPreferences sp;

	public Cache(SharedPreferences sp) {
		this.sp = sp;
	}

	public boolean write(String count, String password) {
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(KEY_CODE_USERNAME, count);
		editor.putString(KEY_CODE_PASSWORD, password);
		
		return editor.commit();
	}
	
	public boolean write(String key, Object value) {
		SharedPreferences.Editor editor = sp.edit();
		switch (key) {
		case KEY_CODE_LOCALMAP:
			editor.putBoolean(key, (boolean)value);
			break;
		case KEY_CODE_ICONEXPLAIN:
			editor.putBoolean(key, (boolean)value);
			break;
		case KEY_CODE_CIRCUITID:
			editor.putInt(key, (int)value);
			break;
		default:
			break;
		}
		return editor.commit();
	}
	
	public Object read(String key) {
		switch(key){
		case KEY_CODE_LOCALMAP:
			return sp.getBoolean(key, false);
		case KEY_CODE_ICONEXPLAIN:
			return sp.getBoolean(key, false);
		case KEY_CODE_CIRCUITID:
			return sp.getInt(key, -1);
		default:
			return sp.getString(key, "");
		}
	}

	public String[] readAll() {
		String[] array = new String[2];
		array[0] = sp.getString(KEY_CODE_USERNAME, "");
		array[1] = sp.getString(KEY_CODE_PASSWORD, "");
		return array;
	}
}
