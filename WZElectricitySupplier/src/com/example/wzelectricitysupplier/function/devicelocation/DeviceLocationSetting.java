package com.example.wzelectricitysupplier.function.devicelocation;

import android.graphics.Color;

import com.example.wzelectricitysupplier.R;

public class DeviceLocationSetting {
	
	/**
	 * ���Ա�ʶ
	 */
	public static final boolean Debug = false;
	/**
	 * ��ʶ�Ƿ�������λ���ܣ�Ĭ�ϣ���)
	 */
	public static boolean StartLocation = true;
	/**
	 *  ��ʶ�Ƿ�ʹ�����綨λ��Ĭ�ϣ���)
	 */
	public static boolean AllowNetworkLocation = false; 
	/**
	 *  �Զ�ƽ�Ƶ���λ�㣨Ĭ�ϣ���)
	 */
	public static boolean AutoPanOnce = true;
	/**
	 *  ��ʶ�Ƿ���涨λ���ƶ���Ĭ�ϣ���)
	 */
	public static boolean AutoPanFollow = false;
	/**
	 *  ��ʶ�Ƿ񻭷�ΧԲ��Ĭ�ϣ���)
	 */
	public static boolean DrawAccuracy = true;
	/**
	 *  ��ʶ�϶���ͼʱ�Ƿ���ʾ��λ�㣨Ĭ�ϣ���)
	 */
	public static boolean UseCourseSymbolOnMovement = true;
	/**
	 *  ��ΧԲ��ԴͼƬ��
	 */
	public static final Integer AccuracyResourceID = R.drawable.accuracy; 
	/**
	 *  ��ΧԲ������ɫ����ͼƬ��ԴʱĬ�ϵ��ã�
	 */
	public static final String AccuracyColorString = "#000000"; 
	/**
	 *  ��λ����ԴͼƬ��
	 */
	public static final Integer PointResourceID = null; 
	/**
	 *  ��λ�������ɫ
	 */
	public static final Integer PointColor = Color.GREEN; 
}
