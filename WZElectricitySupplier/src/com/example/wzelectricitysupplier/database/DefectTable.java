package com.example.wzelectricitysupplier.database;

import java.util.ArrayList;
import java.util.List;

import jsqlite.Database;
import jsqlite.Exception;
import jsqlite.Stmt;

import com.example.wzelectricitysupplier.bean.BDSRecord;
import com.example.wzelectricitysupplier.bean.DXRecord;
import com.example.wzelectricitysupplier.bean.DefectRecord;
import com.example.wzelectricitysupplier.bean.DeviceRecord;
import com.example.wzelectricitysupplier.bean.GBRecord;
import com.example.wzelectricitysupplier.bean.GLRecord;
import com.example.wzelectricitysupplier.bean.GTRecord;
import com.example.wzelectricitysupplier.bean.HWRecord;
import com.example.wzelectricitysupplier.bean.LKRecord;
import com.example.wzelectricitysupplier.bean.PDRecord;
import com.example.wzelectricitysupplier.bean.SwitchRecord;
import com.example.wzelectricitysupplier.bean.XBRecord;
import com.example.wzelectricitysupplier.setting.AppUtil.DeviceType;

public class DefectTable extends NormalTable {

	public final String TAG = "DefectTable";

	// 表名
	private static String mTableName = "defect";;

	public DefectTable(Database mDatabase) {
		super(mDatabase, mTableName, new DefectRecord().getFieldList());
		CreateTable();
	}
	public synchronized ArrayList<DefectRecord> SelectDefects(int belongId, String belongDevice) {
		return SelectDefects("WHERE " + DefectRecord.ZD_BELONG_ID.NAME + " = " + belongId + " AND " +
				DefectRecord.ZD_BELONG_DEVICE.NAME + " = '" + belongDevice + "'")  ;
	}
	
