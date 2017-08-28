package com.test;

public class 泛型无法向上转型 {
	class Info<T> {
		private T var; // 定义泛型变量

		public void setVar(T var) {
			this.var = var;
		}

		public T getVar() {
			return this.var;
		}

		public String toString() { // 直接打印
			return this.var.toString();
		}
	};

	public static void main(String args[]) {
		Info<String> i1 = new 泛型无法向上转型().new Info<String>(); // 泛型类型为String
		Info<Object> i2 = null;
		// i2 = i1 ; //这句会出错 incompatible types
	}
}
