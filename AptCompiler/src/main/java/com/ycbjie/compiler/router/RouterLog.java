package com.ycbjie.compiler.router;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;


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
