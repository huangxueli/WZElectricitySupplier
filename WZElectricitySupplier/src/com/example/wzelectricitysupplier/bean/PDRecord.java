package com.example.wzelectricitysupplier.bean;

import com.example.wzelectricitysupplier.database.Field;
import com.example.wzelectricitysupplier.setting.AppUtil.DeviceType;

/**
 * �����
 */
public class PDRecord extends DeviceRecord{
	
	public static Field ZD_NAME = new Field("���������", "TEXT");
	
	public static Field ZD_CAPACITY = new Field("���������", "TEXT");
	public String mCapacity = "";
	
	public static Field ZD_NUM = new Field("���̨��", "TEXT");
	public String mNum = "";
	
	public static Field ZD_MEDIA = new Field("��Ե����", "TEXT");
	public String mMedia = "";
	
	public static Field ZD_BACKUP_INTERVAL_NUM = new Field("���ü����", "TEXT");
	public String mBackupIntervalNum = "";
	
	public static Field ZD_OUT_INTERVAL_NUM = new Field("���߼����", "TEXT");
	public String mOutIntervalNum = "";
	
	public static Field ZD_IN_INTERVAL_NUM = new Field("���߼����", "TEXT");
	public String mInIntervalNum = "";
	
	public PDRecord(){
		mFieldList.add(ZD_NAME);
		mFieldList.add(ZD_CIRCUIT);
		mFieldList.add(ZD_CAPACITY);
		mFieldList.add(ZD_NUM);
		mFieldList.add(ZD_MEDIA);
		mFieldList.add(ZD_BACKUP_INTERVAL_NUM);
		mFieldList.add(ZD_OUT_INTERVAL_NUM);
		mFieldList.add(ZD_IN_INTERVAL_NUM);
		mFieldList.add(ZD_PICTURE);
		mFieldList.add(ZD_DEFECT_ID);
		mFieldList.add(ZD_REMARK);
		mFieldList.add(ZD_ISEDIT);
	}
	
	public PDRecord(String Name, String Circuit, String Capacity, String Num, String Media, String BackupIntervalNum,
			String OutIntervalNum, String InIntervalNum, String Picture, String DefectID, String Remark, String IsEdit){
		this();
		mName = Name;
		mCircuit = Circuit;
		mCapacity = Capacity;
		mNum = Num;
		mMedia = Media;
		mBackupIntervalNum = BackupIntervalNum;
		mOutIntervalNum = OutIntervalNum;
		mInIntervalNum = InIntervalNum;
		mPicture = Picture;
		mDefectID = DefectID;
		mRemark = Remark;
		mIsEdit = IsEdit;
		
		mValueList.add(Name);
		mValueList.add(Circuit);
		mValueList.add(Capacity);
		mValueList.add(Num);
		mValueList.add(Media);
		mValueList.add(BackupIntervalNum);
		mValueList.add(OutIntervalNum);
		mValueList.add(InIntervalNum);
		mValueList.add(Picture);
		mValueList.add(DefectID);
		mValueList.add(Remark);
		mValueList.add(IsEdit);
	}
	
	@Override
	public String getDeviceType() {
		mDeviceType = DeviceType.DevicePD;
		return super.getDeviceType();
	}
}
