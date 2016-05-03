package com.example.wzelectricitysupplier.listener;

import java.util.HashMap;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Geometry.Type;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.Symbol;
import com.example.wzelectricitysupplier.MainActivity;
import com.example.wzelectricitysupplier.R;
import com.example.wzelectricitysupplier.dialog.BDSWindow;
import com.example.wzelectricitysupplier.dialog.BaseWindow;
import com.example.wzelectricitysupplier.dialog.DXWindow;
import com.example.wzelectricitysupplier.dialog.GBWindow;
import com.example.wzelectricitysupplier.dialog.GLWindow;
import com.example.wzelectricitysupplier.dialog.GTWindow;
import com.example.wzelectricitysupplier.dialog.HWWindow;
import com.example.wzelectricitysupplier.dialog.KBWindow;
import com.example.wzelectricitysupplier.dialog.LKWindow;
import com.example.wzelectricitysupplier.dialog.PDWindow;
import com.example.wzelectricitysupplier.dialog.SwitchWindow;
import com.example.wzelectricitysupplier.dialog.XBWindow;
import com.example.wzelectricitysupplier.fragment.MainFragment;
import com.example.wzelectricitysupplier.function.ArcgisTool;
import com.example.wzelectricitysupplier.function.DrawLineManager;
import com.example.wzelectricitysupplier.function.DrawLineManager.OnSelectTwoPointListener;
import com.example.wzelectricitysupplier.function.route.RouteTakenManager;
import com.example.wzelectricitysupplier.setting.AppUtil;
import com.example.wzelectricitysupplier.setting.Util;

public class MyOnSingleTapListener implements OnSingleTapListener{

	private static final long serialVersionUID = 1L;
	
	private MainFragment mMainFragment;
	private MainActivity mMainActivity;
	private MapView mMap;
	private BaseWindow mWindowBase;
	
	private GraphicsLayer mInterestPointLayer;
	private GraphicsLayer mInterestLineLayer;
	private DrawLineManager mDrawLineManager;
	private RouteTakenManager mRouteTakenManager;
	
	public enum OperaterKind{
		DONOTHING, ADD, SELECT, MOVE, DRAW, DELETE;
	}
	
	public enum AddMode{
		DeviceBDS, DeviceGT, DeviceSwitch, DeviceGL, DeviceLK, DevicePD, DeviceKG, DeviceHW, DeviceGB, DeviceXB,
		LineFour, LineTwo, LineOne;
	}
	
	public MyOnSingleTapListener(MainFragment mMainFragment){
		this.mMainFragment = mMainFragment;
		this.mMap = mMainFragment.getMap();
		this.mMainActivity = (MainActivity)mMainFragment.getActivity();
		this.mInterestPointLayer = mMainFragment.getInterestPointLayer();
		this.mInterestLineLayer = mMainFragment.getInterestLineLayer();
		this.mDrawLineManager = mMainFragment.getDrawLineManager();
		this.mRouteTakenManager = mMainFragment.getRouteTakenManager();
	}
	
	@Override
	public void onSingleTap(float x, float y) {
		if (!mMap.isLoaded()) {
            return;
        }
		doSingleTapOperate(x, y);
		mMainFragment.CheckMessureLine(x, y);
		mRouteTakenManager.CheckPointSingleTapListener(x, y);
	}
	
