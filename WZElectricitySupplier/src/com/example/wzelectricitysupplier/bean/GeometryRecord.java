package com.example.wzelectricitysupplier.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.MultiPath;
import com.esri.core.geometry.Point;
import com.example.wzelectricitysupplier.database.GeometryTable;

public class GeometryRecord extends NormalRecord{
	
	protected Geometry geometry;
	protected String mGeoJson;
	
	public Geometry getGeometry() {
		return geometry;
	}
	
	public String getGeoJson() {
		return mGeoJson;
	}
	
	/**
	 * 只有线类型的几何对象才能使用
	 * @return
	 */
	public Point getLineMidPoint(){
		Point point = null;
		if(geometry.getType().value() == Geometry.Type.POLYLINE.value()){
			try {
				JSONObject object = new JSONObject(mGeoJson);
				JSONArray array = object.getJSONArray("coordinates");
				JSONArray arr1 = array.getJSONArray(0);
				JSONArray arr2 = array.getJSONArray(array.length()-1);
				double x1 = arr1.getDouble(0);
				double y1 = arr1.getDouble(1);
				double x2 = arr2.getDouble(0);
				double y2 = arr2.getDouble(1);
				point = new Point((x1+x2)/2, (y1+y2)/2);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return point;
	}
	public Point getStartPoint(){
		Point point = null;
		if(geometry.getType().value() == Geometry.Type.POLYLINE.value()){
			try {
				JSONObject object = new JSONObject(mGeoJson);
				JSONArray array = object.getJSONArray("coordinates");
				JSONArray arr = array.getJSONArray(0);
				double x = arr.getDouble(0);
				double y = arr.getDouble(1);
				point = new Point(x, y);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return point;
	}
	public Point getEndPoint(){
		Point point = null;
		if(geometry.getType().value() == Geometry.Type.POLYLINE.value()){
			try {
				JSONObject object = new JSONObject(mGeoJson);
				JSONArray array = object.getJSONArray("coordinates");
				JSONArray arr = array.getJSONArray(array.length()-1);
				double x = arr.getDouble(0);
				double y = arr.getDouble(1);
				point = new Point(x, y);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return point;
	}
	public void setGeometry(String json) {
		try {
			mGeoJson = json;
			JSONObject object = new JSONObject(json);
			String type = object.getString("type");
			switch(type){
			case "Point":
				this.geometry = (Point)GeometryTable.getGeometryFromJSON(json);
				break;
			case "LineString":
				this.geometry = (MultiPath)GeometryTable.getGeometryFromJSON(json);
				break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	
}
