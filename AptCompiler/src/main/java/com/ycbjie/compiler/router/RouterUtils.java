package com.ycbjie.compiler.router;

import java.util.Collection;
import java.util.Map;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/12/21
 *     desc  : 工具类
 *     revise: https://github.com/yangchong211/YCApt
 * </pre>
 */
public final class RouterUtils {
    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isEmpty(Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    public static boolean isEmpty(final Map<?, ?> map) {
        return map == null || map.isEmpty();
    }
}
