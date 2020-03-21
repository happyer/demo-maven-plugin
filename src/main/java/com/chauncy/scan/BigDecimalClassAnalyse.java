package com.chauncy.scan;

import java.lang.reflect.Field;

public class BigDecimalClassAnalyse implements ClassAnalyse {

    public void analyse(Class<?> src) {
        Field[] fields = src.getDeclaredFields();
        for (Field field : fields) {
            if (field.getType().toString().contains("BigDecimal")) {
                System.out.println("use BigDecimal  = " + src.getName());
                break;
            }
        }
    }
}
