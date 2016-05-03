package com.example.wzelectricitysupplier.adapter;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wzelectricitysupplier.MyApplication;
import com.example.wzelectricitysupplier.R;
import com.example.wzelectricitysupplier.bean.DefectRecord;
import com.example.wzelectricitysupplier.database.DefectTable;

public class DefectAdapter extends BaseAdapter{
	
	private DefectTable mDefectTable;
	private int belongid;
	private String belongDevice;
	private List<DefectRecord> mDefectList = new ArrayList<DefectRecord>();
	
	public DefectAdapter(DefectTable mDefectTable, int belongid, String belongDevice){
		this.mDefectTable = mDefectTable;
		this.belongid = belongid;
		this.belongDevice = belongDevice;
		mDefectList = mDefectTable.SelectDefects(belongid, belongDevice);
	}
	
	public void reLoadData() {
		mDefectList = mDefectTable.SelectDefects(belongid, belongDevice);
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		int count = 0;
		if(mDefectList!=null){
			count = mDefectList.size();
		}
		return count;
	}
	
	public List<DefectRecord> getDefectList(){
		return mDefectList;
	}
	
	@Override
	public Object getItem(int position) {
		return mDefectList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public final class ViewHolder {
		public TextView name;
		public TextView type;
		public ImageView icon;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = MyApplication.Inflater.inflate(R.layout.listitem_defect, null);
			
			holder = new ViewHolder();
			holder.name = (TextView)convertView.findViewById(R.id.name);
			holder.type = (TextView)convertView.findViewById(R.id.type);
			holder.icon = (ImageView)convertView.findViewById(R.id.icon);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		DefectRecord record = mDefectList.get(position);
		holder.name.setText(record.mName);
		holder.type.setText(record.mType);
        
		switch(record.mType){
		case "基础":
			holder.icon.setBackgroundResource(R.drawable.defect_basic);
			break;
		case "其他":
			holder.icon.setBackgroundResource(R.drawable.defect_other);
			break;
		case "照明":
			holder.icon.setBackgroundResource(R.drawable.defect_light);
			break;
		case "标识":
			holder.icon.setBackgroundResource(R.drawable.defect_mark);
			break;
		case "风机":
			holder.icon.setBackgroundResource(R.drawable.defect_fj);
			break;
		case "门窗":
			holder.icon.setBackgroundResource(R.drawable.defect_mc);
			break;
		case "变压器":
			holder.icon.setBackgroundResource(R.drawable.defect_byq);
			break;
		case "电杆露筋":
			holder.icon.setBackgroundResource(R.drawable.defect_dglj);
			break;
		case "电杆倾斜":
			holder.icon.setBackgroundResource(R.drawable.defect_dgqx);
			break;
		case "电缆":
			holder.icon.setBackgroundResource(R.drawable.defect_dl);
			break;
		case "电缆保护夹":
			holder.icon.setBackgroundResource(R.drawable.defect_dlbhj);
			break;
		case "分接箱":
			holder.icon.setBackgroundResource(R.drawable.defect_fjx);
			break;
		case "杆号":
			holder.icon.setBackgroundResource(R.drawable.defect_gh);
			break;
		case "横担":
			holder.icon.setBackgroundResource(R.drawable.defect_ht);
			break;
		case "红白漆":
			holder.icon.setBackgroundResource(R.drawable.defect_hbq);
			break;
		case "警告牌":
			holder.icon.setBackgroundResource(R.drawable.defect_jgp);
			break;
		case "绝缘子":
			holder.icon.setBackgroundResource(R.drawable.defect_jyz);
			break;
		case "开关":
			holder.icon.setBackgroundResource(R.drawable.defect_switch);
			break;
		case "空杆":
			holder.icon.setBackgroundResource(R.drawable.defect_kg);
			break;
		case "拉线":
			holder.icon.setBackgroundResource(R.drawable.defect_lx);
			break;
		case "树木":
			holder.icon.setBackgroundResource(R.drawable.defect_sm);
			break;
		case "锈蚀":
			holder.icon.setBackgroundResource(R.drawable.defect_xs);
			break;
		case "异物":
			holder.icon.setBackgroundResource(R.drawable.defect_yw);
			break;
		default:
			holder.icon.setBackgroundResource(R.drawable.defect_other);
			break;
		}
		return convertView;
	}

}
