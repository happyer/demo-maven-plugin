package com.chauncy.scan;

import java.util.ArrayList;
import java.util.List;

public class ObserverImpl implements Observer {

    private final List<ClassAnalyse> analyzers;

    public ObserverImpl() {
        this.analyzers = new ArrayList<ClassAnalyse>();
    }

    public void addAnalyser(ClassAnalyse analyzer) {
        this.analyzers.add(analyzer);
    }

    public void removeAnalyser(ClassAnalyse analyzer) {
        this.analyzers.remove(analyzer);
    }

    public void notifyAnalyser(Class<?> clazz) {

        for (ClassAnalyse analyzer : analyzers) {
            analyzer.analyse(clazz);
        }
    }
}
