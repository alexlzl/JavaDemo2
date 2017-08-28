package com.test;

import java.io.*;
public class Demo06 {
    //1。 定义一个函数列出指定目录中所有的java文件。
    public static void listJava(String path){
        File file = new File(path);
        File files[] = file.listFiles();
        for(File tmpFile :files){
            String fileName = tmpFile.getName();
            if(/*fileName.endsWith(".java")*/fileName.matches(".+\\.java")){
                System.out.println(fileName);
            }
        }
    }

    /*2，列出指定目录中所有的子文件名与所有的子目录名，要求目录名与文件名分开列出，格式如下：
    子目录：
        jdk
        视频
        代码
    子文件：
        ...
        ...
    */
    public static void listFiles(String path){
        File file = new File(path);
        File files[] = file.listFiles();
        System.out.println("子目录:");
        for(File tmpFile :files){
            if(tmpFile.isDirectory())
                System.out.println("\t"+tmpFile.getName());
        }
        System.out.println("子文件:");
        for(File tmpFile :files){
            if(tmpFile.isFile())
                System.out.println("\t"+tmpFile.getName());
        }
    }

    public static void main (String[] args) {
    	 listJava("./");
         listFiles("./");
//        listJava("C:/Users/Administrator/Desktop/java/Test/src/com/wxhl/jq0730");
//        listFiles("D:\\视频\\2015传智播客基础班33期\\day19\\代码\\day19");
    }
}