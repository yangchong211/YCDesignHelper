package com.ycbjie.api.router;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;


import com.ycbjie.annotation.router.RouteMeta;
import com.ycbjie.api.router.callback.NavigationCallback;
import com.ycbjie.api.router.exception.NoRouteFoundException;
import com.ycbjie.api.router.info.Postcard;
import com.ycbjie.api.router.inter.IRouteGroup;
import com.ycbjie.api.router.inter.IRouteRoot;
import com.ycbjie.api.router.inter.IService;
import com.ycbjie.api.router.manager.ExtraManager;
import com.ycbjie.api.router.utils.ClassUtils;
import com.ycbjie.api.router.utils.Warehouse;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/06/21
 *     desc  : 路由调用类
 *     revise: https://github.com/yangchong211/YCApt
 * </pre>
 */
public class ARouter {

    private static final String TAG = "ARouter";
    /**
     * 注意这个是注解生成代码的路径
     */
    private static final String ROUTE_ROOT_PACKAGE = "com.ycbjie.api.router.routes";
    private static final String SDK_NAME = "ARouter";
    private static final String SEPARATOR = "_";
    private static final String SUFFIX_ROOT = "Root";
    private static final String SPLITE = "/";

    private static ARouter sInstance;
    private static Application mContext;
    private Handler mHandler;

    private ARouter() {
        //创建handler对象
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static ARouter getsInstance() {
        synchronized (ARouter.class) {
            if (sInstance == null) {
                sInstance = new ARouter();
            }
        }
        return sInstance;
    }

    public static void init(Application application) {
        mContext = application;
        try {
            //分组表制作，首先需要在用户使用路由跳转之前把这些路由映射关系拿到手
            loadInfo();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "初始化失败!", e);
        }
    }

