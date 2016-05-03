package com.example.wzelectricitysupplier.adapter;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.wzelectricitysupplier.MainActivity;
import com.example.wzelectricitysupplier.MyApplication;
import com.example.wzelectricitysupplier.R;
import com.example.wzelectricitysupplier.bean.RouteRecord;
import com.example.wzelectricitysupplier.database.RouteTable;

public class RouteAdapter extends BaseAdapter {
	
	private ArrayList<RouteRecord> mRouteList = new ArrayList<RouteRecord>();
	private RouteTable mRouteTable;
	private String mCurrRouteID = "";
	
	public interface OnRouteLoadListener {
		public abstract void doAfterRouteLoaded(int id);
	}

	public RouteAdapter(MainActivity mMainActivity, RouteTable mRouteTable) {
		this.mRouteTable = mRouteTable;
		mRouteList = mRouteTable.SelectRoutes("1=1");
	}

	public void reLoadData() {
		mRouteList = mRouteTable.SelectRoutes("1=1");
		notifyDataSetChanged();
	}
	public void reLoadData(ArrayList<RouteRecord> mRouteList) {
		this.mRouteList = mRouteList;
		notifyDataSetChanged();
	}
	
	public void setCurrRouteID(String mCurrRouteID){
		this.mCurrRouteID = mCurrRouteID;
	}
	
	@Override
	public int getCount() {
		return mRouteList.size();
	}

	@Override
	public Object getItem(int position) {
		return mRouteList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public final class ViewHolder {
		TextView Index;
		TextView StartTime;
		TextView EndTime;
		ViewGroup Item;
		RouteRecord record;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = MyApplication.Inflater.inflate(R.layout.listitem_route, parent, false);
			holder = new ViewHolder();
			holder.Item = (ViewGroup) convertView.findViewById(R.id.item);
			holder.Index = (TextView) convertView.findViewById(R.id.index);
			holder.StartTime = (TextView) convertView.findViewById(R.id.startTime);
			holder.EndTime = (TextView) convertView.findViewById(R.id.endTime);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		RouteRecord record = mRouteList.get(position);
		holder.record = record; 

		holder.Index.setText(String.valueOf(++position));
		holder.StartTime.setText(record.mStartTime);
		holder.EndTime.setText(record.mEndTime);
		if(!mCurrRouteID.equals("")){
			if(holder.record.getId() == Integer.valueOf(mCurrRouteID)){
				holder.Item.setBackgroundResource(R.drawable.bg_circuit_name_f);
			}else{
				holder.Item.setBackgroundResource(R.drawable.bg_circuit_name_n);
			}
		}else{
			holder.Item.setBackgroundResource(R.drawable.bg_circuit_name_n);
		}
		return convertView;
	}

}
