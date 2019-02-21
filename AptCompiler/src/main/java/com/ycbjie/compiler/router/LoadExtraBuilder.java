package com.ycbjie.compiler.router;

import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.ycbjie.annotation.router.Extra;

import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;


public class LoadExtraBuilder {
    private static final String INJECT_TARGET = "$T t = ($T)target";
    private MethodSpec.Builder builder;
    private Elements elementUtils;
    private Types typeUtils;

    private TypeMirror parcelableType;
    private TypeMirror iServiceType;

    public LoadExtraBuilder(ParameterSpec parameterSpec) {
        // 函数 public void loadExtra(Object target)
        builder = MethodSpec.methodBuilder(RouterConstants.METHOD_LOAD_EXTRA)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(parameterSpec);
    }

    public void setElementUtils(Elements elementUtils) {
        this.elementUtils = elementUtils;
        parcelableType = elementUtils.getTypeElement(RouterConstants.PARCELABLE).asType();
        iServiceType = elementUtils.getTypeElement(RouterConstants.I_SERVICE).asType();
    }

    public void setTypeUtils(Types typeUtils) {
        this.typeUtils = typeUtils;
    }

    public void buildStatement(Element element) {
        TypeMirror typeMirror = element.asType();
        int type = typeMirror.getKind().ordinal();
        //属性名 String text 获得text
        String fieldName = element.getSimpleName().toString();
        //获得注解 name值
        String extraName = element.getAnnotation(Extra.class).name();
        extraName = RouterUtils.isEmpty(extraName) ? fieldName : extraName;
        String defaultValue = "t." + fieldName;
        String statement = defaultValue + " = t.getIntent().";
        if (type == TypeKind.BOOLEAN.ordinal()) {
            statement += "getBooleanExtra($S, " + defaultValue + ")";
        } else if (type == TypeKind.BYTE.ordinal()) {
            statement += "getByteExtra($S, " + defaultValue + ")";
        } else if (type == TypeKind.SHORT.ordinal()) {
            statement += "getShortExtra($S, " + defaultValue + ")";
        } else if (type == TypeKind.INT.ordinal()) {
            statement += "getIntExtra($S, " + defaultValue + ")";
        } else if (type == TypeKind.LONG.ordinal()) {
            statement += "getLongExtra($S, " + defaultValue + ")";
        } else if (type == TypeKind.CHAR.ordinal()) {
            statement += "getCharExtra($S, " + defaultValue + ")";
        } else if (type == TypeKind.FLOAT.ordinal()) {
            statement += "getFloatExtra($S, " + defaultValue + ")";
        } else if (type == TypeKind.DOUBLE.ordinal()) {
            statement += "getDoubleExtra($S, " + defaultValue + ")";
        } else {
            //数组类型
            if (type == TypeKind.ARRAY.ordinal()) {
                addArrayStatement(statement, fieldName, extraName, typeMirror, element);
            } else {
                //Object
                addObjectStatement(statement, fieldName, extraName, typeMirror, element);
            }
            return;
        }
        builder.addStatement(statement, extraName);
    }

