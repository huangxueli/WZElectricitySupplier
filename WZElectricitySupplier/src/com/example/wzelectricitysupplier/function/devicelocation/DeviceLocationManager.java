package com.example.wzelectricitysupplier.function.devicelocation;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.LocationDisplayManager.AutoPanMode;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;
import com.esri.core.symbol.FillSymbol;
import com.esri.core.symbol.MarkerSymbol;
import com.esri.core.symbol.PictureFillSymbol;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol.STYLE;
import com.example.wzelectricitysupplier.MyApplication;
import com.example.wzelectricitysupplier.setting.Util;

public class DeviceLocationManager {
	
	private MapView mMap;
	// ��ʶ�Ƿ�������λ����
	private boolean mStartLocation; 	
	// �Զ�ƽ�Ƶ���λ��
	private boolean mAutoPanOnce;
	// ��ʶ�Ƿ���涨λ���ƶ�
	private boolean mAutoPanFollow; 
	// ��λ��ͼ��
	private GraphicsLayer mDeviceLocationLayer; 
	
	private LocationDisplayManager mLocationDisplayManager;
	private LocationChangeListener mLocationChangeListener;
	
	public DeviceLocationManager(MapView mMap, LocationChangeListener mLocationChangeListener){
		this.mMap = mMap;
		this.mLocationChangeListener = mLocationChangeListener;
		this.mLocationDisplayManager = mMap.getLocationDisplayManager();
		
		this.mStartLocation = DeviceLocationSetting.StartLocation;
		this.mAutoPanOnce = DeviceLocationSetting.AutoPanOnce;
		this.mAutoPanFollow = DeviceLocationSetting.AutoPanFollow;
		// ���ͼ��
		mDeviceLocationLayer = new GraphicsLayer();
		mMap.addLayer(mDeviceLocationLayer);
	}
	
	// �´ζ�λʱ�Զ�ƽ�Ƶ���λ��
	public void AutoPanToLocationNextTime() {
		mLocationDisplayManager.setAutoPanMode(AutoPanMode.LOCATION);
		mAutoPanOnce = false;
	}
	
	// ������λ����
	public void StartLocation(){
		try{
			mLocationDisplayManager.setAccuracyCircleOn(DeviceLocationSetting.DrawAccuracy);
			InitAccuracySymbolStyle(mLocationDisplayManager, DeviceLocationSetting.AccuracyResourceID, DeviceLocationSetting.AccuracyColorString);
//			InitLocationSymbolStyle(DeviceLocationSetting.PointResourceID, DeviceLocationSetting.PointColor);
			mLocationDisplayManager.setLocationListener(new CurrentLocationListener());
			mLocationDisplayManager.setAutoPanMode(AutoPanMode.LOCATION);
			mLocationDisplayManager.setUseCourseSymbolOnMovement(DeviceLocationSetting.UseCourseSymbolOnMovement);
			mLocationDisplayManager.setAllowNetworkLocation(DeviceLocationSetting.AllowNetworkLocation);
			mLocationDisplayManager.start();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public boolean AllowLocation(){
		return mStartLocation;
	}
	
	// ���÷�ΧԲ����ʽ
	private void InitAccuracySymbolStyle(LocationDisplayManager mLocationDisplayManager, 
			Integer mAccuracyResourceID, String mAccuracyColorString) throws Exception{
		FillSymbol mAccuracySymbol = null;
		if(mAccuracyResourceID != null){
			Drawable mDrawable =  MyApplication.Resources.getDrawable(mAccuracyResourceID);
			SimpleLineSymbol mSimpleLineSymbol = new SimpleLineSymbol(Color.BLACK, 1.0F, SimpleLineSymbol.STYLE.NULL);
			mAccuracySymbol = new PictureFillSymbol(mDrawable, mSimpleLineSymbol);
		} else{
			mAccuracySymbol = new SimpleFillSymbol(Color.parseColor(mAccuracyColorString), SimpleFillSymbol.STYLE.SOLID);
		}
		mLocationDisplayManager.setAccuracySymbol(mAccuracySymbol);
	}
	// ���ö�λ�����ʽ
	@SuppressWarnings("unused")
	private void InitLocationSymbolStyle(Integer mPointResourceID, int mPointColor) throws Exception{
		MarkerSymbol mLocationSymbol = null;
		if(mPointResourceID != null && mPointResourceID != -1){
			Drawable drawable =  MyApplication.Resources.getDrawable(mPointResourceID);
			mLocationSymbol = new PictureMarkerSymbol(drawable);
		} else{
			mLocationSymbol = new SimpleMarkerSymbol(mPointColor, 16, STYLE.CIRCLE);
		}
		mLocationDisplayManager.setLocationAcquiringSymbol(mLocationSymbol);
	}
	
	class CurrentLocationListener implements LocationListener{
		
		public static final String ATTRIBUTES_TAG_TIME = "Time";
		
		@Override
		public void onLocationChanged(Location location) {
			
			Point point = mLocationDisplayManager.getPoint();
			
			if (point == null){
				return;
			}else {
				if(DeviceLocationSetting.Debug){
					Log.i("CurrentDeviceLocationManager", "λ�ñ仯��" + point.getX() + " " + point.getY());
					Util.Toast(point.getX() + " " + point.getY());
				}
			}
			
			try {
				UpdateGPSGraphic(point);// ���µ�ͼ��GPS��ע
			} catch (Exception e) {
				e.printStackTrace();
			} 
			
			if(mAutoPanFollow){
				mLocationDisplayManager.setAutoPanMode(AutoPanMode.LOCATION);
			}else{
				if(mAutoPanOnce){ 
					mAutoPanOnce = false;
					mMap.zoomToScale(point, 52223.0000000);// ���������ó�ʼ��ͼ ������
				}else {
					mLocationDisplayManager.setAutoPanMode(AutoPanMode.OFF);
				}
			}
			
			if (mLocationChangeListener != null) {
				mLocationChangeListener.onDeviceLocationChanged(point);
			}
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			Util.Toast("GPS Enabled");
		}

		@Override
		public void onProviderDisabled(String provider) {
			Util.Toast("GPS Disabled");
		}

		private void UpdateGPSGraphic(Point geometry) throws Exception{
			if (mDeviceLocationLayer != null) {
	            // ����λʱ����Ϊ�ر�����Ա�������
	            Map<String, Object> attributes = new HashMap<String, Object>();
	            String time = DateFormat.format("yyyy-MM-dd kk:mm:ss", System.currentTimeMillis()).toString();
	            attributes.put(ATTRIBUTES_TAG_TIME, time);
	        }
		}
		
	}
	
	public static abstract class LocationChangeListener {
		public abstract void onDeviceLocationChanged(Point point);
	}

}
