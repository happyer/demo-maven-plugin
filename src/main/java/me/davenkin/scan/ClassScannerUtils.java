package me.davenkin.scan;

import java.util.Set;
import java.util.function.Predicate;

/**
 * @author : chengxu@corp.netease.com
 * @since : 2020/3/20
 */
public class ClassScannerUtils {

    public static Set<Class<?>> searchClasses(String packageName){
        return searchClasses(packageName,null);
    }

    public static Set<Class<?>> searchClasses(String packageName, Predicate predicate){
        return ScanExecutor.getInstance().search(packageName,predicate);
    }

    public static void  searchClasses(String packageName, Predicate predicate,Observer observer){
        searchClasses(packageName,predicate).forEach(aClass -> observer.notifyAnalyser(aClass));
    }



}
