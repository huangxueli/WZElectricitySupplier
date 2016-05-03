package com.example.wzelectricitysupplier.bean;

import com.example.wzelectricitysupplier.database.Field;
import com.example.wzelectricitysupplier.setting.AppUtil.DeviceType;

/**
 * 裝坢
 */
public class GTRecord extends DeviceRecord{
	
	public static Field ZD_NAME = new Field("裝瘍", "TEXT");
	
	public static Field ZD_HEIGHT = new Field("裝詢", "TEXT");
	public String mHeight = "";
	
	public static Field ZD_MATERIAL = new Field("裝坢第窐", "TEXT");
	public String mMaterial = "";
	
	public static Field ZD_TYPE = new Field("裝倰", "TEXT");
	public String mType = "";
	
	public GTRecord(){
		mFieldList.add(ZD_NAME);
		mFieldList.add(ZD_CIRCUIT);
		mFieldList.add(ZD_HEIGHT);
		mFieldList.add(ZD_MATERIAL);
		mFieldList.add(ZD_TYPE);
		mFieldList.add(ZD_PICTURE);
		mFieldList.add(ZD_DEFECT_ID);
		mFieldList.add(ZD_REMARK);
		mFieldList.add(ZD_ISEDIT);
	}
	
	public GTRecord(String Name, String Circuit, String Height, String Material, String Type, 
			String Picture, String DefectID, String Remark, String IsEdit){
		this();
		mName = Name;
		mCircuit = Circuit;
		mHeight = Height;
		mMaterial = Material;
		mType = Type;
		mPicture = Picture;
		mDefectID = DefectID;
		mRemark = Remark;
		mIsEdit = IsEdit;
		
		mValueList.add(Name);
		mValueList.add(Circuit);
		mValueList.add(Height);
		mValueList.add(Material);
		mValueList.add(Type);
		mValueList.add(Picture);
		mValueList.add(DefectID);
		mValueList.add(Remark);
		mValueList.add(IsEdit);
	}

	@Override
	public String getDeviceType() {
		mDeviceType = DeviceType.DeviceGT;
		return super.getDeviceType();
	}
	
}
