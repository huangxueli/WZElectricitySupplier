package com.example.wzelectricitysupplier.adapter;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.wzelectricitysupplier.MainActivity;
import com.example.wzelectricitysupplier.MyApplication;
import com.example.wzelectricitysupplier.R;
import com.example.wzelectricitysupplier.bean.BDSRecord;
import com.example.wzelectricitysupplier.bean.DeviceRecord;
import com.example.wzelectricitysupplier.bean.GBRecord;
import com.example.wzelectricitysupplier.bean.GLRecord;
import com.example.wzelectricitysupplier.bean.GTRecord;
import com.example.wzelectricitysupplier.bean.HWRecord;
import com.example.wzelectricitysupplier.bean.KBRecord;
import com.example.wzelectricitysupplier.bean.LKRecord;
import com.example.wzelectricitysupplier.bean.PDRecord;
import com.example.wzelectricitysupplier.bean.SwitchRecord;
import com.example.wzelectricitysupplier.bean.XBRecord;
import com.example.wzelectricitysupplier.database.BDSTable;
import com.example.wzelectricitysupplier.database.GBTable;
import com.example.wzelectricitysupplier.database.GLTable;
import com.example.wzelectricitysupplier.database.GTTable;
import com.example.wzelectricitysupplier.database.HWTable;
import com.example.wzelectricitysupplier.database.KBTable;
import com.example.wzelectricitysupplier.database.LKTable;
import com.example.wzelectricitysupplier.database.PDTable;
import com.example.wzelectricitysupplier.database.SwitchTable;
import com.example.wzelectricitysupplier.database.XBTable;
import com.example.wzelectricitysupplier.fragment.MainFragment;
import com.example.wzelectricitysupplier.setting.AppUtil;
import com.example.wzelectricitysupplier.setting.AppUtil.DeviceType;

public class DeviceSearchAdapter extends BaseAdapter {
	
	private String mCircuitID;
	private ArrayList<DeviceRecord> mDeviceList = new ArrayList<DeviceRecord>();
	private MainFragment mMainFragment;
	
	public DeviceSearchAdapter(MainActivity mMainActivity, String mCircuitID) {
		this.mCircuitID = mCircuitID;
		this.mMainFragment = mMainActivity.getMainFragment();
		if(!mCircuitID.equals("")){
			this.mDeviceList = getAllDeviceByCircuit(mCircuitID);
		}
	}
	
	public ArrayList<DeviceRecord> getAllDeviceByCircuit(String mCircuitID){
		return getAllDeviceByCircuit(mCircuitID, null);
	}
	
