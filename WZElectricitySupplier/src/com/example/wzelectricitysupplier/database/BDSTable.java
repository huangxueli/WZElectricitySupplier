package com.example.wzelectricitysupplier.database;

import java.util.ArrayList;
import java.util.List;

import jsqlite.Database;
import jsqlite.Exception;
import jsqlite.Stmt;

import com.esri.core.geometry.Point;
import com.example.wzelectricitysupplier.bean.BDSRecord;
import com.example.wzelectricitysupplier.bean.DefectRecord;

public class BDSTable extends GeometryTable {
	
	public final static String TAG = "BDSTable";
	
	public final static String mTableName = "substation";
	public final static String mGeometryType = "POINT";
	
	public BDSTable(Database mDatabase) {
		this(mDatabase, mTableName, mGeometryType, new BDSRecord().getFieldList());
		super.TAG = TAG;
	}
	public BDSTable(Database mDatabase, String mTableName, String mGeometryType, ArrayList<Field> mFieldList) {
		super(mDatabase, mTableName, mGeometryType, mFieldList);
		CreateTable();
	}
	
	public List<BDSRecord> getSubstationList(int circuitId) {
		String condition = BDSRecord.ZD_CIRCUIT.NAME + "=" + circuitId;
		List<BDSRecord> mRecordList = selectSubstations(condition);
		return mRecordList;
	}
	
	/**
	 * ����������ȡһ����¼
	 * @param primary
	 * @return
	 */
	public synchronized BDSRecord selectSubstation(int primary){
		BDSRecord record = null;
		String condition = DBSetting.ZD_PRIMERY + " = " + primary;
		ArrayList<BDSRecord> list = selectSubstations(condition);
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
	public synchronized ArrayList<BDSRecord> selectSubstations(String condition){
		ArrayList<BDSRecord> list = new ArrayList<BDSRecord>();
		try{
			String mSQL = "SELECT AsGeoJSON(" + DBSetting.ZD_GEOMETRY + ")," + "* FROM " + mTableName + " WHERE " + condition ; 
			Stmt stmt = prepare(mSQL);
			BDSRecord record = null;
			while (stmt.step()) {
				int size = new BDSRecord().getFieldList().size() + 3; // ������������Ϣ��������ϢJSON��ʽ
				int index = 0;
				record = new BDSRecord();
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
					record.mPicture = valueList.get(2);
					record.mDefectID = valueList.get(3);
					record.mRemark = valueList.get(4);
					record.mIsEdit = valueList.get(5);
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
	public boolean update(Point point, BDSRecord record, int id){
		if(point!=null){
			String geo = GeometryTable.GeometryFromText(point);
			String mSQL = "UPDATE " + mTableName + " SET '" + BDSRecord.ZD_NAME.NAME  + "'='"+ record.mName + "','" + 
					BDSRecord.ZD_PICTURE.NAME + "'='" + record.mPicture + "','" + 
					BDSRecord.ZD_DEFECT_ID.NAME + "'='" + record.mDefectID + "','" + 
					BDSRecord.ZD_ISEDIT.NAME + "'='" + record.mIsEdit + "','" + 
					BDSRecord.ZD_REMARK.NAME + "'='" + record.mRemark + "'," + 
					DBSetting.ZD_GEOMETRY + "=" + geo + " WHERE " + DBSetting.ZD_PRIMERY + "=" + id;
			return execute(mSQL);
		}
		return false;
	} 
	
	/**
	 * �������ݣ�����Ӧ�����ݿ������µ����ݿ⣩
	 * @param newDatabase �������ݿ�
	 * @param circuitId ��·ID
	 */
	public List<BDSRecord> export(Database newDatabase, int circuitId){
		BDSTable mBDSTable = new BDSTable(newDatabase);
		List<BDSRecord> mList = getSubstationList(circuitId);
		List<BDSRecord> mNewList = new ArrayList<BDSRecord>();
		if(mList.size()>0){
			for(BDSRecord record:mList){
				mBDSTable.add(record, (Point)record.getGeometry());
			}
			mNewList = mBDSTable.selectSubstations("1=1");
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
		BDSTable mBDSTable = new BDSTable(sourceDatabase);
		DefectTable mDefectTable = new DefectTable(database);
		ArrayList<BDSRecord> mSourceList = mBDSTable.selectSubstations("1=1");
		// �����ݼ��е�������·ID��Ϊָ����ID
		for(BDSRecord record :mSourceList){
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
			BDSRecord newRecord = new BDSRecord(record.mName, newCircuitId, record.mPicture, mDefectIDStr, record.mRemark, "��");
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
