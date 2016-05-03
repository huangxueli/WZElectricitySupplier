package com.example.wzelectricitysupplier.function.route;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.esri.android.map.Callout;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.MultiPath;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.Symbol;
import com.example.wzelectricitysupplier.MyApplication;
import com.example.wzelectricitysupplier.R;
import com.example.wzelectricitysupplier.bean.RoutePointRecord;
import com.example.wzelectricitysupplier.bean.RouteRecord;
import com.example.wzelectricitysupplier.database.RoutePointTable;
import com.example.wzelectricitysupplier.database.RouteTable;
import com.example.wzelectricitysupplier.function.ArcgisTool;
import com.example.wzelectricitysupplier.function.messure.MessureDistanceSetting;
import com.example.wzelectricitysupplier.setting.Util;

/**
 * �켣��¼
 */
public class RouteTakenManager {
	
	public static final String TAG = "RouteTakenManager";
	
	private static final String KEY_TIME_START = "StartTime";
	private static final String KEY_TIME_RECORD = "RecordTime";
	private static final String KEY_TIME_END = "EndTime";
	
	private MapView mMap;
	private RouteTable mRouteTable;
	private RoutePointTable mRoutePointTable;
	private GraphicsLayer mRoutePointLayer;	// Ѳ����
	private GraphicsLayer mRouteLayer;		// Ѳ���߲�
	private Callout mCallout;
	
	private List<Point> mPointList = new ArrayList<Point>();
	private int mLineID = -1;
	private String mStartTime;
	private String mEndTime;
	private boolean mIsTimeUp = true; // ��ֵ
	private boolean mEnableRouteRecord = false;
	
	private View mCalloutView;
	private TextView mLon;
	private TextView mLat;
	private TextView mRecordTimeText;
	
	public RouteTakenManager(MapView mMap, RouteTable mRouteTable, RoutePointTable mRoutePointTable) {
		this.mMap =  mMap;
		this.mRouteTable =  mRouteTable;
		this.mRoutePointTable =  mRoutePointTable;
		
		mRouteLayer = new GraphicsLayer();
		mMap.addLayer(mRouteLayer);
		mRoutePointLayer = new GraphicsLayer();
		mMap.addLayer(mRoutePointLayer);
		initCallout();
	}
	
	// ÿ��λ�øı䶼����ô˷���
	public void recordPostion(Point point) {
		if (mEnableRouteRecord) { 
			if (mIsTimeUp) {
				String recordTime = DateFormat.format("yyyy��MM��dd��  HH:mm:ss", new Date(System.currentTimeMillis())).toString();
				if(RouteTakenSetting.Debug){
					Util.Toast(recordTime + " ��¼һ�ε�ǰλ��");
					Log.i(TAG, recordTime + " ��¼һ�ε�ǰλ��");
				}
				
				drawPoint(point, recordTime, true);
				drawLine(true);
				mIsTimeUp = false;
			}
		}
	}
	
	public void startRecordRoute() {
		clearLayer();
		mEnableRouteRecord = true;
		
		if(RouteTakenSetting.Debug){
			Util.Toast("����Ѳ����·��¼");
		}
		countTime();
	}
	
	public void stopRecordRoute() {
		mEnableRouteRecord = false;

		clearLayer();
		if(RouteTakenSetting.Debug){
			Util.Toast("ֹͣѲ����·��¼");
		}
	}
	
	public void clearLayer() {
		mPointList.clear();
		mRoutePointLayer.removeAll();
		mRouteLayer.removeAll();
	}
	
	public boolean isEnableRecordRoute() {
		return mEnableRouteRecord;
	}
	
	/**
	 * ������¼��һ���㲢ִ�����ݿ����
	 * @param point
	 * @param time
	 */
	private void drawPoint(Point point, String time, boolean save) {
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put(KEY_TIME_RECORD, time);
		int num = mRoutePointLayer.getNumberOfGraphics();
		Symbol symbol = null;
		if (num == 0) { 
			// ���
			if(RouteTakenSetting.mStartPointResID!=null){
				Drawable drawable = MyApplication.Resources.getDrawable(MessureDistanceSetting.mStartPointResID);
				symbol = new PictureMarkerSymbol(drawable);
			}else{
				symbol = new SimpleMarkerSymbol(Color.GREEN, 10, SimpleMarkerSymbol.STYLE.SQUARE);
			}
			mStartTime = time;
		} else {
			if(RouteTakenSetting.mMiddlePointResID!=null){
				Drawable drawable = MyApplication.Resources.getDrawable(MessureDistanceSetting.mMiddlePointResID);
				symbol = new PictureMarkerSymbol(drawable);
			}else{
				symbol = new SimpleMarkerSymbol(Color.BLUE, 10, SimpleMarkerSymbol.STYLE.CIRCLE);
			}
			mEndTime = time;
		} 
		mPointList.add(point);
		Graphic graphic = new Graphic(point, symbol, attributes);
		mRoutePointLayer.addGraphic(graphic); // ����ͼ��
		if(mPointList.size()>2 && save){
			if(mLineID>0){
				savePoint(mLineID);	// ����Ѳ���
			}
		}
	}
	
