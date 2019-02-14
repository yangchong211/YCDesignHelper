package com.ycbjie.annotation.click;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/06/21
 *     desc  : 一定time时间内该点击事件只能执行一次
 *     revise: 该类是作用于点击事件
 *     编译器注解案例
 * </pre>
 */
//@Retention用来修饰这是一个什么类型的注解。这里表示该注解是一个编译时注解。
@Retention(RetentionPolicy.CLASS)
//@Target用来表示这个注解可以使用在哪些地方。
// 比如：类、方法、属性、接口等等。这里ElementType.METHOD 表示这个注解可以用来修饰：方法
@Target(ElementType.METHOD)
//这里的interface并不是说OnceClick是一个接口。就像申明类用关键字class。申明注解用的就是@interface。
public @interface OnceClick {
    //返回值表示这个注解里可以存放什么类型值
    int value();
}
