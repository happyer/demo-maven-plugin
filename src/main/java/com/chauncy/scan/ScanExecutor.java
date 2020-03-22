package com.chauncy.scan;

import java.util.Set;


public class ScanExecutor implements ClassScan {

    private volatile static ScanExecutor instance;

    public Set<Class<?>> search(String packageName, MyPredicate<String> predicate) {
        ClassScan fileSc = new FileScanner();
        Set<Class<?>> fileSearch = fileSc.search(packageName, predicate);
        ClassScan jarScanner = new JarScanner();
        Set<Class<?>> jarSearch = jarScanner.search(packageName,predicate);
        fileSearch.addAll(jarSearch);
        return fileSearch;
    }

    public Set<Class<?>> search(String packageName, MyPredicate<String> predicate,String baseDir) {
        ClassScan fileSc = new FileScanner(baseDir);
        Set<Class<?>> fileSearch = fileSc.search(packageName, predicate);
//        ClassScan jarScanner = new JarScanner();
//        Set<Class<?>> jarSearch = jarScanner.search(packageName,predicate);
//        fileSearch.addAll(jarSearch);
        return fileSearch;
    }

    private ScanExecutor(){}

    public static ScanExecutor getInstance(){
        if(instance == null){
            synchronized (ScanExecutor.class){
                if(instance == null){
                    instance = new ScanExecutor();
                }
            }
        }
        return instance;
    }
}
