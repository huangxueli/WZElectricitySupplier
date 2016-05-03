package com.example.wzelectricitysupplier.setting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.wzelectricitysupplier.MyApplication;
import com.example.wzelectricitysupplier.R;

public class Util {

	public static final int REQUEST_TAKE_SYSTEM_CAMERA = 10000;

	/**
	 * 获取应用根目录
	 */
	public static String getAppRootPath() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			String mAtSDCardPath = IsSDCardExists() + "/"
					+ MyApplication.Resources.getString(R.string.app_name);
			File file = new File(mAtSDCardPath);
			if (!file.exists()) {
				file.mkdir();
			}
			return mAtSDCardPath;
		}
		return null;
	}

	/**
	 * 获取图片根目录
	 */
	public static String getPictureDirRootPath() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			String mAtSDCardPath = getAppRootPath() + "/" + Constants.PICTURE_DIR_NAME + "/";
			File file = new File(mAtSDCardPath);
			if (!file.exists()) {
				file.mkdirs();
			}
			return mAtSDCardPath;
		}
		return null;
	}

	/**
	 * 获取导出文件根目录
	 */
	public static String getExportDirRootPath() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			String mAtSDCardPath = getAppRootPath() + "/" + Constants.EXPROT_DIR_NAME + "/";
			File file = new File(mAtSDCardPath);
			if (!file.exists()) {
				file.mkdirs();
			}
			return mAtSDCardPath;
		}
		return null;
	}
	
	/**
	 * 获取导入文件根目录
	 */
	public static String getImportDirRootPath() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			String mAtSDCardPath = getAppRootPath() + "/" + Constants.IMPROT_DIR_NAME + "/";
			File file = new File(mAtSDCardPath);
			if (!file.exists()) {
				file.mkdirs();
			}
			return mAtSDCardPath;
		}
		return null;
	}
	
	/**
	 * 获取离线地图切片根目录
	 */
	public static String getLocalMapDirRootPath() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			String mAtSDCardPath = getAppRootPath() + "/" + Constants.OFFLINE_MAP_ROOT_DIR + "/";
			File file1 = new File(mAtSDCardPath + Constants.MAP_LOCAL_DIR1);
			File file2 = new File(mAtSDCardPath + Constants.MAP_LOCAL_DIR2);
			File file3 = new File(mAtSDCardPath + Constants.MAP_LOCAL_DIR3);

			if (!file1.exists()) {
				file1.mkdirs();
			}
			if (!file2.exists()) {
				file2.mkdirs();
			}
			if (!file3.exists()) {
				file3.mkdirs();
			}
			return mAtSDCardPath;
		}
		return null;
	}

	/**
	 * 获取屏幕参数
	 * @return 
	 */
	public static int[] GetScreenArgument(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

		int width = dm.widthPixels; 			// 宽度（PX）
		Log.e("LocationName", "宽度（PX）:" + width);
		int height = dm.heightPixels; 			// 高度（PX）
		Log.e("LocationName", "高度（PX）:" + height);
		float density = dm.density; 			// 密度（0.75 / 1.0 / 1.5）
		Log.e("LocationName", "密度（0.75 / 1.0 / 1.5）:" + density);
		int densityDpi = dm.densityDpi; 		// 密度DPI（120 / 160 / 240）
		Log.e("LocationName", "密度DPI（120 / 160 / 240）:" + densityDpi);
		
		return new int[]{width, height};
	}

	public static String GetAppName(Context context) {
		Resources resource = context.getResources();
		String name = resource.getString(R.string.app_name);
		return name;
	}

	public static void Toast(String text) {
		try {
			Toast.makeText(MyApplication.Context, text, Toast.LENGTH_SHORT).show();
		} catch (Exception e) {

		}
	}

	public static void ShowDialog(Context context, final String message) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setTitle("提示");
		alertDialog.setMessage(message);
		alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// Do nothing
					}
				});
		alertDialog.show();
	}

	public static void ShowDialog(Context context, final String message,
			OnClickListener mPositiveListener, OnClickListener mNegativeListener) {
		Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("提示");
		builder.setCancelable(false);
		builder.setMessage(message);
		builder.setPositiveButton("确定", mPositiveListener);
		builder.setNegativeButton("取消", mNegativeListener);
		builder.create().show();
	}

	/**
	 * 判断网络是否可用
	 */
	public static boolean IsNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 隐藏键盘
	 */
	public static void HideKeyboard(Activity activity) {
		WindowManager.LayoutParams params = activity.getWindow().getAttributes();
    	activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN;
	}
	
	/**
	 * 隐藏键盘
	 * @param context
	 * @param view 为接受软键盘输入的视图
	 */
	public static void HideKeyboard(Context context, View view) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(view,InputMethodManager.SHOW_FORCED);  
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0); // 强制隐藏键盘
	}

	/**
	 * 隐藏或显示键盘
	 */
	public static void HideShowKeyboard(Context context) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * 调用系统相机程序
	 */
	public static void DoTakePhoto(Activity activity, String path) {
		File photo = new File(path);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
		activity.startActivityForResult(intent, REQUEST_TAKE_SYSTEM_CAMERA);
	}

	/**
	 * 用系统自带的应用打开文件
	 */
	public static boolean OpenFileWithAndroid(Context context, String filePath,
			String fileType) {
		if (!fileType.equals("")) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.parse("file://" + filePath), fileType);
			context.startActivity(intent);
			return true;
		}
		return false;
	}

	/**
	 * 获取文件类型
	 */
	public static String GetFileTypeFromFileName(String fileName) {
		String retString = "";
		String ext = GetExtByFileName(fileName);
		if (ext.equals("mp4") || ext.equals("3gp") || ext.equals("avi")) {
			retString = "video/*";
		} else if (ext.equals("jpg") || ext.equals("png") || ext.equals("gif")) {
			retString = "image/*";
		}
		return retString;
	}

	/**
	 * 获取文件后缀名
	 */
	public static String GetExtByFileName(String name) {
		String ext = "";
		if (name != null && name.length() > 0) {
			int index = name.lastIndexOf(".");
			if (index > 0 && index < name.length() - 1) {
				ext = name.substring(index + 1);
			}
		}
		return ext;
	}

	/**
	 * 判断外置存储卡是否存在
	 */
	public static String IsSDCardExists() {
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			return Environment.getExternalStorageDirectory().getAbsolutePath();
		} else {
			Toast("未检测到内置SD卡");
		}
		return null;
	}

	/**
	 * 全屏（要在SetContentView前面调用）
	 */
	public static void FullScreen(Activity activity) {
		Window window = activity.getWindow();
		window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	/**
	 * 去标题栏（must be called before adding content）
	 */
	public static void NoTitleBar(Activity activity) {
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为px(像素)
	 */
	public static int Dip2Px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int Px2Dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 获取当前日期
	 */
	public static String GetCurrentDate(String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format, new Locale("CN"));
		Date date = new Date(System.currentTimeMillis());// 获取当前时间
		return formatter.format(date);
	}

	/**
	 * px值转换为sp值
	 */
	public static int Px2Sp(Context context, float px) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (px / fontScale + 0.5f);
	}

	/**
	 * sp值转换为px值
	 */
	public static int Sp2Px(Context context, float sp) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (sp * fontScale + 0.5f);
	}

	/**
	 * 冒泡排序法 得到结果是从小到大
	 * 
	 * @param args
	 * @return
	 */
	public static int[] bubbleSort(int[] args) {
		for (int i = 0; i < args.length - 1; i++) {
			for (int j = i + 1; j < args.length; j++) {
				if (args[i] > args[j]) {
					int temp = args[i];
					args[i] = args[j];
					args[j] = temp;
				}
			}
		}
		return args;
	}

	/**
	 * 拷贝文件
	 * 
	 * @param fromFile
	 * @param toFile
	 * @param rewrite
	 *            是否删除已存在的toFile
	 * @return
	 */
	public static boolean CopyFile(File fromFile, File toFile, Boolean rewrite) {
		if (!fromFile.exists()) {
			return false;
		}
		if (!fromFile.isFile()) {
			return false;
		}
		if (!fromFile.canRead()) {
			return false;
		}
		if (!toFile.getParentFile().exists()) {
			toFile.getParentFile().mkdirs();
		}
		if (toFile.exists() && rewrite) {
			toFile.delete();
		}
		try {
			FileInputStream in = new java.io.FileInputStream(fromFile);
			FileOutputStream out = new FileOutputStream(toFile);
			byte bt[] = new byte[1024];
			int c;
			while ((c = in.read(bt)) > 0) {
				out.write(bt, 0, c); // 将内容写到新文件当中
			}
			in.close();
			out.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * / 删除文件夹
	 * @param folderPath 文件夹完整绝对路径
	 */
	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除指定文件夹下所有文件
	 * @param path 文件夹完整绝对路径
	 * @return
	 */
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * ScrollView中嵌套ListView时使用
	 * 
	 * @param listView
	 */
	public static void setListViewHeight(ListView listView) {

		ListAdapter listAdapter = listView.getAdapter();

		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}
	
	// 解决键盘覆盖的
	public static void ControlKeyboardLayout(final View root, final View scrollToView) {
        root.getViewTreeObserver().addOnGlobalLayoutListener( new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                //获取root在窗体的可视区域
                root.getWindowVisibleDisplayFrame(rect);
                //获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
                int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;
                //若不可视区域高度大于100，则键盘显示
                if (rootInvisibleHeight > 100) {
                    int[] location = new int[2];
                    //获取scrollToView在窗体的坐标
                    scrollToView.getLocationInWindow(location);
                    //计算root滚动高度，使scrollToView在可见区域
                    int srollHeight = (location[1] + scrollToView.getHeight()) - rect.bottom;
                    if(srollHeight>0) 
                    	root.scrollTo(0, srollHeight);
                } else {
                    //键盘隐藏
                    root.scrollTo(0, 0);
                }
            }
        });
    }
	
}
