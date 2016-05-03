package com.example.wzelectricitysupplier.function;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.esri.android.map.FeatureLayer;
import com.esri.android.map.MapView;
import com.esri.core.geodatabase.Geodatabase;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.example.wzelectricitysupplier.setting.Constants;
import com.example.wzelectricitysupplier.setting.Util;

public class GeodatabaseLayerManager {
	
	private MapView mMap;
	
	private FeatureLayer mGeodatabaseFeatureLayer;  
	private ArrayList<FeatureLayer> mFeatureLayers = new ArrayList<FeatureLayer>();
	
	public GeodatabaseLayerManager(MapView mMap){
		this.mMap = mMap;
	}
	
	public void setGeodatabaseFeatureLayerVisible(boolean visible){
		if(visible){
			if(mGeodatabaseFeatureLayer == null){
				// º”‘ÿgeodatabase ˝æ›
				String storage = Util.IsSDCardExists();
				String path1 = storage + File.separator + Constants.GEODATABASE_PATH1;
				String path2 = storage + File.separator + Constants.GEODATABASE_PATH2;
				try {
					Geodatabase base1 = new Geodatabase(path1);
					for(GeodatabaseFeatureTable table : base1.getGeodatabaseTables()){
						if(table.hasGeometry()){
							mGeodatabaseFeatureLayer = new FeatureLayer(table);
							mMap.addLayer(mGeodatabaseFeatureLayer);
							mFeatureLayers.add(mGeodatabaseFeatureLayer);
							mGeodatabaseFeatureLayer.setVisible(true);
						}
					}
					Geodatabase base2 = new Geodatabase(path2);
					for(GeodatabaseFeatureTable table : base2.getGeodatabaseTables()){
						if(table.hasGeometry()){
							mGeodatabaseFeatureLayer = new FeatureLayer(table);
							mMap.addLayer(mGeodatabaseFeatureLayer);
							mFeatureLayers.add(mGeodatabaseFeatureLayer);
							mGeodatabaseFeatureLayer.setVisible(true);
						}
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}else{
			if(mGeodatabaseFeatureLayer!=null){
				for(FeatureLayer layer : mFeatureLayers){
					layer.setVisible(visible);
					mMap.removeLayer(layer);
					mGeodatabaseFeatureLayer = null;
				}
				mFeatureLayers.clear();
			}
		}
	}
}
