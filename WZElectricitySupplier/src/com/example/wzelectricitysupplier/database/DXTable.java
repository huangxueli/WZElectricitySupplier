package com.example.wzelectricitysupplier.database;

import java.util.ArrayList;
import java.util.List;

import jsqlite.Database;
import jsqlite.Exception;
import jsqlite.Stmt;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Geometry.Type;
import com.esri.core.geometry.MultiPath;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.example.wzelectricitysupplier.bean.DXRecord;
import com.example.wzelectricitysupplier.bean.DefectRecord;
import com.example.wzelectricitysupplier.bean.DeviceRecord;

public class DXTable extends GeometryTable {
	
	public String TAG = "DXTable";
	
	public final static String mTableName = "electric_wire";
	public final static String mGeometryType = "LINESTRING";
	
	public DXTable(Database mDatabase) {
		this(mDatabase, mTableName, mGeometryType, new DXRecord().getFieldList());
	}
	public DXTable(Database mDatabase, String mTableName, String mGeometryType, ArrayList<Field> mFieldList) {
		super(mDatabase, mTableName, mGeometryType, mFieldList);
		CreateTable();
	}
	
	public List<DXRecord> getElectricWireList(int circuitId) {
		String condition = DXRecord.ZD_CIRCUIT.NAME + "=" + circuitId;
		List<DXRecord> mRecordList = selectElectricWires(condition);
		return mRecordList;
	}
	
	/**
	 * ����������ȡһ����¼
	 * @param primary
	 * @return
	 */
	public synchronized DXRecord selectElectricWire(int primary){
		DXRecord record = null;
		String condition = DBSetting.ZD_PRIMERY + " = " + primary;
		ArrayList<DXRecord> list = selectElectricWires(condition);
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
	public synchronized ArrayList<DXRecord> selectElectricWires(String condition){
		ArrayList<DXRecord> list = new ArrayList<DXRecord>();
		try{
			String mSQL = "SELECT AsGeoJSON(" + DBSetting.ZD_GEOMETRY + ")," + "* FROM " + mTableName + " WHERE " + condition ;
			Stmt stmt = prepare(mSQL);
			DXRecord record = null;
			while (stmt.step()) {
				int size = new DXRecord().getFieldList().size() + 3; // ������������Ϣ��������ϢJSON��ʽ
				int index = 0;
				record = new DXRecord();
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
					record.mType = valueList.get(3);
					record.mModel = valueList.get(4);
					record.mNum = valueList.get(5);
					record.mDeviceName1 = valueList.get(6);
					record.mDeviceName2 = valueList.get(7);
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
	public boolean update(Geometry geometry, DXRecord record, int id){
		if(geometry!=null){
			String geo = null;
			Type type = geometry.getType();
			if(type == Type.POINT)
				geo = GeometryTable.GeometryFromText((Point)geometry);
			if(type == Type.POLYLINE)
				geo = GeometryTable.GeometryFromText((Polyline)geometry);
			String mSQL = "UPDATE " + mTableName + " SET '" + DXRecord.ZD_NAME.NAME  + "'='"+ record.mName + "','" + 
					DXRecord.ZD_HEIGHT.NAME + "'='" + record.mHeight + "','" + 
					DXRecord.ZD_TYPE.NAME + "'='" + record.mType + "','" + 
					DXRecord.ZD_MODED.NAME + "'='" + record.mModel + "','" + 
					DXRecord.ZD_NUM.NAME + "'='" + record.mNum + "','" + 
					DXRecord.ZD_DEVICENAME1.NAME + "'='" + record.mDeviceName1 + "','" + 
					DXRecord.ZD_DEVICENAME2.NAME + "'='" + record.mDeviceName2 + "','" + 
					DXRecord.ZD_PICTURE.NAME + "'='" + record.mPicture + "','" + 
					DXRecord.ZD_DEFECT_ID.NAME + "'='" + record.mDefectID + "','" + 
					DXRecord.ZD_ISEDIT.NAME + "'='" + record.mIsEdit + "','" + 
					DXRecord.ZD_REMARK.NAME + "'='" + record.mRemark + "'," + 
					DBSetting.ZD_GEOMETRY + "=" + geo + " WHERE " + DBSetting.ZD_PRIMERY + "=" + id;
			return execute(mSQL);
		}
		return false;
	} 
	
	/**
	 * �����ߵļ������ԣ������յ�ı䣩
	 * @param mCircuitID
	 * @param mDeviceRecord �����յ�
	 * @param id
	 * @return
	 */
	public boolean updateGeometry(String mCircuitID, DeviceRecord mDeviceRecord){
		boolean result = true;
		ArrayList<DXRecord> mDXList1 = selectElectricWires(DXRecord.ZD_CIRCUIT.NAME + "='" + mCircuitID +
				"' AND " + DXRecord.ZD_DEVICENAME1.NAME + "='" + mDeviceRecord.mName + "'");
		ArrayList<DXRecord> mDXList2 = selectElectricWires(DXRecord.ZD_CIRCUIT.NAME + "='" + mCircuitID +
				"' AND " + DXRecord.ZD_DEVICENAME2.NAME + "='" + mDeviceRecord.mName + "'");
		for(DXRecord record :mDXList1){
			Point point1 = (Point)mDeviceRecord.getGeometry();
			Point point2 = record.getEndPoint();
			Polyline line = new Polyline();
			line.startPath(point1);
			line.lineTo(point2);
			result = result && super.updateGeometry(line, record.getId());
		}
		for(DXRecord record :mDXList2){
			Point point1 = record.getStartPoint();
			Point point2 = (Point)mDeviceRecord.getGeometry();
			Polyline line = new Polyline();
			line.startPath(point1);
			line.lineTo(point2);
			result = result && super.updateGeometry(line, record.getId());
		}
		return result;
	}
	
	/**
	 * �������ݣ�����Ӧ�����ݿ������µ����ݿ⣩
	 */
	public List<DXRecord> export(Database newDatabase, int circuitId){
		DXTable mDXTable = new DXTable(newDatabase);
		List<DXRecord> mList = getElectricWireList(circuitId);
		List<DXRecord> mNewList = new ArrayList<DXRecord>();
		if(mList.size()>0){
			for(DXRecord record:mList){
				mDXTable.add(record, (MultiPath)record.getGeometry());
			}
			mNewList = mDXTable.selectElectricWires("1=1");
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
		DXTable mDXTable = new DXTable(sourceDatabase);
		DefectTable mDefectTable = new DefectTable(database);
		ArrayList<DXRecord> mSourceList = mDXTable.selectElectricWires("1=1");
		// �����ݼ��е�������·ID��Ϊָ����ID
		for(DXRecord record :mSourceList){
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
			DXRecord newRecord = new DXRecord(record.mName, newCircuitId, record.mHeight, record.mType, 
					record.mModel, record.mNum, record.mDeviceName1, record.mDeviceName2, record.mPicture, 
					mDefectIDStr, record.mRemark, "��");
			int deviceId = add(newRecord, (MultiPath)record.getGeometry());
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
