package com.ease.cred;
/**
 * 这5种包装类默认创建了数值[-128，127]的相应类型的缓存数据，但是超出此范围仍然会去创建新的对象
 * @author lzl
 *
 *在java中将int转到Integer时调用的是Integer.valueof(int i)方法进行转换,而该方法里使用了缓存，缓存里存储了value为0-127的Integer对象。当参数值在该范围时，返回缓存对象
 */
public class Test2 {
	private static boolean  b=true;
	private static Boolean b1=new Boolean(true);

	public static void main(String[] args) {
		// TODO Auto-generated method stub
          System.out.println(b==b1);//true
          Integer i1 = 127;
          Integer i2 = 127;
          Integer i3 = 128;
          Integer i4 = 128;
          System.out.println(i1 == i2);//true
          System.out.println(i3 == i4);//false
          System.out.println(i3 == 128);//true
	}

}
