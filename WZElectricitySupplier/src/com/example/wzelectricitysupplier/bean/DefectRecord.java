package com.example.wzelectricitysupplier.bean;

import java.util.ArrayList;
import java.util.List;

import com.example.wzelectricitysupplier.database.Field;

/**
 * ȱ�ݼ�¼
 */
public class DefectRecord extends NormalRecord{
	
	public static Field ZD_NAME = new Field("ȱ�ݱ��", "TEXT");
	public String mName = "";
	
	public static Field ZD_BELONG_ID = new Field("�������", "TEXT");
	public String mBelongId = "";
	
	public static Field ZD_BELONG_DEVICE = new Field("�����豸", "TEXT");
	public String mBelongDevice = "";
	
	public static Field ZD_PICTURE = new Field("ȱ����Ƭ", "TEXT");
	public String mPicture = "";
	
	public static Field ZD_TYPE = new Field("ȱ������", "TEXT");
	public String mType = "";
	
	public static Field ZD_DESCRIBE = new Field("����", "TEXT");
	public String mDescribe = "";
	
	public static Field ZD_ISEDIT = new Field("�Ƿ��޸�", "TEXT");
	public String mIsEdit = "";
	
	public DefectRecord(){
		mFieldList.add(ZD_NAME);
		mFieldList.add(ZD_BELONG_ID);
		mFieldList.add(ZD_BELONG_DEVICE);
		mFieldList.add(ZD_PICTURE);
		mFieldList.add(ZD_TYPE);
		mFieldList.add(ZD_DESCRIBE);
		mFieldList.add(ZD_ISEDIT);
	}
	
	public DefectRecord(String Name, String BelongId, String BelongDevice, String Picture, String Type, 
			String Describe, String IsEdit){
		this();
		mName = Name;
		mBelongId = BelongId;
		mBelongDevice = BelongDevice;
		mPicture = Picture;
		mType = Type;
		mDescribe = Describe;
		mIsEdit = IsEdit;
		
		mValueList.add(Name);
		mValueList.add(BelongId);
		mValueList.add(BelongDevice);
		mValueList.add(Picture);
		mValueList.add(Type);
		mValueList.add(Describe);
		mValueList.add(IsEdit);
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
