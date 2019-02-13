package com.ycbjie.compiler;

import com.google.auto.service.AutoService;
import com.ycbjie.annotation.OnceClick;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/06/21
 *     desc  : 自定义Processor编译器
 *     revise:
 * </pre>
 */
@AutoService(Processor.class)
public class OnceClickProcessor extends AbstractProcessor {

    private Messager messager;
    private Elements elementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
        elementUtils = processingEnv.getElementUtils();
    }

    /**
     * 所有的注解处理都是从这个方法开始的，你可以理解为，当APT找到所有需要处理的注解后，会回调这个方法，
     * 你可以通过这个方法的参数，拿到你所需要的信息。
     *
     * 参数 Set<? extends TypeElement> annotations ：将返回所有的由该Processor处理，并待处理的 Annotations。
     * (属于该Processor处理的注解，但并未被使用，不存在与这个集合里)
     *
     * 参数 RoundEnvironment roundEnv ：表示当前或是之前的运行环境，可以通过该对象查找找到的注解。
     * @param annotations               annotations
     * @param roundEnv                  roundEnv
     * @return                          返回值 表示这组 annotations 是否被这个 Processor 接受，
     *                                  如果接受true后续子的 Processor 不会再对这个 Annotations 进行处理
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        //获取proxyMap
        Map<String, OnceProxyInfo> proxyMap = getProxyMap(roundEnv);
        //遍历proxyMap，并生成代码
        for (String key : proxyMap.keySet()) {
            OnceProxyInfo proxyInfo = proxyMap.get(key);
            //写入代码
            writeCode(proxyInfo);
        }
        return true;
    }

    /**
     * 注册自定义的注解名称
     * 通过重写该方法，告知Processor哪些注解需要处理。返回一个Set集合，集合内容为自定义注解的包名+类名。
     * @return                          set集合
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        //创建一个set集合，set集合特点是无序不重复
        Set<String> types = new LinkedHashSet<>();
        //添加到集合中
        types.add(OnceClick.class.getCanonicalName());
        return types;
    }


    @Override
    public SourceVersion getSupportedSourceVersion() {
        //获取支持的版本号
        return super.getSupportedSourceVersion();
    }

    /**-----------------------------------------注解----------------------------------------*/

    private Map<String, OnceProxyInfo> getProxyMap(RoundEnvironment roundEnv) {
        Map<String, OnceProxyInfo> proxyMap = new HashMap<>();
        //遍历项目中所有的@OnceClick注解
        Set<? extends Element> elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(OnceClick.class);
        for (Element element : elementsAnnotatedWith) {
            //target相同只能强转。不同使用getEnclosingElement
            ExecutableElement executableElement = (ExecutableElement) element;
            //获取基本的类名、包名
            TypeElement classElement = (TypeElement) element.getEnclosingElement();
            PackageElement packageElement = elementUtils.getPackageOf(classElement);

            //全类路径名称
            String fullClassName = classElement.getQualifiedName().toString();
            //类名
            String className = classElement.getSimpleName().toString();
            //包名
            String packageName = packageElement.getQualifiedName().toString();
            //方法名称
            String methodName = executableElement.getSimpleName().toString();

            //获取注解参数
            int viewId = executableElement.getAnnotation(OnceClick.class).value();

            print("fullClassName: "+ fullClassName + ",  methodName: "+methodName +
                    ",  viewId: "+viewId);


            //取得方法参数类型列表
            List<String> methodParameterTypes = getMethodParameterTypes(executableElement);

            //生成信息
            OnceMethod onceMethod = new OnceMethod(viewId,methodName, methodParameterTypes);

            OnceProxyInfo proxyInfo = proxyMap.get(fullClassName);
            if (proxyInfo != null) {
                proxyInfo.addMethod(onceMethod);
            } else {
                proxyInfo = new OnceProxyInfo(packageName, className);
                proxyInfo.setTypeElement(classElement);
                proxyInfo.addMethod(onceMethod);
                proxyMap.put(fullClassName, proxyInfo);
            }
        }
        return proxyMap;
    }

    /**
     * 取得方法参数类型列表
     */
    private List<String> getMethodParameterTypes(ExecutableElement executableElement) {
        List<? extends VariableElement> methodParameters = executableElement.getParameters();
        if (methodParameters.size()==0){
            return null;
        }
        List<String> types = new ArrayList<>();
        for (VariableElement variableElement : methodParameters) {
            TypeMirror methodParameterType = variableElement.asType();
            if (methodParameterType instanceof TypeVariable) {
                TypeVariable typeVariable = (TypeVariable) methodParameterType;
                methodParameterType = typeVariable.getUpperBound();
            }
            types.add(methodParameterType.toString());
        }
        return types;
    }

    /**
     * 开始写入代码
     * @param proxyInfo             负责存储用于代码生成的信息
     */
    private void writeCode(OnceProxyInfo proxyInfo) {
        try {
            JavaFileObject jfo = processingEnv.getFiler().createSourceFile(
                    proxyInfo.getProxyClassFullName(), proxyInfo.getTypeElement());
            Writer writer = jfo.openWriter();
            writer.write(proxyInfo.generateJavaCode());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            error(proxyInfo.getTypeElement(), "Unable to write injector for type %s: %s",
                    proxyInfo.getTypeElement(), e.getMessage());
        } catch (OnceClickException e){
            error(proxyInfo.getTypeElement(), "The use of irregular %s: %s",
                    proxyInfo.getTypeElement(), e.getMessage());
        }
    }

    private void print(String message) {
        if (message==null || message.length()==0){
            return;
        }
        messager.printMessage(Diagnostic.Kind.NOTE, message);
    }

    private void error(Element element, String message, Object... args) {
        if (message==null || message.length()==0){
            return;
        }
        if (args.length > 0) {
            message = String.format(message, args);
        }
        messager.printMessage(Diagnostic.Kind.ERROR, message, element);
    }

}
