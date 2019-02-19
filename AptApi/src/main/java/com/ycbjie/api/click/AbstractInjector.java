package com.ycbjie.api.click;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/06/21
 *     desc  : 注射代码接口
 *     revise: 代理类接口，所有生成代码类都要实现这个接口。
 * </pre>
 */
public interface AbstractInjector<T> {

    /**
     * 注射代码
     * @param finder            finder
     * @param target            target
     * @param source            source
     */
    void inject(Finder finder, T target, Object source);

    /**
     * 设置间隔时间
     * @param time              time
     */
    void setIntervalTime(long time);
}
