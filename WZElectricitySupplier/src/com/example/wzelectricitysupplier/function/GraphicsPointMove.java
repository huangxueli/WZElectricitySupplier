package com.example.wzelectricitysupplier.function;

import java.util.Map;

import android.content.DialogInterface;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.example.wzelectricitysupplier.bean.DeviceRecord;
import com.example.wzelectricitysupplier.database.CircuitTable;
import com.example.wzelectricitysupplier.database.DXTable;
import com.example.wzelectricitysupplier.database.GeometryTable;
import com.example.wzelectricitysupplier.fragment.MainFragment;
import com.example.wzelectricitysupplier.listener.MyOnSingleTapListener.OperaterKind;
import com.example.wzelectricitysupplier.setting.AppUtil;
import com.example.wzelectricitysupplier.setting.Util;

public class GraphicsPointMove {
	
	public interface OnAfterMoveListener {
		public abstract void doSomethingAfterMove(float screenx, float screeny);
	}
	
	
	public static OnAfterMoveListener Prepare(final MainFragment mMainFragment, final GraphicsLayer mGraphicsLayer, final int mGraphicsId){
		final MapView mMap = mMainFragment.getMap();
		mGraphicsLayer.clearSelection();
		mGraphicsLayer.setSelectedGraphics(new int[]{mGraphicsId}, true);
		Graphic graphic = mGraphicsLayer.getGraphic(mGraphicsId);
		Map<String, Object> attributes = graphic.getAttributes();
		final Geometry geometry = graphic.getGeometry();
		final int id = (int) attributes.get(ArcgisTool.ATTRIBUTE_KEY_ID);
		final String type = (String)attributes.get(ArcgisTool.ATTRIBUTE_KEY_TYPE);
		
		mMainFragment.setOperaterKind(OperaterKind.MOVE);
		OnAfterMoveListener mOnAfterMoveListener = new OnAfterMoveListener() {
			
			@Override
			public void doSomethingAfterMove(float screenx, float screeny) {
				
				final Point point = mMap.toMapPoint(new Point(screenx, screeny));
				final DXTable mDXTable = mMainFragment.getDXTable();
				final CircuitTable mCircuitTable = mMainFragment.getCircuitTable();
				DialogInterface.OnClickListener mPositiveListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 更新数据库
						GeometryTable table = AppUtil.getTableByType(mMainFragment, type);
						if(table.updateGeometry(point, id)){
							DeviceRecord record = AppUtil.getDeviceRecordByDefectRecord(mMainFragment, id, type);
							mDXTable.updateGeometry(mCircuitTable.getCircuitID(), record);
						}
						mGraphicsLayer.clearSelection();
						mMainFragment.doLoadCircuit(mCircuitTable.getCircuitID());
						mMainFragment.setOperaterKind(OperaterKind.SELECT);
					  
					}
				};
				DialogInterface.OnClickListener mNegativeListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						mGraphicsLayer.updateGraphic(mGraphicsId, geometry);
						mGraphicsLayer.clearSelection();
						mMainFragment.setOperaterKind(OperaterKind.SELECT);
					}
				};
				Util.ShowDialog(mMainFragment.getActivity(), "确定将目标点移动到该位置吗?", mPositiveListener, mNegativeListener);
			}
		};
		return mOnAfterMoveListener;
	}
}
