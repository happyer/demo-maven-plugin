package me.davenkin.scan;

import java.lang.reflect.Field;

/**
 * @author : chengxu@corp.netease.com
 * @since : 2020/3/20
 */
public class BigDecimalClassAnalyse implements ClassAnalyse {

    @Override
    public void analyse(Class<?> src) {
        Field[] fields = src.getDeclaredFields();
        for (Field field : fields) {
            if (field.getType().toGenericString().contains("BigDecimal")) {
                System.out.println("use BigDecimal  = " + src.getName());
                break;
            }
        }
    }
}
