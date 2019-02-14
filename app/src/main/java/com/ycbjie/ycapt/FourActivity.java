package com.ycbjie.ycapt;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ycbjie.annotation.contentView.ContentView;
import com.ycbjie.api.contentView.ContentActivity;


@ContentView(R.layout.activity_four)
public class FourActivity extends ContentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViewById(R.id.tv_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FourActivity.this,"运行期注解",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
