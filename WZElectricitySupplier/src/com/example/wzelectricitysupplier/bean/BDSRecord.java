package com.example.wzelectricitysupplier.bean;

import com.example.wzelectricitysupplier.database.Field;
import com.example.wzelectricitysupplier.setting.AppUtil.DeviceType;

/**
 * �����
 */
public class BDSRecord extends DeviceRecord{
	
	public static Field ZD_NAME = new Field("���վ����", "TEXT");
	
	public BDSRecord(){
		mFieldList.add(ZD_NAME);		// ����
		mFieldList.add(ZD_CIRCUIT); 	// ������·
		mFieldList.add(ZD_PICTURE); 	// �ճ���Ƭ
		mFieldList.add(ZD_DEFECT_ID);	// ȱ�ݱ��
		mFieldList.add(ZD_REMARK);		// ��ע
		mFieldList.add(ZD_ISEDIT);		// �Ƿ��޸Ĺ�
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