    /**
     * 分组表制作
     */
    private static void loadInfo() throws PackageManager.NameNotFoundException,
            InterruptedException, ClassNotFoundException,
            NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException {
        //获得所有 apt生成的路由类的全类名 (路由表)
        Set<String> routerMap = ClassUtils.getFileNameByPackageName(mContext, ROUTE_ROOT_PACKAGE);
        for (String className : routerMap) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(ROUTE_ROOT_PACKAGE);
            stringBuilder.append(".");
            stringBuilder.append(SDK_NAME);
            stringBuilder.append(SEPARATOR);
            stringBuilder.append(SUFFIX_ROOT);
            String s = stringBuilder.toString();
            Log.d(TAG,"className-----------"+s);
            if (className.startsWith(s)) {
                //root中注册的是分组信息 将分组信息加入仓库中
                Object o = Class.forName(className).getConstructor().newInstance();
                ((IRouteRoot) o).loadInto(Warehouse.groupsIndex);
            }
        }
        for (Map.Entry<String, Class<? extends IRouteGroup>> stringClassEntry :
                Warehouse.groupsIndex.entrySet()) {
            Log.d(TAG, "Root映射表[ " + stringClassEntry.getKey() + " : "
                    + stringClassEntry.getValue() + "]");
        }
    }

    /**
     * 传入path路径
     * 比如：path="/main/FiveActivity"
     * 那么path="/main/FiveActivity"              group="main"
     * @param path                      path路径
     * @return                          Postcard信息类
     */
    public Postcard build(String path) {
        if (TextUtils.isEmpty(path)) {
            throw new RuntimeException("路由地址无效!");
        } else {
            return build(path, extractGroup(path));
        }
    }

    /**
     * 传入path路径
     * 比如：path="/main/FiveActivity"
     * 那么path="/main/FiveActivity"              group="main"
     * @param path                      path路径
     * @param group                     group组
     * @return                          Postcard信息类
     */
    private Postcard build(String path, String group) {
        if (TextUtils.isEmpty(path) || TextUtils.isEmpty(group)) {
            throw new RuntimeException("路由地址无效!");
        } else {
            Log.d(TAG,"传入path路径----"+path+"--group组---"+group);
            return new Postcard(path, group);
        }
    }

    /**
     * 获得组别
     * @param path                      path路径
     * @return                          字符串
     */
    private String extractGroup(String path) {
        if (TextUtils.isEmpty(path) || !path.startsWith(SPLITE)) {
            throw new RuntimeException(path + " : 不能提取group.");
        }
        try {
            String defaultGroup = path.substring(1, path.indexOf(SPLITE, 1));
            if (TextUtils.isEmpty(defaultGroup)) {
                throw new RuntimeException(path + " : 不能提取group.");
            } else {
                return defaultGroup;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 最终会调用这个方法
     * @param context                   上下文
     * @param postcard                  Postcard
     * @param requestCode               请求码
     * @param callback                  回调callback
     * @return
     */
    public Object navigation(Context context, final Postcard postcard, final int requestCode,
                             final NavigationCallback callback) {
        try {
            prepareCard(postcard);
        }catch (NoRouteFoundException e) {
            e.printStackTrace();
            //没找到
            if (null != callback) {
                callback.onLost(postcard);
            }
            return null;
        }
        if (null != callback) {
            callback.onFound(postcard);
        }

        switch (postcard.getType()) {
            //跳转到activity
            case ACTIVITY:
                startActivity(context,postcard,requestCode,callback);
                break;
            case I_SERVICE:
                return postcard.getService();
            default:
                break;
        }
        return null;
    }

    /**
     * 跳转到activity
     */
    private void startActivity(Context context, final Postcard postcard,
                               final int requestCode, final NavigationCallback callback) {
        final Context currentContext = null == context ? mContext : context;
        final Intent intent = new Intent(currentContext, postcard.getDestination());
        intent.putExtras(postcard.getExtras());
        int flags = postcard.getFlags();
        if (-1 != flags) {
            intent.setFlags(flags);
        } else if (!(currentContext instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Bundle bundle = postcard.getOptionsBundle();
                //可能需要返回码
                if (requestCode > 0) {
                    if (currentContext instanceof Activity){
                        ActivityCompat.startActivityForResult((Activity) currentContext,
                                intent, requestCode, bundle);
                    }
                } else {
                    ActivityCompat.startActivity(currentContext, intent, bundle);
                }

                if (currentContext instanceof Activity){
                    if ((0 != postcard.getEnterAnim() || 0 != postcard.getExitAnim())) {
                        //老版本
                        ((Activity) currentContext).overridePendingTransition(
                                postcard.getEnterAnim(), postcard.getExitAnim());
                    }
                }
                //跳转完成
                if (null != callback) {
                    callback.onArrival(postcard);
                }
            }
        });
    }

    /**
     * 准备卡片
     * @param card                      Postcard信息类
     */
    private void prepareCard(Postcard card) {
        String path = card.getPath();
        String group = card.getGroup();
        Log.i("EasyRouter-------路径",path);
        RouteMeta routeMeta = Warehouse.routes.get(path);
        if (null == routeMeta) {
            Class<? extends IRouteGroup> groupMeta = Warehouse.groupsIndex.get(card.getGroup());
            if (null == groupMeta) {
                throw new NoRouteFoundException("没找到对应路由：分组=" + card.getGroup() + "   路径=" + card.getPath());
            }
            IRouteGroup iGroupInstance;
            try {
                iGroupInstance = groupMeta.getConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("路由分组映射表记录失败.", e);
            }
            iGroupInstance.loadInto(Warehouse.routes);
            //已经准备过了就可以移除了 (不会一直存在内存中)
            Warehouse.groupsIndex.remove(group);
            //再次进入 else
            //TODO  这里使用递归，是否有优化的方案
            prepareCard(card);
        } else {
            //类 要跳转的activity 或IService实现类
            card.setDestination(routeMeta.getDestination());
            card.setType(routeMeta.getType());
            switch (routeMeta.getType()) {
                case I_SERVICE:
                    Class<?> destination = routeMeta.getDestination();
                    IService service = Warehouse.services.get(destination);
                    if (null == service) {
                        try {
                            service = (IService) destination.getConstructor().newInstance();
                            Warehouse.services.put(destination, service);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    card.setService(service);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 注入
     * @param instance              activity上下文
     */
    public void inject(Activity instance) {
        ExtraManager.getInstance().loadExtras(instance);
    }
}
