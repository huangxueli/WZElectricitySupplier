package com.example.wzelectricitysupplier.bean;

import com.example.wzelectricitysupplier.database.Field;

public class CircuitRecord extends NormalRecord{
	
	public static Field ZD_NAME = new Field("Ãû³Æ", "TEXT");
	public String mName = "";
	
	public static Field ZD_REMARK = new Field("±¸×¢", "TEXT");
	public String mRemark = "";
	
	public CircuitRecord(){
		mFieldList.add(ZD_NAME);
		mFieldList.add(ZD_REMARK);
	}
	
	public CircuitRecord(String Name, String Remark){
		this();
		mName = Name;
		mRemark = Remark;
		mValueList.add(Name);
		mValueList.add(Remark);
	}

}
