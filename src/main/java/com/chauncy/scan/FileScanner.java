package com.chauncy.scan;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.Set;
import org.apache.maven.project.MavenProject;

public class FileScanner implements ClassScan {


    private String defaultClassPath = null;

    public String getDefaultClassPath() {
        return defaultClassPath;
    }

    public void setDefaultClassPath(String defaultClassPath) {
        this.defaultClassPath = defaultClassPath;
    }

    public FileScanner(String defaultClassPath) {
        this.defaultClassPath = defaultClassPath;
    }


    public FileScanner(MavenProject mavenProject) {
        this.defaultClassPath = mavenProject.getBasedir().getPath();
    }

    public FileScanner() {
        defaultClassPath = FileScanner.class.getResource("/").getPath();
    }


    private static class ClassSearcher {

        private Set<Class<?>> classPaths = new HashSet<Class<?>>();

        private String baseDir;

        public ClassSearcher(String baseDir) {
            this.baseDir = baseDir;
        }

        private Set<Class<?>> doPath(File file, String packageName, MyPredicate<String> predicate,
            boolean flag) {

            if (file.isDirectory()) {
                //文件夹我们就递归
                File[] files = file.listFiles();
                if (!flag) {
                    packageName = packageName + "." + file.getName();
                }

                for (File f1 : files) {
                    doPath(f1, packageName, predicate, false);
                }
            } else {//标准文件
                //标准文件我们就判断是否是class文件
                if (file.getName().endsWith(CLASS_SUFFIX)) {
                    //如果是class文件我们就放入我们的集合中。
                    try {

                        String name = packageName + "." + file.getName()
                            .substring(0, file.getName().lastIndexOf("."));
                        if (predicate == null || predicate.test(name)) {
                            File file1 = new File(baseDir);
                            URLClassLoader urlClassLoader = new URLClassLoader(
                                new URL[]{file1.toURI().toURL()});
                            Class<?> clazz = Class.forName(name, true, urlClassLoader);
                            classPaths.add(clazz);
                        }

                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e.getMessage(), e);
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e.getMessage(), e);
                    }
                }
            }
            return classPaths;
        }
    }

    public Set<Class<?>> search(String packageName, MyPredicate<String> predicate) {
        //先把包名转换为路径,首先得到项目的classpath
        String classpath = defaultClassPath;
        //然后把我们的包名basPack转换为路径名
        String basePackPath = packageName.replace(".", File.separator);
        String searchPath = classpath + basePackPath;
        return new ClassSearcher(defaultClassPath)
            .doPath(new File(searchPath), packageName, predicate, true);
    }

}
