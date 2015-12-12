package com.app.sdcheck;

import java.io.File;

import android.util.Log;
import android.os.Environment;

public class SDCardCheck {

	private File path;
	private int SDstat;
    private int haha;

	private long nSDTotalSize;
	private long nSDFreeSize;

	public void SDCardCheck() {
		//获取外部存储设备的状态
		String sDStateString = Environment.getExternalStorageState();

		if (sDStateString.equals(Environment.MEDIA_MOUNTED)) {
			//此时SDCard可正常读写
			Log.d("x", "the SDcard mounted");
			SDstat = 1;
		} else if (sDStateString.endsWith(Environment.MEDIA_MOUNTED_READ_ONLY)) {
			//只能对SDCard读操作
			Log.d("x", "the SDcard just can be read");
			SDstat = 2;
		} else if (sDStateString.endsWith(Environment.MEDIA_UNMOUNTED)) {
			//SDCard在但是没被挂载
			Log.d("x", "the SDcard unmounted");
			SDstat = -1;
			System.exit(0);
		} else if (sDStateString.endsWith(Environment.MEDIA_REMOVED)) {
			//SDCard被移除了
			Log.d("x", "the SDcard has been removed");
			SDstat = -2;
			System.exit(0);
		}
	}
	public File getSDCardDir() {
		if(SDstat > 0) {
			path = Environment.getExternalStorageDirectory();
			Log.d("x", path.toString());
			return path;
		}
		return null;
	}
	public void SDCardSize() {
		if (SDstat > 0) {
			path = getSDCardDir();
		    android.os.StatFs statfs = new android.os.StatFs(path.getPath());
		    long nTotalBlocks = statfs.getBlockCount();    //共有多少个block
		    long nBlocSize = statfs.getBlockSize();  //每一个block的大小
		    long nAvailaBlock = statfs.getAvailableBlocks();  //共有多少个可用的block
		    nSDTotalSize = nTotalBlocks * nBlocSize / 1024 / 1024;  //转换单位为MB
		    nSDFreeSize = nAvailaBlock * nBlocSize / 1024 / 1024;  //转换单位为MB
		}
	}

	public long getnSDFreeSize() {
		return nSDFreeSize;
	}


	public long getnSDTotalSize() {
		return nSDTotalSize;
	}

	@Override
	public String toString() {
		return "SDCardCheck [path=" + path + ", SDstat=" + SDstat + "]"
				+ nSDTotalSize + "SDTotalSize"+ "...SDFreeSize" + nSDFreeSize;
	}

	public File getPath() {
		return path;
	}

	public int getSDstat() {
		return SDstat;
	}
}
