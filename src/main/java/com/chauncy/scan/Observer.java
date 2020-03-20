package com.chauncy.scan;

/**
 * @author : cxujdk@gmail.com
 * @since : 2020/3/20
 */
public interface Observer {

    void addAnalyser(ClassAnalyse analyzer);

    void removeAnalyser(ClassAnalyse analyzer);

    void notifyAnalyser(Class<?> clazz);

}
