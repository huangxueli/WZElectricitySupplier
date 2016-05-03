package com.example.wzelectricitysupplier.adapter;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.wzelectricitysupplier.MainActivity;
import com.example.wzelectricitysupplier.MyApplication;
import com.example.wzelectricitysupplier.R;
import com.example.wzelectricitysupplier.bean.CircuitRecord;
import com.example.wzelectricitysupplier.database.CircuitTable;

public class CircuitAdapter extends BaseAdapter {
	
	private ArrayList<CircuitRecord> mCircuitList = new ArrayList<CircuitRecord>();
	private CircuitTable mCircuitTable;
	
	public interface OnCircuitLoadListener {
		public abstract void doAfterCircuitLoaded(int id);
	}

	public CircuitAdapter(MainActivity mMainActivity, CircuitTable mCircuitTable) {
		this.mCircuitTable = mCircuitTable;
		mCircuitList = mCircuitTable.SelectCircuits("");
	}

	public void reLoadData() {
		mCircuitList = mCircuitTable.SelectCircuits("");
		notifyDataSetChanged();
	}
	public void reLoadData(ArrayList<CircuitRecord> mCircuitList) {
		this.mCircuitList = mCircuitList;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mCircuitList.size();
	}

	@Override
	public Object getItem(int position) {
		return mCircuitList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public final class ViewHolder {
		TextView name;
		TextView remark;
		CircuitRecord record;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = MyApplication.Inflater.inflate(R.layout.listitem_circuit, parent, false);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.remark = (TextView) convertView.findViewById(R.id.remark);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		CircuitRecord record = mCircuitList.get(position);
		holder.record = record; 

		holder.name.setText(record.mName);
		holder.remark.setText(record.mRemark);
		String mCircuitID = mCircuitTable.getCircuitID();
		if(!mCircuitID.equals("")){
			if(holder.record.getId() == Integer.valueOf(mCircuitID)){
				holder.name.setBackgroundResource(R.drawable.bg_circuit_name_f);
				holder.remark.setBackgroundResource(R.drawable.bg_circuit_remark_f);
			}else{
				holder.name.setBackgroundResource(R.drawable.bg_circuit_name_n);
				holder.remark.setBackgroundResource(R.drawable.bg_circuit_remark_n);
			}
		}else{
			holder.name.setBackgroundResource(R.drawable.bg_circuit_name_n);
			holder.remark.setBackgroundResource(R.drawable.bg_circuit_remark_n);
		}
		return convertView;
	}

}