    /**
     * 添加对象 String/List/Parcelable
     *
     * @param statement
     * @param extraName
     * @param typeMirror
     * @param element
     */
    private void addObjectStatement(String statement, String fieldName, String extraName,
                                    TypeMirror typeMirror,
                                    Element element) {
        //Parcelable
        if (typeUtils.isSubtype(typeMirror, parcelableType)) {
            statement += "getParcelableExtra($S)";
        } else if (typeMirror.toString().equals(RouterConstants.STRING)) {
            statement += "getStringExtra($S)";
        } else if (typeUtils.isSubtype(typeMirror, iServiceType)) {
            statement = "t." + fieldName + " = ($T) $T.getInstance().build($S).navigation()";
            ClassName router = ClassName.get(RouterConstants.PATH, RouterConstants.PROJECT);
            builder.addStatement(statement, TypeName.get(element.asType()), router, extraName);
            return;
        } else {
            //List
            TypeName typeName = ClassName.get(typeMirror);
            //泛型
            if (typeName instanceof ParameterizedTypeName) {
                //list 或 arraylist
                ClassName rawType = ((ParameterizedTypeName) typeName).rawType;
                //泛型类型
                List<TypeName> typeArguments = ((ParameterizedTypeName) typeName)
                        .typeArguments;
                if (!rawType.toString().equals(RouterConstants.ARRAYLIST) && !rawType.toString()
                        .equals(RouterConstants.LIST)) {
                    throw new RuntimeException("Not Support Inject Type:" + typeMirror + " " +
                            element);
                }
                if (typeArguments.isEmpty() || typeArguments.size() != 1) {
                    throw new RuntimeException("List Must Specify Generic Type:" + typeArguments);
                }
                TypeName typeArgumentName = typeArguments.get(0);
                TypeElement typeElement = elementUtils.getTypeElement(typeArgumentName.toString());
                // Parcelable 类型
                if (typeUtils.isSubtype(typeElement.asType(), parcelableType)) {
                    statement += "getParcelableArrayListExtra($S)";
                } else if (typeElement.asType().toString().equals(RouterConstants.STRING)) {
                    statement += "getStringArrayListExtra($S)";
                } else if (typeElement.asType().toString().equals(RouterConstants.INTEGER)) {
                    statement += "getIntegerArrayListExtra($S)";
                } else {
                    throw new RuntimeException("Not Support Generic Type : " + typeMirror + " " +
                            element);
                }
            } else {
                throw new RuntimeException("Not Support Extra Type : " + typeMirror + " " +
                        element);
            }
        }
        builder.addStatement(statement, extraName);
    }

    /**
     * 添加数组
     *
     * @param statement
     * @param fieldName
     * @param typeMirror
     * @param element
     */
    private void addArrayStatement(String statement, String fieldName, String extraName, TypeMirror
            typeMirror, Element element) {
        //数组
        switch (typeMirror.toString()) {
            case RouterConstants.BOOLEANARRAY:
                statement += "getBooleanArrayExtra($S)";
                break;
            case RouterConstants.INTARRAY:
                statement += "getIntArrayExtra($S)";
                break;
            case RouterConstants.SHORTARRAY:
                statement += "getShortArrayExtra($S)";
                break;
            case RouterConstants.FLOATARRAY:
                statement += "getFloatArrayExtra($S)";
                break;
            case RouterConstants.DOUBLEARRAY:
                statement += "getDoubleArrayExtra($S)";
                break;
            case RouterConstants.BYTEARRAY:
                statement += "getByteArrayExtra($S)";
                break;
            case RouterConstants.CHARARRAY:
                statement += "getCharArrayExtra($S)";
                break;
            case RouterConstants.LONGARRAY:
                statement += "getLongArrayExtra($S)";
                break;
            case RouterConstants.STRINGARRAY:
                statement += "getStringArrayExtra($S)";
                break;
            default:
                //Parcelable 数组
                String defaultValue = "t." + fieldName;
                //object数组 componentType获得object类型
                ArrayTypeName arrayTypeName = (ArrayTypeName) ClassName.get(typeMirror);
                TypeElement typeElement = elementUtils.getTypeElement(arrayTypeName
                        .componentType.toString());
                //是否为 Parcelable 类型
                if (!typeUtils.isSubtype(typeElement.asType(), parcelableType)) {
                    throw new RuntimeException("Not Support Extra Type:" + typeMirror + " " +
                            element);
                }
                statement = "$T[] " + fieldName + " = t.getIntent()" +
                        ".getParcelableArrayExtra" +
                        "($S)";
                builder.addStatement(statement, parcelableType, extraName);
                builder.beginControlFlow("if( null != $L)", fieldName);
                statement = defaultValue + " = new $T[" + fieldName + ".length]";
                builder.addStatement(statement, arrayTypeName.componentType)
                        .beginControlFlow("for (int i = 0; i < " + fieldName + "" +
                                ".length; " +
                                "i++)")
                        .addStatement(defaultValue + "[i] = ($T)" + fieldName + "[i]",
                                arrayTypeName.componentType)
                        .endControlFlow();
                builder.endControlFlow();
                return;
        }
        builder.addStatement(statement, extraName);
    }

    /**
     * 加入 $T t = ($T)target
     *
     * @param className
     */
    public void injectTarget(ClassName className) {
        builder.addStatement(INJECT_TARGET, className, className);

    }

    public MethodSpec build() {
        return builder.build();
    }
}
