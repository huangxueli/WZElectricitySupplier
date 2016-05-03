package com.example.wzelectricitysupplier.setting;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jsqlite.Database;

import com.esri.core.geometry.Polyline;
import com.example.wzelectricitysupplier.bean.DXRecord;
import com.example.wzelectricitysupplier.bean.DefectRecord;
import com.example.wzelectricitysupplier.bean.DeviceRecord;
import com.example.wzelectricitysupplier.database.BDSTable;
import com.example.wzelectricitysupplier.database.DBSetting;
import com.example.wzelectricitysupplier.database.DXTable;
import com.example.wzelectricitysupplier.database.DefectTable;
import com.example.wzelectricitysupplier.database.GBTable;
import com.example.wzelectricitysupplier.database.GLTable;
import com.example.wzelectricitysupplier.database.GTTable;
import com.example.wzelectricitysupplier.database.GeometryTable;
import com.example.wzelectricitysupplier.database.HWTable;
import com.example.wzelectricitysupplier.database.KBTable;
import com.example.wzelectricitysupplier.database.LKTable;
import com.example.wzelectricitysupplier.database.PDTable;
import com.example.wzelectricitysupplier.database.SwitchTable;
import com.example.wzelectricitysupplier.database.XBTable;
import com.example.wzelectricitysupplier.fragment.MainFragment;

public class AppUtil {
	
