package com.example.wzelectricitysupplier.bean;

import com.example.wzelectricitysupplier.database.Field;
import com.example.wzelectricitysupplier.setting.AppUtil.DeviceType;

/**
 * Áî¿Ë
 */
public class LKRecord extends DeviceRecord{
	
	public static Field ZD_NAME = new Field("Áî¿ËÃû³Æ", "TEXT");
	
	public LKRecord(){
		mFieldList.add(ZD_NAME);
		mFieldList.add(ZD_CIRCUIT);
		mFieldList.add(ZD_PICTURE);
		mFieldList.add(ZD_DEFECT_ID);
		mFieldList.add(ZD_REMARK);
		mFieldList.add(ZD_ISEDIT);
	}
	
	public LKRecord(String Name, String Circuit, String Picture, String DefectID, 
			String Remark, String IsEdit){
		this();
		mName = Name;
		mCircuit = Circuit;
		mPicture = Picture;
		mDefectID = DefectID;
		mRemark = Remark;
		mIsEdit = IsEdit;
		
		mValueList.add(Name);
		mValueList.add(Circuit);
		mValueList.add(Picture);
		mValueList.add(DefectID);
		mValueList.add(Remark);
		mValueList.add(IsEdit);
	}
	
	@Override
	public String getDeviceType() {
		mDeviceType = DeviceType.DeviceLK;
		return super.getDeviceType();
	}
}
