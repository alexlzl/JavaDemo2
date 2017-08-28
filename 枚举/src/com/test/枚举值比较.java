package com.test;

public class 枚举值比较 {
	enum Color {
		BLACK, WHITE;
		public String get() {
			return "ss";
		}
	};

	enum Chiral {
		LEFT, RIGHT
	};

	public String get() {
		return "ss";
	}

	public static void main(String[] str) {
		if (Color.BLACK.equals(Chiral.LEFT))
			System.out.println("true");
		; // compiles fine
			// if (Color.BLACK == Chiral.LEFT); //编译不过
		if (Color.BLACK == Color.WHITE)
			System.out.println("===");
		try {
			枚举值比较 ss = 枚举值比较.class.newInstance();
			System.out.println(ss.get());
			//枚举类型的反射实例化也是被禁止的  下面代码报异常
			Color c = Color.class.newInstance();
			System.out.println(c.get());
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