	/**
	 * @param mCircuitID
	 * @param key ¹Ø¼ü×Ö
	 * @return
	 */
	public ArrayList<DeviceRecord> getAllDeviceByCircuit(String mCircuitID, String key){
		ArrayList<DeviceRecord> mDeviceList = new ArrayList<>();
		ArrayList<? extends DeviceRecord> mList = null;
		String condition = "";
		BDSTable mBDSTable = (BDSTable)AppUtil.getTableByType(mMainFragment, DeviceType.DeviceBDS);
		if(!"".equals(key) && key!=null){
			if(key.equals(DeviceType.DeviceBDS)){
				 mList = mBDSTable.selectSubstations(DeviceRecord.ZD_CIRCUIT.NAME + " = " + mCircuitID);
				 mDeviceList.addAll(mList);
				 return mDeviceList;
			}else{
				condition = " AND " + "(" + BDSRecord.ZD_NAME.NAME + " LIKE '%" + key + "%' or " + 
						DeviceRecord.ZD_REMARK.NAME + " LIKE '%" + key + "%')";
			}
		}else{
			condition = "";
		}
		mList = mBDSTable.selectSubstations(DeviceRecord.ZD_CIRCUIT.NAME + " = " + mCircuitID + condition);
		mDeviceList.addAll(mDeviceList.size(), mList);
		
		GBTable mGBTable = (GBTable)AppUtil.getTableByType(mMainFragment, DeviceType.DeviceGB);
		if(!"".equals(key) && key!=null){
			if(key.equals(DeviceType.DeviceGB)){
				 mList = mGBTable.selectBarTypeVariables(DeviceRecord.ZD_CIRCUIT.NAME + " = " + mCircuitID);
				 mDeviceList.addAll(mList);
				 return mDeviceList;
			}else{
				condition = " AND " + "(" + GBRecord.ZD_NAME.NAME + " LIKE '%" + key + "%' or " + 
						DeviceRecord.ZD_REMARK.NAME + " LIKE '%" + key + "%')";
			}
		}else{
			condition = "";
		}
		mList = mGBTable.selectBarTypeVariables(DeviceRecord.ZD_CIRCUIT.NAME + " = " + mCircuitID + condition);
		mDeviceList.addAll(mDeviceList.size(), mList);
		
		GLTable mGLTable = (GLTable)AppUtil.getTableByType(mMainFragment, DeviceType.DeviceGL);
		if(!"".equals(key) && key!=null){
			if(key.equals(DeviceType.DeviceGL)){
				 mList = mGLTable.selectDisconnectores(DeviceRecord.ZD_CIRCUIT.NAME + " = " + mCircuitID);
				 mDeviceList.addAll(mList);
				 return mDeviceList;
			}else{
				condition = " AND " + "(" + GLRecord.ZD_NAME.NAME + " LIKE '%" + key + "%' or " + 
						DeviceRecord.ZD_REMARK.NAME + " LIKE '%" + key + "%')";
			}
		}else{
			condition = "";
		}
		mList = mGLTable.selectDisconnectores(DeviceRecord.ZD_CIRCUIT.NAME + " = " + mCircuitID + condition);
		mDeviceList.addAll(mDeviceList.size(), mList);
		
		GTTable mGTTable = (GTTable)AppUtil.getTableByType(mMainFragment, DeviceType.DeviceGT);
		if(!"".equals(key) && key!=null){
			if(key.equals(DeviceType.DeviceGT)){
				 mList = mGTTable.selectTowers(DeviceRecord.ZD_CIRCUIT.NAME + " = " + mCircuitID);
				 mDeviceList.addAll(mList);
				 return mDeviceList;
			}else{
				condition = " AND " + "(" + GTRecord.ZD_NAME.NAME + " LIKE '%" + key + "%' or " + 
						DeviceRecord.ZD_REMARK.NAME + " LIKE '%" + key + "%')";
			}
		}else{
			condition = "";
		}
		mList = mGTTable.selectTowers(DeviceRecord.ZD_CIRCUIT.NAME + " = " + mCircuitID + condition);
		mDeviceList.addAll(mDeviceList.size(), mList);
		
		HWTable mHWTable = (HWTable)AppUtil.getTableByType(mMainFragment, DeviceType.DeviceHW);
		if(!"".equals(key) && key!=null){
			if(key.equals(DeviceType.DeviceHW)){
				 mList = mHWTable.selectRingMainUnits(DeviceRecord.ZD_CIRCUIT.NAME + " = " + mCircuitID);
				 mDeviceList.addAll(mList);
				 return mDeviceList;
			}else{
				condition = " AND " + "(" + HWRecord.ZD_NAME.NAME + " LIKE '%" + key + "%' or " + 
						DeviceRecord.ZD_REMARK.NAME + " LIKE '%" + key + "%')";
			}
		}else{
			condition = "";
		}
		mList = mHWTable.selectRingMainUnits(DeviceRecord.ZD_CIRCUIT.NAME + " = " + mCircuitID + condition);
		mDeviceList.addAll(mDeviceList.size(), mList);
		
		KBTable mKBTable = (KBTable)AppUtil.getTableByType(mMainFragment, DeviceType.DeviceKG);
		if(!"".equals(key) && key!=null){
			if(key.equals(DeviceType.DeviceKG)){
				 mList = mKBTable.selectSwitchingStations(DeviceRecord.ZD_CIRCUIT.NAME + " = " + mCircuitID);
				 mDeviceList.addAll(mList);
				 return mDeviceList;
			}else{
				condition = " AND " + "(" + KBRecord.ZD_NAME.NAME + " LIKE '%" + key + "%' or " + 
						DeviceRecord.ZD_REMARK.NAME + " LIKE '%" + key + "%')";
			}
		}else{
			condition = "";
		}
		mList = mKBTable.selectSwitchingStations(DeviceRecord.ZD_CIRCUIT.NAME + " = " + mCircuitID + condition);
		mDeviceList.addAll(mDeviceList.size(), mList);
		
		LKTable mLKTable = (LKTable)AppUtil.getTableByType(mMainFragment, DeviceType.DeviceLK);
		if(!"".equals(key) && key!=null){
			if(key.equals(DeviceType.DeviceLK)){
				 mList = mLKTable.selectLineConnectors(DeviceRecord.ZD_CIRCUIT.NAME + " = " + mCircuitID);
				 mDeviceList.addAll(mList);
				 return mDeviceList;
			}else{
				condition = " AND " + "(" + LKRecord.ZD_NAME.NAME + " LIKE '%" + key + "%' or " + 
						DeviceRecord.ZD_REMARK.NAME + " LIKE '%" + key + "%')";
			}
		}else{
			condition = "";
		}
		mList = mLKTable.selectLineConnectors(DeviceRecord.ZD_CIRCUIT.NAME + " = " + mCircuitID + condition);
		mDeviceList.addAll(mDeviceList.size(), mList);
		
		PDTable mPDTable = (PDTable)AppUtil.getTableByType(mMainFragment, DeviceType.DevicePD);
		if(!"".equals(key) && key!=null){
			if(key.equals(DeviceType.DevicePD)){
				 mList = mPDTable.selectSwitchingRooms(DeviceRecord.ZD_CIRCUIT.NAME + " = " + mCircuitID);
				 mDeviceList.addAll(mList);
				 return mDeviceList;
			}else{
				condition = " AND " + "(" + PDRecord.ZD_NAME.NAME + " LIKE '%" + key + "%' or " + 
						DeviceRecord.ZD_REMARK.NAME + " LIKE '%" + key + "%')";
			}
		}else{
			condition = "";
		}
		mList = mPDTable.selectSwitchingRooms(DeviceRecord.ZD_CIRCUIT.NAME + " = " + mCircuitID + condition);
		mDeviceList.addAll(mDeviceList.size(), mList);
		
		SwitchTable mSwitchTable = (SwitchTable)AppUtil.getTableByType(mMainFragment, DeviceType.DeviceSwitch);
		if(!"".equals(key) && key!=null){
			if(key.equals(DeviceType.DeviceSwitch)){
				 mList = mSwitchTable.selectSwitchs(DeviceRecord.ZD_CIRCUIT.NAME + " = " + mCircuitID);
				 mDeviceList.addAll(mList);
				 return mDeviceList;
			}else{
				condition = " AND " + "(" + SwitchRecord.ZD_NAME.NAME + " LIKE '%" + key + "%' or " + 
						DeviceRecord.ZD_REMARK.NAME + " LIKE '%" + key + "%')";
			}
		}else{
			condition = "";
		}
		mList = mSwitchTable.selectSwitchs(DeviceRecord.ZD_CIRCUIT.NAME + " = " + mCircuitID + condition);
		mDeviceList.addAll(mDeviceList.size(), mList);
		
		XBTable mXBTable = (XBTable)AppUtil.getTableByType(mMainFragment, DeviceType.DeviceXB);
		if(!"".equals(key) && key!=null){
			if(key.equals(DeviceType.DeviceXB)){
				 mList = mXBTable.selectBoxChanges(DeviceRecord.ZD_CIRCUIT.NAME + " = " + mCircuitID);
				 mDeviceList.addAll(mList);
				 return mDeviceList;
			}else{
				condition = " AND " + "(" + XBRecord.ZD_NAME.NAME + " LIKE '%" + key + "%' or " + 
						DeviceRecord.ZD_REMARK.NAME + " LIKE '%" + key + "%')";
			}
		}else{
			condition = "";
		}
		mList = mXBTable.selectBoxChanges(DeviceRecord.ZD_CIRCUIT.NAME + " = " + mCircuitID + condition);
		mDeviceList.addAll(mDeviceList.size(), mList);
		return mDeviceList;
	}
	
