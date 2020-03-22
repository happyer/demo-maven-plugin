package com.chauncy.scan;

import java.util.Set;


public class ClassScannerUtils {

    public static Set<Class<?>> searchClasses(String packageName) {
        return searchClasses(packageName, null);
    }

    public static Set<Class<?>> searchClasses(String packageName, MyPredicate<String> predicate) {
        return ScanExecutor.getInstance().search(packageName, predicate);
    }


    public static Set<Class<?>> searchClasses(String packageName, MyPredicate<String> predicate,
        String baseDir) {
        return ScanExecutor.getInstance().search(packageName, predicate, baseDir);
    }

    public static void searchClasses(String packageName, MyPredicate<String> predicate,
        Observer observer) {
        for (Class<?> searchClass : searchClasses(packageName, predicate)) {
            observer.notifyAnalyser(searchClass);
        }
    }

    public static void searchClasses(String packageName, MyPredicate<String> predicate,
        Observer observer, String baseDir) {
        for (Class<?> searchClass : searchClasses(packageName, predicate, baseDir)) {
            observer.notifyAnalyser(searchClass);
        }
    }


}
