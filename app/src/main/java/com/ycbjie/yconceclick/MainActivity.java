package com.ycbjie.yconceclick;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ycbjie.annotation.OnceClick;
import com.ycbjie.api.OnceInit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化OnceClick,并设置点击事件间隔是2秒
        OnceInit.once(this,2000);
    }

    @OnceClick(R.id.tv_1)
    public void Click1(){
        Log.d("tag","tv_1");
    }

    @OnceClick(R.id.tv_2)
    public void Click2(View v){
        Log.d("tag","tv_2");
    }

}