	public class DeviceType{
		public static final String DeviceBDS = "变电站";
		public static final String DeviceGT = "杆塔";
		public static final String DeviceSwitch = "开关";
		public static final String DeviceGL = "隔离开关";
		public static final String DeviceLK = "令克";
		public static final String DevicePD = "配电室";
		public static final String DeviceKG = "开闭所";
		public static final String DeviceHW = "环网单元";
		public static final String DeviceGB = "杆式变";
		public static final String DeviceXB = "箱变";
		public static final String Line = "电线";
	}
	/**
	 * 根据设备类型获取设备记录（包括电线）
	 * @param mMainFragment
	 * @param id
	 * @param type
	 * @return
	 */
	public static DeviceRecord getDeviceRecordByDefectRecord(MainFragment mMainFragment, int id, String type){
		switch(type){
		case "DeviceBDS":
		case DeviceType.DeviceBDS:
			return mMainFragment.getBDSTable().selectSubstation(id);
		case "DeviceGT":
		case DeviceType.DeviceGT:
			return mMainFragment.getGTTable().selectTower(id);
		case "DeviceSwitch":
		case DeviceType.DeviceSwitch:
			return mMainFragment.getSwitchTable().selectSwitch(id);
		case "DeviceGL":
		case DeviceType.DeviceGL:
			return mMainFragment.getGLTable().selectDisconnectore(id);
		case "DeviceLK":
		case DeviceType.DeviceLK:
			return mMainFragment.getLKTable().selectLineConnector(id);
		case "DevicePD":
		case DeviceType.DevicePD:
			return  mMainFragment.getPDTable().selectSwitchingRoom(id);
		case "DeviceKG":
		case DeviceType.DeviceKG:
			return mMainFragment.getKBTable().selectSwitchingStation(id);
		case "DeviceHW":
		case DeviceType.DeviceHW:
			return mMainFragment.getHWTable().selectRingMainUnit(id);
		case "DeviceGB":
		case DeviceType.DeviceGB:
			return mMainFragment.getGBTable().selectBarTypeVariable(id);
		case "DeviceXB":
		case DeviceType.DeviceXB:
			return mMainFragment.getXBTable().selectBoxChange(id);
		case "LineFour":
		case "LineTwo":
		case "LineOne":
		case DeviceType.Line:
			return mMainFragment.getDXTable().selectElectricWire(id);
		}
		return null;
	}
	/**
	 * 根据设备类型获取设备记录（包括电线）
	 * @param mMainFragment
	 * @param id
	 * @param type
	 * @return
	 */
	public static DeviceRecord getDeviceRecordByDefectRecord(Database mDb, int id, String type){
		switch(type){
		case "DeviceBDS":
		case DeviceType.DeviceBDS:
			return new BDSTable(mDb).selectSubstation(id);
		case "DeviceGT":
		case DeviceType.DeviceGT:
			return new GTTable(mDb).selectTower(id);
		case "DeviceSwitch":
		case DeviceType.DeviceSwitch:
			return new SwitchTable(mDb).selectSwitch(id);
		case "DeviceGL":
		case DeviceType.DeviceGL:
			return new GLTable(mDb).selectDisconnectore(id);
		case "DeviceLK":
		case DeviceType.DeviceLK:
			return new LKTable(mDb).selectLineConnector(id);
		case "DevicePD":
		case DeviceType.DevicePD:
			return  new PDTable(mDb).selectSwitchingRoom(id);
		case "DeviceKG":
		case DeviceType.DeviceKG:
			return new KBTable(mDb).selectSwitchingStation(id);
		case "DeviceHW":
		case DeviceType.DeviceHW:
			return new HWTable(mDb).selectRingMainUnit(id);
		case "DeviceGB":
		case DeviceType.DeviceGB:
			return new GBTable(mDb).selectBarTypeVariable(id);
		case "DeviceXB":
		case DeviceType.DeviceXB:
			return new XBTable(mDb).selectBoxChange(id);
		case "LineFour":
		case "LineTwo":
		case "LineOne":
		case DeviceType.Line:
			return new DXTable(mDb).selectElectricWire(id);
		}
		return null;
	}
	/**
	 * 根据设备类型获取设备表
	 * @param mMainFragment
	 * @param type
	 * @return
	 */
	public static GeometryTable getTableByType(MainFragment mMainFragment, String type){
		switch(type){
		case "DeviceBDS":
		case DeviceType.DeviceBDS:
			return mMainFragment.getBDSTable();
		case "DeviceGT":
		case DeviceType.DeviceGT:
			return mMainFragment.getGTTable();
		case "DeviceSwitch":
		case DeviceType.DeviceSwitch:
			return mMainFragment.getSwitchTable();
		case "DeviceGL":
		case DeviceType.DeviceGL:
			return mMainFragment.getGLTable();
		case "DeviceLK":
		case DeviceType.DeviceLK:
			return mMainFragment.getLKTable();
		case "DevicePD":
		case DeviceType.DevicePD:
			return  mMainFragment.getPDTable();
		case "DeviceKG":
		case DeviceType.DeviceKG:
			return mMainFragment.getKBTable();
		case "DeviceHW":
		case DeviceType.DeviceHW:
			return mMainFragment.getHWTable();
		case "DeviceGB":
		case DeviceType.DeviceGB:
			return mMainFragment.getGBTable();
		case "DeviceXB":
		case DeviceType.DeviceXB:
			return mMainFragment.getXBTable();
		case "LineFour":
		case "LineTwo":
		case "LineOne":
		case DeviceType.Line:
			return mMainFragment.getDXTable();
		}
		return null;
	}
	/**
	 * 更新电线表、缺陷表字段(电线名称、电线照片、起点或终点设备、缺陷照片)，更新本地普通照片和缺陷照片
	 * @param mCircuitID 
	 * @param newname 
	 * @param oldname
	 * @return
	 */
	public static boolean updateDXField(MainFragment mMainFragment, String mCircuitID, String newDevicename, String oldDevicename){
		boolean result = true;
		DXTable mDXTable = mMainFragment.getDXTable();
		List<DXRecord> mList = mDXTable.getElectricWireList(Integer.valueOf(mCircuitID));
		for(DXRecord record :mList){
			int id = record.getId();
			Polyline geo = (Polyline)record.getGeometry();
			if(record.mDeviceName1.equals(oldDevicename)){
				int index1 = record.mName.indexOf("[");
				int index2 = record.mName.indexOf("]");
				String name1 = record.mName.substring(index1+1, index2);
				String newname1 = newDevicename.substring(newDevicename.indexOf(" ")+1);
				String newdxname = record.mName.replace(name1, newname1); // 新电线名称
				
				// 历史照片重命名
				List<String> pictureList = record.getPictureList();
				for(int i=0; i<pictureList.size(); i++){ 
					String picname = pictureList.get(i);
					File pictureFile = new File(Util.getPictureDirRootPath(), picname);
					String oldChar = picname.split("_")[1];
					picname = picname.replace(oldChar, newdxname);
					pictureList.set(i, picname);
					if(pictureFile.exists()) {
						pictureFile.renameTo(new File(Util.getPictureDirRootPath() + picname));
					}
				}
				String pictureStr = "";
				for(String picture :pictureList){
					if(pictureStr.equals("")) pictureStr = picture;
					else pictureStr += "&" + picture;
				}
				// 和电线相关的缺陷表字段与照片
				updateDefectPicRelateDX(mMainFragment, record.mDefectID, newdxname);
				// 更新电线表
				record = new DXRecord(newdxname, record.mCircuit, record.mHeight, record.mType, record.mModel, record.mNum, newDevicename, record.mDeviceName2,
						pictureStr, record.mDefectID, record.mRemark, record.mIsEdit);
				mDXTable.update(geo, record, id);
				
				
			}
			if(record.mDeviceName2.equals(oldDevicename)){
				int index1 = record.mName.lastIndexOf("[");
				int index2 = record.mName.lastIndexOf("]");
				String name2 = record.mName.substring(index1+1, index2);
				String newname2 = newDevicename.substring(newDevicename.indexOf(" ")+1);
				String newdxname = record.mName.replace(name2, newname2); // 新电线名称
				// 历史照片重命名
				List<String> pictureList = record.getPictureList();
				for(int i=0; i<pictureList.size(); i++){ 
					String picname = pictureList.get(i);
					File pictureFile = new File(Util.getPictureDirRootPath(), picname);
					String oldChar = picname.split("_")[1];
					picname = picname.replace(oldChar, newdxname);
					pictureList.set(i, picname);
					if(pictureFile.exists()) {
						pictureFile.renameTo(new File(Util.getPictureDirRootPath() + picname));
					}
				}
				String pictureStr = "";
				for(String picture :pictureList){
					if(pictureStr.equals("")){
						pictureStr = picture;
					}else{
						pictureStr += "&" + picture;
					} 
				}
				// 电线相关的缺陷照片
				updateDefectPicRelateDX(mMainFragment, record.mDefectID, newdxname);
				// 更新电线表
				record = new DXRecord(newdxname, record.mCircuit, record.mHeight, record.mType, record.mModel, record.mNum, record.mDeviceName1, newDevicename,
						pictureStr, record.mDefectID, record.mRemark, record.mIsEdit);
				mDXTable.update(geo, record, id);
			}
		}
		return result;
	}
	
