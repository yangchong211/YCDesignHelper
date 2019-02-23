package com.ycbjie.api.router.utils;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

import dalvik.system.DexFile;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/12/21
 *     desc  : 工具类
 *     revise: https://github.com/yangchong211/YCApt
 * </pre>
 */
public class ClassUtils {

    /**
     * 获得程序所有的apk(instant run会产生很多split apk)
     * @param context                       上下文
     * @return                              集合
     * @throws PackageManager.NameNotFoundException
     */
    private static List<String> getSourcePaths(Context context) throws
            PackageManager.NameNotFoundException {
        ApplicationInfo applicationInfo = context.getPackageManager()
                .getApplicationInfo(context.getPackageName(), 0);
        List<String> sourcePaths = new ArrayList<>();
        sourcePaths.add(applicationInfo.sourceDir);
        //instant run
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (null != applicationInfo.splitSourceDirs) {
                sourcePaths.addAll(Arrays.asList(applicationInfo.splitSourceDirs));
            }
        }
        return sourcePaths;
    }

    /**
     * 得到路由表的类名
     * @param context                       上下文
     * @param packageName                   包名
     * @return                              集合
     * @throws PackageManager.NameNotFoundException
     * @throws InterruptedException
     */
    public static Set<String> getFileNameByPackageName(Application context,
                                                       @NonNull final String packageName)
            throws PackageManager.NameNotFoundException, InterruptedException {
        //创建一个set集合，set集合元素不会重复
        final Set<String> classNames = new HashSet<>();
        //获得程序所有的apk
        List<String> paths = getSourcePaths(context);
        //使用同步计数器判断均处理完成
        final CountDownLatch countDownLatch = new CountDownLatch(paths.size());
        //创建线程池
        ThreadPoolExecutor threadPoolExecutor = DefaultPoolExecutor
                .newDefaultPoolExecutor(paths.size());
        for (final String path : paths) {
            if (threadPoolExecutor != null) {
                //思考一下，这里为何要使用线程池
                //关于线程池：https://github.com/yangchong211/YCThreadPool
                threadPoolExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        DexFile dexFile = null;
                        try {
                            //加载 apk中的dex 并遍历 获得所有包名为 {packageName} 的类
                            dexFile = new DexFile(path);
                            Enumeration<String> dexEntries = dexFile.entries();
                            while (dexEntries.hasMoreElements()) {
                                String className = dexEntries.nextElement();
                                if (!TextUtils.isEmpty(className) &&
                                        className.startsWith(packageName)) {
                                    classNames.add(className);
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (null != dexFile) {
                                try {
                                    dexFile.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            //释放一个
                            countDownLatch.countDown();
                        }
                    }
                });
            }
        }
        //等待执行完成
        countDownLatch.await();
        return classNames;
    }


}
