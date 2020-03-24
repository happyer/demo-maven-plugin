package com.chauncy;

import com.chauncy.scan.BigDecimalClassAnalyse;
import com.chauncy.scan.ClassScannerUtils;
import com.chauncy.scan.MyPredicate;
import com.chauncy.scan.Observer;
import com.chauncy.scan.ObserverImpl;
import com.chauncy.scan.SerializerClassAnalyse;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo(name = "scan")
public class DemoMojo extends AbstractMojo {


    @Parameter(property = "project", readonly = true)
    private MavenProject project;


    public void execute() {

        Observer observer = new ObserverImpl();
        observer.addAnalyser(new BigDecimalClassAnalyse());
        observer.addAnalyser(new SerializerClassAnalyse());

        ClassScannerUtils.searchClasses("com.netease.music", new MyPredicate<String>() {
            public boolean test(String oo) {
                return oo.contains("DTO") || oo.contains("VO") || oo
                    .contains("Dto");
            }
        }, observer,project);


    }

}