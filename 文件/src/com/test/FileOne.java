package com.test;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * FileDescriptor 是“文件描述符”。 FileDescriptor 可以被用来表示开放文件、开放套接字等。
 * 以FileDescriptor表示文件来说：当FileDescriptor表示某文件时，我们可以通俗的将FileDescriptor看成是该文件。但是，
 * 我们不能直接通过FileDescriptor对该文件进行操作；若需要通过FileDescriptor对该文件进行操作，
 * 则需要新创建FileDescriptor对应的FileOutputStream，再对文件进行操作。
 * 
 * @author lzl
 *
 */
public class FileOne {
	/**
	 * 从中，可以看出 (01) out就是一个FileDescriptor对象。它是通过构造函数FileDescriptor(int fd)创建的。
	 * (02) FileDescriptor(int fd)的操作：就是给fd对象(int类型)赋值，并新建一个使用计数变量useCount。
	 * fd对象是非常重要的一个变量，“fd=1”就代表了“标准输出”，“fd=0”就代表了“标准输入”，“fd=2”就代表了“标准错误输出”。
	 * 
	 * FileOutputStream out = new FileOutputStream(FileDescriptor.out);
	 * 就是利用构造函数FileOutputStream(FileDescriptor
	 * fdObj)来创建“Filed.out对应的FileOutputStream对象”。
	 * 
	 * 关于System.out是如何定义的。可以参考" 深入了解System.out.println("hello world"); " TODO
	 * 
	 * 通过上面的学习，我们知道，我们可以自定义标准的文件描述符[即，in(标准输入),out(标准输出),err(标准错误输出)]的流，从而完成输入/
	 * 输出功能；但是，java已经为我们封装好了相应的接口，即我们可以更方便的System.in, System.out,
	 * System.err去使用它们。
	 * 另外，我们也可以自定义“文件”、“Socket”等的文件描述符，进而对它们进行操作。参考下面示例代码中的testWrite(),
	 * testRead()等接口。
	 * 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			/**
			 * 执行上面的程序，会在屏幕上输出字母'A'。
			 * 
			 * 为了方便我们操作，java早已为我们封装好了“能方便的在屏幕上输出信息的接口”：通过System.out，
			 * 我们能方便的输出信息到屏幕上。 因此，我们可以等价的将上面的程序转换为如下代码： System.out.print('A');
			 * 
			 * 
			 */
			FileOutputStream out = new FileOutputStream(FileDescriptor.out);
			out.write('A');
			out.close();
		} catch (IOException e) {
		}
	}

}
