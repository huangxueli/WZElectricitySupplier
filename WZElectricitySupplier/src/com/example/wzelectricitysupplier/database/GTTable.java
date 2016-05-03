package com.example.wzelectricitysupplier.database;

import java.util.ArrayList;
import java.util.List;

import jsqlite.Database;
import jsqlite.Exception;
import jsqlite.Stmt;

import com.esri.core.geometry.Point;
import com.example.wzelectricitysupplier.bean.DefectRecord;
import com.example.wzelectricitysupplier.bean.GTRecord;

public class GTTable extends GeometryTable {
	
	public String TAG = "GTTable";
	
	public final static String mTableName = "tower";
	public final static String mGeometryType = "POINT";
	
	public GTTable(Database mDatabase) {
		this(mDatabase, mTableName, mGeometryType, new GTRecord().getFieldList());
	}
	public GTTable(Database mDatabase, String mTableName, String mGeometryType, ArrayList<Field> mFieldList) {
		super(mDatabase, mTableName, mGeometryType, mFieldList);
		CreateTable();
	}
	
	public List<GTRecord> getTowerList(int circuitId) {
		String condition = GTRecord.ZD_CIRCUIT.NAME + "=" + circuitId;
		List<GTRecord> mRecordList = selectTowers(condition);
		return mRecordList;
	}
	
