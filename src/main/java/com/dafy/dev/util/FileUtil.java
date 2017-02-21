package com.dafy.dev.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by chunxiaoli on 10/12/16.
 */
public class FileUtil {
    public static boolean fileExists(String file) {
        return new File(file).exists();
    }

    public static boolean createDir(String path) {
        return new File(path).mkdirs();
    }

    public static boolean createFile(String path) {
        try {
            return new File(path).createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void write(String content, String file) {
        try {
            new FileOutputStream(new File(file)).write(content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void write(String content, OutputStream outputStream) {
        try {
            outputStream.write(content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void copy(String source, String target) {

        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(source);
            outputStream = new FileOutputStream(target);
            try {
                int c;
                while ((c = inputStream.read()) != -1) {
                    outputStream.write(c);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void save(InputStream inputStream, String target) {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(target);
            try {
                int c;
                while ((c = inputStream.read()) != -1) {
                    outputStream.write(c);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean checkPermission(String file){
        Path path=new File(file).toPath();
        return Files.isReadable(path)&&Files.isWritable(path);
    }

    public static String readFromFile(String file){
        try {
            return new String(Files.readAllBytes(Paths.get(file)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean delete(String path){
        File f=new File(path);
        boolean ret=false;
        if(f.isDirectory()){
            File []files=f.listFiles();
            if(files!=null){
                int c=files.length;
                for (File t:files) {
                    if(t.isDirectory()){
                        boolean r= delete(t.getPath());
                        if(r){
                            c--;
                        }
                    }else {
                        boolean r= t.delete();
                        if(r){
                            c--;
                        }
                    }
                }
                ret=f.delete();
                ret=ret&&(c==0);
            }else {
                ret=f.delete();
            }
        }else{
            ret=f.delete();
        }
        return ret;

    }

    public static boolean isJavaFile(File f){
        return f!=null&&f.isFile()&&f.getName().endsWith(".java");
    }

    public static File[] getAllJavaFiles(String dir){

        if(StringUtil.isEmpty(dir)){
            return null;
        }
        File path= new File(dir);
        if(path.isFile()){
            return null;
        }
        return path.listFiles(pathname -> pathname.isFile()&&pathname.getName().endsWith(".java"));


    }

    public static List<File> getAllJavaFiles(String dir, boolean recursive){
        List<File> ret=new ArrayList<>();
        if(recursive){
            File[]files=new File(dir).listFiles();
            if(files!=null&&files.length>0){
                for(File f:files){
                    if(f.isDirectory()){
                        ret.addAll(getAllJavaFiles(f.getPath(),true));
                    }else if(FileUtil.isJavaFile(f)) {
                        ret.add(f);
                    }
                }
            }
        }else {
             File[]arr=getAllJavaFiles(dir);
             if(arr!=null){
                 ret=Arrays.stream(arr).collect(Collectors.toList());
             }
        }
        return ret;
    }
}
