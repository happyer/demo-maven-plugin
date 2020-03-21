package com.chauncy.scan;

public interface Observer {

    void addAnalyser(ClassAnalyse analyzer);

    void removeAnalyser(ClassAnalyse analyzer);

    void notifyAnalyser(Class<?> clazz);

}
