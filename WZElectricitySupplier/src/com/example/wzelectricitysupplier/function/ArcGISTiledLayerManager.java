package com.example.wzelectricitysupplier.function;

import java.io.File;

import com.esri.android.map.MapView;
import com.esri.android.map.TiledLayer;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.example.wzelectricitysupplier.MainActivity;
import com.example.wzelectricitysupplier.fragment.MainFragment;
import com.example.wzelectricitysupplier.setting.Constants;
import com.example.wzelectricitysupplier.setting.Util;
import com.example.wzelectricitysupplier.tianditu.TianDiTuLayer;

public class ArcGISTiledLayerManager {
	
	private MainActivity mMainActivity;
	private MapView mMap;
	private MAP_MODE mMode = MAP_MODE.TDT;
	private TiledLayer mLayer; // 当前显示图层
	private TianDiTuLayer mTianDiTuLayer; // 天地图
	private TianDiTuLayer mTianDiTuAnnotationLayer; // 天地图注释
	private ArcGISTiledMapServiceLayer mServiceLayer1; 	// 外网图层一
	private ArcGISTiledMapServiceLayer mServiceLayer2; 	// 外网图层二
	private ArcGISTiledMapServiceLayer mServiceLayer3; 	// 外网图层三
	private ArcGISLocalTiledLayer mLocalLayer1; // 本地图层一
	private ArcGISLocalTiledLayer mLocalLayer2; // 本地图层二
	private ArcGISLocalTiledLayer mLocalLayer3; // 本地图层三
	
	// 本地地图地址
	private String mLocalPath1;
	private String mLocalPath2;
	private String mLocalPath3;
	// 外网地图地址
	private String mServerPath1 = Constants.MAP_SERVICE_PATH1;
	private String mServerPath2 = Constants.MAP_SERVICE_PATH2;
	private String mServerPath3 = Constants.MAP_SERVICE_PATH3;
	
	private boolean mUseLocalMap; // 是否使用离线地图
	
	public enum MAP_MODE{
		MAP1, MAP2, MAP3, TDT;
	}
	
	public ArcGISTiledLayerManager(MainFragment mMainFragment, boolean mUseLocalMap, 
			String localPath1, String localPath2, String localPath3){
		this.mMainActivity = (MainActivity) mMainFragment.getActivity();
		this.mMap = mMainFragment.getMap();
		this.mUseLocalMap = mUseLocalMap;
		
		this.mLocalPath1 = localPath1;
		this.mLocalPath2 = localPath2;
		this.mLocalPath3 = localPath3;
	}
	
	public MAP_MODE getMode(){
		return mMode;
	}
	
	public TiledLayer getLayer(){
		return mLayer;
	}
	
	/**
	 * 切换地图
	 */
	public void switchMap(MAP_MODE mode){
		mMode = mode;
		switch(mode){
		case MAP1:
			if(mUseLocalMap && mLocalLayer1!=null){ 
				showLocalLayer(mLocalLayer1);
			}else {
				if(mServiceLayer1 != null){
					showServiceLayer(mServiceLayer1);
					if(mUseLocalMap){
						Util.Toast("本地"+ Constants.MAP_LOCAL_DIR1 +"地图文件不存在，调用在线地图代替");
					}
				}else{
					Util.Toast("在线"+ Constants.MAP_LOCAL_DIR1 +"地图服务失效");
				}
			}
			break;
		case MAP2:
			if(mUseLocalMap && mLocalLayer2!=null){ 
				showLocalLayer(mLocalLayer2);
			}else {
				if(mServiceLayer2 != null){
					showServiceLayer(mServiceLayer2);
					if(mUseLocalMap){
						Util.Toast("本地"+ Constants.MAP_LOCAL_DIR2 +"地图文件不存在，调用在线地图代替");
					}
				}else{
					Util.Toast("在线"+ Constants.MAP_LOCAL_DIR2 +"地图服务失效");
				}
			}
			break;
		case MAP3:
			if(mUseLocalMap && mLocalLayer3!=null){ 
				showLocalLayer(mLocalLayer3);
			}else {
				if(mServiceLayer3 != null){
					showServiceLayer(mServiceLayer3);
					if(mUseLocalMap){
						Util.Toast("本地"+ Constants.MAP_LOCAL_DIR3 +"地图文件不存在，调用在线地图代替");
					}
				}else{
					Util.Toast("在线"+ Constants.MAP_LOCAL_DIR3 +"地图服务失效");
				}
			}
			break;
		case TDT:
			if(mTianDiTuLayer!=null && mTianDiTuAnnotationLayer!=null){
				mTianDiTuLayer.setVisible(true);
				mTianDiTuAnnotationLayer.setVisible(true);
			}else{
				Util.ShowDialog(mMainActivity, "天地图加载失败，请检查网络！");
			}
			break;
		}
	}
	