	/**
	 * ����������ȡһ����¼
	 * @param primary
	 * @return
	 */
	public synchronized GTRecord selectTower(int primary){
		GTRecord record = null;
		String condition = DBSetting.ZD_PRIMERY + " = " + primary;
		ArrayList<GTRecord> list = selectTowers(condition);
		if(!list.isEmpty()){
			record = list.get(0);
		}
		return record;
	}
	/**
	 * ���ݲ�ѯ������ȡ������¼��
	 * @param condition
	 * @return
	 */
	public synchronized ArrayList<GTRecord> selectTowers(String condition){
		ArrayList<GTRecord> list = new ArrayList<GTRecord>();
		try{
			String mSQL = "SELECT AsGeoJSON(" + DBSetting.ZD_GEOMETRY + ")," + "* FROM " + mTableName + " WHERE " + condition ; //DBSetting.ZD_PRIMERY + " = " + primary;
			Stmt stmt = prepare(mSQL);
			GTRecord record = null;
			while (stmt.step()) {
				int size = new GTRecord().getFieldList().size() + 3; // ������������Ϣ��������ϢJSON��ʽ
				int index = 0;
				record = new GTRecord();
				if(index<size){ // ������ϢJSON��ʽ
					record.setGeometry(stmt.column_string(index));
					index++;
				}
				if(index<size){ // ����
					record.setId(stmt.column_int(index));
					index++;
				}
				ArrayList<String> valueList = record.getValueList();
				while(index<size-1){
					valueList.add(stmt.column_string(index));
					index++;
				}
				index++;
				if(index == size){
					record.mName = valueList.get(0);
					record.mCircuit = valueList.get(1);
					record.mHeight = valueList.get(2);
					record.mMaterial = valueList.get(3);
					record.mType = valueList.get(4);
					record.mPicture = valueList.get(5);
					record.mDefectID = valueList.get(6);
					record.mRemark = valueList.get(7);
					record.mIsEdit = valueList.get(8);
					list.add(record);
				};
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * ���¼�¼
	 * @return
	 */
	public boolean update(Point point, GTRecord record, int id){
		if(point!=null){
			String geo = GeometryTable.GeometryFromText(point);
			String mSQL = "UPDATE " + mTableName + " SET '" + GTRecord.ZD_NAME.NAME  + "'='"+ record.mName + "','" + 
					GTRecord.ZD_HEIGHT.NAME + "'='" + record.mHeight + "','" + 
					GTRecord.ZD_MATERIAL.NAME + "'='" + record.mMaterial + "','" + 
					GTRecord.ZD_TYPE.NAME + "'='" + record.mType + "','" + 
					GTRecord.ZD_PICTURE.NAME + "'='" + record.mPicture + "','" + 
					GTRecord.ZD_DEFECT_ID.NAME + "'='" + record.mDefectID + "','" + 
					GTRecord.ZD_ISEDIT.NAME + "'='" + record.mIsEdit + "','" + 
					GTRecord.ZD_REMARK.NAME + "'='" + record.mRemark + "'," + 
					DBSetting.ZD_GEOMETRY + "=" + geo + " WHERE " + DBSetting.ZD_PRIMERY + "=" + id;
			return execute(mSQL);
		}
		return false;
	} 
	
	/**
	 * �������ݣ�����Ӧ�����ݿ������µ����ݿ⣩
	 */
	public List<GTRecord> export(Database newDatabase, int circuitId){
		GTTable mGTTable = new GTTable(newDatabase);
		List<GTRecord> mList = getTowerList(circuitId);
		List<GTRecord> mNewList = new ArrayList<GTRecord>();
		if(mList.size()>0){
			for(GTRecord record:mList){
				mGTTable.add(record, (Point)record.getGeometry());
			}
			mNewList = mGTTable.selectTowers("1=1");
		}
		return mNewList;
	}
	/**
	 * ��������
	 * @param sourceDatabase �������ݿ�
	 * @param newCircuitId �������ݵ�����·ID
	 */
	public boolean inport(Database database, Database sourceDatabase, String newCircuitId, List<DefectRecord> mAddedDefects){// ����·���������豸��ȱ�� û���ض�˳��
		boolean result = true;
		GTTable mGTTable = new GTTable(sourceDatabase);
		DefectTable mDefectTable = new DefectTable(database);
		ArrayList<GTRecord> mSourceList = mGTTable.selectTowers("1=1");
		// �����ݼ��е�������·ID��Ϊָ����ID
		for(GTRecord record :mSourceList){
			String mDefectIDStr = "";
			String[] defectIDs = null;
			// �����豸����е�ȱ�ݱ���ֶ�    
			if(!"".equals(record.mDefectID)){
				if(record.mDefectID.contains("&")){
					defectIDs = record.mDefectID.split("&");
					for(String id : defectIDs){
						int index = Integer.valueOf(id)-1;
						if(index>=0 && index<mAddedDefects.size()){
							if(mDefectIDStr.equals("")) // ����mSourceDefectList �ҵ���Ӧ��index ����index ��mAddedDefects���ҵ�id
								mDefectIDStr = String.valueOf(mAddedDefects.get(index).getId());
							else
								mDefectIDStr += "&" + mAddedDefects.get(index).getId();
						}
					}
				}else{
					defectIDs = new String[]{record.mDefectID};
					int index = Integer.valueOf(defectIDs[0])-1;
					if(index>=0 && index<mAddedDefects.size()){
						mDefectIDStr = String.valueOf(mAddedDefects.get(index).getId());
					}
					
				}
			}
			GTRecord newRecord = new GTRecord(record.mName, newCircuitId, record.mHeight, record.mMaterial, 
					record.mType, record.mPicture, mDefectIDStr, record.mRemark, "��");
			int deviceId = add(newRecord, (Point)record.getGeometry());
			if(deviceId==-1) result = false;
			// ����ȱ�ݱ��е������豸�ֶ�
			if(defectIDs!=null){
				for(String oldDefectId : defectIDs){
					int index = Integer.valueOf(oldDefectId)-1;
					if(index>=0 && index<mAddedDefects.size()){
						String condition = "WHERE " + DBSetting.ZD_PRIMERY + " = " + mAddedDefects.get(index).getId();
						result = result && mDefectTable.update(DefectRecord.ZD_BELONG_ID.NAME, String.valueOf(deviceId), condition);
					}
				}
			}
		}
		return result;
	}
}
