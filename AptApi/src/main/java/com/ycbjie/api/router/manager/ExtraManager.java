package com.ycbjie.api.router.manager;

import android.app.Activity;
import android.util.LruCache;

import com.ycbjie.api.router.inter.IExtra;

import java.lang.reflect.Constructor;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/06/21
 *     desc  : 注入manager
 *     revise:
 * </pre>
 */
public class ExtraManager {

    private static final String SUFFIX_AUTO_WIRED = "_Extra";
    private static ExtraManager instance;
    private LruCache<String, IExtra> classCache;

    public static ExtraManager getInstance() {
        if (instance == null) {
            synchronized (ExtraManager.class) {
                if (instance == null) {
                    instance = new ExtraManager();
                }
            }
        }
        return instance;
    }


    private ExtraManager() {
        //创建LruCache缓存对象，设置最大缓存数量为50
        classCache = new LruCache<>(50);
    }


    /**
     * 注入
     * @param instance                  必须是Activity类型上下文
     */
    public void loadExtras(Activity instance) {
        //获取当前Activity的类名
        String className = instance.getClass().getName();
        //查找对应activity的缓存
        IExtra iExtra = classCache.get(className);
        try {
            if (null == iExtra) {
                //获取class，共有三种方式
                Class<?> aClass = Class.forName(instance.getClass().getName() + SUFFIX_AUTO_WIRED);
                //获取构造方法
                Constructor<?> constructor = aClass.getConstructor();
                iExtra = (IExtra) constructor.newInstance();
            }
            iExtra.loadExtra(instance);
            //存储对应activity到缓存中
            classCache.put(className, iExtra);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
