package com.example.wzelectricitysupplier.bean;

import java.util.ArrayList;

import com.example.wzelectricitysupplier.database.Field;

public class NormalRecord {
	
	protected int id;
	
	protected String mFieldStr = "";
	protected ArrayList<Field> mFieldList = new ArrayList<Field>();
	protected String mValueStr = "";
	protected ArrayList<String> mValueList = new ArrayList<String>();
	
	public ArrayList<Field> getFieldList() {
		return mFieldList;
	}
	
	public ArrayList<String> getValueList() {
		return mValueList;
	}

	public String getFieldStr(){
		String fieldStr = "";
		for(Field field:mFieldList){
			if("".equals(fieldStr))
				fieldStr += field.NAME;
			else
				fieldStr += "," + field.NAME;
		}
		return fieldStr;
	}
	
	public String getValueStr(){
		String valueStr = "";
		for(String value:mValueList){
			if("".equals(valueStr))
				valueStr += " '" + value + "'";
			else
				valueStr += ",'" + value + "'";
		}
		return valueStr;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
