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
		case "����":
			holder.icon.setBackgroundResource(R.drawable.defect_basic);
			break;
		case "����":
			holder.icon.setBackgroundResource(R.drawable.defect_other);
			break;
		case "����":
			holder.icon.setBackgroundResource(R.drawable.defect_light);
			break;
		case "��ʶ":
			holder.icon.setBackgroundResource(R.drawable.defect_mark);
			break;
		case "���":
			holder.icon.setBackgroundResource(R.drawable.defect_fj);
			break;
		case "�Ŵ�":
			holder.icon.setBackgroundResource(R.drawable.defect_mc);
			break;
		case "��ѹ��":
			holder.icon.setBackgroundResource(R.drawable.defect_byq);
			break;
		case "���¶��":
			holder.icon.setBackgroundResource(R.drawable.defect_dglj);
			break;
		case "�����б":
			holder.icon.setBackgroundResource(R.drawable.defect_dgqx);
			break;
		case "����":
			holder.icon.setBackgroundResource(R.drawable.defect_dl);
			break;
		case "���±�����":
			holder.icon.setBackgroundResource(R.drawable.defect_dlbhj);
			break;
		case "�ֽ���":
			holder.icon.setBackgroundResource(R.drawable.defect_fjx);
			break;
		case "�˺�":
			holder.icon.setBackgroundResource(R.drawable.defect_gh);
			break;
		case "�ᵣ":
			holder.icon.setBackgroundResource(R.drawable.defect_ht);
			break;
		case "�����":
			holder.icon.setBackgroundResource(R.drawable.defect_hbq);
			break;
		case "������":
			holder.icon.setBackgroundResource(R.drawable.defect_jgp);
			break;
		case "��Ե��":
			holder.icon.setBackgroundResource(R.drawable.defect_jyz);
			break;
		case "����":
			holder.icon.setBackgroundResource(R.drawable.defect_switch);
			break;
		case "�ո�":
			holder.icon.setBackgroundResource(R.drawable.defect_kg);
			break;
		case "����":
			holder.icon.setBackgroundResource(R.drawable.defect_lx);
			break;
		case "��ľ":
			holder.icon.setBackgroundResource(R.drawable.defect_sm);
			break;
		case "��ʴ":
			holder.icon.setBackgroundResource(R.drawable.defect_xs);
			break;
		case "����":
			holder.icon.setBackgroundResource(R.drawable.defect_yw);
			break;
		default:
			holder.icon.setBackgroundResource(R.drawable.defect_other);
			break;
		}
		return convertView;
	}

}
