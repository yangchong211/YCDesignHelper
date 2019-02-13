package com.ycbjie.compiler;

import java.util.List;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/06/21
 *     desc  : 方法
 *     revise:
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
