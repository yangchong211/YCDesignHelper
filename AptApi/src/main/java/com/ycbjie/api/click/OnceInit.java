package com.ycbjie.api.click;


import android.app.Activity;
import android.view.View;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/06/21
 *     desc  : 初始化类
 *     revise:
 * </pre>
 */
public class OnceInit {

    private static Map<Class<?>, AbstractInjector<Object>> INJECTORS = new LinkedHashMap<>();

    private static final long INTERVAL_TIME = 2000;
    private static final String PROXY = "_Once_Proxy";

    public static void once(Activity activity, long intervalTime) {
        AbstractInjector<Object> injector = findInjector(activity);
        injector.inject(Finder.ACTIVITY, activity, activity);
        injector.setIntervalTime(intervalTime);
    }

    public static void once(View view, long intervalTime) {
        AbstractInjector<Object> injector = findInjector(view);
        injector.inject(Finder.VIEW, view, view);
        injector.setIntervalTime(intervalTime);
    }

    public static void once(Activity activity) {
        once(activity, INTERVAL_TIME);
    }

    public static void once(View view) {
        once(view, INTERVAL_TIME);
    }

    /**
     * 查找生成的代码
     * @param activity              activity
     * @return                      AbstractInjector<Object>
     */
    private static AbstractInjector<Object> findInjector(Object activity) {
        Class<?> clazz = activity.getClass();
        //使用Map缓存一下，避免重复查找
        AbstractInjector<Object> injector = INJECTORS.get(clazz);
        if (injector == null) {
            try {
                //生成代码的类名是有格式的，className$$PROXY.所以我们可以通过字符串找到类，并初始化它。
                Class injectorClazz = Class.forName(clazz.getName() + PROXY);
                injector = (AbstractInjector<Object>) injectorClazz.newInstance();
                INJECTORS.put(clazz, injector);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return injector;
    }

}
