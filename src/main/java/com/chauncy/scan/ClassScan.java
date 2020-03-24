package com.chauncy.scan;

import java.util.Set;


public interface ClassScan {
    String CLASS_SUFFIX = ".class";
    String JAR_SUFFIX = ".jar";

    Set<Class<?>> search(String packageName, MyPredicate<String> predicate);


}
