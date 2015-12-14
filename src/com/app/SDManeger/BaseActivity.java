package com.app.SDManeger;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by baigu on 2015/12/12.
 */
public class BaseActivity extends Activity {
    //一个基类，项目里其他的Activity都继承自这个Activity，这样在OnCreate函数被调用时能够打印提示信息，知道自己处在哪一个Activity。
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("BaseActivity", getClass().getSimpleName());
    }
}