	public synchronized ArrayList<DefectRecord> SelectDefects(String condition) {
		ArrayList<DefectRecord> list = new ArrayList<>();
		try {
			String mSQL = "SELECT * FROM " + mTableName + " " + condition;
			Stmt stmt = prepare(mSQL);
			DefectRecord record = null;
			while (stmt.step()) {
				int size = new DefectRecord().getFieldList().size() + 1; // 主键
				int index = 0;
				record = new DefectRecord();
				if (index < size) {
					record.setId(stmt.column_int(index));
					index++;
				}
				ArrayList<String> valueList = record.getValueList();
				while (index < size) {
					valueList.add(stmt.column_string(index));
					index++;
				}
				if (index == size) {
					record.mName = valueList.get(0);
					record.mBelongId = valueList.get(1);
					record.mBelongDevice = valueList.get(2);
					record.mPicture = valueList.get(3);
					record.mType = valueList.get(4);
					record.mDescribe = valueList.get(5);
					record.mIsEdit = valueList.get(6);
					list.add(record);
				}
				;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public synchronized DefectRecord SelectDefect(int primary) {
		DefectRecord record = null;
		String condition = "WHERE " + DBSetting.ZD_PRIMERY + " = " + primary;
		ArrayList<DefectRecord> list = SelectDefects(condition);
		if (!list.isEmpty()) {
			record = list.get(0);
		}
		return record;
	}

	public boolean update(DefectRecord record, int id) {
		String mSQL = "UPDATE " + mTableName + " SET '"+ 
				DefectRecord.ZD_NAME.NAME + "'='" + record.mName + "','" + 
				DefectRecord.ZD_PICTURE.NAME + "'='" + record.mPicture + "','" + 
				DefectRecord.ZD_TYPE.NAME + "'='" + record.mType + "','" + 
				DefectRecord.ZD_ISEDIT.NAME + "'='" + record.mIsEdit + "','" + 
				DefectRecord.ZD_DESCRIBE.NAME + "'='" + record.mDescribe + "'" + 
				" WHERE " + DBSetting.ZD_PRIMERY + "=" + id;
		return execute(mSQL);
	}
	
	/**
	 * 导出数据（将对应的数据拷贝到新的数据库）
	 * @param newDatabase 导出数据库
	 */
	public void export(Database newDatabase, List<DeviceRecord> mNewDeviceList,  List<DeviceRecord> mOldDeviceList){
		DefectTable mDefectTable = new DefectTable(newDatabase);
		GeometryTable table = null;
		for(DeviceRecord deviceRecord:mOldDeviceList){
			String type = deviceRecord.getDeviceType();
			String name = deviceRecord.mName;
			String newBelongId = "";
			for(DeviceRecord newDeviceRecord : mNewDeviceList){
				if(newDeviceRecord.mName.equals(name)){
					newBelongId = String.valueOf(newDeviceRecord.getId());
					break;
				}
			}
			ArrayList<DefectRecord> defectList = SelectDefects(deviceRecord.getId(), type);
			String defectidStr = "";
			for(DefectRecord record:defectList){
				DefectRecord newRecord = new DefectRecord(record.mName, newBelongId, record.mBelongDevice, record.mPicture, record.mType, record.mDescribe, record.mIsEdit);
				int defectid = mDefectTable.add(newRecord);
				if(defectidStr.equals("")) 
					defectidStr += defectid;
				else 
					defectidStr += "&" +defectid;
			}
			String condition = "";
			switch(type){
			case DeviceType.DeviceBDS:
				table = new BDSTable(newDatabase); 
				condition = " WHERE " + BDSRecord.ZD_NAME.NAME + " = '" + name + "'";
				break;
			case DeviceType.DeviceGT:
				table = new GTTable(newDatabase);
				condition = " WHERE " + GTRecord.ZD_NAME.NAME + " = '" + name + "'";
				break;
			case DeviceType.DeviceSwitch:
				table = new SwitchTable(newDatabase);
				condition = " WHERE " + SwitchRecord.ZD_NAME.NAME + " = '" + name + "'";
				break;
			case DeviceType.DeviceGL:
				table = new GLTable(newDatabase);
				condition = " WHERE " + GLRecord.ZD_NAME.NAME + " = '" + name + "'";
				break;
			case DeviceType.DeviceLK:
				table = new LKTable(newDatabase);
				condition = " WHERE " + LKRecord.ZD_NAME.NAME + " = '" + name + "'";
				break;
			case DeviceType.DevicePD:
				table = new PDTable(newDatabase);
				condition = " WHERE " + PDRecord.ZD_NAME.NAME + " = '" + name + "'";
				break;
			case DeviceType.DeviceKG:
				table = new KBTable(newDatabase);
				condition = " WHERE " + GBRecord.ZD_NAME.NAME + " = '" + name + "'";
				break;
			case DeviceType.DeviceHW:
				table = new HWTable(newDatabase);
				condition = " WHERE " + HWRecord.ZD_NAME.NAME + " = '" + name + "'";
				break;
			case DeviceType.DeviceGB:
				table = new GBTable(newDatabase);
				condition = " WHERE " + GBRecord.ZD_NAME.NAME + " = '" + name + "'";
				break;
			case DeviceType.DeviceXB:
				table = new XBTable(newDatabase);
				condition = " WHERE " + XBRecord.ZD_NAME.NAME + " = '" + name + "'";
				break;
			case DeviceType.Line:
				table = new DXTable(newDatabase);
				condition = " WHERE " + DXRecord.ZD_NAME.NAME + " = '" + name + "'";
				break;
			}
			table.update(DeviceRecord.ZD_DEFECT_ID.NAME, defectidStr, condition); // 改变设备表中的缺陷ID
		}
	}
	
	/**
	 * 导入数据
	 * @param sourceDatabase 导入数据库
	 */
	public ArrayList<DefectRecord> inport(Database sourceDatabase){
		DefectTable mDefectTable = new DefectTable(sourceDatabase);
		ArrayList<DefectRecord> mSourceList = mDefectTable.SelectDefects("");
		ArrayList<DefectRecord> mAddedList = new ArrayList<>();
		// 将数据集中的所属线路ID改为指定的ID
		for(DefectRecord record :mSourceList){
			DefectRecord newRecord = new DefectRecord(record.mName, record.mBelongId, record.mBelongDevice, record.mPicture, record.mType, record.mDescribe, "否");
			int id = add(newRecord);
			newRecord.setId(id);
			mAddedList.add(newRecord);
		}
		return mAddedList;
	}
}
