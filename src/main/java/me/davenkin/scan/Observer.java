package me.davenkin.scan;

/**
 * @author : chengxu@corp.netease.com
 * @since : 2020/3/20
 */
public interface Observer {

    void addAnalyser(ClassAnalyse analyzer);

    void removeAnalyser(ClassAnalyse analyzer);

    void notifyAnalyser(Class<?> clazz);

}
