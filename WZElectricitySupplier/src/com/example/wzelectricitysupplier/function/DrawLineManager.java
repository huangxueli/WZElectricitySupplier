package com.example.wzelectricitysupplier.function;

import java.util.Map;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Geometry;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.Symbol;
import com.example.wzelectricitysupplier.fragment.MainFragment;
import com.example.wzelectricitysupplier.listener.MyOnSingleTapListener.OperaterKind;
import com.example.wzelectricitysupplier.setting.Util;

public class DrawLineManager {
	
	private MainFragment mMainFragment;
	
	private GraphicsLayer mGraphicsLayer;
	private Graphic mStartGraphic;
	private OnSelectTwoPointListener mOnSelectTwoPointListener;
	
	public interface OnSelectTwoPointListener {
		public abstract void doSomethingAfterSelectTwoPoint(Graphic start, Graphic end);
	}
	
	public DrawLineManager(MainFragment mMainFragment, GraphicsLayer mGraphicsLayer){
		this.mMainFragment = mMainFragment;
		this.mGraphicsLayer = mGraphicsLayer;
	}
	
	public void setSelectPoint(GraphicsLayer mPointGraphicsLayer, int mGraphicsId){
		if (mGraphicsId != -1) {
			Graphic graphic = mPointGraphicsLayer.getGraphic(mGraphicsId);
			if(mStartGraphic == null){
				mStartGraphic = graphic;
			} else {
				if(mStartGraphic.getId()==mGraphicsId){
					// 同一个设备点
					Util.Toast("起点和终点不能为同一个点");
				}else{
					mOnSelectTwoPointListener.doSomethingAfterSelectTwoPoint(mStartGraphic, graphic);
					mStartGraphic = null;
				}
			}
			mPointGraphicsLayer.setSelectedGraphics(new int[]{mGraphicsId}, true);
		}
	}
	
	public void StartDrawLine(OnSelectTwoPointListener mOnSelectTwoPointListener){
		mMainFragment.setOperaterKind(OperaterKind.DRAW);
		mGraphicsLayer.clearSelection();
		mStartGraphic = null;
		this.mOnSelectTwoPointListener = mOnSelectTwoPointListener;
	}
	public void StopDrawLine(){
		mMainFragment.setOperaterKind(OperaterKind.SELECT);
		mGraphicsLayer.clearSelection();
		mStartGraphic = null;
//		mOnSelectTwoPointListener = null;
	}
	
	public int drawLine(Geometry path, Symbol symbol, Map<String, Object> attributes){
		return mGraphicsLayer.addGraphic(new Graphic(path, symbol, null));
	}
}
