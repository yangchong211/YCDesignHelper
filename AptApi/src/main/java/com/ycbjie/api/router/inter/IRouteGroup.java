package com.ycbjie.api.router.inter;


import com.ycbjie.annotation.router.RouteMeta;

import java.util.Map;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/06/21
 *     desc  : 组
 *     revise:
 * </pre>
 */
public interface IRouteGroup {
    void loadInto(Map<String, RouteMeta> atlas);
}
