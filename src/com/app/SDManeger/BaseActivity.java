package com.app.SDManeger;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by baigu on 2015/12/12.
 */
public class BaseActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("BaseActivity", getClass().getSimpleName());
    }
}