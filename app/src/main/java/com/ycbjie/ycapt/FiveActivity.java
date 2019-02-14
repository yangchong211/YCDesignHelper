package com.ycbjie.ycapt;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.ycbjie.annotation.router.Extra;
import com.ycbjie.annotation.router.Router;
import com.ycbjie.api.router.ARouter;
import com.ycbjie.api.router.inter.IExtra;


@Router(path = Path.five)
public class FiveActivity extends AppCompatActivity {

    @Extra
    String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_five);
        //添加这行代码，实际上就是自动生成了下面获取参数值的代码
        ARouter.getsInstance().inject(this);
        //这个是自动生成的代码
        /*public class FiveActivity_Extra implements IExtra {
            @Override
            public void loadExtra(Object target) {
                FiveActivity t = (FiveActivity)target;
                t.title = t.getIntent().getStringExtra("title");
            }
        }*/



        //如果不添加插入注解，则可以直接用下面的代码。
        //Intent intent = getIntent();
        //String title = intent.getStringExtra("title");
        Toast.makeText(this, "title=" + title, Toast.LENGTH_SHORT).show();
    }

}
