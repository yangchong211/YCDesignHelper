# YCApt关于apt方案实践与探索
#### 目录介绍
- 00.注解系列博客汇总
- 01.什么是apt
- 02.annotationProcessor和apt区别
- 03.项目目录结构
- 04.该案例作用
- 05.使用说明
- 06.编译期注解生成代码[点击事件案例]
- 07.运行期注解案例[setContentView案例]
- 08.使用注解替代枚举
- 09.使用注解搭建路由[综合案例]
    - 9.1 搭建路由条件
    - 9.2 通过注解去实现路由跳转
    - 9.3 自定义路由Processor编译器
    - 9.4 利用apt生成路由映射文件
    - 9.5 路由框架的设计
    - 9.6 路由参数的传递和接收
    - 9.7 为何需要依赖注入
    - 9.8 Activity属性注入



### 00.注解系列博客汇总
#### 0.1 注解基础系列博客
- [01.Annotation注解详细介绍](https://github.com/yangchong211/YCBlogs/blob/master/android/%E6%B3%A8%E8%A7%A3/01.Annotation%E6%B3%A8%E8%A7%A3%E8%AF%A6%E7%BB%86%E4%BB%8B%E7%BB%8D.md)
    ```
    1.Annotation库的简单介绍
    2.@Nullable和@NonNull
    3.资源类型注释
    4.类型定义注释
    5.线程注释
    6.RGB颜色纸注释
    7.值范围注释
    8.权限注释
    9.重写函数注释
    10.返回值注释
    11.@Keep注释
    12.@SuppressWarnings注解
    13.其他
    ```
- [02.Dagger2深入分析，待更新]()
- [03.注解详细介绍](https://github.com/yangchong211/YCBlogs/blob/master/android/%E6%B3%A8%E8%A7%A3/03.%E6%B3%A8%E8%A7%A3%E8%AF%A6%E7%BB%86%E4%BB%8B%E7%BB%8D.md)
    - 什么是注解，注解分类有哪些？自定义注解分类？运行注解案例展示分析，以一个最简单的案例理解注解……使用注解替代枚举，使用注解限定类型
- [04.APT技术详解](https://github.com/yangchong211/YCBlogs/blob/master/android/%E6%B3%A8%E8%A7%A3/04.APT%E6%8A%80%E6%9C%AF%E8%AF%A6%E8%A7%A3.md)
    - 什么是apt？理解注解处理器的作用和用途……android-apt被替代？annotationProcessor和apt区别？ 什么是jack编译方式？
- [06.自定义annotation注解](https://github.com/yangchong211/YCBlogs/blob/master/android/%E6%B3%A8%E8%A7%A3/06.%E8%87%AA%E5%AE%9A%E4%B9%89annotation%E6%B3%A8%E8%A7%A3.md)
    - @Retention的作用？@Target(ElementType.TYPE)的解释，@Inherited注解可以被继承吗？Annotation里面的方法为何不能是private？
- [07.注解之兼容kotlin](https://github.com/yangchong211/YCBlogs/blob/master/android/%E6%B3%A8%E8%A7%A3/07.%E6%B3%A8%E8%A7%A3%E4%B9%8B%E5%85%BC%E5%AE%B9kotlin.md)
    - 后期更新
- [08.注解之处理器类Processor](https://github.com/yangchong211/YCBlogs/blob/master/android/%E6%B3%A8%E8%A7%A3/08.%E6%B3%A8%E8%A7%A3%E4%B9%8B%E5%A4%84%E7%90%86%E5%99%A8%E7%B1%BBProcessor.md)
    - 处理器类Processor介绍，重要方法，Element的作用，修饰方法的注解和ExecutableElement，了解修饰属性、类成员的注解和VariableElement……
- [10.注解遇到问题和解决方案](https://github.com/yangchong211/YCBlogs/blob/master/android/%E6%B3%A8%E8%A7%A3/10.%E6%B3%A8%E8%A7%A3%E9%81%87%E5%88%B0%E9%97%AE%E9%A2%98%E5%92%8C%E8%A7%A3%E5%86%B3%E6%96%B9%E6%A1%88.md)
    - 无法引入javax包下的类库，成功运行一次，修改代码后再运行就报错
- [11.注解代替枚举](https://github.com/yangchong211/YCBlogs/blob/master/android/%E6%B3%A8%E8%A7%A3/11.%E6%B3%A8%E8%A7%A3%E4%BB%A3%E6%9B%BF%E6%9E%9A%E4%B8%BE.md)
    - 在做内存优化时，推荐使用注解代替枚举，因为枚举占用的内存更高，如何说明枚举占用内存高呢？这是为什么呢？
- [12.注解练习案例开源代码](https://github.com/yangchong211/YCApt)
    - 注解学习小案例，比较系统性学习注解并且应用实践。简单应用了运行期注解，通过注解实现了setContentView功能；简单应用了编译器注解，通过注解实现了防暴力点击的功能，同时支持设置时间间隔；使用注解替代枚举；使用注解一步步搭建简单路由案例。结合相应的博客，在来一些小案例，从此应该对注解有更加深入的理解……



#### 0.2 [注解系列博客问题答疑](https://juejin.im/post/5c665c5ae51d450e675331c0)
- 13.0.0.1 什么是注解？系统内置的标准注解有哪些？SuppressWarnings用过没？Android中提供了哪些与线程相关的注解？
- 13.0.0.2 什么是apt？apt的难点和优势？什么是注解处理器？抽象处理器中四个方法有何作用？annotationProcessor和apt区别？
- 13.0.0.3 注解是怎么分类的？自定义注解又是怎么分类的？运行期注解原理是什么？实际注解案例有哪些？
- 13.0.0.4 在自定义注解中，Annotation里面的方法为何不能是private？Annotation里面的方法参数有哪些？
- 13.0.0.5 @Inherited是什么意思？注解是不可以继承的，这是为什么？注解的继承这个概念该如何理解？
- 13.0.0.6 什么是依赖注入？依赖注入案例举例说明，有哪些方式，具备什么优势？依赖查找和依赖注入有什么区别？
- 13.0.0.7 路由框架为何需要依赖注入，不用的话行不行？路由用什么方式注入，这些注入方式各具何特点，为何选择注解注入？
- 13.0.0.8 实际开发中使用到注解有哪些，使用注解替代枚举？如何通过注解限定传入的类型？为何说枚举损耗性能？



### 01.什么是apt
- 什么是apt
    - APT，就是Annotation Processing Tool的简称，就是可以在代码编译期间对注解进行处理，并且生成Java文件，减少手动的代码输入。注解我们平时用到的比较多的可能会是运行时注解，比如大名鼎鼎的retrofit就是用运行时注解，通过动态代理来生成网络请求。编译时注解平时开发中可能会涉及的比较少，但并不是说不常用，比如我们经常用的轮子Dagger2, ButterKnife, EventBus3 都在用，所以要紧跟潮流来看看APT技术的来龙去脉。
- 编译时注解。
    - 也有人叫它代码生成，其实他们还是有些区别的，在编译时对注解做处理，通过注解，获取必要信息，在项目中生成代码，运行时调用，和直接运行手写代码没有任何区别。而更准确的叫法：APT - Annotation Processing Tool
- 大概原理
    - Java API 已经提供了扫描源码并解析注解的框架，开发者可以通过继承 AbstractProcessor 类来实现自己的注解解析逻辑。APT 的原理就是在注解了某些代码元素（如字段、函数、类等）后，在编译时编译器会检查 AbstractProcessor 的子类，并且自动调用其 process() 方法，然后将添加了指定注解的所有代码元素作为参数传递给该方法，开发者再根据注解元素在编译期输出对应的 Java 代码


### 02.annotationProcessor和apt区别
- annotationProcessor和apt区别
    - Android 官方的 annotationProcessor 同时支持 javac 和 jack 编译方式，而 android-apt 只支持 javac 方式。当然，目前 android-apt 在 Android Gradle 插件 2.2 版本上面仍然可以正常运行，如果你没有想支持 jack 编译方式的话，可以继续使用 android-apt。
    - 目前比如一些常用框架dagger2，butterKnife，ARouter等，都支持annotationProcessor
- 什么是jack编译方式？
    - Jack (Java Android Compiler Kit)是新的Android 编译工具，从Android 6.0 开始加入,替换原有的编译工具，例如javac, ProGuard, jarjar和 dx。它主要负责将java代码编译成dex包，并支持代码压缩，混淆等。
- Jack工具的主要优势
    - 完全开放源码，源码均在AOSP中，合作伙伴可贡献源码
    - 加快编译源码，Jack 提供特殊的配置，减少编译时间：pre-dexing, 增量编译和Jack编译服务器.
    - 支持代码压缩，混淆，重打包和multidex，不在使用额外单独的包，例如ProGuard。


### 03.项目目录结构
- 项目目录结构如图：
    - app：Demo
    - OnceClickAnnotation：java Library主要放一些项目中需要用到的自定义注解及相关代码
    - OnceClickApi：Android Library. OnceClick是我们真正对外发布并交由第三方使用的库，它引用了apt-jar包
    - OnceClickCompiler：java Library主要是应用apt技术处理注解，生成相关代码或者相关源文件，是核心所在。


### 04.该案例作用
- 前期仅仅是为了学习，同时先让demo运行起来，虽然网上很多讲解apt的博客写的很详细，但是还是有必要结合实际案例练习一下。
- 在一定时间内，按钮点击事件只能执行一次。未到指定时间，不执行点击事件。


### 05.使用说明
- 如下所示
    ```
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化OnceClick,并设置点击事件间隔是2秒
        OnceInit.once(this,2000);
    }

    @OnceClick(R.id.tv_1)
    public void Click1(){
        Log.d("tag--------------------","tv_1");
    }
    ```

### 06.编译期注解生成代码
- 如下所示，在app/build/generated/source/apt/debug/MainActivity$$_Once_Proxy目录下
    ```
    // 编译生成的代码，不要修改
    // 更多内容：https://github.com/yangchong211
    package com.ycbjie.ycapt;

    import android.view.View;
    import com.ycbjie.api.Finder;
    import com.ycbjie.api.AbstractInjector;

    public class MainActivity$$_Once_Proxy<T extends MainActivity> implements AbstractInjector<T> {

        public long intervalTime;

        @Override
        public void setIntervalTime(long time) {
            intervalTime = time;
        }

        @Override
        public void inject(final Finder finder, final T target, Object source) {
            View view;
            view = finder.findViewById(source, 2131165325);
            if(view != null){
                view.setOnClickListener(new View.OnClickListener() {
                long time = 0L;
                @Override
                public void onClick(View v) {
                    long temp = System.currentTimeMillis();
                    if (temp - time >= intervalTime) {
                        time = temp;
                        target.Click1();
                    }
                }});
            }
            view = finder.findViewById(source, 2131165326);
            if(view != null){
                view.setOnClickListener(new View.OnClickListener() {
                long time = 0L;
                @Override
                public void onClick(View v) {
                    long temp = System.currentTimeMillis();
                    if (temp - time >= intervalTime) {
                        time = temp;
                        target.Click2(v);
                    }
                }});
            }
      }

    }
    ```


### 07.运行期注解案例
- 首先先定义自定义注解
    ```
    //@Retention用来修饰这是一个什么类型的注解。这里表示该注解是一个运行时注解。
    @Retention(RetentionPolicy.RUNTIME)
    //@Target用来表示这个注解可以使用在哪些地方。比如：类、方法、属性、接口等等。
    //这里ElementType.TYPE 表示这个注解可以用来修饰：Class, interface or enum declaration。
    //当你用ContentView修饰一个方法时，编译器会提示错误。
    @Target({ElementType.TYPE})
    //这里的interface并不是说ContentView是一个接口。
    //就像申明类用关键字class。申明枚举用enum。申明注解用的就是@interface。
    public @interface ContentView {
        //返回值表示这个注解里可以存放什么类型值。
        int value();
    }
    ```
- 然后需要在activity中做注解解析
    ```
    @SuppressLint("Registered")
    public class ContentActivity extends AppCompatActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //注解解析
            //遍历所有的子类
            for (Class c = this.getClass(); c != Context.class; c = c.getSuperclass()) {
                assert c != null;
                //找到修饰了注解ContentView的类
                ContentView annotation = (ContentView) c.getAnnotation(ContentView.class);
                if (annotation != null) {
                    try {
                        //获取ContentView的属性值
                        int value = annotation.value();
                        //调用setContentView方法设置view
                        this.setContentView(value);
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }
        }
    }
    ```
- 关于如何使用，注意你写的Activity需要实现ContentActivity，才能让注解生效
    ```
    @ContentView(R.layout.activity_four)
    public class FourActivity extends ContentActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            findViewById(R.id.tv_1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(FourActivity.this,"运行期注解",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    ```




### 20.其他说明
#### 01.关于博客汇总链接
- 1.[技术博客汇总](https://www.jianshu.com/p/614cb839182c)
- 2.[开源项目汇总](https://blog.csdn.net/m0_37700275/article/details/80863574)
- 3.[生活博客汇总](https://blog.csdn.net/m0_37700275/article/details/79832978)
- 4.[喜马拉雅音频汇总](https://www.jianshu.com/p/f665de16d1eb)
- 5.[其他汇总](https://www.jianshu.com/p/53017c3fc75d)



#### 02.关于我的博客
- 我的个人站点：www.yczbj.org，www.ycbjie.cn
- github：https://github.com/yangchong211
- 知乎：https://www.zhihu.com/people/yczbj/activities
- 简书：http://www.jianshu.com/u/b7b2c6ed9284
- csdn：http://my.csdn.net/m0_37700275
- 喜马拉雅听书：http://www.ximalaya.com/zhubo/71989305/
- 开源中国：https://my.oschina.net/zbj1618/blog
- 泡在网上的日子：http://www.jcodecraeer.com/member/content_list.php?channelid=1
- 邮箱：yangchong211@163.com
- 阿里云博客：https://yq.aliyun.com/users/article?spm=5176.100- 239.headeruserinfo.3.dT4bcV
- segmentfault头条：https://segmentfault.com/u/xiangjianyu/articles
- 掘金：https://juejin.im/user/5939433efe88c2006afa0c6e





