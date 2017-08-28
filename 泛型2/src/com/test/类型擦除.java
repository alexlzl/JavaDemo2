package com.test;

/**
 * 上面的 Manipulator 是一个泛型类，内部用一个泛型化的变量 obj，在 manipulate 方法中，调用了 obj 的方法
 * f()，但是这行代码无法编译。因为类型擦除，编译器不确定 obj 是否有 f() 方法。解决这个问题的方法是给 T 一个边界:
 * 
 * 在 T 的类型是 <T extends HasF>，这表示 T 必须是 HasF 或者 HasF 的导出类型。这样，调用 f() 方法才安全。HasF
 * 就是 T 的边界，因此通过类型擦除后，所有出现 T 的 地方都用 HasF 替换。这样编译器就知道 obj 是有方法 f() 的。
 * 
 * 类型擦除导致泛型丧失了一些功能，任何在运行期需要知道确切类型的代码都无法工作。比如下面的例子：
 * 
 * 
 * 在上面，两次提到了原始类型，什么是原始类型？原始类型（raw
 * type）就是擦除去了泛型信息，最后在字节码中的类型变量的真正类型。无论何时定义一个泛型类型，相应的原始类型都会被自动地提供。类型变量被擦除（crased
 * ），并使用其限定类型（无限定的变量用Object）替换。
 * 
 * @author lzl
 *
 * @param <T>
 */
public class 类型擦除<T extends HasF> {

	private T obj;

	public 类型擦除(T obj) {
		this.obj = obj;
	}

	public void manipulate() {
		obj.f(); // 无法编译 找不到符号 f()
	}

	public static void main(String[] args) {
		HasF hasF = new HasF();
		类型擦除<HasF> manipulator = new 类型擦除<>(hasF);
		manipulator.manipulate();

	}

	private final int SIZE = 100;

	/**
	 * 类型擦除导致泛型丧失了一些功能，任何在运行期需要知道确切类型的代码都无法工作。比如下面的例子：
	 * 
	 * 通过 new T() 创建对象是不行的，一是由于类型擦除，二是由于编译器不知道 T
	 * 是否有默认的构造器。一种解决的办法是传递一个工厂对象并且通过它创建新的实例
	 * 
	 * @param arg
	 */
	public static void f(Object arg) {
		// if (arg instanceof T) {
		// } // Error
		// T var = new T(); // Error
		// T[] array = new T[SIZE]; // Error
		// T[] array = (T) new Object[SIZE]; // Unchecked warning
	}

	/**
	 * 因为在Pair<T>中，T是一个无限定的类型变量，所以用Object替换。其结果就是一个普通的类，如同泛型加入java变成语言之前已经实现的那样。
	 * 在程序中可以包含不同类型的Pair，如Pair<String>或Pair
	 * <Integer>，但是，擦除类型后它们就成为原始的Pair类型了，原始类型都是Object。
	 * 从上面的那个例2中，我们也可以明白ArrayList
	 * <Integer>被擦除类型后，原始类型也变成了Object，所以通过反射我们就可以存储字符串了。
	 * 
	 * 如果类型变量有限定，那么原始类型就用第一个边界的类型变量来替换
	 * 
	 * @author lzl
	 *
	 * @param <T>
	 */
	class Pair<T> {
		private T value;

		public T getValue() {
			return value;
		}

		public void setValue(T value) {
			this.value = value;
		}
	}
	/**
	 * 如果类型变量有限定，那么原始类型就用第一个边界的类型变量来替换。
比如Pair这样声明  那么原始类型就是Comparable

	 * @author lzl
	 *
	 * @param <T>
	 */
	public class Pair1<T extends Comparable>{
		
	}
}
