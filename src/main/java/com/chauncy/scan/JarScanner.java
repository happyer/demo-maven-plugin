package com.chauncy.scan;

import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


public class JarScanner implements ClassScan {

    public Set<Class<?>> search(String packageName, MyPredicate<String> predicate) {

        Set<Class<?>> classes = new HashSet<Class<?>>();

        try {
            Enumeration<URL> urlEnumeration = Thread.currentThread().getContextClassLoader()
                .getResources(packageName.replace(".", "/"));
            while (urlEnumeration.hasMoreElements()) {
                URL url = urlEnumeration
                    .nextElement();
                String protocol = url.getProtocol();//大概是jar
                if ("jar".equalsIgnoreCase(protocol)) {
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
                                        Class<?> cls = getClass().getClassLoader().loadClass(className);
                                        classes.add(cls);
                                    }
                                }
                            }
                        }
                    }
                } else if ("file".equalsIgnoreCase(protocol)) {
                    //从maven子项目中扫描
                    FileScanner fileScanner = new FileScanner();
                    fileScanner.setDefaultClassPath(
                        url.getPath().replace(packageName.replace(".", "/"), ""));
                    classes.addAll(fileScanner.search(packageName, predicate));
                }
            }
        } catch (Exception  e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return classes;
    }
}
