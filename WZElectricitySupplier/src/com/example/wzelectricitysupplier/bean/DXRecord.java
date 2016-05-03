package com.example.wzelectricitysupplier.bean;

import com.example.wzelectricitysupplier.database.Field;
import com.example.wzelectricitysupplier.setting.AppUtil.DeviceType;

/**
 * 电线
 */
public class DXRecord extends DeviceRecord{
	
	public static Field ZD_NAME = new Field("线路名称", "TEXT");
	
	public static Field ZD_HEIGHT = new Field("长度", "TEXT");
	public String mHeight = "";
	
	public static Field ZD_TYPE = new Field("导线类型", "TEXT");
	public String mType = "";
	
	public static Field ZD_MODED = new Field("导线型号", "TEXT");
	public String mModel = "";
	
	public static Field ZD_NUM = new Field("回路数", "TEXT");
	public String mNum = "";
	
	public static Field ZD_DEVICENAME1 = new Field("起始设备或杆位", "TEXT");
	public String mDeviceName1 = "";
	
	public static Field ZD_DEVICENAME2 = new Field("终止设备或杆位", "TEXT");
	public String mDeviceName2 = "";
	
	public DXRecord(){
		mFieldList.add(ZD_NAME);
		mFieldList.add(ZD_CIRCUIT);
		mFieldList.add(ZD_HEIGHT);
		mFieldList.add(ZD_TYPE);
		mFieldList.add(ZD_MODED);
		mFieldList.add(ZD_NUM);
		mFieldList.add(ZD_DEVICENAME1);
		mFieldList.add(ZD_DEVICENAME2);
		mFieldList.add(ZD_PICTURE);
		mFieldList.add(ZD_DEFECT_ID);
		mFieldList.add(ZD_REMARK);
		mFieldList.add(ZD_ISEDIT);
	}
	/**
	 * 新增
	 */
	public DXRecord(String Name, String Circuit, String Height, String Type, String Model, String Num, 
			String DeviceName1, String DeviceName2, String Picture, String DefectID, String Remark, String IsEdit){
		this();
		mName = Name;
		mCircuit = Circuit;
		mHeight = Height;
		mType = Type;
		mModel = Model;
		mNum = Num;
		mDeviceName1 = DeviceName1;
		mDeviceName2 = DeviceName2;
		mPicture = Picture;
		mDefectID = DefectID;
		mRemark = Remark;
		mIsEdit = IsEdit;
		
		mValueList.add(Name);
		mValueList.add(Circuit);
		mValueList.add(Height);
		mValueList.add(Type);
		mValueList.add(Model);
		mValueList.add(Num);
		mValueList.add(DeviceName1);
		mValueList.add(DeviceName2);
		mValueList.add(Picture);
		mValueList.add(DefectID);
		mValueList.add(Remark);
		mValueList.add(IsEdit);
	}
	
	@Override
	public String getDeviceType() {
		mDeviceType = DeviceType.Line;
		return super.getDeviceType();
	}
}