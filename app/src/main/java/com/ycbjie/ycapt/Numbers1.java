package com.ycbjie.ycapt;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public class Numbers1 {
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;

    @IntDef({ONE, TWO, THREE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface NumbersInt {

    }

    private void number(@NumbersInt int number){
        switch (number){
            case ONE:

                break;
            case TWO:

                break;
            case THREE:

                break;
        }
    }
}