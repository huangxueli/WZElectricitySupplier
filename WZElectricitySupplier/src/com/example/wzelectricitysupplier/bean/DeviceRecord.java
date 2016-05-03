package com.example.wzelectricitysupplier.bean;

import java.util.ArrayList;
import java.util.List;

import com.example.wzelectricitysupplier.database.Field;

public class DeviceRecord extends GeometryRecord{
	
	public static Field ZD_NAME = new Field("����", "TEXT");
	public String mName = "";
	
	public static Field ZD_CIRCUIT = new Field("������·", "TEXT");
	public String mCircuit = "";
	
	public static Field ZD_PICTURE = new Field("��ͨ��Ƭ", "TEXT");
	public String mPicture = "";
	
	public static Field ZD_DEFECT_ID = new Field("ȱ�ݱ��", "TEXT");
	public String mDefectID = "";
	
	public static Field ZD_REMARK = new Field("��ע", "TEXT");
	public String mRemark = "";
	
	public static Field ZD_ISEDIT = new Field("�Ƿ��޸�", "TEXT");
	public String mIsEdit = "";
	
	protected String mDeviceType = "";

	public String getDeviceType() {
		return mDeviceType;
	}
	
	public List<String> getPictureList(){
		List<String> mPicNameList = new ArrayList<String>();
		if(!"".equals(mPicture)){
			if(mPicture.contains("&")){
				String[] arr = mPicture.split("&");
				for(String name:arr){
					mPicNameList.add(name);
				}
			}else{
				mPicNameList.add(mPicture);
			}
		}
		return mPicNameList;
	}

}
