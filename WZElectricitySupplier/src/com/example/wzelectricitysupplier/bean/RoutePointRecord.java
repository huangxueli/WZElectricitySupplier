package com.example.wzelectricitysupplier.bean;

import com.example.wzelectricitysupplier.database.Field;

public class RoutePointRecord extends GeometryRecord{
	
	public static Field ZD_RECORD_TIME = new Field("¼ÇÂ¼Ê±¼ä", "TEXT");
	public String mRecordTime = "";
	
	public static Field ZD_OWNER_LINE = new Field("OwnerLine", "TEXT");
	public String mOwnerLine = "";
	
	public RoutePointRecord(){
		mFieldList.add(ZD_RECORD_TIME);
		mFieldList.add(ZD_OWNER_LINE);
	}
	
	public RoutePointRecord(String RecordTime, String OwnerLine){
		this();
		this.mRecordTime = RecordTime;
		this.mOwnerLine = OwnerLine;
		
		mValueList.add(mRecordTime);
		mValueList.add(mOwnerLine);
	}
	
}
