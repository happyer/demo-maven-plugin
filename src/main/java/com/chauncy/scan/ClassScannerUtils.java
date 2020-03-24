package com.chauncy.scan;

import java.util.Set;
import org.apache.maven.project.MavenProject;


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

    public static Set<Class<?>> searchClasses(String packageName, MyPredicate<String> predicate,
        MavenProject mavenProject) {
        return ScanExecutor.getInstance().search(packageName, predicate, mavenProject);
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


    public static void searchClasses(String packageName, MyPredicate<String> predicate,
        Observer observer, MavenProject mavenProject) {
        for (Class<?> searchClass : searchClasses(packageName, predicate, mavenProject)) {
            observer.notifyAnalyser(searchClass);
        }
    }

}
