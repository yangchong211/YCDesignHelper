package com.ycbjie.compiler.router;


import com.squareup.javapoet.ClassName;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/06/21
 *     desc  : 常量
 *     revise:
 * </pre>
 */
public class RouterConstants {


    public static final ClassName ROUTER = ClassName.get(
            "com.ycbjie.api.router", "EasyRouter");
    public static final String I_SERVICE = "com.ycbjie.api.router.inter.IService";
    public static final String IROUTE_GROUP = "com.ycbjie.api.router.inter.IRouteGroup";
    public static final String IROUTE_ROOT = "com.ycbjie.api.router.inter.IRouteRoot";
    public static final String IEXTRA = "com.ycbjie.api.router.inter.IExtra";
    public static final String ANN_TYPE_EXTRA = "com.ycbjie.annotation.router.Extra";
    public static final String PACKAGE_OF_GENERATE_FILE = "com.ycbjie.api.router.routes";

    public static final String ARGUMENTS_NAME = "moduleName";
    public static final String ACTIVITY = "android.app.Activity";

    public static final String METHOD_LOAD_INTO = "loadInto";
    public static final String METHOD_LOAD_EXTRA = "loadExtra";


    public static final String SEPARATOR = "_";
    public static final String PROJECT = "EaseRouter";
    public static final String NAME_OF_GROUP = PROJECT + SEPARATOR + "Group" + SEPARATOR;
    public static final String NAME_OF_ROOT = PROJECT + SEPARATOR + "Root" + SEPARATOR;



    public static final String PARCELABLE = "android.os.Parcelable";

    private static final String LANG = "java.lang";
    public static final String BYTE = LANG + ".Byte";
    public static final String SHORT = LANG + ".Short";
    public static final String INTEGER = LANG + ".Integer";
    public static final String LONG = LANG + ".Long";
    public static final String FLOAT = LANG + ".Float";
    public static final String DOUBEL = LANG + ".Double";
    public static final String BOOLEAN = LANG + ".Boolean";
    public static final String STRING = LANG + ".String";
    public static final String ARRAY = "ARRAY";

    public static final String ARRAYLIST = "java.util.ArrayList";
    public static final String LIST = "java.util.List";

    public static final String BYTEARRAY = "byte[]";
    public static final String SHORTARRAY = "short[]";
    public static final String BOOLEANARRAY = "boolean[]";
    public static final String CHARARRAY = "char[]";
    public static final String DOUBLEARRAY = "double[]";
    public static final String FLOATARRAY = "float[]";
    public static final String INTARRAY = "int[]";
    public static final String LONGARRAY = "long[]";
    public static final String STRINGARRAY = "java.lang.String[]";


    public static final String NAME_OF_EXTRA = SEPARATOR + "Extra";

}
