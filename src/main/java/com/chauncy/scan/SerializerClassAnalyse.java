package com.chauncy.scan;

import java.io.Serializable;

/**
 * @author : chengxu@corp.netease.com
 * @since : 2020/3/20
 */
public class SerializerClassAnalyse implements ClassAnalyse {

    @Override
    public void analyse(Class<?> src) {

        if (!(src instanceof Serializable)) {
            System.out.println(" not Serializer impl = " + src.getName());

        }
    }
}
