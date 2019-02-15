package com.ycbjie.ycapt;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ycbjie.annotation.contentView.ContentView;
import com.ycbjie.api.click.OnceInit;
import com.ycbjie.annotation.click.OnceClick;


@ContentView(R.layout.activity_six)
public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnceInit.once(this,2000);
    }


    @OnceClick(R.id.tv_1)
    public void Click1(){
        Log.d("tag--------------------","tv_1");
    }


}
