package com.example.wzelectricitysupplier.function.route;

import android.graphics.Color;

import com.esri.core.symbol.SimpleLineSymbol.STYLE;

public class RouteTakenSetting {

	/**
	 * ���Ա�ʶ
	 */
	public static final boolean Debug = false;
	/**
	 * ��¼������
	 */
	public static final int RouteTimePeriod = 10*1000;
	/**
	 * Ѳ��·�����ͼƬ��ʽ ����ΪNULL����Ĭ�ϼ�����ʽ
	 */
	public static final Integer mStartPointResID = null;
	/**
	 * Ѳ��·����ͼƬ��ʽ ����ΪNULL����Ĭ�ϼ�����ʽ
	 */
	public static final Integer mMiddlePointResID = null;
	/**
	 * Ѳ��·���յ�ͼƬ��ʽ ����ΪNULL����Ĭ�ϼ�����ʽ
	 */
	public static final Integer mEndPointResID = null;
	/**
	 * Ѳ��·���ߵ���ɫ
	 */
	public static final int mLineColor = Color.BLACK;
	/**
	 * Ѳ��·���ߵĿ��
	 */
	public static final int mLineWidth = 4;
	/**
	 * Ѳ��·���ߵ���ʽ
	 */
	public static final STYLE mLineStyle = STYLE.DASH;
}
