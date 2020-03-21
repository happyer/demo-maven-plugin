package com.chauncy.scan;

import java.io.Serializable;


public class SerializerClassAnalyse implements ClassAnalyse {

    public void analyse(Class<?> src) {

        if (!(src instanceof Serializable)) {
            System.out.println(" not Serializer impl = " + src.getName());

        }
    }
}
