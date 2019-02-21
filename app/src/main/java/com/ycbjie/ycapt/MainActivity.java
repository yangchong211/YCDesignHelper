package com.ycbjie.ycapt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ycbjie.annotation.router.RouteMeta;
import com.ycbjie.api.router.ARouter;
import com.ycbjie.api.router.callback.NavigationCallback;
import com.ycbjie.api.router.info.Postcard;
import com.ycbjie.api.router.inter.IRouteGroup;
import com.ycbjie.api.router.routes.ARouter_Root_app;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,FirstActivity.class));
            }
        });
        findViewById(R.id.tv_4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,FourActivity.class));
            }
        });
        findViewById(R.id.tv_5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("title","标题-------------");
                ARouter.getsInstance()
                        .build(Path.five)
                        .withBundle(bundle)
                        .navigation();
            }
        });
        findViewById(R.id.tv_6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getsInstance().build(Path.six)
                        .navigation(MainActivity.this, new NavigationCallback() {
                    @Override
                    public void onFound(Postcard postcard) {
                        Log.e("NavigationCallback","找到跳转页面");
                    }

                    @Override
                    public void onLost(Postcard postcard) {
                        Log.e("NavigationCallback","未找到");
                    }

                    @Override
                    public void onArrival(Postcard postcard) {
                        Log.e("NavigationCallback","成功跳转");
                    }
                });
            }
        });
        findViewById(R.id.tv_7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test();
            }
        });

        int a = getInt(Numbers1.ONE);
    }

    private int getInt(@Numbers1.NumbersInt int a){
        return a;
    }


    /**
     * 注解生成的代码，在build/generated/source/apt/debug/
     * 模拟跳转到FiveActivity页面
     */
    public void test() {
        ARouter_Root_app rootApp = new ARouter_Root_app();
        HashMap<String, Class<? extends IRouteGroup>> rootMap = new HashMap<>();
        rootApp.loadInto(rootMap);
        //得到/main分组
        Class<? extends IRouteGroup> aClass = rootMap.get("main");
        try {
            HashMap<String, RouteMeta> groupMap = new HashMap<>();
            aClass.newInstance().loadInto(groupMap);
            //得到MainActivity
            RouteMeta main = groupMap.get("/main/FiveActivity");
            Class<?> clazz = main.getDestination();
            Intent intent = new Intent(this, clazz);
            startActivity(intent);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
