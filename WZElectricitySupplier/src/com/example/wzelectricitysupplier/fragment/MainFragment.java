package com.example.wzelectricitysupplier.fragment;

import java.util.List;

import jsqlite.Database;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TextView;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.map.event.OnZoomListener;
import com.esri.android.runtime.ArcGISRuntime;
import com.esri.core.geometry.MultiPath;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.Symbol;
import com.example.wzelectricitysupplier.MainActivity;
import com.example.wzelectricitysupplier.MyApplication;
import com.example.wzelectricitysupplier.R;
import com.example.wzelectricitysupplier.bean.BDSRecord;
import com.example.wzelectricitysupplier.bean.CircuitRecord;
import com.example.wzelectricitysupplier.bean.DXRecord;
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
import com.example.wzelectricitysupplier.database.CircuitTable;
import com.example.wzelectricitysupplier.database.DXTable;
import com.example.wzelectricitysupplier.database.DefectTable;
import com.example.wzelectricitysupplier.database.GBTable;
import com.example.wzelectricitysupplier.database.GLTable;
import com.example.wzelectricitysupplier.database.GTTable;
import com.example.wzelectricitysupplier.database.HWTable;
import com.example.wzelectricitysupplier.database.KBTable;
import com.example.wzelectricitysupplier.database.LKTable;
import com.example.wzelectricitysupplier.database.PDTable;
import com.example.wzelectricitysupplier.database.RoutePointTable;
import com.example.wzelectricitysupplier.database.RouteTable;
import com.example.wzelectricitysupplier.database.SwitchTable;
import com.example.wzelectricitysupplier.database.XBTable;
import com.example.wzelectricitysupplier.dialog.BaseWindow;
import com.example.wzelectricitysupplier.dialog.DXWindow;
import com.example.wzelectricitysupplier.function.ArcgisTool;
import com.example.wzelectricitysupplier.function.Cache;
import com.example.wzelectricitysupplier.function.DrawLineManager;
import com.example.wzelectricitysupplier.function.DrawLineManager.OnSelectTwoPointListener;
import com.example.wzelectricitysupplier.function.GeodatabaseLayerManager;
import com.example.wzelectricitysupplier.function.ArcGISTiledLayerManager;
import com.example.wzelectricitysupplier.function.devicelocation.DeviceLocationManager;
import com.example.wzelectricitysupplier.function.devicelocation.DeviceLocationManager.LocationChangeListener;
import com.example.wzelectricitysupplier.function.messure.MessureDistanceManager;
import com.example.wzelectricitysupplier.function.route.RouteTakenManager;
import com.example.wzelectricitysupplier.listener.MyOnSingleTapListener;
import com.example.wzelectricitysupplier.listener.MyOnSingleTapListener.AddMode;
import com.example.wzelectricitysupplier.listener.MyOnSingleTapListener.OperaterKind;
import com.example.wzelectricitysupplier.setting.AppUtil;
import com.example.wzelectricitysupplier.setting.Constants;
import com.example.wzelectricitysupplier.setting.Util;

public class MainFragment extends Fragment implements Callback{
	
	public final static String TAG = "MainFragment";

	private MainActivity mMainActivity;
	private MapView mMap;
	private Handler mHandler;
	private Database mDatabase;
	private Cache mCache;
	
	private OperaterKind mCurrOperaterKind = OperaterKind.SELECT;
	private AddMode mDeviceAddMode = AddMode.DeviceBDS;
	private AddMode mLineAddMode = AddMode.LineOne;
	
	private ArcGISTiledLayerManager mTiledLayerManager; 	
	private GeodatabaseLayerManager mFeatureLayerManager;
	private MessureDistanceManager mMessureDistanceManager;
	private RouteTakenManager mRouteTakenManager;
	private DeviceLocationManager mDeviceLocationManager;
	private DrawLineManager mDrawLineManager;
	
	private GraphicsLayer mSearchLayer;				// 搜索结果图层
	private GraphicsLayer mCurLocationLayer;		// 定位点图层
	private GraphicsLayer mInterestPointLayer;		// 兴趣点图层
	private GraphicsLayer mInterestLineLayer;		// 兴趣线图层
	private GraphicsLayer mDeviceTextLayer;			// 项目名称图层
	
	private CircuitTable mCircuitTable;
	private RouteTable mRouteTable;
	private DefectTable mDefectTable;
	private BDSTable mBDSTable;
	private GTTable mGTTable;
	private SwitchTable mSwitchTable;
	private GLTable mGLTable;
	private LKTable mLKTable;
	private PDTable mPDTable;
	private KBTable mKBTable;
	private HWTable mHWTable;
	private GBTable mGBTable;
	private XBTable mXBTable;
	private DXTable mDXTable;
	
