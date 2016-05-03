package com.example.wzelectricitysupplier.function;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.map.CallbackListener;
import com.esri.core.tasks.tilecache.ExportTileCacheParameters;
import com.esri.core.tasks.tilecache.ExportTileCacheParameters.ExportBy;
import com.esri.core.tasks.tilecache.ExportTileCacheStatus;
import com.esri.core.tasks.tilecache.ExportTileCacheTask;
import com.example.wzelectricitysupplier.MyApplication;
import com.example.wzelectricitysupplier.setting.Constants;

public class CacheTiledLayer {
	
	public static final String TAG = "CacheTiledLayer";
	
	private MapView mMapView;
	private Activity mActivity;
	private ArcGISLocalTiledLayer localTiledLayer;
	
	private boolean mIsLocalLayerVisible = false;
	private boolean mCreateAsTPK = false;
	private boolean mCreateAsTilePackage = false;
	
	private String mTileURL;
	private static String DEFAULT_BASEMAP_PATH;
	private static String mDefaultTileCachePath = null;
	
	private double[] mLevels;
	private ArrayList<Double> mLevelsArraylist = new ArrayList<Double>();
	private final CharSequence[] items = { "Level ID:0", "Level ID:1", "Level ID:2",
			"Level ID:3", "Level ID:4", "Level ID:5", "Level ID:6",
			"Level ID:7", "Level ID:8", "Level ID:9", };
	private double[] mMapResolution = { 156543.03392800014, 78271.51696399994,
			39135.75848200009, 19567.87924099992, 9783.93962049996, 4891.96981024998, 
			2445.98490512499, 1222.992452562495, 611.4962262813797, 305.74811314055756 };
	
	public CacheTiledLayer(MapView mMapView, Activity activity){
		this.mMapView = mMapView;
		this.mActivity = activity;
		
		DEFAULT_BASEMAP_PATH = Constants.OFFLINE_MAP_ROOT_DIR;
		mDefaultTileCachePath = Environment.getExternalStorageDirectory().getPath()
				+ DEFAULT_BASEMAP_PATH;
	}
	
	public void downloadBasemap() {

		Envelope extentForTPK = new Envelope();
		mMapView.getExtent().queryEnvelope(extentForTPK);

		// If the user does not select the Level of details then give out the status message in a toast

		mLevels = new double[mLevelsArraylist.size()];
		final String tileCachePath = mDefaultTileCachePath;

		// Specify all the Levels of details in an integer array
		for (int i = 0; i < mLevelsArraylist.size(); i++) {
			mLevels[i] = mLevelsArraylist.get(i);
		}

		// Create an instance of ExportTileCacheTask for the mapService that supports the exportTiles() operation
		final ExportTileCacheTask exportTileCacheTask = new ExportTileCacheTask(mTileURL, null);

		// Set up GenerateTileCacheParameters
		ExportTileCacheParameters params = new ExportTileCacheParameters(mCreateAsTilePackage, mLevels, ExportBy.ID, extentForTPK,
				mMapView.getSpatialReference());

		// Create tile cache
		createTileCache(params, exportTileCacheTask, tileCachePath);
	}

	private void createTileCache(ExportTileCacheParameters params, final ExportTileCacheTask exportTileCacheTask,
			final String tileCachePath) {

		exportTileCacheTask.estimateTileCacheSize(params,
				new CallbackListener<Long>() {

					@Override
					public void onError(Throwable e) {
						Log.d("TileCacheSize error: ", "" + e);
					}

					@Override
					public void onCallback(Long objs) {
						Log.d("TileCacheSize:", "" + objs);
						final long tileCacheSize = objs / 1000;
						mActivity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(MyApplication.Context,
										"Approx. Tile Cache size to download : "+ tileCacheSize + " KB",
										Toast.LENGTH_LONG).show();
							}
						});

					}
				});

		// create status listener for generateTileCache
		CallbackListener<ExportTileCacheStatus> statusListener = new CallbackListener<ExportTileCacheStatus>() {

			@Override
			public void onError(Throwable e) {
				Log.d("TileCacheStatus error: ", "" + e);
			}

			@Override
			public void onCallback(ExportTileCacheStatus objs) {
				Log.d("TileCacheStatus: ", objs.getStatus().toString());
				final String status = objs.getStatus().toString();
				mActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(mActivity.getApplicationContext(), status,
								Toast.LENGTH_SHORT).show();
					}
				});

			}
		};

		// Submit tile cache job and download
		exportTileCacheTask.generateTileCache(params, statusListener,
				new CallbackListener<String>() {
					private boolean errored = false;

					@Override
					public void onError(Throwable e) {
						errored = true;
						// print out the error message and disable the progress bar
						Log.d("TileCacheStatus error: ", "" + e);
						final String error = e.toString();
						mActivity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								mActivity.setProgressBarIndeterminateVisibility(false);
								Toast.makeText(mActivity.getApplicationContext(),
										"generateTileCache error: " + error, Toast.LENGTH_LONG).show();
							}
						});
					}

					@Override
					public void onCallback(String path) {
						if (!errored) {
							Log.d("the Download Path = ", "" + path);

							// switch to the successfully downloaded local layer
							localTiledLayer = new ArcGISLocalTiledLayer(path);
							mMapView.addLayer(localTiledLayer);
							// initially setting the visibility to false, turning it back on in the switchToLocalLayer() method
							mMapView.getLayers()[1].setVisible(false);

							mActivity.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									// Hide the progress bar
									mActivity.setProgressBarIndeterminateVisibility(false);
									Toast.makeText(mActivity.getApplicationContext(),
											"TileCache successfully downloaded, Switching to Local Tiled Layer",
											Toast.LENGTH_LONG).show();
									switchToLocalLayer();
								}
							});
						}
					}
				}, tileCachePath);

	}
	
	public void switchToLocalLayer() {
		// Set the resolution of the map to the first level selected by user
		mMapView.setResolution(mMapResolution[(int) mLevels[0]]);
		mIsLocalLayerVisible = true;
		mMapView.getLayers()[0].setVisible(false);
		mMapView.getLayers()[1].setVisible(true);

	}

	public void switchToOnlineLayer() {
		mIsLocalLayerVisible = false;
		mMapView.getLayers()[1].setVisible(false);
		mMapView.getLayers()[0].setVisible(true);
	}
}
