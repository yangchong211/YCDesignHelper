package com.ycbjie.compiler;

import java.util.List;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/06/21
 *     desc  : 方法
 *     revise: 每一个使用了@OnceClick注解的Activity或View，都会为其生成一个代理类，而一个代理中有可能有
 *             很多个@OnceClick修饰的方法，所以我们专门为每个方法有创建了一个javaBean用于保存方法信息
 * </pre>
 */
public class OnceMethod {

    private int id;
    private String methodName;
    private List<String> methodParameters;

    OnceMethod(int id, String methodName, List<String> methodParameters) {
        this.id = id;
        this.methodName = methodName;
        this.methodParameters = methodParameters;
    }

    int getMethodParametersSize() {
        return methodParameters == null ? 0 : methodParameters.size();
    }

    int getId() {
        return id;
    }

    String getMethodName() {
        return methodName;
    }

    List<String> getMethodParameters() {
        return methodParameters;
    }

}
