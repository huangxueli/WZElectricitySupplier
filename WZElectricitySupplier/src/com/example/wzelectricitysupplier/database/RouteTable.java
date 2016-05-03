package com.example.wzelectricitysupplier.database;

import java.util.ArrayList;

import jsqlite.Database;
import jsqlite.Exception;
import jsqlite.Stmt;

import com.esri.core.geometry.Polyline;
import com.example.wzelectricitysupplier.bean.RouteRecord;

public class RouteTable extends GeometryTable {
	
	public final String TAG = "RotationTable";
	
	private final static String mTableName = "rotation";
	private final static String mGeometryType = "LINESTRING";
	
	public RouteTable(Database mDatabase) {
		super(mDatabase, mTableName, mGeometryType, new RouteRecord().getFieldList());
		CreateTable();
	}
	
	/**
	 * 添加或更新一条巡检线
	 * @param line
	 * @param mStartTime
	 * @param mEndTime
	 * @param lineId
	 * @return
	 */
	public int addAndUpdateRouteRecord(Polyline line, RouteRecord record, int lineId) {
		int id = -1;
		try {
			if(isRecordExist(lineId)){ // 存在
				boolean result = update(line, record, lineId);
				if(result){
					id = lineId;
				}
			}else{ // 不存在
				id = add(record, line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}
	
	public synchronized ArrayList<RouteRecord> SelectRoutes(String condition) {
		ArrayList<RouteRecord> list = new ArrayList<>();
		try {
			String mSQL = "SELECT * FROM " + mTableName + " WHERE " + condition;
			Stmt stmt = prepare(mSQL);
			RouteRecord record = null;
			while (stmt.step()) {
				int size = new RouteRecord().getFieldList().size() + 1; // 主键
				int index = 0;
				record = new RouteRecord();
				if (index < size) {
					record.setId(stmt.column_int(index));
					index++;
				}
				ArrayList<String> valueList = record.getValueList();
				while (index < size) {
					valueList.add(stmt.column_string(index));
					index++;
				}
				if (index == size) {
					record.mStartTime = valueList.get(0);
					record.mEndTime = valueList.get(1);
					list.add(record);
				}
				;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 更新记录
	 * @return
	 */
	public boolean update(Polyline line, RouteRecord record, int id){
		if(line!=null){
			String geo = GeometryTable.GeometryFromText(line);
			String mSQL = "UPDATE " + mTableName + " SET '" + RouteRecord.ZD_START_TIME.NAME  + "'= '"+ record.mStartTime + "','" + 
					RouteRecord.ZD_END_TIME.NAME + "'= '" + record.mEndTime + "'," + 
					DBSetting.ZD_GEOMETRY + "=" + geo + " WHERE " + DBSetting.ZD_PRIMERY + "=" + id;
			return execute(mSQL);
		}
		return false;
	} 
	
}
