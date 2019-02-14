package com.ycbjie.api.router.utils;

import com.ycbjie.annotation.router.RouteMeta;
import com.ycbjie.api.router.inter.IRouteGroup;
import com.ycbjie.api.router.inter.IService;

import java.util.HashMap;
import java.util.Map;


public class Warehouse {

    // root 映射表 保存分组信息
    public static Map<String, Class<? extends IRouteGroup>> groupsIndex = new HashMap<>();

    // group 映射表 保存组中的所有数据
    public static Map<String, RouteMeta> routes = new HashMap<>();

    // group 映射表 保存组中的所有数据
    public static Map<Class, IService> services = new HashMap<>();
    // TestServiceImpl.class , TestServiceImpl 没有再反射
}