	private View mMarkView;
	private boolean mShowTextGraphic = false;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.e(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		mMainActivity = (MainActivity)getActivity();
		mHandler = new Handler(this);
		mDatabase = mMainActivity.getDatabase();
		mCache = mMainActivity.getCache();
		
		mCircuitTable = new CircuitTable(mDatabase);
		mRouteTable = new RouteTable(mDatabase);
		mDefectTable = new DefectTable(mDatabase);
		mBDSTable = new BDSTable(mDatabase);
		mSwitchTable = new SwitchTable(mDatabase);
		mGTTable = new GTTable(mDatabase);
		mGLTable = new GLTable(mDatabase);
		mLKTable = new LKTable(mDatabase);
		mPDTable = new PDTable(mDatabase);
		mKBTable = new KBTable(mDatabase);
		mHWTable = new HWTable(mDatabase);
		mGBTable = new GBTable(mDatabase);
		mXBTable = new XBTable(mDatabase);
		mDXTable = new DXTable(mDatabase);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.e(TAG, "onCreateView");
		View view = inflater.inflate(R.layout.fragment_main, null);
		mMarkView = view.findViewById(R.id.mark);

		// 初始化地图
		initArcgisMap(view);
		// 初始化缩进、缩出、定位按钮
		initAreaButtonGroup(view);
		// 初始化图层切换按钮
		initAreaMapSwitch(view);
		
		// 加载上次的路线
		int id = (Integer)mCache.read(Cache.KEY_CODE_CIRCUITID);
		if(id !=-1){
			doLoadCircuit(String.valueOf(id));
		}
		
		// 初始化底层栏
		initBottomBar(view);
		return view;
	}
	
	public void doLoadNoCircuit(){
		GraphicsLayer pointlayer = getInterestPointLayer();
		pointlayer.removeAll();
		GraphicsLayer linelayer = getInterestLineLayer();
		linelayer.removeAll();
		GraphicsLayer textlayer = getDeviceTextLayer();
		textlayer.removeAll();
	}
	
