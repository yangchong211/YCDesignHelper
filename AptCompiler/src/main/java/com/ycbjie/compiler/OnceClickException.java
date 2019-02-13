package com.ycbjie.compiler;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/06/21
 *     desc  : 自定义异常
 *     revise:
 * </pre>
 */
public class OnceClickException extends Exception {

    public OnceClickException(String message) {
        //自定义异常
        super(message);
    }
}
