package com.chauncy;

import com.chauncy.scan.BigDecimalClassAnalyse;
import com.chauncy.scan.SerializerClassAnalyse;
import com.chauncy.scan.ClassScannerUtils;
import com.chauncy.scan.Observer;
import com.chauncy.scan.ObserverImpl;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * @author : chengxu@corp.netease.com
 * @since : 2020/3/20
 */
@Mojo(name = "demo")
public class DemoMojo extends AbstractMojo {


    public void execute() throws MojoExecutionException, MojoFailureException {
        Observer observer = new ObserverImpl();
        observer.addAnalyser(new BigDecimalClassAnalyse());
        observer.addAnalyser(new SerializerClassAnalyse());

        ClassScannerUtils.searchClasses("com.netease.music", o -> {
            String oo = o.toString();
            return oo.contains("DTO") || oo.contains("VO") || oo
                .contains("Dto") || oo.contains("Vo") || oo.contains("DO");
        }, observer);
    }
}

