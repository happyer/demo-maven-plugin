package me.davenkin.scan;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : chengxu@corp.netease.com
 * @since : 2020/3/20
 */
public class ObserverImpl implements Observer {

    private final List<ClassAnalyse> analyzers;

    public ObserverImpl() {
        this.analyzers = new ArrayList<>();
    }

    @Override
    public void addAnalyser(ClassAnalyse analyzer) {
        this.analyzers.add(analyzer);
    }

    @Override
    public void removeAnalyser(ClassAnalyse analyzer) {
        this.analyzers.remove(analyzer);
    }

    @Override
    public void notifyAnalyser(Class<?> clazz) {
        analyzers.forEach(analyzer -> analyzer.analyse(clazz));
    }
}
