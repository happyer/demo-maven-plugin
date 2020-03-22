package com.chauncy;

import com.chauncy.scan.BigDecimalClassAnalyse;
import com.chauncy.scan.ClassScannerUtils;
import com.chauncy.scan.MyPredicate;
import com.chauncy.scan.Observer;
import com.chauncy.scan.ObserverImpl;
import com.chauncy.scan.SerializerClassAnalyse;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "hello")
public class DemoMojo extends AbstractMojo {


    public void execute() throws MojoExecutionException, MojoFailureException {

        Observer observer = new ObserverImpl();
        observer.addAnalyser(new BigDecimalClassAnalyse());
        observer.addAnalyser(new SerializerClassAnalyse());

        String baseDir = "/Users/chauncy/Desktop/self/Study/MyNetty4/target/classes/";
        ClassScannerUtils.searchClasses("com.netease.music", new MyPredicate<String>() {
            public boolean test(String oo) {
                return oo.contains("DTO") || oo.contains("VO") || oo
                    .contains("Dto");
            }
        }, observer, baseDir);
    }

}