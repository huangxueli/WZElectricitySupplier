package com.example.wzelectricitysupplier.database;

import java.util.ArrayList;
import java.util.List;

import jsqlite.Database;
import jsqlite.Exception;
import jsqlite.Stmt;

import com.esri.core.geometry.Point;
import com.example.wzelectricitysupplier.bean.DefectRecord;
import com.example.wzelectricitysupplier.bean.GBRecord;

public class GBTable extends GeometryTable {
	
	public String TAG = "GBTable";
	
	public final static String mTableName = "bar_type_variable";
	public final static String mGeometryType = "POINT";
	
	public GBTable(Database mDatabase) {
		this(mDatabase, mTableName, mGeometryType, new GBRecord().getFieldList());
	}
	public GBTable(Database mDatabase, String mTableName, String mGeometryType, ArrayList<Field> mFieldList) {
		super(mDatabase, mTableName, mGeometryType, mFieldList);
		CreateTable();
	}
	
	public List<GBRecord> getBarTypeVariableList(int circuitId) {
		String condition = GBRecord.ZD_CIRCUIT.NAME + "=" + circuitId;
		List<GBRecord> mRecordList = selectBarTypeVariables(condition);
		return mRecordList;
	}
	
	/**
	 * ����������ȡһ����¼
	 * @param primary
	 * @return
	 */
	public synchronized GBRecord selectBarTypeVariable(int primary){
		GBRecord record = null;
		String condition = DBSetting.ZD_PRIMERY + " = " + primary;
		ArrayList<GBRecord> list = selectBarTypeVariables(condition);
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
	public synchronized ArrayList<GBRecord> selectBarTypeVariables(String condition){
		ArrayList<GBRecord> list = new ArrayList<GBRecord>();
		try{
			String mSQL = "SELECT AsGeoJSON(" + DBSetting.ZD_GEOMETRY + ")," + "* FROM " + mTableName + " WHERE " + condition ; //DBSetting.ZD_PRIMERY + " = " + primary;
			Stmt stmt = prepare(mSQL);
			GBRecord record = null;
			while (stmt.step()) {
				int size = new GBRecord().getFieldList().size() + 3; // ������������Ϣ��������ϢJSON��ʽ
				int index = 0;
				record = new GBRecord();
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
					record.mCapacity = valueList.get(2);
					record.mPicture = valueList.get(3);
					record.mDefectID = valueList.get(4);
					record.mRemark = valueList.get(5);
					record.mIsEdit = valueList.get(6);
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
	public boolean update(Point point, GBRecord record, int id){
		if(point!=null){
			String geo = GeometryTable.GeometryFromText(point);
			String mSQL = "UPDATE " + mTableName + " SET '" + GBRecord.ZD_NAME.NAME  + "'='"+ record.mName + "','" + 
					GBRecord.ZD_CAPACITY.NAME + "'='" + record.mCapacity + "','" + 
					GBRecord.ZD_PICTURE.NAME + "'='" + record.mPicture + "','" + 
					GBRecord.ZD_DEFECT_ID.NAME + "'='" + record.mDefectID + "','" + 
					GBRecord.ZD_ISEDIT.NAME + "'='" + record.mIsEdit + "','" + 
					GBRecord.ZD_REMARK.NAME + "'='" + record.mRemark + "'," + 
					DBSetting.ZD_GEOMETRY + "=" + geo + " WHERE " + DBSetting.ZD_PRIMERY + "=" + id;
			return execute(mSQL);
		}
		return false;
	} 
	
	/**
	 * �������ݣ�����Ӧ�����ݿ������µ����ݿ⣩
	 */
	public List<GBRecord> export(Database newDatabase, int circuitId){
		GBTable mGBTable = new GBTable(newDatabase);
		List<GBRecord> mList = getBarTypeVariableList(circuitId);
		List<GBRecord> mNewList = new ArrayList<GBRecord>();
		if(mList.size()>0){
			for(GBRecord record:mList){
				mGBTable.add(record, (Point)record.getGeometry());
			}
			mNewList = mGBTable.selectBarTypeVariables("1=1");
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
		GBTable mGBTable = new GBTable(sourceDatabase);
		DefectTable mDefectTable = new DefectTable(database);
		ArrayList<GBRecord> mSourceList = mGBTable.selectBarTypeVariables("1=1");
		// �����ݼ��е�������·ID��Ϊָ����ID
		for(GBRecord record :mSourceList){
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
			GBRecord newRecord = new GBRecord(record.mName, newCircuitId, record.mCapacity, record.mPicture, mDefectIDStr, record.mRemark, "��");
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
