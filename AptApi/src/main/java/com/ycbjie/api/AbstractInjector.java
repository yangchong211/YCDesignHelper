package com.ycbjie.api;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/06/21
 *     desc  : 注射代码接口
 *     revise:
 * </pre>
 */
public interface AbstractInjector<T> {

    //注射代码
    void inject(Finder finder, T target, Object source);

    //设置间隔时间
    void setIntervalTime(long time);
}
