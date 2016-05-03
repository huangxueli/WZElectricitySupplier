package com.example.wzelectricitysupplier.bean;

import com.example.wzelectricitysupplier.database.Field;
import com.example.wzelectricitysupplier.setting.AppUtil.DeviceType;

/**
 * 变电所
 */
public class BDSRecord extends DeviceRecord{
	
	public static Field ZD_NAME = new Field("变电站名称", "TEXT");
	
	public BDSRecord(){
		mFieldList.add(ZD_NAME);		// 名称
		mFieldList.add(ZD_CIRCUIT); 	// 所属线路
		mFieldList.add(ZD_PICTURE); 	// 日常照片
		mFieldList.add(ZD_DEFECT_ID);	// 缺陷编号
		mFieldList.add(ZD_REMARK);		// 备注
		mFieldList.add(ZD_ISEDIT);		// 是否修改过
	}
	
	public BDSRecord(String Name, String Circuit, String Picture, String DefectID, String Remark, String IsEdit){
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
		mDeviceType = DeviceType.DeviceBDS;
		return super.getDeviceType();
	}
}
