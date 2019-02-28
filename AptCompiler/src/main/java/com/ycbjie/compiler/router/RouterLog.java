package com.ycbjie.compiler.router;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/12/21
 *     desc  : 工具类
 *     revise: https://github.com/yangchong211/YCApt
 * </pre>
 */
public class RouterLog {

    private Messager messager;

    private RouterLog(Messager messager) {
        this.messager = messager;
    }

    public static RouterLog newLog(Messager messager) {
        return new RouterLog(messager);
    }

    public void i(String msg) {
        messager.printMessage(Diagnostic.Kind.NOTE, msg);
    }
}
