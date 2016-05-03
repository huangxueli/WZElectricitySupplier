package com.example.wzelectricitysupplier.function.route;

import android.graphics.Color;

import com.esri.core.symbol.SimpleLineSymbol.STYLE;

public class RouteTakenSetting {

	/**
	 * 调试标识
	 */
	public static final boolean Debug = false;
	/**
	 * 记录点周期
	 */
	public static final int RouteTimePeriod = 10*1000;
	/**
	 * 巡检路线起点图片样式 设置为NULL调用默认简易样式
	 */
	public static final Integer mStartPointResID = null;
	/**
	 * 巡检路线中图片样式 设置为NULL调用默认简易样式
	 */
	public static final Integer mMiddlePointResID = null;
	/**
	 * 巡检路线终点图片样式 设置为NULL调用默认简易样式
	 */
	public static final Integer mEndPointResID = null;
	/**
	 * 巡检路线线的颜色
	 */
	public static final int mLineColor = Color.BLACK;
	/**
	 * 巡检路线线的宽度
	 */
	public static final int mLineWidth = 4;
	/**
	 * 巡检路线线的样式
	 */
	public static final STYLE mLineStyle = STYLE.DASH;
}