	public static List<String> turnStringToList(String complexStr){
		List<String> list = new ArrayList<String>();
		if(!complexStr.equals("")){
			if(complexStr.contains("&")){
				String[] strArr = complexStr.split("&");
				list = Arrays.asList(strArr);
			}else{
				list.add(complexStr);
			}
		}
		return list;
	}
	
	public static void updateDefectPicRelateDX(MainFragment mMainFragment, String defectIds, String newdxname){
		DefectTable mDefectTable = mMainFragment.getDefectTable();
		List<String> mDefectIDList = turnStringToList(defectIds);
		for(String defectid : mDefectIDList){
			DefectRecord defectrecord = mDefectTable.SelectDefect(Integer.valueOf(defectid));
			List<String> mDefectPicList = turnStringToList(defectrecord.mPicture);
			String defectpicStr = "";
			for(String defectPic : mDefectPicList){
				String replacePart = defectPic.split("_")[1];
				String newDefectPic = defectPic.replace(replacePart, newdxname);
				if(defectpicStr.equals(""))
					defectpicStr = newDefectPic;
				else
					defectpicStr += "&" + newDefectPic;
				File mDefectPicFile = new File(Util.getPictureDirRootPath(), defectPic);
				if(mDefectPicFile.exists()){
					mDefectPicFile.renameTo(new File(Util.getPictureDirRootPath(), newDefectPic)); // 照片重命名
				}
			}
			mDefectTable.update(DefectRecord.ZD_PICTURE.NAME, defectpicStr, defectrecord.getId());// 更新表数据
		}
	}
	public static void updateDefectField(MainFragment mMainFragment, String newname, String oldname, String mDefectID) {
		DefectTable mDefectTable = mMainFragment.getDefectTable();
		mDefectID = mDefectID.replace("&", ",");
		ArrayList<DefectRecord> mDefectList = mDefectTable.SelectDefects(
				"where " + DBSetting.ZD_PRIMERY + " in (" + mDefectID + ")");
		for(DefectRecord defectrecord : mDefectList){
			List<String> mDefectPicList = turnStringToList(defectrecord.mPicture);
			String defectpicStr = "";
			for(String defectPic : mDefectPicList){
				String replacePart = defectPic.split("_")[1];
				String newDefectPic = defectPic.replace(replacePart, newname);
				if(defectpicStr.equals(""))
					defectpicStr = newDefectPic;
				else
					defectpicStr += "&" + newDefectPic;
				File mDefectPicFile = new File(Util.getPictureDirRootPath(), defectPic);
				if(mDefectPicFile.exists()){
					mDefectPicFile.renameTo(new File(Util.getPictureDirRootPath(), newDefectPic)); // 照片重命名
				}
			}
			mDefectTable.update(DefectRecord.ZD_PICTURE.NAME, defectpicStr, defectrecord.getId());// 更新表数据
		}
	}
}
