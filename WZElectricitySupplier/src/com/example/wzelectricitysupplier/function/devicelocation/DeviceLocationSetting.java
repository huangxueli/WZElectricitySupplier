package com.example.wzelectricitysupplier.function.devicelocation;

import android.graphics.Color;

import com.example.wzelectricitysupplier.R;

public class DeviceLocationSetting {
	
	/**
	 * 调试标识
	 */
	public static final boolean Debug = false;
	/**
	 * 标识是否启动定位功能（默认：是)
	 */
	public static boolean StartLocation = true;
	/**
	 *  标识是否使用网络定位（默认：是)
	 */
	public static boolean AllowNetworkLocation = false; 
	/**
	 *  自动平移到定位点（默认：是)
	 */
	public static boolean AutoPanOnce = true;
	/**
	 *  标识是否跟随定位点移动（默认：否)
	 */
	public static boolean AutoPanFollow = false;
	/**
	 *  标识是否画范围圆（默认：是)
	 */
	public static boolean DrawAccuracy = true;
	/**
	 *  标识拖动地图时是否显示定位点（默认：是)
	 */
	public static boolean UseCourseSymbolOnMovement = true;
	/**
	 *  范围圆资源图片号
	 */
	public static final Integer AccuracyResourceID = R.drawable.accuracy; 
	/**
	 *  范围圆区域颜色（无图片资源时默认调用）
	 */
	public static final String AccuracyColorString = "#000000"; 
	/**
	 *  定位点资源图片号
	 */
	public static final Integer PointResourceID = null; 
	/**
	 *  定位点填充颜色
	 */
	public static final Integer PointColor = Color.GREEN; 
}
