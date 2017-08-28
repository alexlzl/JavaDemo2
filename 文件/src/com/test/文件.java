package com.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class 文件 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		File file = new File("./Test");
//		System.out.println(file.getPath());
//		System.out.println(file.getAbsolutePath());
//		System.out.println(file.exists());
//		try {
//		    System.out.println(file.getCanonicalPath());
//		} catch (IOException e) {
//		    e.printStackTrace();
//		}
		try {
//		    File dir = new File("dir");    // 获取目录“dir”对应的File对象
//		    File file1 = new File(dir, "file1.txt");
			 File file1 = new File("file1.txt");
//			 file1.mkdir();
		    file1.createNewFile();
		    String s="hello";
		    FileOutputStream fos=new FileOutputStream(file1);
		    fos.write(s.getBytes());
		} catch (IOException e) {
		    e.printStackTrace();
		}
		File sub1 = new File("dir", "sub1");
		sub1.mkdir();
		File dir = new File("dirq");
		dir.mkdir();
		System.out.println(dir.getParent());
		File sub3 = new File("dir/sub3");
		sub3.mkdirs();
		File file = new File("Test.java");
		file.mkdir();	
		try {    
		  FileOutputStream fileOutputStream = new FileOutputStream(file);
		} catch (IOException e) {
		  e.printStackTrace();
		}
		System.out.println(file.canExecute());
//		System.out.println(file.delete());
		System.out.println(File.separator);// 分隔符"/"
		System.out.println(File.pathSeparator);// 路径分割符":"
		System.out.println(File.separatorChar);
	}

}
