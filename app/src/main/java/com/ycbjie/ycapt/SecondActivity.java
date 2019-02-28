package com.ycbjie.ycapt;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.ycbjie.annotation.click.OnceClick;
import com.ycbjie.annotation.contentView.ContentView;
import com.ycbjie.api.click.OnceInit;
import com.ycbjie.api.contentView.ContentActivity;


@ContentView(R.layout.activity_first)
public class SecondActivity extends ContentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv_1 = findViewById(R.id.tv_1);
        tv_1.setText("使用注解[运行期]，实现setContentView的功能");
    }


}
