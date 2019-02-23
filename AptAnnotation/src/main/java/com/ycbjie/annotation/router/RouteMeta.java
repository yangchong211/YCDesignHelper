package com.ycbjie.annotation.router;


import javax.lang.model.element.Element;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/12/12
 *     desc  : 路由
 *     revise:
 *     编译器注解案例
 * </pre>
 */

public class RouteMeta {

    public enum Type {
        //activity
        ACTIVITY ,
        //service
        I_SERVICE
    }

    private Type type;

    /**
     * 节点（Activity）
     */
    private Element element;
    /**
     * 注解使用的类对象
     */
    private Class<?> destination;
    /**
     * 路由地址
     */
    private String path;
    /**
     * 路由组
     */
    private String group;

    public static RouteMeta build(Type type, Class<?> destination, String path, String group) {
        return new RouteMeta(type, null, destination, path, group);
    }

    public RouteMeta() {

    }

    public RouteMeta(Type type, Router route, Element element) {
        this(type, element, null, route.path(), route.group());
    }

    private RouteMeta(Type type, Element element, Class<?> destination, String path, String group) {
        this.type = type;
        this.destination = destination;
        this.element = element;
        this.path = path;
        this.group = group;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public Class<?> getDestination() {
        return destination;
    }

    public void setDestination(Class<?> destination) {
        this.destination = destination;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