	public void reLoadData() {
		reLoadData(mCircuitID);
	}
	
	public void reLoadData(String mCircuitID) {
		if(mCircuitID.equals("")){
			mDeviceList.clear();
		}else{
			mDeviceList = getAllDeviceByCircuit(mCircuitID);
		}
		notifyDataSetChanged();
	}
	public void reLoadData(String mCircuitID, String key) {
		if(mCircuitID.equals("")){
			mDeviceList.clear();
		}else{
			mDeviceList = getAllDeviceByCircuit(mCircuitID, key);
		}
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return mDeviceList.size();
	}

	@Override
	public Object getItem(int position) {
		return mDeviceList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public final class ViewHolder {
		TextView Name;
		TextView Type;
		TextView Remark;
		ViewGroup Item;
		DeviceRecord record;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = MyApplication.Inflater.inflate(R.layout.listitem_device_manager, parent, false);
			holder = new ViewHolder();
			holder.Item = (ViewGroup) convertView.findViewById(R.id.item);
			holder.Name = (TextView) convertView.findViewById(R.id.name);
			holder.Type = (TextView) convertView.findViewById(R.id.type);
			holder.Remark = (TextView) convertView.findViewById(R.id.remark);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.record = mDeviceList.get(position);

		holder.Name.setText(holder.record.mName);
		holder.Type.setText(holder.record.getDeviceType());
		holder.Remark.setText(holder.record.mRemark);
		return convertView;
	}

}
