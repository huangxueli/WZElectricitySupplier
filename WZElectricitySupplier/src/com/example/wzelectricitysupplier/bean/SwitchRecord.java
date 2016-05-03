package com.example.wzelectricitysupplier.bean;

import com.example.wzelectricitysupplier.database.Field;
import com.example.wzelectricitysupplier.setting.AppUtil.DeviceType;

/**
 * 开关
 */
public class SwitchRecord extends DeviceRecord{
	
	public static Field ZD_NAME = new Field("开关编号", "TEXT");
	
	public static Field ZD_HAVEDZ = new Field("是否带刀闸", "TEXT");
	public String mHavedz = "";
	
	public static Field ZD_STATE = new Field("状态", "TEXT");
	public String mState = "";
	
	public SwitchRecord(){
		mFieldList.add(ZD_NAME);
		mFieldList.add(ZD_CIRCUIT);
		mFieldList.add(ZD_HAVEDZ);
		mFieldList.add(ZD_STATE);
		mFieldList.add(ZD_PICTURE);
		mFieldList.add(ZD_DEFECT_ID);
		mFieldList.add(ZD_REMARK);
		mFieldList.add(ZD_ISEDIT);
	}
	
	public SwitchRecord(String Name, String Circuit, String Havedz, String State, String Picture, 
			String DefectID, String Remark, String IsEdit){
		this();
		mName = Name;
		mCircuit = Circuit;
		mHavedz = Havedz;
		mState = State;
		mPicture = Picture;
		mDefectID = DefectID;
		mRemark = Remark;
		mIsEdit = IsEdit;
		
		mValueList.add(Name);
		mValueList.add(Circuit);
		mValueList.add(Havedz);
		mValueList.add(State);
		mValueList.add(Picture);
		mValueList.add(DefectID);
		mValueList.add(Remark);
		mValueList.add(IsEdit);
	}
	
	@Override
	public String getDeviceType() {
		mDeviceType = DeviceType.DeviceSwitch;
		return super.getDeviceType();
	}
}
