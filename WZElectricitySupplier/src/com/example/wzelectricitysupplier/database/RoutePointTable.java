package com.example.wzelectricitysupplier.database;

import java.util.ArrayList;
import java.util.List;

import jsqlite.Database;
import jsqlite.Exception;
import jsqlite.Stmt;

import com.example.wzelectricitysupplier.bean.RoutePointRecord;

public class RoutePointTable extends GeometryTable {
	
	public final String TAG = "RotationPointTable";
	
	private final static String mTableName = "rotation_point";
	private final static String mGeometryType = "POINT";
	
	public RoutePointTable(Database mDatabase) {
		super(mDatabase, mTableName, mGeometryType, new RoutePointRecord().getFieldList());
		CreateTable();
	}
	
	public List<RoutePointRecord> getPointList(int lineId) {
		List<RoutePointRecord> mRecordList = new ArrayList<RoutePointRecord>();
		String mSQL = "SELECT " + DBSetting.ZD_PRIMERY + "," + RoutePointRecord.ZD_RECORD_TIME.NAME + 
				",AsGeoJSON("+ DBSetting.ZD_GEOMETRY + ") FROM " + mTableName + 
				" WHERE " + RoutePointRecord.ZD_OWNER_LINE.NAME + "=" + lineId;
		try {
			String id;
			String recordTime;
			String geoJson;

			Stmt stmt = prepare(mSQL);
			while (stmt.step()) {
				id = stmt.column_string(0);
				recordTime = stmt.column_string(1);
				geoJson = stmt.column_string(2);
				RoutePointRecord record = new RoutePointRecord(recordTime, id);
				record.setGeometry(geoJson);
				mRecordList.add(record); // 数据读取完毕
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mRecordList;
	}
	
}
