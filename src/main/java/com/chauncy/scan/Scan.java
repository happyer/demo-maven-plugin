package com.chauncy.scan;

import java.util.Set;
import java.util.function.Predicate;

/**
 * @author : chengxu@corp.netease.com
 * @since : 2020/3/20
 */
public interface Scan {


    String CLASS_SUFFIX = ".class";

    Set<Class<?>> search(String packageName, Predicate<String> predicate);

    default Set<Class<?>> search(String packageName) {
        return search(packageName, null);
    }

}
