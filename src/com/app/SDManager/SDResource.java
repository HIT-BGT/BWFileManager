package com.app.SDManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import com.app.sdcheck.SDCardCheck;
import com.app.sdfile.SDFile;

public class SDResource extends BaseActivity implements SearchView.OnQueryTextListener {
	SDCardCheck sdCardCheck;
	SDFile sdFile;
	File SDpath;
	String temp;
	ArrayList<HashMap<String, Object>> lstImageItem;
	private GridView gridview;
    private MenuItem searchItem;
    private SearchView searchView;
    private DrawerLayout drawerLayout;
	private ListView drawerList;

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);	//去标题
		setContentView(R.layout.main);
		gridview = (GridView) findViewById(R.id.gridview);
		gridview.setOnItemClickListener(mGridViewItemClickListener);	//给grid中的每个item注册一个listener
		gridview.setOnItemLongClickListener(mGridViewItemLongClickListener); //给grid中的每个item注册一个长时间按的listener
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);
        SimpleAdapter listAdapter = new SimpleAdapter(this, getData(), R.layout.sliding_item,
                new String[]{"img", "text"},
                new int[]{R.id.img, R.id.text});
        drawerList.setAdapter(listAdapter);
        drawerList.setOnItemClickListener(new DrawerItemClickListener());

		sdCardCheck = new SDCardCheck();
		sdFile = new SDFile();

		SDpath = sdCardCheck.getSDCardDir();
        Log.d("SDResource#onCreate", SDpath.getAbsolutePath());
		reFleshView(SDpath);
	}
    //获得侧边菜单栏数据
    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("text", "创建");
        map.put("img", R.drawable.create);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("text", "刷新");
        map.put("img", R.drawable.delete);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("text", "远程管理");
        map.put("img", R.drawable.wifi);
        list.add(map);

        return list;
    }
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            if (position==0){
                createDialog();
            }else if(position==1){
                reFleshView(SDpath);
            }else if(position==2){
                Intent intent = new Intent(SDResource.this, ServerControlActivity.class);
                startActivity(intent);
                Log.d("x", "in FTP");
            }
            drawerLayout.closeDrawer(drawerList);
        }
    }
	//得到view
	private void reFleshView(File filePath) {
		if (filePath != null) {
			lstImageItem = sdFile.getFileList(filePath);
		}
		// TODO Auto-generated method stub
		if (lstImageItem != null) {
			setFleshView(lstImageItem);
		}
        if (searchItem != null) searchItem.collapseActionView();
	}
	//设置view
	private void setFleshView(ArrayList<HashMap<String, Object>> lst) {
		SimpleAdapter saImageItems = new SimpleAdapter(this, lst,
				R.layout.item, new String[] { "ItemImage", "ItemText" },
				new int[] { R.id.ItemImage, R.id.ItemText });
		gridview.setAdapter(saImageItems);

	}


	private void createFile(String type) {
        //创建文件/文件夹
		AlertDialog.Builder builder = new Builder(this);
		final EditText editText = new EditText(this);   //用于输入文件名的输入框
		builder.setMessage("请输入文件夹名：");
		builder.setView(editText);
		builder.setPositiveButton("确认", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				String str = editText.getText().toString();
				File f = new File(SDpath + File.separator + str);
				if (type.equals("file")) {
					try {
						f.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (type.equals("directory")) {
					f.mkdir();
				}
				reFleshView(SDpath);
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Log.d("x", "casel");
			}
		});

		builder.create().show();
	}

	private void createDialog() {
		//创建create时的对话框
		AlertDialog.Builder builder = new Builder(this);
        builder.setTitle("创建");
		builder.setMessage("请选择要创建的类型：");
		builder.setPositiveButton("文件", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                createFile("file");
			}
		});
		builder.setNegativeButton("文件夹", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				createFile("directory");
				Log.d("x", "casel");
			}
		});
		builder.create().show();
	}

	private GridView.OnItemClickListener mGridViewItemClickListener = new GridView.OnItemClickListener() {
		//当某个文件/文件夹被点击时注册的listener
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			HashMap<String, Object> mf = lstImageItem.get(position);    //position和item是怎么对应起来的（明白了）
            Log.d("LstImageView", lstImageItem.toString());
            Log.d("Item Position", String.valueOf(position));   //打印position
			File f = new File(SDpath + File.separator
					+ mf.get("ItemText").toString());
			if (!f.exists())
				return;
			if (f.isDirectory()) {
				SDpath = f; //如果是目录那么打开目录
				reFleshView(f);
			} else if (f.isFile()) {
				startActivity(sdFile.openFile(f));  //如果是文件那么打开文件
			}

		}
	};

	private GridView.OnItemLongClickListener mGridViewItemLongClickListener = new GridView.OnItemLongClickListener() {
		//长按某个item时注册的listener
        public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			HashMap<String, Object> mf = lstImageItem.get(position);
			File f = new File(SDpath + File.separator
					+ mf.get("ItemText").toString());
			Log.d("longclick", "position and id is " + position + id);
			Log.d("longclick", "long itemImage" + mf.get("ItemImage"));
			Log.d("longclick", mf.get("ItemText").toString() + "Absolute Path:"
					+ f.getAbsolutePath());
			if (f.exists()) {
                Log.d("x", f.getAbsolutePath());
			}
			Intent longClickIntent = new Intent();
			longClickIntent.putExtra("fileName", f.getAbsolutePath());
			longClickIntent.setClass(SDResource.this, ResourceDetails.class);
			startActivityForResult(longClickIntent, 0);
			return true;
		}
	};

	public void backEvent() {
		temp = SDpath.getAbsolutePath();
		Log.d("Back key pressed: ", temp);
		if (temp.equals("/storage/sdcard0")) {   //这里改成什么就好了呢？调试看一下就知道啦！哈哈哈我好聪明
			finish();
            return;
		}
		int lastFd = temp.lastIndexOf("/");
		if (lastFd == -1) {
			Log.d("Back key pressed: ", " Failed to index");
		}
		temp = temp.substring(0, lastFd);   //将SDPath设置为更高一级的目录
		SDpath = new File(temp);
		reFleshView(SDpath);
	}

	@Override
	public void onBackPressed() {
        //设置按下返回键时的动作
		backEvent();
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //创建菜单，能否改成滑动出现的？
		getMenuInflater().inflate(R.menu.sd_resource_menu, menu);
        searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
		return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        lstImageItem = sdFile.getSearchList(SDpath, newText);
        setFleshView(lstImageItem);
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchView.clearFocus();
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
			case R.id.sdcard_state:
				Intent sdCardState = new Intent(SDResource.this, SDCardState.class);
				startActivity(sdCardState);
				break;
			case R.id.escape:
				onDestroy();
				break;
		}
        return true;
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case 1:
        case 2:
			reFleshView(SDpath);
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.exit(0);
	}
}