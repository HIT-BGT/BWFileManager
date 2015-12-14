package com.app.SDManeger;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

import com.app.sdfile.FileUtil;

public class ResurceDetails extends BaseActivity{
	private ListView listView;
	private File file;
	private FileUtil fileUtil;
	private Intent intent;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);	//去掉窗口的标题
		intent = getIntent();
		String filePath = intent.getStringExtra("fileName");	//得到文件名
		Log.d("ResurceDetails#onCreate", filePath);	//打印调试信息
		file = new File(filePath);
		if(!file.exists()){
			return;
		}
        setContentView(R.layout.resourcedetails_layout);
		fileUtil = new FileUtil();
		listView = (ListView) findViewById(R.id.resourcedetails_lst);
		listView.setAdapter(new ArrayAdapter<>(this,
				android.R.layout.simple_expandable_list_item_1, getData()));
		listView.setOnItemClickListener(new ListItemListentener());
	}

	class ListItemListentener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
            switch (position){
                case 0: //如果点击了第一个item
                    deleteDialog(); //那么创建delete对话框
                    break;
                case 1: //如果点击了第二个item
                    renameDialog(); //创建rename对话框
                    break;
                case 2: //如果点击了第三个iten
                    detailsDialog(); //新建details对话框
                    break;
                default:
                    break;
            }
		}

	}

	private List<String> getData() {
		//获得list中应该显示的数据
		List<String> data = new ArrayList<>();
		data.add("删除");
		data.add("重命名");
		data.add("详细信息");
		return data;
	}

	private void deleteDialog() {
		AlertDialog.Builder builder = new Builder(ResurceDetails.this);
		builder.setMessage("确定要删除吗？");
		builder.setTitle("删除");
		builder.setPositiveButton("确认", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Log.d("ResDets#deleteDialog", "confirm");
				file.delete();
				setResult(1);
				finish();
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();   //只dismiss了这个dialog，要不要把上一级的也dismiss掉？比如用finish()
                Log.d("ResDets#deleteDialog", "cancel");
			}
		});
		builder.create().show();
	}

	private void renameDialog() {
		AlertDialog.Builder builder = new Builder(ResurceDetails.this);
		final EditText editText = new EditText(ResurceDetails.this);    //用于输入新文件名的输入框
		builder.setMessage("请输入文件名：");
		builder.setTitle("重命名");
		builder.setView(editText);
		builder.setPositiveButton("确认", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();   //使dialog消失
				String str = editText.getText().toString(); //得到新文件名
				String temp = file.getAbsolutePath();
				int i = temp.lastIndexOf("/");
				temp = temp.substring(0,  i);
				Log.d("ResDets#renameDialog", "New filename: " + temp + "/" + str  );
				File fileTemp = new File(temp + File.separator + str);
				Log.d( "ResDets#renameDialog", "New absolute path: " + fileTemp.getAbsolutePath());
                file.renameTo(fileTemp);    //执行重命名操作
				setResult(2);
				finish();
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Log.d("ResDets#renameDialog", "cancel");
			}
		});
		builder.create().show();
	}

	private void detailsDialog() {
		ArrayList<HashMap<String, Object>> lstDetails;
		lstDetails = fileUtil.getFileDetails(file); //调用得到文件详细信息
		ListView listView1 = new ListView(this);
		SimpleAdapter sAdapter = new SimpleAdapter(this,lstDetails,
				android.R.layout.simple_expandable_list_item_2, 
				new String[] { "list", "source" },
				new int[] { android.R.id.text1, android.R.id.text2 });
		listView1.setAdapter(sAdapter);
		setContentView(listView1);  //为什么能直接setContentView？原来是直接改变了现在activity的view，是否可以再写一个activity？
	}
}