	private void doSingleTapOperate(final float screenx, final float screeny){

		OperaterKind kind = mMainFragment.getOperaterKind();
		int mGraphicId = -1;
		switch(kind){
		case ADD:
			AddMode mode = mMainFragment.getAddMode();
			Geometry geometry = mMap.toMapPoint(screenx, screeny);
			int graphicId = -1;
			switch(mode){
			case DeviceBDS:
				graphicId = ArcgisTool.addGraphic(geometry, R.drawable.icon_small_bds, mInterestPointLayer);
				mWindowBase = new BDSWindow(mMainActivity, (Point)geometry, graphicId);
				mWindowBase.showWindow(screenx, screeny, mInterestPointLayer);
				mWindowBase.toEditView();
				break;
			case DeviceGT:
				graphicId = ArcgisTool.addGraphic(geometry, R.drawable.icon_small_gt, mInterestPointLayer);
				mWindowBase = new GTWindow(mMainActivity, (Point)geometry, graphicId);
				mWindowBase.showWindow(screenx, screeny, mInterestPointLayer);
				mWindowBase.toEditView();
				break;
			case DeviceSwitch:
				graphicId = ArcgisTool.addGraphic(geometry, R.drawable.icon_small_switch, mInterestPointLayer);
				mWindowBase = new SwitchWindow(mMainActivity, (Point)geometry, graphicId);
				mWindowBase.showWindow(screenx, screeny, mInterestPointLayer);
				mWindowBase.toEditView();
				break;
			case DeviceGL:
				graphicId = ArcgisTool.addGraphic(geometry, R.drawable.icon_small_gl, mInterestPointLayer);
				mWindowBase = new GLWindow(mMainActivity, (Point)geometry, graphicId);
				mWindowBase.showWindow(screenx, screeny, mInterestPointLayer);
				mWindowBase.toEditView();
				break;
			case DeviceLK:
				graphicId = ArcgisTool.addGraphic(geometry, R.drawable.icon_small_lk, mInterestPointLayer);
				mWindowBase = new LKWindow(mMainActivity, (Point)geometry, graphicId);
				mWindowBase.showWindow(screenx, screeny, mInterestPointLayer);
				mWindowBase.toEditView();
				break;
			case DevicePD:
				graphicId = ArcgisTool.addGraphic(geometry, R.drawable.icon_small_pd, mInterestPointLayer);
				mWindowBase = new PDWindow(mMainActivity, (Point)geometry, graphicId);
				mWindowBase.showWindow(screenx, screeny, mInterestPointLayer);
				mWindowBase.toEditView();
				break;
			case DeviceKG:
				graphicId = ArcgisTool.addGraphic(geometry, R.drawable.icon_small_kg, mInterestPointLayer);
				mWindowBase = new KBWindow(mMainActivity, (Point)geometry, graphicId);
				mWindowBase.showWindow(screenx, screeny, mInterestPointLayer);
				mWindowBase.toEditView();
				break;
			case DeviceHW:
				graphicId = ArcgisTool.addGraphic(geometry, R.drawable.icon_small_hw, mInterestPointLayer);
				mWindowBase = new HWWindow(mMainActivity, (Point)geometry, graphicId);
				mWindowBase.showWindow(screenx, screeny, mInterestPointLayer);
				mWindowBase.toEditView();
				break;
			case DeviceGB:
				graphicId = ArcgisTool.addGraphic(geometry, R.drawable.icon_small_gb, mInterestPointLayer);
				mWindowBase = new GBWindow(mMainActivity, (Point)geometry, graphicId);
				mWindowBase.showWindow(screenx, screeny, mInterestPointLayer);
				mWindowBase.toEditView();
				break;
			case DeviceXB:
				graphicId = ArcgisTool.addGraphic(geometry, R.drawable.icon_small_xb, mInterestPointLayer);
				mWindowBase = new XBWindow(mMainActivity, (Point)geometry, graphicId);
				mWindowBase.showWindow(screenx, screeny, mInterestPointLayer);
				mWindowBase.toEditView();
				break;
			case LineFour:
				mDrawLineManager.StartDrawLine(new OnSelectTwoPointListener() {
					@Override
					public void doSomethingAfterSelectTwoPoint(final Graphic graphic1, final Graphic graphic2) {
						Util.ShowDialog(mMainActivity, "确定添加同杆四回线吗？", new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Point point1 = (Point)graphic1.getGeometry();
								Point point2 = (Point)graphic2.getGeometry();
								int id1 = (int)graphic1.getAttributes().get(ArcgisTool.ATTRIBUTE_KEY_ID);
								int id2 = (int)graphic2.getAttributes().get(ArcgisTool.ATTRIBUTE_KEY_ID);
								String type1 = (String) graphic1.getAttributes().get(ArcgisTool.ATTRIBUTE_KEY_TYPE);
								String type2 = (String) graphic2.getAttributes().get(ArcgisTool.ATTRIBUTE_KEY_TYPE);
								String name1 = AppUtil.getDeviceRecordByDefectRecord(mMainFragment, id1, type1).mName;
								String name2 = AppUtil.getDeviceRecordByDefectRecord(mMainFragment, id2, type2).mName;
								Polyline path = new Polyline();
								path.startPath(point1);
								path.lineTo(point2);
								Symbol symbol = ArcgisTool.getLineFourSymbol(null);
								int lineGraphicId = mDrawLineManager.drawLine(path, symbol, null);
								mInterestPointLayer.clearSelection();
								for(int graphicid : mInterestPointLayer.getGraphicIDs()){
									mInterestPointLayer.bringToFront(graphicid);
								}
								mWindowBase = new DXWindow(mMainActivity, path, lineGraphicId, name1, name2, "4");
								mWindowBase.showWindow(screenx, screeny, mInterestLineLayer);
								mWindowBase.toEditView();
							}
						}, new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								mMainFragment.getInterestPointLayer().clearSelection();
							}
						} );
						
					}
				});
				break;
			case LineTwo:
				mDrawLineManager.StartDrawLine(new OnSelectTwoPointListener() {
					@Override
					public void doSomethingAfterSelectTwoPoint(final Graphic graphic1, final Graphic graphic2) {
						Util.ShowDialog(mMainActivity, "确定添加同杆双回线吗？", new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Point point1 = (Point)graphic1.getGeometry();
								Point point2 = (Point)graphic2.getGeometry();
								int id1 = (int)graphic1.getAttributes().get(ArcgisTool.ATTRIBUTE_KEY_ID);
								int id2 = (int)graphic2.getAttributes().get(ArcgisTool.ATTRIBUTE_KEY_ID);
								String type1 = (String) graphic1.getAttributes().get(ArcgisTool.ATTRIBUTE_KEY_TYPE);
								String type2 = (String) graphic2.getAttributes().get(ArcgisTool.ATTRIBUTE_KEY_TYPE);
								String name1 = AppUtil.getDeviceRecordByDefectRecord(mMainFragment, id1, type1).mName;
								String name2 = AppUtil.getDeviceRecordByDefectRecord(mMainFragment, id2, type2).mName;
								Polyline path = new Polyline();
								path.startPath(point1);
								path.lineTo(point2);
								Symbol symbol = ArcgisTool.getLineTwoSymbol(null);
								int lineGraphicId = mDrawLineManager.drawLine(path, symbol, null);
								mInterestPointLayer.clearSelection();
								for(int graphicid : mInterestPointLayer.getGraphicIDs()){
									mInterestPointLayer.bringToFront(graphicid);
								}
								mWindowBase = new DXWindow(mMainActivity, path, lineGraphicId, name1, name2, "2");
								mWindowBase.showWindow(screenx, screeny, mInterestLineLayer);
								mWindowBase.toEditView();
							}
						}, new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								mMainFragment.getInterestPointLayer().clearSelection();
							}
						} );
						
					}
				});
				break;
			case LineOne:
				mDrawLineManager.StartDrawLine(new OnSelectTwoPointListener() {
					@Override
					public void doSomethingAfterSelectTwoPoint(final Graphic graphic1, final Graphic graphic2) {
						Util.ShowDialog(mMainActivity, "确定添加支线吗？", new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Point point1 = (Point)graphic1.getGeometry();
								Point point2 = (Point)graphic2.getGeometry();
								int id1 = (int)graphic1.getAttributes().get(ArcgisTool.ATTRIBUTE_KEY_ID);
								int id2 = (int)graphic2.getAttributes().get(ArcgisTool.ATTRIBUTE_KEY_ID);
								String type1 = (String) graphic1.getAttributes().get(ArcgisTool.ATTRIBUTE_KEY_TYPE);
								String type2 = (String) graphic2.getAttributes().get(ArcgisTool.ATTRIBUTE_KEY_TYPE);
								String name1 = AppUtil.getDeviceRecordByDefectRecord(mMainFragment, id1, type1).mName;
								String name2 = AppUtil.getDeviceRecordByDefectRecord(mMainFragment, id2, type2).mName;
								Polyline path = new Polyline();
								path.startPath(point1);
								path.lineTo(point2);
								Symbol symbol = ArcgisTool.judgeLineOneSymbolColor(mMainFragment, null, name1, name2);
								int lineGraphicId = mDrawLineManager.drawLine(path, symbol, null);
								mInterestPointLayer.clearSelection();
								for(int graphicid : mInterestPointLayer.getGraphicIDs()){
									mInterestPointLayer.bringToFront(graphicid);
								}
								mWindowBase = new DXWindow(mMainActivity, path, lineGraphicId, name1, name2, "1");
								mWindowBase.showWindow(screenx, screeny, mInterestLineLayer);
								mWindowBase.toEditView();
							}
						}, new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								mMainFragment.getInterestPointLayer().clearSelection();
							}
						} );
						
					}
				});
				break;
			}
			break;
		case SELECT:
			mGraphicId = ArcgisTool.getGraphicId(mMap, screenx, screeny, mInterestPointLayer);
			if (mGraphicId != -1) {
				Graphic pointGraphic = mInterestPointLayer.getGraphic(mGraphicId);
				if(pointGraphic !=null){
					Geometry geo = pointGraphic.getGeometry();
					if(geo.getType().value() == Type.POINT.value()){
						Point point = (Point)geo;
						HashMap<String, Object> attributes =  (HashMap<String, Object>) pointGraphic.getAttributes();
						int primaryId = (int) attributes.get(ArcgisTool.ATTRIBUTE_KEY_ID);
						String type = (String) attributes.get(ArcgisTool.ATTRIBUTE_KEY_TYPE);
						mInterestPointLayer.setSelectedGraphics(new int[]{mGraphicId}, true);
						switch(type){
						case "DeviceBDS":
							mWindowBase = new BDSWindow(mMainActivity, primaryId, point, mGraphicId);
							mWindowBase.showWindow(screenx, screeny, mInterestPointLayer);
							mMainFragment.unCheckAddDeviceBtn();
							break;
						case "DeviceGT":
							mWindowBase = new GTWindow(mMainActivity, primaryId, point, mGraphicId);
							mWindowBase.showWindow(screenx, screeny, mInterestPointLayer);
							mMainFragment.unCheckAddDeviceBtn();
							break;
						case "DeviceSwitch":
							mWindowBase = new SwitchWindow(mMainActivity, primaryId, point, mGraphicId);
							mWindowBase.showWindow(screenx, screeny, mInterestPointLayer);
							mMainFragment.unCheckAddDeviceBtn();
							break;
						case "DeviceGL":
							mWindowBase = new GLWindow(mMainActivity, primaryId, point, mGraphicId);
							mWindowBase.showWindow(screenx, screeny, mInterestPointLayer);
							mMainFragment.unCheckAddDeviceBtn();
							break;
						case "DeviceLK":
							mWindowBase = new LKWindow(mMainActivity, primaryId, point, mGraphicId);
							mWindowBase.showWindow(screenx, screeny, mInterestPointLayer);
							mMainFragment.unCheckAddDeviceBtn();
							break;
						case "DevicePD":
							mWindowBase = new PDWindow(mMainActivity, primaryId, point, mGraphicId);
							mWindowBase.showWindow(screenx, screeny, mInterestPointLayer);
							mMainFragment.unCheckAddDeviceBtn();
							break;
						case "DeviceKG":
							mWindowBase = new KBWindow(mMainActivity, primaryId, point, mGraphicId);
							mWindowBase.showWindow(screenx, screeny, mInterestPointLayer);
							mMainFragment.unCheckAddDeviceBtn();
							break;
						case "DeviceHW":
							mWindowBase = new HWWindow(mMainActivity, primaryId, point, mGraphicId);
							mWindowBase.showWindow(screenx, screeny, mInterestPointLayer);
							mMainFragment.unCheckAddDeviceBtn();
							break;
						case "DeviceGB":
							mWindowBase = new GBWindow(mMainActivity, primaryId, point, mGraphicId);
							mWindowBase.showWindow(screenx, screeny, mInterestPointLayer);
							mMainFragment.unCheckAddDeviceBtn();
							break;
						case "DeviceXB":
							mWindowBase = new XBWindow(mMainActivity, primaryId, point, mGraphicId);
							mWindowBase.showWindow(screenx, screeny, mInterestPointLayer);
							mMainFragment.unCheckAddDeviceBtn();
							break;
						}
						break;
					}
				}
			}else{
				mInterestPointLayer.clearSelection();
			}
			mGraphicId = ArcgisTool.getGraphicId(mMap, screenx, screeny, mInterestLineLayer);
			if (mGraphicId != -1) {
				Graphic lineGraphic = mInterestLineLayer.getGraphic(mGraphicId);
				if (lineGraphic != null) {
					Geometry geo = lineGraphic.getGeometry();
					if(geo.getType().value() == Type.POLYLINE.value()){
						Polyline line = (Polyline)geo;
						HashMap<String, Object> lineAttributes =  (HashMap<String, Object>) lineGraphic.getAttributes();
						int linePrimaryId = (int) lineAttributes.get(ArcgisTool.ATTRIBUTE_KEY_ID);
						String lineType = (String) lineAttributes.get(ArcgisTool.ATTRIBUTE_KEY_TYPE);
						mInterestLineLayer.setSelectedGraphics(new int[]{mGraphicId}, true);
						switch(lineType){
						case "LineFour":
							mWindowBase = new DXWindow(mMainActivity, linePrimaryId, line, mGraphicId, "4");
							mWindowBase.showWindow(screenx, screeny, mInterestLineLayer);
							mMainFragment.unCheckAddLineBtn();
							break;
						case "LineTwo":
							mWindowBase = new DXWindow(mMainActivity, linePrimaryId, line, mGraphicId, "2");
							mWindowBase.showWindow(screenx, screeny, mInterestLineLayer);
							mMainFragment.unCheckAddLineBtn();
							break;
						case "LineOne":
							mWindowBase = new DXWindow(mMainActivity, linePrimaryId, line, mGraphicId, "1");
							mWindowBase.showWindow(screenx, screeny, mInterestLineLayer);
							mMainFragment.unCheckAddLineBtn();
							break;
						}
						break;
					}
				}
			}else{
				mInterestLineLayer.clearSelection();
			}
			break;
		case MOVE:
			mWindowBase.getOnAfterMoveListener().doSomethingAfterMove(screenx, screeny);;
			break;
		case DRAW:
			mGraphicId = ArcgisTool.getGraphicId(mMap, screenx, screeny, mInterestPointLayer);
			mDrawLineManager.setSelectPoint(mInterestPointLayer, mGraphicId);
			break;
		case DELETE:
			mGraphicId = ArcgisTool.getGraphicId(mMap, screenx, screeny, mInterestLineLayer);
			mInterestLineLayer.setSelectedGraphics(new int[]{mGraphicId}, true);
			Graphic mGraphic = mInterestLineLayer.getGraphic(mGraphicId);
			if(mGraphic!=null){
				final int id = (int)(mGraphic.getAttributes().get(ArcgisTool.ATTRIBUTE_KEY_ID));
				DialogInterface.OnClickListener mPositiveListener = new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						mInterestLineLayer.clearSelection();
						if(id!=-1){
							boolean result = mMainFragment.getDXTable().delete(id);
							if(result) {
								mMainFragment.doLoadCircuit(mMainFragment.getCircuitTable().getCircuitID());
							}
						}else{
							Util.Toast("尚未添加");
						}
					}
				};
				DialogInterface.OnClickListener mNegativeListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						mInterestLineLayer.clearSelection();
					}
				};
				Util.ShowDialog(mMainActivity, "删除后数据不可恢复，确定要删除吗？", mPositiveListener, mNegativeListener);
			}
			break;
		case DONOTHING:
			break;
		}
	}
	
	
}
