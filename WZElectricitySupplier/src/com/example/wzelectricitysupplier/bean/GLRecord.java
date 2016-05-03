package com.example.wzelectricitysupplier.bean;

import com.example.wzelectricitysupplier.database.Field;
import com.example.wzelectricitysupplier.setting.AppUtil.DeviceType;

/**
 * 隔离开关
 */
public class GLRecord extends DeviceRecord{
	
	public static Field ZD_NAME = new Field("隔离开关名称", "TEXT");
	
	public static Field ZD_STATE = new Field("状态", "TEXT");
	public String mState = "";
	
	public GLRecord(){
		mFieldList.add(ZD_NAME);
		mFieldList.add(ZD_CIRCUIT);
		mFieldList.add(ZD_STATE);
		mFieldList.add(ZD_PICTURE);
		mFieldList.add(ZD_DEFECT_ID);
		mFieldList.add(ZD_REMARK);
		mFieldList.add(ZD_ISEDIT);
	}
	
	public GLRecord(String Name, String Circuit, String State, String Picture, 
			String DefectID, String Remark, String IsEdit){
		this();
		mName = Name;
		mCircuit = Circuit;
		mState = State;
		mPicture = Picture;
		mDefectID = DefectID;
		mRemark = Remark;
		mIsEdit = IsEdit;
		
		mValueList.add(Name);
		mValueList.add(Circuit);
		mValueList.add(State);
		mValueList.add(Picture);
		mValueList.add(DefectID);
		mValueList.add(Remark);
		mValueList.add(IsEdit);
	}

	@Override
	public String getDeviceType() {
		mDeviceType = DeviceType.DeviceGL;
		return super.getDeviceType();
	}
	
}
