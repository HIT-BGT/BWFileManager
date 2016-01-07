package com.app.SDManager;

import android.app.ActionBar;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.sdcheck.SDCardCheck;

public class SDCardState extends BaseActivity{
	
	private TextView aSize;
	private TextView tSize;
	private ProgressBar progressBar ;
	private SDCardCheck sdCheck;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filedetails);

		aSize = (TextView)findViewById(R.id.asizen);	//可用大小
		tSize = (TextView) findViewById(R.id.tsizen);	//总大小
		progressBar = (ProgressBar)findViewById(R.id.progressBar1);	//进度条
		progressBar.setMax(100);
		sdCheck = new SDCardCheck();
		putSDCardContext();
		ActionBar actionBar = getActionBar();   //获得位于顶部的ActionBar
		actionBar.setDisplayHomeAsUpEnabled(true);
	}
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    TaskStackBuilder.create(this)
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                } else {
                    upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
            default:
                return false;
        }
    }


    public void putSDCardContext(){
		sdCheck.SDCardSize();	//在SDCardCheck类中用于设置nSDTotalSize和nSDFreeSize
		long nFreeSize = sdCheck.getnSDFreeSize();  //获取剩余空间
		long nTotalSize = sdCheck.getnSDTotalSize();    //获取总空间
		if (nFreeSize > 1024){  //如果剩余空间>1G
            nFreeSize = nFreeSize/1024;
            aSize.setText("   " + String.valueOf(nFreeSize) + " G");
        }else{
            aSize.setText("   " + String.valueOf(nFreeSize) + " MB");
        }

		if(nTotalSize > 1024){  //如果总空间>1G
			nTotalSize = nTotalSize/1024;
			tSize.setText("   " + String.valueOf(nTotalSize) + " G");
		}else {
			tSize.setText("   " + String.valueOf(nTotalSize) + " MB");
		}

		if(nTotalSize != 0) {   //为进度条设置进度
			progressBar.setProgress(100 - (int)(nFreeSize*100/nTotalSize));
		}
	}
}
