package com.app.sdfile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;

public class FileUtil {

	private String spaces = null;
	private double space = 0;
	private double count = 0;

    //递归计算目录下所有文件的体积之和
	private void attDir(File file) throws IOException {
		if (file.isHidden()) {
			return;
		}
		File[] liFile = file.listFiles();	//返回file下的文件列表
		for (File nFile : liFile) {
			if (nFile.isFile()) {
				attFile(nFile, 1);
				count += space;
			} else {
				attDir(nFile);
			}
		}
	}

    //requestCode=0：设置spaces；request=1： 不设置spaces
	private void attFile(File file, int requestCode) throws IOException {
		// TODO Auto-generated method stub
		FileInputStream fi = new FileInputStream(file);
		space = fi.available();	//返回文件所占字节数
		Log.d("attFile", String.valueOf(space));
		if (requestCode==0) {
            if (space > 1048576) {
                spaces = String.valueOf(space / 1048576).substring(0,
                        String.valueOf(space / 1048576).lastIndexOf(".") + 4)
                        + "MB"; //如果是MB就显示xxxx.xxxMB
            } else {
                spaces = String.valueOf(space / 1024).substring(0,
                        String.valueOf(space / 1024).lastIndexOf(".") + 2)
                        + "KB"; //如果是KB就显示xxxx.xKB
            }
        }
	}

	public String getSize(File file) {
        //获取【当前】文件/文件夹的体积大小
		String temp;
		if (file.isFile()) {
			try {
				attFile(file, 0);
			} catch (IOException e) {
				e.printStackTrace();
			}
			temp = spaces;
		} else {
			try {
				attDir(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (count > 1048576) {
				temp = String.valueOf(count / 1048576).substring(0,
						String.valueOf(count / 1048576).lastIndexOf(".") + 4)
						+ "MB";  //如果是MB就显示xxxx.xxxMB
			} else {
				temp = String.valueOf(count / 1024).substring(0,
						String.valueOf(count / 1024).lastIndexOf(".") + 2)
						+ "KB"; //如果是KB就显示xxxx.xKB
			}
		}
		return temp;
	}

	public ArrayList<HashMap<String, Object>> getFileDetails(File file) {   //貌似也可以写成静态方法？

		ArrayList<HashMap<String, Object>> lstDetails = new ArrayList<>();
		String temp;
        HashMap<String, Object> map;
		map = new HashMap<>();
		temp = file.getName();
		map.put("list", "名称："); //获取文件名称
		map.put("source", temp);
        lstDetails.add(map);

        map = new HashMap<>();
		temp = file.getAbsolutePath();
		map.put("list", "位置："); //获取文件位置
		map.put("source", temp);
        lstDetails.add(map);

		map = new HashMap<>();
		map.put("list", "大小：");
		temp = getSize(file);   //获取文件大小
		map.put("source", temp);
        lstDetails.add(map);

		map = new HashMap<>();
		map.put("list", "是否可读：");
		map.put("source", file.canRead() ? "是" : "否");   //获取文件是否可读
        lstDetails.add(map);

		map = new HashMap<>();
		map.put("list", "是否可写：");
		map.put("source", file.canWrite() ? "是" : "否");  //获取文件是否可写
        lstDetails.add(map);

		map = new HashMap<>();
		map.put("list", "是否隐藏：");
		map.put("source", file.isHidden() ? "是" : "否");  //获取文件是否隐藏
        lstDetails.add(map);

		map = new HashMap<>();
		map.put("list", "类型：");
		map.put("source", file.isFile() ? "文件" : "目录");    //获取文件是否是文件（还是目录）
        lstDetails.add(map);

		return lstDetails;
	}

}