	public void doLoadCircuit(String idStr) {
		if(idStr.equals("")){
			doLoadNoCircuit();
			mCache.write(Cache.KEY_CODE_CIRCUITID, -1);
			mCircuitTable.setCircuitID("");
			return;
		}
		int circuitId = Integer.valueOf(idStr);
		mCache.write(Cache.KEY_CODE_CIRCUITID, circuitId);
		mCircuitTable.setCircuitID(String.valueOf(circuitId));
		CircuitRecord mCircuitRecord = mCircuitTable.SelectCircuitRecord(circuitId);
		GraphicsLayer pointlayer = getInterestPointLayer();
		pointlayer.removeAll();
		GraphicsLayer linelayer = getInterestLineLayer();
		linelayer.removeAll();
		GraphicsLayer textlayer = getDeviceTextLayer();
		textlayer.removeAll();
		
		List<DXRecord>  mDXList = mDXTable.getElectricWireList(circuitId);
		for(DXRecord record : mDXList){
			MultiPath path = (MultiPath)record.getGeometry();
			int id = record.getId();
			int num = Integer.valueOf(record.mNum).intValue();
			String deviceName1 = record.mDeviceName1;
			String deviceName2 = record.mDeviceName2;
			String type = record.mType;
			AddMode mode= null;
			Symbol symbol = null;
			String text = "";
			switch(num){
			case 1:
				mode = AddMode.LineOne;
				symbol = ArcgisTool.judgeLineOneSymbolColor(this, type, deviceName1, deviceName2);
				text = record.mHeight;
				ArcgisTool.addTextGraphic(record.getLineMidPoint(), text, mMap, textlayer, android.R.color.holo_green_light);
				break;
			case 2:
				mode = AddMode.LineTwo;
				symbol = ArcgisTool.getLineTwoSymbol(type);
				if(mCircuitRecord.mRemark.contains("、")){
					String[] arr = mCircuitRecord.mRemark.split("、");
					for(String s : arr){
						text += s + "\n";
					}
					text = text.substring(0, text.lastIndexOf("\n"));
				}else{
					text = mCircuitRecord.mRemark;
				}
				ArcgisTool.addTextGraphic(record.getLineMidPoint(), text, mMap, textlayer, R.color.black);
				break;
			case 4:
				mode = AddMode.LineFour;
				symbol = ArcgisTool.getLineFourSymbol(type);
				if(mCircuitRecord.mRemark.contains("、")){
					String[] arr = mCircuitRecord.mRemark.split("、");
					for(String s : arr){
						text += s + "\n";
					}
					text = text.substring(0, text.lastIndexOf("\n"));
				}else{
					text = mCircuitRecord.mRemark;
				}
				ArcgisTool.addTextGraphic(record.getLineMidPoint(), text, mMap, textlayer, R.color.black);
				break;
			}
			int graphicid = ArcgisTool.addLineGraphic(path, symbol, linelayer);
			ArcgisTool.updateGraphic(graphicid, linelayer, id, mode);
			
		}
		List<BDSRecord>  mBDSList = mBDSTable.getSubstationList(circuitId);
		for(BDSRecord record : mBDSList){
			Point point = (Point) record.getGeometry();
			int id = record.getId();
			AddMode mode = AddMode.DeviceBDS;
			int graphicid = ArcgisTool.addGraphic(point, R.drawable.icon_small_bds, pointlayer);
			ArcgisTool.updateGraphic(graphicid, pointlayer, id, mode);
			String text = "";
			if(record.mName.contains(" ")){
				text = record.mName.split(" ")[1];
			}
			ArcgisTool.addTextGraphic(point, text, mMap, textlayer, 5, 0);
		}
		List<GTRecord>  mGTList = mGTTable.getTowerList(circuitId);
		for(GTRecord record : mGTList){
			Point point = (Point) record.getGeometry();
			int id = record.getId();
			AddMode mode = AddMode.DeviceGT;
			int graphicid = ArcgisTool.addGraphic(point, R.drawable.icon_small_gt, pointlayer);
			ArcgisTool.updateGraphic(graphicid, pointlayer, id, mode);
			String text = "";
			if(record.mName.contains(" ")){
				text = record.mName.split(" ")[1];
			}
			if(!record.mType.equals("直线杆")){
				ArcgisTool.addTextGraphic(point, text, mMap, textlayer, 5, 0);
			}
		}
		List<SwitchRecord>  mSwitchList = mSwitchTable.getSwitchList(circuitId);
		for(SwitchRecord record : mSwitchList){
			Point point = (Point) record.getGeometry();
			int id = record.getId();
			AddMode mode = AddMode.DeviceSwitch;
			int graphicid = ArcgisTool.addGraphic(point, R.drawable.icon_small_switch, pointlayer);
			ArcgisTool.updateGraphic(graphicid, pointlayer, id, mode);
			String text = "";
			if(record.mName.contains(" ")){
				text = record.mName.split(" ")[1];
			}
			ArcgisTool.addTextGraphic(point, text, mMap, textlayer, 5, 10);
		}
		List<GLRecord>  mGLList = mGLTable.getDisconnectoreList(circuitId);
		for(GLRecord record : mGLList){
			Point point = (Point) record.getGeometry();
			int id = record.getId();
			AddMode mode = AddMode.DeviceGL;
			int graphicid = ArcgisTool.addGraphic(point, R.drawable.icon_small_gl, pointlayer);
			ArcgisTool.updateGraphic(graphicid, pointlayer, id, mode);
			String text = "";
			if(record.mName.contains(" ")){
				text = record.mName.split(" ")[1];
			}
			ArcgisTool.addTextGraphic(point, text, mMap, textlayer, 5, 20);
		}
		List<LKRecord>  mLKList = mLKTable.getLineConnectorList(circuitId);
		for(LKRecord record : mLKList){
			Point point = (Point) record.getGeometry();
			int id = record.getId();
			AddMode mode = AddMode.DeviceLK;
			int graphicid = ArcgisTool.addGraphic(point, R.drawable.icon_small_lk, pointlayer);
			ArcgisTool.updateGraphic(graphicid, pointlayer, id, mode);
			String text = "";
			if(record.mName.contains(" ")){
				text = record.mName.split(" ")[1];
			}
			ArcgisTool.addTextGraphic(point, text, mMap, textlayer, 5, -10);
		}
		List<PDRecord>  mPDList = mPDTable.getSwitchingRoomList(circuitId);
		for(PDRecord record : mPDList){
			Point point = (Point) record.getGeometry();
			int id = record.getId();
			AddMode mode = AddMode.DevicePD;
			int graphicid = ArcgisTool.addGraphic(point, R.drawable.icon_small_pd, pointlayer);
			ArcgisTool.updateGraphic(graphicid, pointlayer, id, mode);
			String text = "";
			if(record.mName.contains(" ")){
				text = record.mName.split(" ")[1];
			}
			ArcgisTool.addTextGraphic(point, text, mMap, textlayer, 5, 0);
		}
		List<KBRecord>  mKBList = mKBTable.getSwitchingStationList(circuitId);
		for(KBRecord record : mKBList){
			Point point = (Point) record.getGeometry();
			int id = record.getId();
			AddMode mode = AddMode.DeviceKG;
			int graphicid = ArcgisTool.addGraphic(point, R.drawable.icon_small_kg, pointlayer);
			ArcgisTool.updateGraphic(graphicid, pointlayer, id, mode);
			String text = "";
			if(record.mName.contains(" ")){
				text = record.mName.split(" ")[1];
			}
			ArcgisTool.addTextGraphic(point, text, mMap, textlayer, 5, 0);
		}
		List<HWRecord>  mHWList = mHWTable.getRingMainUnitList(circuitId);
		for(HWRecord record : mHWList){
			Point point = (Point) record.getGeometry();
			int id = record.getId();
			AddMode mode = AddMode.DeviceHW;
			int graphicid = ArcgisTool.addGraphic(point, R.drawable.icon_small_hw, pointlayer);
			ArcgisTool.updateGraphic(graphicid, pointlayer, id, mode);
			String text = "";
			if(record.mName.contains(" ")){
				text = record.mName.split(" ")[1];
			}
			ArcgisTool.addTextGraphic(point, text, mMap, textlayer, 5, 0);
		}
		List<GBRecord>  mGBList = mGBTable.getBarTypeVariableList(circuitId);
		for(GBRecord record : mGBList){
			Point point = (Point) record.getGeometry();
			int id = record.getId();
			AddMode mode = AddMode.DeviceGB;
			int graphicid = ArcgisTool.addGraphic(point, R.drawable.icon_small_gb, pointlayer);
			ArcgisTool.updateGraphic(graphicid, pointlayer, id, mode);
			String text = "";
			if(record.mName.contains(" ")){
				text = record.mName.split(" ")[1];
			}
			ArcgisTool.addTextGraphic(point, text, mMap, textlayer, 5, 10);
		}
		List<XBRecord>  mXBList = mXBTable.getBoxChangeList(circuitId);
		for(XBRecord record : mXBList){
			Point point = (Point) record.getGeometry();
			int id = record.getId();
			AddMode mode = AddMode.DeviceXB;
			int graphicid = ArcgisTool.addGraphic(point, R.drawable.icon_small_xb, pointlayer);
			ArcgisTool.updateGraphic(graphicid, pointlayer, id, mode);
			String text = "";
			if(record.mName.contains(" ")){
				text = record.mName.split(" ")[1];
			}
			ArcgisTool.addTextGraphic(point, text, mMap, textlayer, 5, 0);
		}
		if(mShowTextGraphic)
			textlayer.setVisible(true);
		else
			textlayer.setVisible(false);
	}

