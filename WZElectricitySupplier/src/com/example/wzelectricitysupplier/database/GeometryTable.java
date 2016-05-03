package com.example.wzelectricitysupplier.database;

import java.util.ArrayList;

import jsqlite.Database;
import jsqlite.Exception;
import jsqlite.Stmt;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.MultiPath;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.example.wzelectricitysupplier.bean.DeviceRecord;
import com.example.wzelectricitysupplier.bean.NormalRecord;

public class GeometryTable extends NormalTable{

	protected String TAG = "GeometryTable";
	
	// 几何属性类型
	protected String mGeometryType ;

	public GeometryTable(Database mDatabase, String mTableName, String mGeometryType, ArrayList<Field> mFieldList){
		super(mDatabase, mTableName, mFieldList);
		this.mGeometryType = mGeometryType;
	}
	
	public String getGeometryType(){
		if(mGeometryType==null){
			return "";
		}
		return mGeometryType;
	}
	/**
	 *  创建表格
	 * @param primery
	 * @return
	 * @throws Exception
	 */
	protected synchronized boolean CreateTable(Boolean primery) {
		boolean result = true;
		result = super.CreateTable(primery);
		if (result) {
			result = AddGeometryColumn(mTableName, DBSetting.ZD_GEOMETRY, DBSetting.SRID, mGeometryType, "XY");
		}
		return result;
	}
	
	/**
	 * 插入一条数据 -1表示插入不成功
	 * @param record 记录
	 * @param geometry 
	 * @return
	 */
	public int add(NormalRecord record, Geometry geometry) {
		int mLastRecordID = -1;
		String geoStr = "";
		if (geometry != null) {
			geoStr = GeometryFromText(geometry);
		}
		String mSQL = "";
		String mFiledStr = record.getFieldStr();
		String mValueStr = record.getValueStr();
		if (geoStr.equals("")) {
			mSQL = "INSERT INTO  " + mTableName + "(" + mFiledStr + ") VALUES (" + mValueStr + ")";
		} else {
			mSQL = "INSERT INTO  " + mTableName + "(" + mFiledStr + "," + DBSetting.ZD_GEOMETRY + ") VALUES (" + mValueStr + "," + geoStr + ")";
		}
		if (execute(mSQL)) {
			mSQL = "SELECT " + DBSetting.ZD_PRIMERY + " FROM " + mTableName + " ORDER BY " + DBSetting.ZD_PRIMERY + " DESC";
			try {
				Stmt stmt = prepare(mSQL);
				if (stmt.step()) {
					mLastRecordID = stmt.column_int(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return mLastRecordID;
	}
	/**
	 * 更新几何属性
	 * @param point
	 * @param record
	 * @param id
	 * @return
	 */
	public boolean updateGeometry(Geometry geometry, int id){
		String geo = GeometryTable.GeometryFromText(geometry);
		String mSQL = "UPDATE " + mTableName + " SET " + 
				DBSetting.ZD_GEOMETRY + "=" + geo + " WHERE " + DBSetting.ZD_PRIMERY + "=" + id;
		return execute(mSQL);
	}
	
	public synchronized ArrayList<DeviceRecord> getRecords(String condition){
		return null;
	}
	
	// 给表中添加Geometry字段
	protected boolean AddGeometryColumn(String table_name, String column_name, int SRID, String geometry_type, String dimension){
		boolean result = true;
		String mSQL = "SELECT AddGeometryColumn('" + table_name + "', '"+ column_name + "', " + SRID + ", '" + geometry_type + "', '" + dimension + "');";
		result = execute(mSQL);
		
		// 创建空间索引
		mSQL = "SELECT CreateSpatialIndex('" + table_name+"', '" + column_name+"');";
		result = execute(mSQL);
		
		int type = 1;
		if(geometry_type.equals("LINESTRING")){
			type = 2;
		}
		String values = "'" + table_name +"','" + column_name + "'," + type + "," + "2," + SRID + "," + 0+")";
		mSQL = "INSERT INTO geometry_columns('f_table_name','f_geometry_column','geometry_type','coord_dimension','srid','spatial_index_enabled') "
				+ "values(" + values;
		result = execute(mSQL);
		return result;
	}
	
	protected static String GeometryFromText(Point geometry) {
		String geo = "";
		if (geometry != null) {
			geo = "GeomFromText('POINT(" + geometry.getX() + " " + geometry.getY() + ")'," + DBSetting.SRID + ")";
		}
		return geo;
	}
	
	protected static String GeometryFromText(Polyline line){
		String geo = "";
		if (line != null) {
			geo = "GeomFromText('LINESTRING(";
			for (int i = 0; i < line.getPointCount(); i++) {
				Point geometry = line.getPoint(i);
				if (i > 0) {
					geo += ",";
				}
				geo += (geometry.getX() + " " + geometry.getY());
			}
			geo += ")'," + DBSetting.SRID + ")";
		}
		return geo;
	}
	
	protected static String GeometryFromText(Geometry geometry) {
		String geo = "";
		if (geometry != null) {
			if(geometry.getType().value() == Geometry.Type.POINT.value()){
				geo = GeometryFromText((Point)geometry);
			} else if(geometry.getType().value() == Geometry.Type.POLYLINE.value()){
				geo = GeometryFromText((Polyline)geometry);
			} 
		}
		return geo;
	}
	
	public static Geometry getGeometryFromJSON(String json) {
		JSONObject jsonObject ;
		try{
			jsonObject = new JSONObject(json);
			String type = jsonObject.getString("type"); // 类型
			if (type.equals("Point")) {
				JSONArray jsonResults = jsonObject.getJSONArray("coordinates");
				if (jsonResults.length() == 2) {
					double x = jsonResults.getDouble(0);
					double y = jsonResults.getDouble(1);
					Point point = new Point(x, y);
					return point;
				}
			} else if (type.equals("LineString")) {
				MultiPath path = new Polyline();
				JSONArray jsonArray = jsonObject.getJSONArray("coordinates");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONArray jsonPoint = jsonArray.getJSONArray(i);
					double x = jsonPoint.getDouble(0);
					double y = jsonPoint.getDouble(1);
					if (i == 0) {
						path.startPath(x, y);	//创建起始点
					} else {
						path.lineTo(x, y);
					}
				}
				return path;
			}
		}catch (JSONException e){
			e.printStackTrace();
		}
		return null;
	}
	
	
}