	/**
	 *  ���߻���²�ִ�����ݿ���������������ڶ���ʱ��ʼ����
	 */
	private void drawLine(boolean save) {
		int size = mPointList.size();
		if (size > 1) {
			Map<String, Object> attributes = new HashMap<String, Object>();
			MultiPath path = new Polyline();
			for (int i = 0; i < size; i++) {
				if (i == 0) { 
					// ���
					path.startPath((Point) mPointList.get(0));
					attributes.put(KEY_TIME_START, mStartTime);
				} else {
					 // �м��
					path.lineTo((Point) mPointList.get(i));
					if (i == size - 1) { 
						// β��
						attributes.put(KEY_TIME_END, mEndTime);
					}
				}
			}
			if (mRouteLayer.getNumberOfGraphics() > 0) { // ���� ����
				int[] ids = mRouteLayer.getGraphicIDs();
				mRouteLayer.updateGraphic(ids[0], path);
				mRouteLayer.updateGraphic(ids[0], attributes);
			} else { // ������  ���һ��
				Symbol symbol = new SimpleLineSymbol(RouteTakenSetting.mLineColor, RouteTakenSetting.mLineWidth, RouteTakenSetting.mLineStyle);
				mRouteLayer.addGraphic(new Graphic(path, symbol, attributes));
			}
			if(save){
				mLineID = saveLine(); // ����Ѳ����·
			}
			
		}
	}
	
	/**
	 * ����һ����¼���������
	 * @return
	 */
	private int saveLine() {
		if (mRouteLayer != null) {
			int count = mRouteLayer.getNumberOfGraphics();
			if (count > 0) {
				int[] ids = mRouteLayer.getGraphicIDs();
				Graphic graphic = mRouteLayer.getGraphic(ids[0]);
				Polyline line = (Polyline) graphic.getGeometry();
				Map<String, Object> attrubute = graphic.getAttributes();
				RouteRecord record = new RouteRecord((String) attrubute.get(KEY_TIME_START), (String) attrubute.get(KEY_TIME_END));
				mLineID = mRouteTable.addAndUpdateRouteRecord(line, record, mLineID);
			}
		}
		return mLineID;
	}
	
	/**
	 * �����ߵĻ����ϲŻᱣ������ݣ������㿪ʼ���棩
	 * @param lineid
	 */
	private void savePoint(int lineid) {
		int count = mRoutePointLayer.getNumberOfGraphics();
		if (count > 0 && lineid > 0) {
			int[] ids = mRoutePointLayer.getGraphicIDs();
			if(ids.length == 3){ // ���Ⱥ�˳�򱣴�ǰ������
				ids = Util.bubbleSort(ids);
				for(int i:ids){
					Graphic graphic = mRoutePointLayer.getGraphic(i);
					Point point = (Point) graphic.getGeometry();
					Map<String, Object> attrubute = graphic.getAttributes();
					RoutePointRecord record = new  RoutePointRecord((String) attrubute.get(KEY_TIME_RECORD), String.valueOf(lineid));
					mRoutePointTable.add(record, point);
				}
				return ;
			}
			if (ids.length > 3) { // �������һ�μ�¼�ĵ�
				Graphic graphic = mRoutePointLayer.getGraphic(ids[0]);
				Point point = (Point) graphic.getGeometry();
				Map<String, Object> attrubute = graphic.getAttributes();
				RoutePointRecord record = new  RoutePointRecord((String) attrubute.get(KEY_TIME_RECORD), String.valueOf(lineid));
				mRoutePointTable.add(record, point);
			}
		}
	}

	/**
	 * ��ѯ���ݿ��ȡһ��Ѳ��·�߲�����
	 * @param lineId
	 * @return
	 */
	public void drawHistoryRoute(int lineId) {
		List<RoutePointRecord> list = mRoutePointTable.getPointList(lineId);
		RoutePointRecord record = null;
		clearLayer();
		for (int i = 0; i < list.size(); i++) {
			record = list.get(i);
			drawPoint((Point)record.getGeometry(), record.mRecordTime, false);
		}
		drawLine(false);
	}
	
	private void initCallout(){
		mCalloutView = MyApplication.Inflater.inflate(R.layout.callout_route_point, null);
		mLon = (TextView)mCalloutView.findViewById(R.id.longitudeText);
		mLat = (TextView)mCalloutView.findViewById(R.id.latitudeText);
		mRecordTimeText = (TextView)mCalloutView.findViewById(R.id.recordTimeText);
	}
	
	private void showCallout(Point coordinates, String recordTime){
		mLon.setText(String.valueOf(coordinates.getX()));
		mLat.setText(String.valueOf(coordinates.getY()));
		mRecordTimeText.setText(recordTime);
		mCallout = mMap.getCallout();
		mCallout.setContent(mCalloutView);
		mCallout.setCoordinates(coordinates);
		mCallout.setMaxHeight(Util.Dip2Px(MyApplication.Context, 100));
		mCallout.setMaxWidth(Util.Dip2Px(MyApplication.Context, 250));
		mCallout.show();
	}
	
	public void hideCallout(){
		if(mCallout!=null)
			mCallout.hide();
	}
	
	public void CheckPointSingleTapListener(float x, float y){
		int id = ArcgisTool.getGraphicId(mMap, x, y, mRoutePointLayer);
		if (id > 0) {
			Graphic graphic = mRoutePointLayer.getGraphic(id);
			Map<String, Object> att = graphic.getAttributes();
			Geometry geo = graphic.getGeometry();
			showCallout((Point)geo,(String)att.get(KEY_TIME_RECORD));
		}else{
			hideCallout();
		}
	}
	
	/**
	 * ��ʱ
	 */
	private void countTime() {
		if (RouteTakenSetting.RouteTimePeriod > 999 && mEnableRouteRecord) {
		
			mIsTimeUp = true;
			if(RouteTakenSetting.Debug){
				Log.i(TAG, RouteTakenSetting.RouteTimePeriod + "ʱ�䵽");
			}
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					countTime();
				}
			}, RouteTakenSetting.RouteTimePeriod);
		}
	}
}
