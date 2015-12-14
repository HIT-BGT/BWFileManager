package com.app.sdfile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import com.app.SDManeger.R;

public class SDFile {

	public ArrayList<HashMap<String, Object>> getFileList(File path) {
		//申请一个数组用来装每个文件
		ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<>();
		HashMap<String, Object> map;
		String temp;
		int i;
        //如果path不存在或者不可读或者不是目录
		if (!path.exists() && !path.canRead() && !path.isDirectory()) {
			return lstImageItem;
		}
        //如果path下没有文件
		if (path.list() == null) {
			return lstImageItem;
		}
        //如果path被隐藏了
		if (path.isHidden()) {
			path.setReadable(true);
		}
		temp = path.getName(); //获得path名称
		if (temp.startsWith(".")) {
			temp = temp.substring(1, temp.length());    //去掉'.'
			i = path.getAbsoluteFile().toString().lastIndexOf("/");
			temp = path.getAbsolutePath().substring(0, i) + File.separator
					+ temp;
			path.renameTo(new File(temp));  //重命名？
			Log.d("x", "int sub");
		}
		for (File f : path.listFiles()) {
			temp = f.getName();
			map = new HashMap<>();
			if (f.isDirectory()) {  //将文件的图片放进map里
				map.put("ItemImage", R.drawable.filefolder1);
			} else if (f.isFile() && temp.endsWith(".pdf")) {
				map.put("ItemImage", R.drawable.pdf);
			} else if (f.isFile() && temp.endsWith(".mp3")) {
				map.put("ItemImage", R.drawable.mp3);
			} else if (f.isFile() && temp.endsWith(".txt")) {
				map.put("ItemImage", R.drawable.txt);
			}else if (f.isFile()) {
				map.put("ItemImage", R.drawable.file1);
			}
			map.put("ItemText", f.getName());   //将文件名放入map里
			lstImageItem.add(map);
		}
		return lstImageItem;
	}

	//打开文件
	public Intent openFile(File file) {	//返回一个intent可以用在startActivity里
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);    //新建一个task
		intent.setAction(Intent.ACTION_VIEW);
		String type = getMIMEType(file);
		intent.setDataAndType(Uri.fromFile(file), type);
		return intent;	//在使用intent时最好try一下
	}

	/**
	 * 根据不同的文件，选择不同的打开方式。 
	 */
	private String getMIMEType(File file) {

		String type = "*/*";    //为什么要返回这个呢?
		String fName = file.getName();
		int dotIndex = fName.lastIndexOf(".");	//获取后缀名前的分隔符"."在fName中的位置
		if (dotIndex < 0) {
			return type;
		}
        //获取文件的后缀名
		String end = fName.substring(dotIndex, fName.length()).toLowerCase();
		if (end.equals("") )
			return type;
        for (String[] mime_type : MIME_MapTable){
            if (end.equals(mime_type[0])){
                type = mime_type[1];
            }
        }
		return type;
	}
      //MIMEType 映射表（是否可以改写成HashMap?）
      private final String[][] MIME_MapTable = {
			{ ".3gp", "video/3gpp"},
			{ ".apk", "application/vnd.android.package-archive" },
			{ ".asf", "video/x-ms-asf" },
			{ ".avi", "video/x-msvideo" },
			{ ".bin", "application/octet-stream" },
			{ ".bmp", "image/bmp" },
			{ ".c", "text/plain" },
			{ ".class", "application/octet-stream" },
			{ ".conf", "text/plain" },
			{ ".cpp", "text/plain" },
			{ ".doc", "application/msword" },
			{ ".docx",
					"application/vnd.openxmlformats-officedocument.wordprocessingml.document" },
			{ ".xls", "application/vnd.ms-excel" },
			{ ".xlsx",
					"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" },
			{ ".exe", "application/octet-stream" },
			{ ".gif", "image/gif" },
			{ ".gtar", "application/x-gtar" },
			{ ".gz", "application/x-gzip" },
			{ ".h", "text/plain" },
			{ ".htm", "text/html" },
			{ ".html", "text/html" },
			{ ".jar", "application/java-archive" },
			{ ".java", "text/plain" },
			{ ".jpeg", "image/jpeg" },
			{ ".jpg", "image/jpeg" },
			{ ".js", "application/x-javascript" },
			{ ".log", "text/plain" },
			{ ".m3u", "audio/x-mpegurl" },
			{ ".m4a", "audio/mp4a-latm" },
			{ ".m4b", "audio/mp4a-latm" },
			{ ".m4p", "audio/mp4a-latm" },
			{ ".m4u", "video/vnd.mpegurl" },
			{ ".m4v", "video/x-m4v" },
			{ ".mov", "video/quicktime" },
			{ ".mp2", "audio/x-mpeg" },
			{ ".mp3", "audio/x-mpeg" },
			{ ".mp4", "video/mp4" },
			{ ".mpc", "application/vnd.mpohun.certificate" },
			{ ".mpe", "video/mpeg" },
			{ ".mpeg", "video/mpeg" },
			{ ".mpg", "video/mpeg" },
			{ ".mpg4", "video/mp4" },
			{ ".mpga", "audio/mpeg" },
			{ ".msg", "application/vnd.ms-outlook" },
			{ ".ogg", "audio/ogg" },
			{ ".pdf", "application/pdf" },
			{ ".png", "image/png" },
			{ ".pps", "application/vnd.ms-powerpoint" },
			{ ".ppt", "application/vnd.ms-powerpoint" },
			{ ".pptx",
					"application/vnd.openxmlformats-officedocument.presentationml.presentation" },
			{ ".prop", "text/plain" }, { ".rc", "text/plain" },
			{ ".rmvb", "audio/x-pn-realaudio" }, { ".rtf", "application/rtf" },
			{ ".sh", "text/plain" }, { ".tar", "application/x-tar" },
			{ ".tgz", "application/x-compressed" }, { ".txt", "text/plain" },
			{ ".wav", "audio/x-wav" }, { ".wma", "audio/x-ms-wma" },
			{ ".wmv", "audio/x-ms-wmv" },
			{ ".wps", "application/vnd.ms-works" }, { ".xml", "text/plain" },
			{ ".z", "application/x-compress" },
			{ ".zip", "application/x-zip-compressed" },
			{ ".lrc", "text/plain" }, { "", "*/*" } };

	public ArrayList<HashMap<String, Object>> getSearchList(File path,
			String str) {

		ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<>();
		HashMap<String, Object> map;
		String temp;

		if (!path.exists() && !path.canRead() && !path.isDirectory()) {
			return lstImageItem;
		}
		if (path.list() == null) {
			return lstImageItem;
		}
		if (path.isHidden()) {
			path.setReadable(true);
		}
		for (File f : path.listFiles()) {
			temp = f.getName();
			if (temp.contains(str)) {
				map = new HashMap<String, Object>();

				if (f.isDirectory()) {
					map.put("ItemImage", R.drawable.filefolder1);
				} else if (f.isFile()&& temp.endsWith(".html")) {
					map.put("ItemImage", R.drawable.html);
				} else if (f.isFile() && temp.endsWith(".mp3")) {
					map.put("ItemImage", R.drawable.mp3);
				} else if (f.isFile() && temp.endsWith(".txt")) {
					map.put("ItemImage", R.drawable.txt);
				}else if(f.isFile()){
					map.put("ItemImage", R.drawable.file1);
				}
				map.put("ItemText", f.getName());
				lstImageItem.add(map);
			}
		}
		return lstImageItem;
	}
}
