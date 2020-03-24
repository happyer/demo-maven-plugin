package com.chauncy.scan;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Dependency;
import org.apache.maven.project.MavenProject;


public class JarScanner implements ClassScan {


    private MavenProject mavenProject;


    public JarScanner() {
    }

    public JarScanner(MavenProject mavenProject) {
        this.mavenProject = mavenProject;
    }

    public Set<Class<?>> search(String packageName, MyPredicate<String> predicate) {

        Set<Class<?>> classes = new HashSet<Class<?>>();

        try {
            if (mavenProject == null) {
                CurrentClassLoaderJarSearch currentClassLoaderJarSearch = new CurrentClassLoaderJarSearch();
                classes.addAll(currentClassLoaderJarSearch.doPath(packageName, predicate));
            } else {

                String baseDir = mavenProject.getProjectBuildingRequest().getLocalRepository()
                    .getBasedir();

                List<String> paths = new ArrayList<String>();

                Map<String, Artifact> artifactMap = mavenProject.getManagedVersionMap();
                for (Entry<String, Artifact> stringArtifactEntry : artifactMap.entrySet()) {
                    Artifact artifact = stringArtifactEntry.getValue();
                    StringBuilder sb = new StringBuilder(baseDir).append(File.separator);
                    String groupId = artifact.getGroupId();
                    String artId = artifact.getArtifactId();
                    String version = artifact.getVersion();
                    sb.append(groupId.replace(".", File.separator))
                        .append(File.separator)
                        .append(artId)
                        .append(File.separator)
                        .append(version)
                        .append(File.separator)
                        .append(artId + "-" + version)
                        .append(".jar");
                    paths.add(sb.toString());

                }

                List<String> runtimeClp = mavenProject.getRuntimeClasspathElements();
                paths.addAll(runtimeClp);

                URL[] runtimeUrls = new URL[paths.size()];

                for (int i = 0; i < paths.size(); i++) {
                    runtimeUrls[i] = new File(paths.get(i)).toURI().toURL();
                }

                URLClassLoader urlClassLoader = new URLClassLoader(runtimeUrls);
                URLClassLoaderJarSearch URLClassLoaderJarSearch = new URLClassLoaderJarSearch(
                    urlClassLoader);

                classes.addAll(
                    URLClassLoaderJarSearch
                        .doPath(mavenProject.getBasedir(), packageName, predicate));
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return classes;
    }


    private static class URLClassLoaderJarSearch {

        Set<Class<?>> classes = new HashSet<Class<?>>();

        private URLClassLoader urlClassLoader;


        public URLClassLoaderJarSearch(URLClassLoader urlClassLoader) {
            this.urlClassLoader = urlClassLoader;
        }

        private Set<Class<?>> doPath(File file, String packageName, MyPredicate<String> predicate) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File file1 : files) {
                    doPath(file1, packageName, predicate);
                }
            } else {
                //标准文件
                if (file.getName().endsWith(JAR_SUFFIX)) {
                    try {

                        JarFile jarFile = null;
                        try {
                            jarFile = new JarFile(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (jarFile != null) {

                            Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();
                            while (jarEntryEnumeration.hasMoreElements()) {

                                JarEntry entry = jarEntryEnumeration.nextElement();
                                String jarEntryName = entry.getName();
                                if (jarEntryName.contains(".class") && jarEntryName
                                    .replaceAll("/", ".").startsWith(packageName)) {
                                    String className = jarEntryName
                                        .substring(0, jarEntryName.lastIndexOf("."))
                                        .replace("/", ".");
                                    if (predicate == null || predicate.test(className)) {

                                        Class<?> cls = urlClassLoader.loadClass(className);
                                        classes.add(cls);
                                    }
                                }
                            }
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }

            }
            return classes;
        }
    }


    private static class CurrentClassLoaderJarSearch {

        Set<Class<?>> classes = new HashSet<Class<?>>();


        public Set<Class<?>> doPath(String packageName, MyPredicate<String> predicate) {

            Enumeration<URL> urlEnumeration = null;
            try {
                urlEnumeration = Thread.currentThread().getContextClassLoader()
                    .getResources(packageName.replace(".", "/"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (urlEnumeration.hasMoreElements()) {
                URL url = urlEnumeration.nextElement();

                String protocol = url.getProtocol();//大概是jar
                if ("jar".equalsIgnoreCase(protocol)) {
                    try {
                        parseJar(packageName, predicate, classes, url);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else if ("file".equalsIgnoreCase(protocol)) {
                    //从maven子项目中扫描
                    FileScanner fileScanner = new FileScanner();
                    fileScanner.setDefaultClassPath(
                        url.getPath().replace(packageName.replace(".", "/"), ""));
                    classes.addAll(fileScanner.search(packageName, predicate));
                }
            }
            return classes;
        }


        private void parseJar(String packageName, MyPredicate<String> predicate,
            Set<Class<?>> classes,
            URL url) throws IOException, ClassNotFoundException {
            JarURLConnection connection = (JarURLConnection) url.openConnection();
            if (connection != null) {
                JarFile jarFile = connection.getJarFile();
                if (jarFile != null) {

                    Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();
                    while (jarEntryEnumeration.hasMoreElements()) {

                        JarEntry entry = jarEntryEnumeration.nextElement();
                        String jarEntryName = entry.getName();
                        if (jarEntryName.contains(".class") && jarEntryName
                            .replaceAll("/", ".").startsWith(packageName)) {
                            String className = jarEntryName
                                .substring(0, jarEntryName.lastIndexOf("."))
                                .replace("/", ".");
                            if (predicate == null || predicate.test(className)) {
                                Class<?> cls = getClass().getClassLoader()
                                    .loadClass(className);
                                classes.add(cls);
                            }
                        }
                    }
                }
            }
        }
    }


}