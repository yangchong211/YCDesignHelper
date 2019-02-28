package com.ycbjie.ycapt;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.ycbjie.annotation.router.RouteMeta;
import com.ycbjie.api.router.ARouter;
import com.ycbjie.api.router.callback.NavigationCallback;
import com.ycbjie.api.router.info.Postcard;
import com.ycbjie.api.router.inter.IRouteGroup;
import com.ycbjie.api.router.routes.ARouter_Root_app;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private Toast toast;

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
        findViewById(R.id.tv_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SecondActivity.class));
            }
        });
        findViewById(R.id.tv_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getsInstance().build("/app/yc")
                        .navigation(MainActivity.this, new NavigationCallback() {
                            @Override
                            public void onFound(Postcard postcard) {
                                showToast("NavigationCallback"+"找到跳转页面");
                            }

                            @Override
                            public void onLost(Postcard postcard) {
                                showToast("NavigationCallback"+"未找到");
                            }

                            @Override
                            public void onArrival(Postcard postcard) {
                                showToast("NavigationCallback"+"成功跳转");
                            }
                        });
            }
        });
        findViewById(R.id.tv_4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                                showToast("NavigationCallback"+"找到跳转页面");
                            }

                            @Override
                            public void onLost(Postcard postcard) {
                                showToast("NavigationCallback"+"未找到");
                            }

                            @Override
                            public void onArrival(Postcard postcard) {
                                showToast("NavigationCallback"+"成功跳转");
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
        findViewById(R.id.tv_8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("title","接收数据");
                ARouter.getsInstance()
                        .build(Path.eight)
                        .withBundle(bundle)
                        .navigation(MainActivity.this,100);
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
    
    
    @SuppressLint("ShowToast")
    private void showToast(String content){
        if (content==null || content.length()==0){
            showToast("传递的吐司内容不能为空");
            return;
        }
        if (toast==null){
            toast = Toast.makeText(MainActivity.this, content, Toast.LENGTH_SHORT);
        }
        toast.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("onActivityResult",requestCode+"------------"+resultCode);
        if (requestCode==100 && data!=null){
            String title = data.getStringExtra("title");
            showToast(title);
        }
    }
}
