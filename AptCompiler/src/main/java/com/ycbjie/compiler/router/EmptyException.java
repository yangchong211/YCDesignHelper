package com.ycbjie.compiler.router;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/06/21
 *     desc  : 自定义路由异常
 *     revise:
 * </pre>
 */
public class EmptyException extends RuntimeException {

    EmptyException(String message) {
        //自定义异常
        super(message);
    }
}
