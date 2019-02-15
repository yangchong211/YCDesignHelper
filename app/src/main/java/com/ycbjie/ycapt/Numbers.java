package com.ycbjie.ycapt;

import android.support.annotation.IntDef;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


//使用注解替代枚举
public class Numbers {
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;

    public static final String STR_ONE = "ONE";
    public static final String STR_TWO = "TWO";
    public static final String STR_THREE = "THREE";

    @IntDef({ONE, TWO, THREE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface NumbersInt {

    }

    @StringDef({STR_ONE, STR_TWO, STR_THREE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface NumbersString {

    }
}