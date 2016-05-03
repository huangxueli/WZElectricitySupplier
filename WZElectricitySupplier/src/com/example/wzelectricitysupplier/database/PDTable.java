package com.example.wzelectricitysupplier.database;

import java.util.ArrayList;
import java.util.List;

import jsqlite.Database;
import jsqlite.Exception;
import jsqlite.Stmt;

import com.esri.core.geometry.Point;
import com.example.wzelectricitysupplier.bean.DefectRecord;
import com.example.wzelectricitysupplier.bean.PDRecord;

public class PDTable extends GeometryTable {
	
	public String TAG = "PDTable";
	
	public final static String mTableName = "switching_room";
	public final static String mGeometryType = "POINT";
	
	public PDTable(Database mDatabase) {
		this(mDatabase, mTableName, mGeometryType, new PDRecord().getFieldList());
	}
	public PDTable(Database mDatabase, String mTableName, String mGeometryType, ArrayList<Field> mFieldList) {
		super(mDatabase, mTableName, mGeometryType, mFieldList);
		CreateTable();
	}
	
	public List<PDRecord> getSwitchingRoomList(int circuitId) {
		String condition = PDRecord.ZD_CIRCUIT.NAME + "=" + circuitId;
		List<PDRecord> mRecordList = selectSwitchingRooms(condition);
		return mRecordList;
	}
	
	/**
	 * ����������ȡһ����¼
	 * @param primary
	 * @return
	 */
	public synchronized PDRecord selectSwitchingRoom(int primary){
		PDRecord record = null;
		String condition = DBSetting.ZD_PRIMERY + " = " + primary;
		ArrayList<PDRecord> list = selectSwitchingRooms(condition);
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
	public synchronized ArrayList<PDRecord> selectSwitchingRooms(String condition){
		ArrayList<PDRecord> list = new ArrayList<PDRecord>();
		try{
			String mSQL = "SELECT AsGeoJSON(" + DBSetting.ZD_GEOMETRY + ")," + "* FROM " + mTableName + " WHERE " + condition ; //DBSetting.ZD_PRIMERY + " = " + primary;
			Stmt stmt = prepare(mSQL);
			PDRecord record = null;
			while (stmt.step()) {
				int size = new PDRecord().getFieldList().size() + 3; // ������������Ϣ��������ϢJSON��ʽ
				int index = 0;
				record = new PDRecord();
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
					record.mNum = valueList.get(3);
					record.mMedia = valueList.get(4);
					record.mBackupIntervalNum = valueList.get(5);
					record.mOutIntervalNum = valueList.get(6);
					record.mInIntervalNum = valueList.get(7);
					record.mPicture = valueList.get(8);
					record.mDefectID = valueList.get(9);
					record.mRemark = valueList.get(10);
					record.mIsEdit = valueList.get(11);
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
	public boolean update(Point point, PDRecord record, int id){
		if(point!=null){
			String geo = GeometryTable.GeometryFromText(point);
			String mSQL = "UPDATE " + mTableName + " SET '" + PDRecord.ZD_NAME.NAME  + "'='"+ record.mName + "','" + 
					PDRecord.ZD_CAPACITY.NAME + "'='" + record.mCapacity + "','" + 
					PDRecord.ZD_NUM.NAME + "'='" + record.mNum + "','" + 
					PDRecord.ZD_MEDIA.NAME + "'='" + record.mMedia + "','" + 
					PDRecord.ZD_BACKUP_INTERVAL_NUM.NAME + "'='" + record.mBackupIntervalNum + "','" + 
					PDRecord.ZD_OUT_INTERVAL_NUM.NAME + "'='" + record.mOutIntervalNum + "','" + 
					PDRecord.ZD_IN_INTERVAL_NUM.NAME + "'='" + record.mInIntervalNum + "','" + 
					PDRecord.ZD_PICTURE.NAME + "'='" + record.mPicture + "','" + 
					PDRecord.ZD_DEFECT_ID.NAME + "'='" + record.mDefectID + "','" + 
					PDRecord.ZD_ISEDIT.NAME + "'='" + record.mIsEdit + "','" + 
					PDRecord.ZD_REMARK.NAME + "'='" + record.mRemark + "'," + 
					DBSetting.ZD_GEOMETRY + "=" + geo + " WHERE " + DBSetting.ZD_PRIMERY + "=" + id;
			return execute(mSQL);
		}
		return false;
	} 
	
	/**
	 * �������ݣ�����Ӧ�����ݿ������µ����ݿ⣩
	 */
	public List<PDRecord> export(Database newDatabase, int circuitId){
		PDTable mPDTable = new PDTable(newDatabase);
		List<PDRecord> mList = getSwitchingRoomList(circuitId);
		List<PDRecord> mNewList = new ArrayList<PDRecord>();
		if(mList.size()>0){
			for(PDRecord record:mList){
				mPDTable.add(record, (Point)record.getGeometry());
			}
			mNewList = mPDTable.selectSwitchingRooms("1=1");
		}
		return mNewList;
	}
	/**
	 * ��������
	 * @param sourceDatabase �������ݿ�
	 * @param newCircuitId �������ݵ�����·ID
	 */
	public ArrayList<PDRecord> inport(Database sourceDatabase, String newCircuitId){
		PDTable mPDTable = new PDTable(sourceDatabase);
		ArrayList<PDRecord> mSourceList = mPDTable.selectSwitchingRooms("1=1");
		ArrayList<PDRecord> mAddedList = new ArrayList<>();
		// �����ݼ��е�������·ID��Ϊָ����ID
		for(PDRecord record :mSourceList){
			PDRecord newRecord = new PDRecord(record.mName, newCircuitId, record.mCapacity, record.mNum, record.mMedia, 
					record.mBackupIntervalNum, record.mOutIntervalNum, record.mInIntervalNum, record.mPicture, 
					record.mDefectID, record.mRemark, "��");
			int deviceId = add(newRecord, (Point)record.getGeometry());
			newRecord.setId(deviceId);
			mAddedList.add(newRecord);
		}
		return mAddedList;
	}
	/**
	 * ��������
	 * @param sourceDatabase �������ݿ�
	 * @param newCircuitId �������ݵ�����·ID
	 */
	public boolean inport(Database database, Database sourceDatabase, String newCircuitId, List<DefectRecord> mAddedDefects){// ����·���������豸��ȱ�� û���ض�˳��
		boolean result = true;
		PDTable mPDTable = new PDTable(sourceDatabase);
		DefectTable mDefectTable = new DefectTable(database);
		ArrayList<PDRecord> mSourceList = mPDTable.selectSwitchingRooms("1=1");
		// �����ݼ��е�������·ID��Ϊָ����ID
		for(PDRecord record :mSourceList){
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
			PDRecord newRecord = new PDRecord(record.mName, newCircuitId, record.mCapacity, record.mNum, record.mMedia, 
					record.mBackupIntervalNum, record.mOutIntervalNum, record.mInIntervalNum, record.mPicture, 
					mDefectIDStr, record.mRemark, "��");
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
