package com.ycbjie.compiler.router;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;
import com.ycbjie.annotation.router.RouteMeta;
import com.ycbjie.annotation.router.Router;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/06/21
 *     desc  : 自定义路由Processor编译器
 *     revise:
 * </pre>
 */
@AutoService(Processor.class)
/**
 处理器接收的参数 替代 {@link AbstractProcessor#getSupportedOptions()} 函数
 */
@SupportedOptions(RouterConstants.ARGUMENTS_NAME)
public class RouterProcessor extends AbstractProcessor {


    /**
     * key:组名 value:类名
     */
    private Map<String, String> rootMap = new TreeMap<>();
    /**
     * 分组 key:组名 value:对应组的路由信息
     */
    private Map<String, List<RouteMeta>> groupMap = new HashMap<>();

    private Elements elementUtils;
    private Filer filer;
    private Types typeUtils;
    private RouterLog log;
    private String moduleName;

    /**
     * 初始化方法
     * @param processingEnvironment                 获取信息
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        //节点工具类 (类、函数、属性都是节点)
        elementUtils = processingEnvironment.getElementUtils();
        //文件生成器 类/资源
        filer = processingEnvironment.getFiler();
        //Locale locale = processingEnvironment.getLocale();
        Messager messager = processingEnvironment.getMessager();
        log = RouterLog.newLog(messager);
        Map<String, String> options = processingEnvironment.getOptions();
        //SourceVersion sourceVersion = processingEnvironment.getSourceVersion();
        //type(类信息)工具类
        typeUtils = processingEnvironment.getTypeUtils();

        //参数是模块名 为了防止多模块/组件化开发的时候 生成相同的编译文件 xx$$ROOT$$文件
        if (!RouterUtils.isEmpty(options)) {
            moduleName = options.get(RouterConstants.ARGUMENTS_NAME);
        }
        if (RouterUtils.isEmpty(moduleName)) {
            throw new EmptyException("Not set processor moduleName option !");
        }
        log.i("init RouterProcessor " + moduleName + " success !");
    }

    /**
     * 所有的注解处理都是从这个方法开始的，你可以理解为，当APT找到所有需要处理的注解后，会回调这个方法，
     * 你可以通过这个方法的参数，拿到你所需要的信息。
     *
     * 参数Set<? extends TypeElement> annotations：将返回所有由该Processor处理，并待处理的Annotations。
     * (属于该Processor处理的注解，但并未被使用，不存在与这个集合里)
     *
     * 参数 RoundEnvironment roundEnv ：表示当前或是之前的运行环境，可以通过该对象查找找到的注解。
     * @param annotations               annotations
     * @param roundEnv                  roundEnv
     * @return                          返回值 表示这组 annotations 是否被这个 Processor 接受，
     *                                  如果接受true后续子的 Processor不会再对这个Annotations进行处理
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (!RouterUtils.isEmpty(annotations)) {
            //被Route注解的节点集合
            Set<? extends Element> rootElements = roundEnv.getElementsAnnotatedWith(Router.class);
            if (!RouterUtils.isEmpty(rootElements)) {
                processorRouter(rootElements);
            }
            return true;
        }
        return false;
    }

    /*@Override
    public Set<String> getSupportedOptions() {
        return super.getSupportedOptions();
    }*/

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
        types.add(Router.class.getCanonicalName());
        return types;
        //return super.getSupportedAnnotationTypes();
    }

    /**
     * 获取当前支持的版本号
     * 当然也可以：在类添加注解，效果是一样的 @SupportedSourceVersion(SourceVersion.RELEASE_7)
     * @return                          jdk版本号
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        //获取支持的版本号
        return SourceVersion.RELEASE_7;
    }



    /**-----------------------------------------注解----------------------------------------*/

    private void processorRouter(Set<? extends Element> rootElements) {
        //首先获取activity信息
        TypeElement activity = elementUtils.getTypeElement(RouterConstants.ACTIVITY);
        TypeElement service = elementUtils.getTypeElement(RouterConstants.I_SERVICE);
        for (Element element : rootElements) {
            RouteMeta routeMeta;
            //类信息
            TypeMirror typeMirror = element.asType();
            log.i("Route class:" + typeMirror.toString());
            //获取注解
            Router route = element.getAnnotation(Router.class);
            if (typeUtils.isSubtype(typeMirror, activity.asType())) {
                routeMeta = new RouteMeta(RouteMeta.Type.ACTIVITY, route, element);
            } else if (typeUtils.isSubtype(typeMirror, service.asType())) {
                routeMeta = new RouteMeta(RouteMeta.Type.I_SERVICE, route, element);
            } else {
                throw new RuntimeException("Just support Activity or IService Route: " + element);
            }
            categories(routeMeta);
        }

        //获取
        TypeElement iRouteGroup = elementUtils.getTypeElement(RouterConstants.I_ROUTE_GROUP);
        TypeElement iRouteRoot = elementUtils.getTypeElement(RouterConstants.I_ROUTE_ROOT);

        //生成Group记录分组表
        generatedGroup(iRouteGroup);

        //生成Root类 作用：记录<分组，对应的Group类>
        generatedRoot(iRouteRoot, iRouteGroup);
    }


    /**
     * 检查是否配置 group 如果没有配置 则从path截取出组名
     * @param routeMeta             routeMeta
     */
    private void categories(RouteMeta routeMeta) {
        if (routeVerify(routeMeta)) {
            log.i("Group : " + routeMeta.getGroup() + " path=" + routeMeta.getPath());
            //分组与组中的路由信息
            List<RouteMeta> routeMetas = groupMap.get(routeMeta.getGroup());
            if (RouterUtils.isEmpty(routeMetas)) {
                routeMetas = new ArrayList<>();
                routeMetas.add(routeMeta);
                groupMap.put(routeMeta.getGroup(), routeMetas);
            } else {
                routeMetas.add(routeMeta);
            }
        } else {
            //TODO 这个地方建议给出提示语
            log.i("Group info error:" + routeMeta.getPath());
        }
    }

    /**
     * 验证path路由地址的合法性
     * path路径必须是：path = "/test/TestActivity"   也就是  path = "/group/path"
     * @param routeMeta             routeMeta
     * @return                      true表示合法
     */
    private boolean routeVerify(RouteMeta routeMeta) {
        String path = routeMeta.getPath();
        String group = routeMeta.getGroup();
        // 必须以 / 开头来指定路由地址
        if (!path.startsWith("/")) {
            return false;
        }
        //如果group没有设置 我们从path中获得group
        if (RouterUtils.isEmpty(group)) {
            String defaultGroup = path.substring(1, path.indexOf("/", 1));
            //截取出的group还是空
            if (RouterUtils.isEmpty(defaultGroup)) {
                return false;
            }
            routeMeta.setGroup(defaultGroup);
        }
        return true;
    }

    private void generatedGroup(TypeElement iRouteGroup) {
        //创建参数类型 Map<String, RouteMeta>
        ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ClassName.get(RouteMeta.class));
        ParameterSpec altas = ParameterSpec.builder(parameterizedTypeName, "atlas").build();

        for (Map.Entry<String, List<RouteMeta>> entry : groupMap.entrySet()) {
            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(RouterConstants.METHOD_LOAD_INTO)
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .addParameter(altas);

            String groupName = entry.getKey();
            List<RouteMeta> groupData = entry.getValue();
            for (RouteMeta routeMeta : groupData) {
                //函数体的添加
                methodBuilder.addStatement("atlas.put($S,$T.build($T.$L,$T.class,$S,$S))",
                        routeMeta.getPath(),
                        ClassName.get(RouteMeta.class),
                        ClassName.get(RouteMeta.Type.class),
                        routeMeta.getType(),
                        ClassName.get(((TypeElement) routeMeta.getElement())),
                        routeMeta.getPath(),
                        routeMeta.getGroup());
            }
            String groupClassName = RouterConstants.NAME_OF_GROUP + groupName;
            TypeSpec typeSpec = TypeSpec.classBuilder(groupClassName)
                    .addSuperinterface(ClassName.get(iRouteGroup))
                    .addModifiers(Modifier.PUBLIC)
                    .addMethod(methodBuilder.build())
                    .build();
            JavaFile javaFile = JavaFile.builder(RouterConstants.PACKAGE_OF_GENERATE_FILE,
                    typeSpec).build();
            try {
                javaFile.writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            rootMap.put(groupName, groupClassName);

        }
    }


    /**
     * 生成Root类  作用：记录<分组，对应的Group类>
     * @param iRouteRoot
     * @param iRouteGroup
     */
    private void generatedRoot(TypeElement iRouteRoot, TypeElement iRouteGroup) {
        //创建参数类型 Map<String,Class<? extends IRouteGroup>> routes>
        //Wildcard 通配符
        ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ParameterizedTypeName.get(
                        ClassName.get(Class.class),
                        WildcardTypeName.subtypeOf(ClassName.get(iRouteGroup))
                ));
        //参数 Map<String,Class<? extends IRouteGroup>> routes> routes
        ParameterSpec parameter = ParameterSpec.builder(parameterizedTypeName, "routes").build();
        //函数 public void loadInfo(Map<String,Class<? extends IRouteGroup>> routes> routes)
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(RouterConstants.METHOD_LOAD_INTO)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(parameter);
        //函数体
        for (Map.Entry<String, String> entry : rootMap.entrySet()) {
            methodBuilder.addStatement("routes.put($S, $T.class)", entry.getKey(), ClassName.get(RouterConstants.PACKAGE_OF_GENERATE_FILE, entry.getValue()));
        }
        //生成$Root$类
        String className = RouterConstants.NAME_OF_ROOT + moduleName;
        TypeSpec typeSpec = TypeSpec.classBuilder(className)
                .addSuperinterface(ClassName.get(iRouteRoot))
                .addModifiers(Modifier.PUBLIC)
                .addMethod(methodBuilder.build())
                .build();
        JavaFile build = JavaFile.builder(RouterConstants.PACKAGE_OF_GENERATE_FILE, typeSpec).build();
        try {
            build.writeTo(filer);
            log.i("Generated RouteRoot：" + RouterConstants.PACKAGE_OF_GENERATE_FILE + "." + className);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
