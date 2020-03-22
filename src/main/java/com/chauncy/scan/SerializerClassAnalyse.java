package com.chauncy.scan;

import java.io.Serializable;


public class SerializerClassAnalyse implements ClassAnalyse {

    public void analyse(Class<?> src) {
        try {
            Object o = src.newInstance();
            if (!(o instanceof Serializable)) {
                System.out.println(" not Serializer impl = " + src.getName());
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
