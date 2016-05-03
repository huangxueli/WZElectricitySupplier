package com.example.wzelectricitysupplier.bean;

import com.example.wzelectricitysupplier.database.Field;

public class RouteRecord extends GeometryRecord{
	
	public static Field ZD_START_TIME = new Field("开始时间", "TEXT");
	public String mStartTime = "";
	
	public static Field ZD_END_TIME = new Field("结束时间", "TEXT");
	public String mEndTime = "";
	
	public RouteRecord(){
		mFieldList.add(ZD_START_TIME);
		mFieldList.add(ZD_END_TIME);
	}
	
	public RouteRecord(String StartTime, String EndTime){
		this();
		mStartTime = StartTime;
		mEndTime = EndTime;
		mValueList.add(mStartTime);
		mValueList.add(mEndTime);
	}

}
