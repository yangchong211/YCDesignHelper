# YCApt关于apt方案实践与探索
#### 目录介绍
- 00.注解系列博客汇总
- 01.什么是apt
- 02.annotationProcessor和apt区别
- 03.项目目录结构
- 04.该案例作用
- 05.使用说明
- 06.编译期注解生成代码
- 07.运行期注解案例
- 08.使用注解替代枚举
- 09.使用注解搭建路由




### 00.注解系列博客汇总


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