	/**
	 * 检测离线地图文件是否存在
	 * @return
	 */
	private boolean isLocalLayerExist(String path){
		if(path!=null && !"".equals(path)){
			File file = new File(path);
			if(file.exists()){
				return true;
			}
		}
		return false;
	}
	/**
	 * 检测在线地图文件是否可用
	 * @return
	 */
	private boolean isServerLayerAvailable(String path){
		if(Util.IsNetworkConnected(mMainActivity)){
			if(path!=null && !"".equals(path)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 添加所有的本地地图
	 */
	public boolean load(MAP_MODE mode){
		boolean added = false;
//		if(Util.IsNetworkConnected(mMainActivity)){
//			mTianDiTuLayer = new TianDiTuLayer(TianDiTuLayerTypes.TIANDITU_VECTOR_2000);
//			mTianDiTuAnnotationLayer = new TianDiTuLayer(TianDiTuLayerTypes.TIANDITU_VECTOR_ANNOTATION_CHINESE_2000);
//			mTianDiTuLayer.setVisible(false);
//			mTianDiTuAnnotationLayer.setVisible(false);
//			mMap.addLayer(mTianDiTuLayer);
//			mMap.addLayer(mTianDiTuAnnotationLayer);
//			added = true;
//		}
		if(isLocalLayerExist(mLocalPath1)){ 
			mLocalLayer1 = new ArcGISLocalTiledLayer(mLocalPath1);
			mLocalLayer1.setVisible(false);
			mMap.addLayer(mLocalLayer1);
			added = true;
		} 
		if(isLocalLayerExist(mLocalPath2)){
			mLocalLayer2 = new ArcGISLocalTiledLayer(mLocalPath2);
			mLocalLayer2.setVisible(false);
			mMap.addLayer(mLocalLayer2);
			added = true;
		} 
		if(isLocalLayerExist(mLocalPath3)){
			mLocalLayer3 = new ArcGISLocalTiledLayer(mLocalPath3);
			mLocalLayer3.setVisible(false);
			mMap.addLayer(mLocalLayer3);
			added = true;
		} 
		
		if(isServerLayerAvailable(mServerPath1)){
			mServiceLayer1 = new ArcGISTiledMapServiceLayer(mServerPath1);
			mServiceLayer1.setVisible(false);
			mMap.addLayer(mServiceLayer1);
			added = true;
		}
		if(isServerLayerAvailable(mServerPath2)){
			mServiceLayer2 = new ArcGISTiledMapServiceLayer(mServerPath2);
			mServiceLayer2.setVisible(false);
			mMap.addLayer(mServiceLayer2);
			added = true;
		}
		if(isServerLayerAvailable(mServerPath3)){
			mServiceLayer3 = new ArcGISTiledMapServiceLayer(mServerPath3);
			mServiceLayer3.setVisible(false);
			mMap.addLayer(mServiceLayer3);
			added = true;
		}
		switchMap(mode);
		return added;
	}
	
	/**
	 * 显示离线地图
	 */
	private void showLocalLayer(TiledLayer layer){
		if(mLayer!=null){
			mLayer.setVisible(false);// 隐藏当前地图
		}
		mLayer = layer;
		mLayer.setVisible(true);
	}
	
	/**
	 * 显示在线地图
	 */
	private void showServiceLayer(TiledLayer layer){
		if(Util.IsNetworkConnected(mMainActivity)){
			if(mLayer!=null){
				mLayer.setVisible(false);
			}
			mLayer = layer;
			mLayer.setVisible(true);
		} else{
			Util.Toast("当前网络不可用");
		}
	}
	
	
}