	private void initAreaButtonGroup(View view){
		ImageButton mLocationBtn = (ImageButton) view.findViewById(R.id.LocationBtn);
		mLocationBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDeviceLocationManager.AutoPanToLocationNextTime();
				mRouteTakenManager.drawHistoryRoute(2);
			}
		});
		
		ImageButton mZoomInBtn = (ImageButton) view.findViewById(R.id.ZoomInBtn);
		mZoomInBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mMap.zoomin();
				Log.i(TAG, "当前比例尺：" + mMap.getScale());
//				Util.Toast("最大比例尺：" + mMap.getMaxScale());
//				Util.Toast("当前比例尺：" + mMap.getScale());
//				Util.Toast("最小比例尺：" + mMap.getMinScale());
			}
		});
		ImageButton mZoomOutBtn = (ImageButton) view.findViewById(R.id.ZoomOutBtn);
		mZoomOutBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mMap.zoomout();
			}
		});
	}
	
	private void initAreaMapSwitch(View view){
		RadioButton mTurnVector = (RadioButton) view.findViewById(R.id.turnVector);
		mTurnVector.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					mTiledLayerManager.switchMap(ArcGISTiledLayerManager.MAP_MODE.MAP1);		
				}
			}
		});
		RadioButton mTurnTwofive = (RadioButton) view.findViewById(R.id.turnTwofive);
		mTurnTwofive.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					mTiledLayerManager.switchMap(ArcGISTiledLayerManager.MAP_MODE.MAP2);		
				}
			}
		});
		RadioButton mTurnImage = (RadioButton) view.findViewById(R.id.turnImage);
		mTurnImage.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					mTiledLayerManager.switchMap(ArcGISTiledLayerManager.MAP_MODE.MAP3);		
				}
			}
		});
	}
	
	private View mMessageArea;
	private TextView mBottomBarPrompt;
	private ViewGroup mTuli;
	private ViewGroup mLineContainer;
	private CheckBox mAddEquipmentBtn;
	private CheckBox mAddLineBtn;
	private CheckBox mDeleteLineBtn;
	private CheckBox measureDistanceBtn;
	private View initBottomBar(View view){
		final View mAreaCableChoose = view.findViewById(R.id.AreaCableChoose);
		mTuli = (ViewGroup)view.findViewById(R.id.tuli);
		mLineContainer = (ViewGroup)mTuli.findViewById(R.id.Container);
		for(int i=0; i<mCircuitTable.getBranchName().size(); i++){
			if(ArcgisTool.getColorByIndex(i)!=-1)
				adLineExplainRow(mLineContainer, mCircuitTable.getBranchName().get(i), ArcgisTool.getColorByIndex(i));
		}
		final RadioButton mChooseLineOneBtn = (RadioButton)mAreaCableChoose.findViewById(R.id.ChooseLineOneBtn);
		mChooseLineOneBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					SetMessageAreaText("添加支线 选择起点");
					mDrawLineManager.StartDrawLine(new OnSelectTwoPointListener() {
						@Override
						public void doSomethingAfterSelectTwoPoint(final Graphic graphic1, final Graphic graphic2) {
							Util.ShowDialog(mMainActivity, "确定添加支线吗？", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									Point point1 = (Point)graphic1.getGeometry();
									Point point2 = (Point)graphic2.getGeometry();
									int id1 = (int)graphic1.getAttributes().get(ArcgisTool.ATTRIBUTE_KEY_ID);
									int id2 = (int)graphic2.getAttributes().get(ArcgisTool.ATTRIBUTE_KEY_ID);
									String type1 = (String) graphic1.getAttributes().get(ArcgisTool.ATTRIBUTE_KEY_TYPE);
									String type2 = (String) graphic2.getAttributes().get(ArcgisTool.ATTRIBUTE_KEY_TYPE);
									String name1 = AppUtil.getDeviceRecordByDefectRecord(MainFragment.this, id1, type1).mName;
									String name2 = AppUtil.getDeviceRecordByDefectRecord(MainFragment.this, id2, type2).mName;
									Polyline path = new Polyline();
									path.startPath(point1);
									path.lineTo(point2);
									Symbol symbol = ArcgisTool.judgeLineOneSymbolColor(MainFragment.this, null, name1, name2);
									int lineGraphicId = mDrawLineManager.drawLine(path, symbol, null);
									mInterestPointLayer.clearSelection();
									for(int graphicid : mInterestPointLayer.getGraphicIDs()){
										mInterestPointLayer.bringToFront(graphicid);
									}
									BaseWindow mWindowBase = new DXWindow(mMainActivity, path, lineGraphicId, name1, name2, "1");
									mWindowBase.showWindow(0, 0, mInterestLineLayer); // 电线的对话框中 xy坐标没有用，所以写出0
									mWindowBase.toEditView();
								}
							}, new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									MainFragment.this.getInterestPointLayer().clearSelection();
								}
							} );
							
						}
					});
				}else{
					MainFragment.this.getInterestPointLayer().clearSelection();
				}
			}
		});
		final RadioButton mChooseLineTwoBtn = (RadioButton)mAreaCableChoose.findViewById(R.id.ChooseLineTwoBtn);
		mChooseLineTwoBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(mCircuitTable!=null){
					int branchNum = mCircuitTable.getBranchName().size();
					if(branchNum<2){
						if(isChecked){
							mChooseLineTwoBtn.setChecked(false);
							Util.Toast("当前线路不允许添加双回线");
						}
					}else{
						if(isChecked){
							SetMessageAreaText("添加同杆双回线 选择起点");
							mDrawLineManager.StartDrawLine(new OnSelectTwoPointListener() {
								@Override
								public void doSomethingAfterSelectTwoPoint(final Graphic graphic1, final Graphic graphic2) {
									Util.ShowDialog(mMainActivity, "确定添加同杆双回线吗？", new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											Point point1 = (Point)graphic1.getGeometry();
											Point point2 = (Point)graphic2.getGeometry();
											int id1 = (int)graphic1.getAttributes().get(ArcgisTool.ATTRIBUTE_KEY_ID);
											int id2 = (int)graphic2.getAttributes().get(ArcgisTool.ATTRIBUTE_KEY_ID);
											String type1 = (String) graphic1.getAttributes().get(ArcgisTool.ATTRIBUTE_KEY_TYPE);
											String type2 = (String) graphic2.getAttributes().get(ArcgisTool.ATTRIBUTE_KEY_TYPE);
											String name1 = AppUtil.getDeviceRecordByDefectRecord(MainFragment.this, id1, type1).mName;
											String name2 = AppUtil.getDeviceRecordByDefectRecord(MainFragment.this, id2, type2).mName;
											Polyline path = new Polyline();
											path.startPath(point1);
											path.lineTo(point2);
											Symbol symbol = ArcgisTool.getLineTwoSymbol(null);
											int lineGraphicId = mDrawLineManager.drawLine(path, symbol, null);
											mInterestPointLayer.clearSelection();
											for(int graphicid : mInterestPointLayer.getGraphicIDs()){
												mInterestPointLayer.bringToFront(graphicid);
											}
											BaseWindow mWindowBase = new DXWindow(mMainActivity, path, lineGraphicId, name1, name2, "2");
											mWindowBase.showWindow(0, 0, mInterestLineLayer);
											mWindowBase.toEditView();
										}
									}, new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											MainFragment.this.getInterestPointLayer().clearSelection();
										}
									} );
									
								}
							});
						}else{
							MainFragment.this.getInterestPointLayer().clearSelection();
						}
					}
				}
			}
		});
		final RadioButton mChooseLineFourBtn = (RadioButton)mAreaCableChoose.findViewById(R.id.ChooseLineFourBtn);
		mChooseLineFourBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(mCircuitTable!=null){
					int branchNum = mCircuitTable.getBranchName().size();
					if(branchNum<4){
						if(isChecked){
							mChooseLineFourBtn.setChecked(false);
							Util.Toast("当前线路不允许添加四回线");
						}
					}else{
						if(isChecked){
							SetMessageAreaText("添加同杆四回线 选择起点");
							mDrawLineManager.StartDrawLine(new OnSelectTwoPointListener() {
								@Override
								public void doSomethingAfterSelectTwoPoint(final Graphic graphic1, final Graphic graphic2) {
									Util.ShowDialog(mMainActivity, "确定添加同杆四回线吗？", new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											Point point1 = (Point)graphic1.getGeometry();
											Point point2 = (Point)graphic2.getGeometry();
											int id1 = (int)graphic1.getAttributes().get(ArcgisTool.ATTRIBUTE_KEY_ID);
											int id2 = (int)graphic2.getAttributes().get(ArcgisTool.ATTRIBUTE_KEY_ID);
											String type1 = (String) graphic1.getAttributes().get(ArcgisTool.ATTRIBUTE_KEY_TYPE);
											String type2 = (String) graphic2.getAttributes().get(ArcgisTool.ATTRIBUTE_KEY_TYPE);
											String name1 = AppUtil.getDeviceRecordByDefectRecord(MainFragment.this, id1, type1).mName;
											String name2 = AppUtil.getDeviceRecordByDefectRecord(MainFragment.this, id2, type2).mName;
											Polyline path = new Polyline();
											path.startPath(point1);
											path.lineTo(point2);
											Symbol symbol = ArcgisTool.getLineFourSymbol(null);
											int lineGraphicId = mDrawLineManager.drawLine(path, symbol, null);
											mInterestPointLayer.clearSelection();
											for(int graphicid : mInterestPointLayer.getGraphicIDs()){
												mInterestPointLayer.bringToFront(graphicid);
											}
											BaseWindow mWindowBase = new DXWindow(mMainActivity, path, lineGraphicId, name1, name2, "4");
											mWindowBase.showWindow(0, 0, mInterestLineLayer);
											mWindowBase.toEditView();
										}
									}, new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											MainFragment.this.getInterestPointLayer().clearSelection();
										}
									} );
									
								}
							});
						}else{
							MainFragment.this.getInterestPointLayer().clearSelection();
						}
					}
				}
			}
		});
		final View mAreaDeviceChoose = view.findViewById(R.id.AreaDeviceChoose);
		RadioButton mChooseBDSBtn = (RadioButton)mAreaDeviceChoose.findViewById(R.id.ChooseBDSBtn);
		mChooseBDSBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					mDeviceAddMode = AddMode.DeviceBDS;
					SetMessageAreaText("点击添加变电所");
				}
			}
		});
		RadioButton mChooseGTBtn = (RadioButton)mAreaDeviceChoose.findViewById(R.id.ChooseGTBtn);
		mChooseGTBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					mDeviceAddMode = AddMode.DeviceGT;
					SetMessageAreaText("点击添加杆塔");
				}
			}
		});
		RadioButton mChooseSwitchBtn = (RadioButton)mAreaDeviceChoose.findViewById(R.id.ChooseSwitchBtn);
		mChooseSwitchBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					mDeviceAddMode = AddMode.DeviceSwitch;
					SetMessageAreaText("点击添加开关");
				}
			}
		});
		RadioButton mChooseGLBtn = (RadioButton)mAreaDeviceChoose.findViewById(R.id.ChooseGLBtn);
		mChooseGLBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					mDeviceAddMode = AddMode.DeviceGL;
					SetMessageAreaText("点击添加隔离开关");
				}
			}
		});
		RadioButton mChooseLKBtn = (RadioButton)mAreaDeviceChoose.findViewById(R.id.ChooseLKBtn);
		mChooseLKBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					mDeviceAddMode = AddMode.DeviceLK;
					SetMessageAreaText("点击添加令克");
				}
			}
		});
		RadioButton mChooseGBBtn = (RadioButton)mAreaDeviceChoose.findViewById(R.id.ChooseGBBtn);
		mChooseGBBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					mDeviceAddMode = AddMode.DeviceGB;
					SetMessageAreaText("点击添加杠式变");
				}
			}
		});
		RadioButton mChoosePDBtn = (RadioButton)mAreaDeviceChoose.findViewById(R.id.ChoosePDBtn);
		mChoosePDBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					mDeviceAddMode = AddMode.DevicePD;
					SetMessageAreaText("点击添加配电室");
				}
			}
		});
		RadioButton mChooseKGBtn = (RadioButton)mAreaDeviceChoose.findViewById(R.id.ChooseKGBtn);
		mChooseKGBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					mDeviceAddMode = AddMode.DeviceKG;
					SetMessageAreaText("点击添加开闭所");
				}
			}
		});
		RadioButton mChooseHWBtn = (RadioButton)mAreaDeviceChoose.findViewById(R.id.ChooseHWBtn);
		mChooseHWBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					mDeviceAddMode = AddMode.DeviceHW;
					SetMessageAreaText("点击添加环网单元");
				}
			}
		});
		RadioButton mChooseXBBtn = (RadioButton)mAreaDeviceChoose.findViewById(R.id.ChooseXBBtn);
		mChooseXBBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					mDeviceAddMode = AddMode.DeviceXB;
					SetMessageAreaText("点击添加箱变");
				}
			}
		});
		
		mMessageArea = view.findViewById(R.id.MessageArea);
		mBottomBarPrompt = (TextView)view.findViewById(R.id.BottomBarPrompt);
		mAddEquipmentBtn = (CheckBox)view.findViewById(R.id.addEquipmentBtn);
		mAddLineBtn = (CheckBox)view.findViewById(R.id.addLineBtn);
		mDeleteLineBtn = (CheckBox)view.findViewById(R.id.deleteLineBtn);
		measureDistanceBtn = (CheckBox)view.findViewById(R.id.measureDistanceBtn);
		mAddEquipmentBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					mAreaDeviceChoose.setVisibility(View.VISIBLE);
					SetMessageAreaText("选择要添加的设备");
					mCurrOperaterKind = OperaterKind.ADD;
					mAddLineBtn.setChecked(!isChecked);
					mDeleteLineBtn.setChecked(!isChecked);
					measureDistanceBtn.setChecked(!isChecked);
					mInterestPointLayer.clearSelection();
				} else{
					mAreaDeviceChoose.setVisibility(View.GONE);
					mMessageArea.setVisibility(View.GONE);
					mCurrOperaterKind = OperaterKind.SELECT;
					if(mAddLineBtn.isChecked()) {
						mCurrOperaterKind = OperaterKind.ADD;
						mMessageArea.setVisibility(View.VISIBLE);
					}
					if(mDeleteLineBtn.isChecked()){
						mCurrOperaterKind = OperaterKind.DELETE;
						mMessageArea.setVisibility(View.VISIBLE);
					} 
					if(measureDistanceBtn.isChecked()){
						mCurrOperaterKind = OperaterKind.DONOTHING;
						mMessageArea.setVisibility(View.VISIBLE);
					} 
				}
			}
		});
		mAddLineBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					mAreaCableChoose.setVisibility(View.VISIBLE);
					SetMessageAreaText("选择要添加的线缆");
					mCurrOperaterKind = OperaterKind.DRAW;
					mChooseLineOneBtn.setChecked(true);
					mAddEquipmentBtn.setChecked(!isChecked);
					mDeleteLineBtn.setChecked(!isChecked);
					measureDistanceBtn.setChecked(!isChecked);
					mInterestPointLayer.clearSelection();
				} else{
					getInterestPointLayer().clearSelection();
					mAreaCableChoose.setVisibility(View.GONE);
					mMessageArea.setVisibility(View.GONE);
					mCurrOperaterKind = OperaterKind.SELECT;
					mDrawLineManager.StopDrawLine();
					mInterestLineLayer.clearSelection();
					
					if(mAddEquipmentBtn.isChecked()) {
						mCurrOperaterKind = OperaterKind.ADD;
						mMessageArea.setVisibility(View.VISIBLE);
					}
					if(mDeleteLineBtn.isChecked()){
						mCurrOperaterKind = OperaterKind.DELETE;
						mMessageArea.setVisibility(View.VISIBLE);
					} 
					if(measureDistanceBtn.isChecked()){
						mCurrOperaterKind = OperaterKind.DONOTHING;
						mMessageArea.setVisibility(View.VISIBLE);
					} 
				}
			}
		});
		mDeleteLineBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					SetMessageAreaText("点击一条线缆进行删除操作");
					mCurrOperaterKind = OperaterKind.DELETE;
					mAddEquipmentBtn.setChecked(!isChecked);
					mAddLineBtn.setChecked(!isChecked);
					measureDistanceBtn.setChecked(!isChecked);
					mInterestPointLayer.clearSelection();
				} else{
					mMessageArea.setVisibility(View.GONE);
					mCurrOperaterKind = OperaterKind.SELECT;
					if(mAddEquipmentBtn.isChecked()) {
						mCurrOperaterKind = OperaterKind.ADD;
						mMessageArea.setVisibility(View.VISIBLE);
					}
					if(mAddLineBtn.isChecked()){
						mCurrOperaterKind = OperaterKind.ADD;
						mMessageArea.setVisibility(View.VISIBLE);
					} 
					if(measureDistanceBtn.isChecked()){
						mCurrOperaterKind = OperaterKind.DONOTHING;
						mMessageArea.setVisibility(View.VISIBLE);
					} 
				}
			}
		});
		measureDistanceBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					mMessureDistanceManager.StartMessure();
					SetMessageAreaText("距离测量:请选择测量起点");
					mCurrOperaterKind = OperaterKind.DONOTHING;
					mAddEquipmentBtn.setChecked(!isChecked);
					mAddLineBtn.setChecked(!isChecked);
					mDeleteLineBtn.setChecked(!isChecked);
					mInterestPointLayer.clearSelection();
				} else{
					mMessureDistanceManager.StopMessure();
					mMessageArea.setVisibility(View.GONE);
					mCurrOperaterKind = OperaterKind.SELECT;
					if(mAddEquipmentBtn.isChecked()) {
						mCurrOperaterKind = OperaterKind.ADD;
						mMessageArea.setVisibility(View.VISIBLE);
					}
					if(mAddLineBtn.isChecked()){
						mCurrOperaterKind = OperaterKind.ADD;
						mMessageArea.setVisibility(View.VISIBLE);
					} 
					if(mDeleteLineBtn.isChecked()){
						mCurrOperaterKind = OperaterKind.DELETE;
						mMessageArea.setVisibility(View.VISIBLE);
					} 
				}
			}
		});		
		ImageButton mAreaCableHideBtn = (ImageButton)view.findViewById(R.id.AreaCableHideBtn);
		mAreaCableHideBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mAddLineBtn.setChecked(false);
			}
		});
		ImageButton mAreaDeviceHideBtn = (ImageButton)view.findViewById(R.id.AreaDeviceHideBtn);
		mAreaDeviceHideBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mAddEquipmentBtn.setChecked(false);
			}
		});
		return view;
	}
	
	public void SetMessageAreaText(String text){
		mMessageArea.setVisibility(View.VISIBLE);
		mBottomBarPrompt.setText(text);
	}
	
	
	private void initArcgisMap(View view) {
		mMap = (MapView) view.findViewById(R.id.mapView);
		mMap.setEsriLogoVisible(false);
		mMap.setMapBackground(0xffffff, 0xffffff, 0, 0);// 设置地图背景全白无网格
		ArcGISRuntime.setClientId("1eFHW78avlnRUPHm");// 去除水印
		
//		mMap.setMaxResolution(3.437082168079019E-4); // 设置最大分辨率
//		mMap.setMinResolution(2.6852217450794713E-6);// 设置最小分辨率
//		mMap.setResolution(3.437082168079019E-4);
		
		String mLocalMapDirRootPath = Util.getLocalMapDirRootPath(); // 离线地图根目录
			
		// 设置切片地图路径
		String mLocalLayerPath1 = mLocalMapDirRootPath + Constants.MAP_LOCAL_DIR1 + "/" + Constants.MAP_LOCAL_FILE1;
		String mLocalLayerPath2 = mLocalMapDirRootPath + Constants.MAP_LOCAL_DIR2 + "/" + Constants.MAP_LOCAL_FILE2;
		String mLocalLayerPath3 = mLocalMapDirRootPath + Constants.MAP_LOCAL_DIR3 + "/" + Constants.MAP_LOCAL_FILE3;
		// 底图管理器
		mTiledLayerManager = new ArcGISTiledLayerManager(this, (boolean) mCache.read(Cache.KEY_CODE_LOCALMAP), 
				mLocalLayerPath1, mLocalLayerPath2, mLocalLayerPath3);
		mTiledLayerManager.load(ArcGISTiledLayerManager.MAP_MODE.MAP1);
		
		// 测距功能管理器
		mMessureDistanceManager = new MessureDistanceManager(mMap);
		// 路线记录管理器
		mRouteTakenManager = new RouteTakenManager(mMap, new RouteTable(mDatabase), new RoutePointTable(mDatabase));
		// 设备定位功能管理器		
		mDeviceLocationManager = new DeviceLocationManager(mMap, new LocationChangeListener() {
			@Override
			public void onDeviceLocationChanged(Point point) {
				MenuFragment mMenuFragment = mMainActivity.getMenuFragment();
				mMenuFragment.setLongitudeLatitude(point.getX(), point.getY());
				mRouteTakenManager.recordPostion(point);
			}
		});
		mDrawLineManager = new DrawLineManager(this, getInterestLineLayer());
		
		mMap.setOnSingleTapListener(new MyOnSingleTapListener(this));
		mMap.setOnStatusChangedListener(new OnStatusChangedListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void onStatusChanged(Object source, STATUS status) {
				if (status == STATUS.INITIALIZED) {
					if(mDeviceLocationManager.AllowLocation()){
						mDeviceLocationManager.StartLocation();
					}
				}
			}
		});
		
		mMap.setOnZoomListener(new OnZoomListener() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void preAction(float pivotX, float pivotY, double factor) {
				
			}
			
			@Override
			public void postAction(float pivotX, float pivotY, double factor) {
				double currentScale = mMap.getScale();
				if((int)currentScale <4000){
					if(mShowTextGraphic!=true){
						mShowTextGraphic = true;
						mDeviceTextLayer.setVisible(true);
					}  
				}else{
					if(mShowTextGraphic!=false){
						mShowTextGraphic = false;
						mDeviceTextLayer.setVisible(false);
					}
				}
			}
		});
	}

	public MapView getMap() {
		return mMap;
	}
	public Handler getHandler() {
		return mHandler;
	}
	public GraphicsLayer getInterestPointLayer() {
		if (mInterestPointLayer == null) {
			mInterestPointLayer = new GraphicsLayer();
			mMap.addLayer(mInterestPointLayer);
		}
		return mInterestPointLayer;
	}
	public GraphicsLayer getInterestLineLayer() {
		if (mInterestLineLayer == null) {
			mInterestLineLayer = new GraphicsLayer();
			mMap.addLayer(mInterestLineLayer);
		}
		return mInterestLineLayer;
	}
	public GraphicsLayer getDeviceTextLayer() {
		if (mDeviceTextLayer == null) {
			mDeviceTextLayer = new GraphicsLayer();
			mMap.addLayer(mDeviceTextLayer);
		}
		return mDeviceTextLayer;
	}
	public GraphicsLayer getLocationLayer() {
		if (mCurLocationLayer == null) {
			mCurLocationLayer = new GraphicsLayer();
			mMap.addLayer(mCurLocationLayer);
		}
		return mCurLocationLayer;
	}
	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case Constants.MESSAGE_WHAT_BASE:
			
			break;

		default:
			break;
		}
		return false;
	}
	
	public AddMode getAddMode(){
		if(mAddLineBtn.isChecked())
			return mLineAddMode;
		if(mAddEquipmentBtn.isChecked())
			return mDeviceAddMode;
		return null;
	}
	public OperaterKind getOperaterKind(){
		return mCurrOperaterKind;
	}
	public void setOperaterKind(OperaterKind kind){
		mCurrOperaterKind = kind;
	}
	public View getMarkView(){
		return mMarkView;
	}

	public String CheckMessureLine(float x, float y){
		String length = mMessureDistanceManager.CheckMessureLine(x, y);
		if(measureDistanceBtn.isChecked() && length!=null){
			SetMessageAreaText(length);
		}
		return length;
	}
	public ArcGISTiledLayerManager getTiledLayerManager(){
		return mTiledLayerManager;
	}
	public RouteTakenManager getRouteTakenManager(){
		return mRouteTakenManager;
	}
	public DrawLineManager getDrawLineManager(){
		return mDrawLineManager;
	}
	
	/************ 获取数据库表格 ************/
	public CircuitTable getCircuitTable(){
		return mCircuitTable;
	}
	public RouteTable getRouteTable(){
		return mRouteTable;
	}
	public DefectTable getDefectTable(){
		return mDefectTable;
	}
	public BDSTable getBDSTable(){
		return mBDSTable;
	}
	public GTTable getGTTable(){
		return mGTTable;
	}
	public SwitchTable getSwitchTable(){
		return mSwitchTable;
	}
	public GLTable getGLTable(){
		return mGLTable;
	}
	public LKTable getLKTable(){
		return mLKTable;
	}
	public PDTable getPDTable(){
		return mPDTable;
	}
	public KBTable getKBTable(){
		return mKBTable;
	}
	public HWTable getHWTable(){
		return mHWTable;
	}
	public GBTable getGBTable(){
		return mGBTable;
	}
	public XBTable getXBTable(){
		return mXBTable;
	}
	public DXTable getDXTable(){
		return mDXTable;
	}
	
	public void unCheckAddDeviceBtn(){
		mAddEquipmentBtn.setChecked(false);;
	}
	public void unCheckAddLineBtn(){
		mAddLineBtn.setChecked(false);;
	}
	
	public void hideTuli(boolean b){
		if(b){
			mTuli.setVisibility(View.GONE);
		}else{
			mTuli.setVisibility(View.VISIBLE);
		}
	}
	// 添加图例线路解释部分
	private void adLineExplainRow(ViewGroup parent, String name, int color){
		View view = MyApplication.Inflater.inflate(R.layout.row_tuli_miditem, null);
		TableLayout.LayoutParams params = new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(0, Util.Dip2Px(mMainActivity, 0), 0, 0);
		view.setLayoutParams(params);
		ImageView mCircuitColor = (ImageView)view.findViewById(R.id.CircuitColor);
		mCircuitColor.setBackgroundColor(color);
		
		TextView mCircuitName = (TextView)view.findViewById(R.id.CircuitName);
		mCircuitName.setText(name);
		parent.addView(view);
	}
	// 更新图例
	public void refreshTuli(){
		mLineContainer.removeAllViews();
		for(int i=0; i<mCircuitTable.getBranchName().size(); i++){
			if(ArcgisTool.getColorByIndex(i)!=-1)
				adLineExplainRow(mLineContainer, mCircuitTable.getBranchName().get(i), ArcgisTool.getColorByIndex(i));
		}
	}
}
