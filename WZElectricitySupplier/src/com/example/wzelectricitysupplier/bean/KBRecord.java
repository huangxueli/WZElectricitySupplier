package com.example.wzelectricitysupplier.bean;

import com.example.wzelectricitysupplier.database.Field;
import com.example.wzelectricitysupplier.setting.AppUtil.DeviceType;

/**
 * 开闭所
 */
public class KBRecord extends DeviceRecord{
	
	public static Field ZD_NAME = new Field("开闭所名称", "TEXT");
	
	public static Field ZD_BACKUP_INTERVAL_NUM = new Field("备用间隔回路数", "TEXT");
	public String mBackupIntervalNum = "";
	
	public static Field ZD_OUT_INTERVAL_NUM = new Field("出线间隔回路数", "TEXT");
	public String mOutIntervalNum = "";
	
	public static Field ZD_IN_INTERVAL_NUM = new Field("进线间隔回路数", "TEXT");
	public String mInIntervalNum = "";
	
	public KBRecord(){
		mFieldList.add(ZD_NAME);
		mFieldList.add(ZD_CIRCUIT);
		mFieldList.add(ZD_BACKUP_INTERVAL_NUM);
		mFieldList.add(ZD_OUT_INTERVAL_NUM);
		mFieldList.add(ZD_IN_INTERVAL_NUM);
		mFieldList.add(ZD_PICTURE);
		mFieldList.add(ZD_DEFECT_ID);
		mFieldList.add(ZD_REMARK);
		mFieldList.add(ZD_ISEDIT);
	}
	
	public KBRecord(String Name, String Circuit, String BackupIntervalNum, String OutIntervalNum, 
			String InIntervalNum, String Picture, String DefectID, String Remark, String IsEdit){
		this();
		mName = Name;
		mCircuit = Circuit;
		mBackupIntervalNum = BackupIntervalNum;
		mOutIntervalNum = OutIntervalNum;
		mInIntervalNum = InIntervalNum;
		mPicture = Picture;
		mDefectID = DefectID;
		mRemark = Remark;
		mIsEdit = IsEdit;
		
		mValueList.add(Name);
		mValueList.add(Circuit);
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
		mDeviceType = DeviceType.DeviceKG;
		return super.getDeviceType();
	}
}
