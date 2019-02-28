package com.ycbjie.ycapt;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.ycbjie.annotation.router.Router;


@Router(path = Path.eight)
public class EightActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_six);
        TextView tv_1 = findViewById(R.id.tv_1);
        tv_1.setText("测试路由跳转8页面并且回传消息");
        tv_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //数据是使用Intent返回
                Intent intent = new Intent();
                //把返回数据存入Intent
                intent.putExtra("title", "小杨是个逗比");
                //设置返回数据
                EightActivity.this.setResult(200, intent);
                //关闭Activity
                EightActivity.this.finish();
            }
        });
    }

}
