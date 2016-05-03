package com.example.wzelectricitysupplier.bean;

import com.example.wzelectricitysupplier.database.Field;
import com.example.wzelectricitysupplier.setting.AppUtil.DeviceType;

/**
 * ���
 */
public class XBRecord extends DeviceRecord{
	
	public static Field ZD_NAME = new Field("�������", "TEXT");
	
	public static Field ZD_CAPACITY = new Field("���������", "TEXT");
	public String mCapacity = "";
	
	public static Field ZD_NUM = new Field("��ѹ��̨��", "TEXT");
	public String mNum = "";
	
	public static Field ZD_TYPE = new Field("�������", "TEXT");
	public String mType = "";
	
	public XBRecord(){
		mFieldList.add(ZD_NAME);
		mFieldList.add(ZD_CIRCUIT);
		mFieldList.add(ZD_CAPACITY);
		mFieldList.add(ZD_NUM);
		mFieldList.add(ZD_TYPE);
		mFieldList.add(ZD_PICTURE);
		mFieldList.add(ZD_DEFECT_ID);
		mFieldList.add(ZD_REMARK);
		mFieldList.add(ZD_ISEDIT);
	}
	
	public XBRecord(String Name, String Circuit, String Capacity, String Num, String Type, 
			String Picture, String DefectID, String Remark, String IsEdit){
		this();
		mName = Name;
		mCircuit = Circuit;
		mCapacity = Capacity;
		mNum = Num;
		mType = Type;
		mPicture = Picture;
		mDefectID = DefectID;
		mRemark = Remark;
		mIsEdit = IsEdit;
		
		mValueList.add(Name);
		mValueList.add(Circuit);
		mValueList.add(Capacity);
		mValueList.add(Num);
		mValueList.add(Type);
		mValueList.add(Picture);
		mValueList.add(DefectID);
		mValueList.add(Remark);
		mValueList.add(IsEdit);
	}
	
	@Override
	public String getDeviceType() {
		mDeviceType = DeviceType.DeviceXB;
		return super.getDeviceType();
	}

}
