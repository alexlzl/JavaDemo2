package com.test;

import java.io.File;
import java.io.IOException;
import java.util.*;
public class Demo01 {

    public static void main(String[] args) throws IOException {
        //获取当前项目绝对路径(不是xxx.java文件路径哦)
//        File file = new File(".");
//        System.out.println(file.getAbsolutePath());

        //用相对路径获取上级文件
//        File file = new File("../../test.txt");
//        System.out.println(file.exists());

        //用相对路径获取下级文件
//        File file = new File("./src/test.txt");
//        System.out.println(file.exists());

        //测试createNewFile();
//        File file = new File(System.getProperty("user.home")+"/Desktop/test.txt");
//        System.out.println("创建成功了吗?"+file.createNewFile());

        //测试mkdir(只能创建单级目录)
//        File file = new File(System.getProperty("user.home")+"/Desktop/testFolder");
//        System.out.println("文件夹创建成功了吗?"+file.mkdir());

        //测试mkdir(可以创建多级目录)
//        File file = new File(System.getProperty("user.home")+"/Desktop/testFolder/aa");
//        System.out.println("文件夹创建成功了吗?"+file.mkdirs());

        //renameTo--重命名文件
//        File file1 = new File(System.getProperty("user.home")+"/Desktop/a.txt");
//        File file2 = new File(System.getProperty("user.home")+"/Desktop/b.txt");

        //renameTo--剪切文件
//        File file1 = new File(System.getProperty("user.home")+"/Desktop/a.txt");
//        File file2 = new File(System.getProperty("user.home")+"/b.txt");
//        file1.renameTo(file2);
    }
}
