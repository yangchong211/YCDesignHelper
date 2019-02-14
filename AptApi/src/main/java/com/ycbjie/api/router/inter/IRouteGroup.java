package com.ycbjie.api.router.inter;


import com.ycbjie.annotation.router.RouteMeta;

import java.util.Map;



public interface IRouteGroup {
    void loadInto(Map<String, RouteMeta> atlas);
}
