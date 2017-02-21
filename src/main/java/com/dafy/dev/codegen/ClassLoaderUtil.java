package com.dafy.dev.codegen;

import com.dafy.dev.generator.ProjectGenerator;
import com.dafy.dev.util.FileUtil;
import com.dafy.dev.util.SourceCodeUtil;
import com.dafy.dev.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chunxiaoli on 12/30/16.
 */
public class ClassLoaderUtil {

    private static final Logger logger = LoggerFactory.getLogger(ProjectGenerator.class);

    public final static String tmp = "/var/tmp/project-generator";

    private final static String classReg   = "(class|interface)\\s+[a-zA-Z0-9]+\\s+";
    private final static String packageReg = "package\\s+[a-zA-Z0-9.]+;";

    private final static Pattern pacakgePattern = Pattern.compile(packageReg);

    private final static Pattern classPattern = Pattern.compile(classReg);

    public static String resolvePackage(String sourceCode) {
        Matcher m = pacakgePattern.matcher(sourceCode);
        if (m.find()) {
            String s = m.group(0);
            return s.split("\\s+")[1].split(";")[0];
        }
        return null;
    }



    public static String resolveFullName(String sourceCode) {
        return resolvePackage(sourceCode) + "." + resolveClassName(sourceCode);
    }

    public static String resolveClassName(String sourceCode) {
        Matcher m = classPattern.matcher(sourceCode);
        if (m.find()) {
            String s = m.group(0);
            return s.split("\\s+")[1];
        }
        return null;
    }

    public static void main(String args[]) {
        String source = "package test; public class Test { static { System.out.println(\"hello\"); } public Test() { System.out.println(\"world\"); } }";
        Class cls = loadFromSource(source);
        String packageName = resolvePackage(source);
        String className = resolveClassName(source);
        System.out.println(packageName);
        System.out.println(className);
        System.out.println(cls);

    }

    public static Class loadFromFile(String file) {
        Class ret=null;
        try{
            ret=loadFromSource(FileUtil.readFromFile(file));
            if(ret==null){
                logger.error("Class not found try to compiler and reload");
                loadAllClass(file.replace(".java",""));
                ret=loadFromSource(FileUtil.readFromFile(file));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return ret;
    }

    public static Class loadFromSource(String sourceCode) {

        // Save source in .java file.
        File root = new File(tmp);
        String packageName = resolvePackage(sourceCode);
        String className = resolveClassName(sourceCode);

        if (StringUtil.isEmpty(packageName) || StringUtil.isEmpty(className)) {
            return null;
        }

        String tmpClass =
                SourceCodeUtil.convertPackage2Dir(packageName) + File.separator + className;
        File sourceFile = new File(root, tmpClass + ".java");
        sourceFile.getParentFile().mkdirs();
        try {
            Files.write(sourceFile.toPath(), sourceCode.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Compile source file.
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        compiler.run(null, null, null, sourceFile.getPath());

        // Load and instantiate compiled class.
        URLClassLoader classLoader = null;
        try {
            classLoader = URLClassLoader.newInstance(new URL[] { root.toURI().toURL() });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Class<?> cls = null;
        try {
            cls = Class.forName(packageName + "." + className, true, classLoader);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        boolean ret = FileUtil.delete(root.getPath());

        System.out.println("delete " + root + " : " + ret);

        return cls;
    }

    public static String compilerFiles(String inputDir, String outDir) {
        if (!StringUtil.isEmpty(inputDir)) {
            List<File> files = FileUtil.getAllJavaFiles(inputDir,true);

            if (files != null && files.size() > 0) {
                if(!new File(outDir).exists()){
                    FileUtil.createDir(outDir);
                }


                JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
                DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();

                List<JavaFileObject> compilationUnits = new ArrayList<>();
                List<String> clsList = new ArrayList<>();

                for (File f : files) {
                    String source = FileUtil.readFromFile(f.getPath());
                    JavaFileObject file = new JavaSourceFromFile(f.toURI().toString(), source);
                    compilationUnits.add(file);
                    clsList.add(resolveFullName(source));
                }

                if(compilationUnits.size()>0){
                    // Compiler options
                    List<String>options=new ArrayList<>();
                    options.add("-verbose");
                    options.add("-parameters");

                    StandardJavaFileManager sjfm = compiler
                            .getStandardFileManager(diagnostics, null, null);
                    try {
                        sjfm.setLocation(StandardLocation.CLASS_OUTPUT,
                                Collections.singleton(new File(outDir)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    JavaCompiler.CompilationTask task = compiler.getTask(null, sjfm, diagnostics,
                            options, null, compilationUnits);

                    boolean success=false;
                    try {
                        success = task.call();
                    } catch (Throwable e) {
                        e.printStackTrace();
                        System.out.println(
                                "load class from " + inputDir + " cls" + compilationUnits + " error " + e);
                    }

                    for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
                        System.out.println(diagnostic.getCode());
                        System.out.println(diagnostic.getKind());
                        System.out.println(diagnostic.getPosition());
                        System.out.println(diagnostic.getStartPosition());
                        System.out.println(diagnostic.getEndPosition());
                        System.out.println(diagnostic.getSource());
                        System.out.println(diagnostic.getMessage(null));

                    }
                    System.out.println("Success: " + success);

                    if (success) {
                        return outDir;
                    }
                }
            }
        }
        return null;
    }

    public static Class loadClass(String file, ClassLoader classLoader) {
        String source = FileUtil.readFromFile(file);
        if (!StringUtil.isEmpty(source)) {
            try {
                return Class.forName(resolveFullName(source), true, classLoader);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static Class load(String classFullName, ClassLoader classLoader) {
        if (!StringUtil.isEmpty(classFullName)) {
            try {
                return Class.forName(classFullName, true, classLoader);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static ClassLoader loadAllClass(String classDir){
        ClassLoader classLoader=null;
        ClassLoaderUtil.compilerFiles(classDir, ClassLoaderUtil.tmp);
        try {
            classLoader = URLClassLoader.newInstance(new URL[] { new File(ClassLoaderUtil.tmp).toURI().toURL() });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return classLoader;
    }
}

class CompileSourceInMemory {
    public static void main(String args[]) throws IOException {

    }
}

