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
	 * ��ȡӦ�ø�Ŀ¼
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
	 * ��ȡͼƬ��Ŀ¼
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
	 * ��ȡ�����ļ���Ŀ¼
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
	 * ��ȡ�����ļ���Ŀ¼
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
	 * ��ȡ���ߵ�ͼ��Ƭ��Ŀ¼
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
	 * ��ȡ��Ļ����
	 * @return 
	 */
	public static int[] GetScreenArgument(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

		int width = dm.widthPixels; 			// ��ȣ�PX��
		Log.e("LocationName", "��ȣ�PX��:" + width);
		int height = dm.heightPixels; 			// �߶ȣ�PX��
		Log.e("LocationName", "�߶ȣ�PX��:" + height);
		float density = dm.density; 			// �ܶȣ�0.75 / 1.0 / 1.5��
		Log.e("LocationName", "�ܶȣ�0.75 / 1.0 / 1.5��:" + density);
		int densityDpi = dm.densityDpi; 		// �ܶ�DPI��120 / 160 / 240��
		Log.e("LocationName", "�ܶ�DPI��120 / 160 / 240��:" + densityDpi);
		
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
		alertDialog.setTitle("��ʾ");
		alertDialog.setMessage(message);
		alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "ȷ��",
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
		builder.setTitle("��ʾ");
		builder.setCancelable(false);
		builder.setMessage(message);
		builder.setPositiveButton("ȷ��", mPositiveListener);
		builder.setNegativeButton("ȡ��", mNegativeListener);
		builder.create().show();
	}

	/**
	 * �ж������Ƿ����
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
	 * ���ؼ���
	 */
	public static void HideKeyboard(Activity activity) {
		WindowManager.LayoutParams params = activity.getWindow().getAttributes();
    	activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN;
	}
	
	/**
	 * ���ؼ���
	 * @param context
	 * @param view Ϊ����������������ͼ
	 */
	public static void HideKeyboard(Context context, View view) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(view,InputMethodManager.SHOW_FORCED);  
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0); // ǿ�����ؼ���
	}

	/**
	 * ���ػ���ʾ����
	 */
	public static void HideShowKeyboard(Context context) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * ����ϵͳ�������
	 */
	public static void DoTakePhoto(Activity activity, String path) {
		File photo = new File(path);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
		activity.startActivityForResult(intent, REQUEST_TAKE_SYSTEM_CAMERA);
	}

	/**
	 * ��ϵͳ�Դ���Ӧ�ô��ļ�
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
	 * ��ȡ�ļ�����
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
	 * ��ȡ�ļ���׺��
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
	 * �ж����ô洢���Ƿ����
	 */
	public static String IsSDCardExists() {
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			return Environment.getExternalStorageDirectory().getAbsolutePath();
		} else {
			Toast("δ��⵽����SD��");
		}
		return null;
	}

	/**
	 * ȫ����Ҫ��SetContentViewǰ����ã�
	 */
	public static void FullScreen(Activity activity) {
		Window window = activity.getWindow();
		window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	/**
	 * ȥ��������must be called before adding content��
	 */
	public static void NoTitleBar(Activity activity) {
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);// ȥ��������
	}

	/**
	 * �����ֻ��ķֱ��ʴ� dp �ĵ�λ ת��Ϊpx(����)
	 */
	public static int Dip2Px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * �����ֻ��ķֱ��ʴ� px(����) �ĵ�λ ת��Ϊ dp
	 */
	public static int Px2Dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * ��ȡ��ǰ����
	 */
	public static String GetCurrentDate(String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format, new Locale("CN"));
		Date date = new Date(System.currentTimeMillis());// ��ȡ��ǰʱ��
		return formatter.format(date);
	}

	/**
	 * pxֵת��Ϊspֵ
	 */
	public static int Px2Sp(Context context, float px) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (px / fontScale + 0.5f);
	}

	/**
	 * spֵת��Ϊpxֵ
	 */
	public static int Sp2Px(Context context, float sp) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (sp * fontScale + 0.5f);
	}

	/**
	 * ð������ �õ�����Ǵ�С����
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
	 * �����ļ�
	 * 
	 * @param fromFile
	 * @param toFile
	 * @param rewrite
	 *            �Ƿ�ɾ���Ѵ��ڵ�toFile
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
				out.write(bt, 0, c); // ������д�����ļ�����
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
	 * / ɾ���ļ���
	 * @param folderPath �ļ�����������·��
	 */
	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // ɾ����������������
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // ɾ�����ļ���
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ɾ��ָ���ļ����������ļ�
	 * @param path �ļ�����������·��
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
				delAllFile(path + "/" + tempList[i]);// ��ɾ���ļ���������ļ�
				delFolder(path + "/" + tempList[i]);// ��ɾ�����ļ���
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * ScrollView��Ƕ��ListViewʱʹ��
	 * 
	 * @param listView
	 */
	public static void setListViewHeight(ListView listView) {

		ListAdapter listAdapter = listView.getAdapter();

		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()�������������Ŀ
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0); // ��������View �Ŀ��
			totalHeight += listItem.getMeasuredHeight(); // ͳ������������ܸ߶�
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}
	
	// ������̸��ǵ�
	public static void ControlKeyboardLayout(final View root, final View scrollToView) {
        root.getViewTreeObserver().addOnGlobalLayoutListener( new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                //��ȡroot�ڴ���Ŀ�������
                root.getWindowVisibleDisplayFrame(rect);
                //��ȡroot�ڴ���Ĳ���������߶�(������View�ڵ�������߶�)
                int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;
                //������������߶ȴ���100���������ʾ
                if (rootInvisibleHeight > 100) {
                    int[] location = new int[2];
                    //��ȡscrollToView�ڴ��������
                    scrollToView.getLocationInWindow(location);
                    //����root�����߶ȣ�ʹscrollToView�ڿɼ�����
                    int srollHeight = (location[1] + scrollToView.getHeight()) - rect.bottom;
                    if(srollHeight>0) 
                    	root.scrollTo(0, srollHeight);
                } else {
                    //��������
                    root.scrollTo(0, 0);
                }
            }
        });
    }
	
}
