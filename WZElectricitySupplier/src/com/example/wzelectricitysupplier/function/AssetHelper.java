package com.example.wzelectricitysupplier.function;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class AssetHelper {
	private static final String TAG = "AssetHelper";

	public static void CopyAsset(Context context, String copyname, String filepath) throws IOException {
		
		AssetManager mAssetManager = context.getAssets();
		InputStream in = null;
		OutputStream out = null;

		try {
			in = mAssetManager.open(copyname);
			out = new FileOutputStream(filepath);
			CopyFile(in, out);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
			throw e;
		} finally {
			if (in != null) {
				in.close();
				in = null;
			}
			if (out != null) {
				out.flush();
				out.close();
				out = null;
			}
		}
	}

	private static void CopyFile(InputStream in, OutputStream out)
			throws IOException {
		byte[] buffer = new byte[1024];
		int read;

		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
	}
}
