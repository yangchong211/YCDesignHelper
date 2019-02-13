package com.ycbjie.api;

import android.app.Activity;
import android.view.View;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/06/21
 *     desc  : 枚举
 *     revise: Finder的作用是根据不同的类型，去实现不同的findViewById方法。
 * </pre>
 */
public enum Finder {
    VIEW {
        @Override
        public View findViewById(Object source, int id) {
            return ((View) source).findViewById(id);
        }
    },
    ACTIVITY {
        @Override
        public View findViewById(Object source, int id) {
            return ((Activity) source).findViewById(id);
        }
    };
    public abstract View findViewById(Object source, int id);
}
