package com.ycbjie.api.router.inter;

import java.util.Map;


public interface IRouteRoot {
    void loadInto(Map<String, Class<? extends IRouteGroup>> routes);
}
