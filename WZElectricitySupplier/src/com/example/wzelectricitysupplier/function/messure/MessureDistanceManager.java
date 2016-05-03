package com.example.wzelectricitysupplier.function.messure;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.MultiPath;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol.STYLE;
import com.esri.core.symbol.Symbol;
import com.example.wzelectricitysupplier.MyApplication;

public class MessureDistanceManager {

	private MapView mMap;
	private List<Point> mMessurePointList = new ArrayList<Point>();
	private boolean mMessrueAble = false;
	private GraphicsLayer mMessureDistanceLLayer; // 测量线图层
	private GraphicsLayer mMessureDistancePLayer; // 测量点图层

	public MessureDistanceManager(MapView mMap) {
		this.mMap = mMap;
		// 加入线测量层
		mMessureDistanceLLayer = new GraphicsLayer();
		mMap.addLayer(mMessureDistanceLLayer);
		// 加入点测量层
		mMessureDistancePLayer = new GraphicsLayer();
		mMap.addLayer(mMessureDistancePLayer);
	}

	public void StartMessure() {
		mMessrueAble = true;
		mMessurePointList.clear();
		mMessureDistancePLayer.removeAll();
		mMessureDistanceLLayer.removeAll();
	}

	public void StopMessure() {
		mMessrueAble = false;
		mMessurePointList.clear();
		mMessureDistancePLayer.removeAll();
		mMessureDistanceLLayer.removeAll();
	}
	
	public String CheckMessureLine(float x, float y) {
		String length = null;
		if (mMessrueAble) {
			Point geometry = mMap.toMapPoint(new Point(x, y));
			mMessurePointList.add(geometry);
			length = ShowMutLineAndMessure();
		}
		return length;
	}

	private String ShowMutLineAndMessure() {

		if (mMessureDistanceLLayer != null && mMessurePointList != null) {
			mMessureDistanceLLayer.removeAll();
			mMessureDistancePLayer.removeAll();
			int size = mMessurePointList.size();
			if (size > 1) {
				MultiPath path = new Polyline();
				for (int i = 0; i < size; i++) {
					if (i == 0) {
						path.startPath(mMessurePointList.get(i));
						ShowStartPointOnMessurePointLayer(mMessurePointList.get(i));
					} else {
						path.lineTo(mMessurePointList.get(i));
						if (i == size - 1) {
							ShowEndPointOnMessurePointLayer(mMessurePointList.get(i));
						} else {
							ShowMiddlePointOnMessurePointLayer(mMessurePointList.get(i));
						}
					}
				}
				double length = GeometryEngine.geodesicLength(path, mMap.getSpatialReference(), null);
//				mMainActivity.SetMessageAreaText("长度:" + CallLength(length));
				mMessureDistanceLLayer.addGraphic(new Graphic(path, 
						new SimpleLineSymbol(MessureDistanceSetting.mLineColor, MessureDistanceSetting.mLineWidth)));
				String mLengthStr = "长度:" + CallLength(length);
				return mLengthStr;
			} else if (size == 1) {
				ShowStartPointOnMessurePointLayer(mMessurePointList.get(0));
			}
		}
		return null;
	}

	private void ShowStartPointOnMessurePointLayer(Point geometry) {
		Symbol symbol = null;
		if(MessureDistanceSetting.mStartPointResID !=null){
			Drawable drawable = MyApplication.Resources.getDrawable(MessureDistanceSetting.mStartPointResID);
			symbol = new PictureMarkerSymbol(drawable);
		} else{
			symbol = new SimpleMarkerSymbol(Color.RED, 10, STYLE.CIRCLE);
		}
		Graphic graphic = new Graphic(geometry, symbol);
		mMessureDistancePLayer.addGraphic(graphic);
	}
	private void ShowMiddlePointOnMessurePointLayer(Point geometry) {
		Symbol symbol = null;
		if(MessureDistanceSetting.mMiddlePointResID !=null){
			Drawable drawable = MyApplication.Resources.getDrawable(MessureDistanceSetting.mMiddlePointResID);
			symbol = new PictureMarkerSymbol(drawable);
		} else{
			symbol = new SimpleMarkerSymbol(Color.RED, 10, STYLE.CIRCLE);
		}
		Graphic graphic = new Graphic(geometry, symbol);
		mMessureDistancePLayer.addGraphic(graphic);
	}
	private void ShowEndPointOnMessurePointLayer(Point geometry) {
		Symbol symbol = null;
		if(MessureDistanceSetting.mEndPointResID !=null){
			Drawable drawable = MyApplication.Resources.getDrawable(MessureDistanceSetting.mEndPointResID);
			symbol = new PictureMarkerSymbol(drawable);
		} else{
			symbol = new SimpleMarkerSymbol(Color.RED, 10, STYLE.CIRCLE);
		}
		Graphic graphic = new Graphic(geometry, symbol);
		mMessureDistancePLayer.addGraphic(graphic);
	}
	
	@SuppressLint("DefaultLocale")
	private String CallLength(double lengthInMeter) {
		String str = "";
		String unit = "米";

		if (lengthInMeter > 1000) {
			unit = "公里";
			lengthInMeter /= 1000;
		}

		double l = ((double) ((int) (lengthInMeter * 100))) / 100.0f; // 保留两位小数
		str = String.format("%.2f %s", l, unit);
		return str;
	}
}
