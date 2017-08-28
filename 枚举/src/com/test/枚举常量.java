package com.test;

/**
 * java.util.EnumSet和java.util.EnumMap是两个枚举集合。EnumSet保证集合中的元素不重复;EnumMap中的
 * key是enum类型，而value则可以是任意类型。关于这个两个集合的使用就不在这里赘述 四、 通常定义常量方法和枚举定义常量方法区别
 * 
 * 以下内容可能有些无聊，但绝对值得一窥
 * 
 * 1. 代码：
 * 
 * 复制代码 public class State {
 * 
 * public static final int ON = 1;
 * 
 * public static final Int OFF= 0;
 * 
 * } 复制代码 有什么不好了，大家都这样用了很长时间了，没什么问题啊。
 * 
 * 首先，它不是类型安全的。你必须确保是int
 * 
 * 其次，你还要确保它的范围是0和1
 * 
 * 最后，很多时候你打印出来的时候，你只看到 1 和0 ，
 * 
 * 但其没有看到代码的人并不知道你的企图，抛弃你所有旧的public static final常量
 * 
 * 2. 可以创建一个enum类，把它看做一个普通的类。除了它不能继承其他类了。(java是单继承，它已经继承了Enum),
 * 
 * 可以添加其他方法，覆盖它本身的方法
 * 
 * 3. switch()参数可以使用enum了
 * 
 * 4. values()方法是编译器插入到enum定义中的static方法，所以，当你将enum实例向上转型为父类Enum是，values()就不可访问了。
 * 解决办法：在Class中有一个getEnumConstants()方法，所以即便Enum接口中没有values()方法，
 * 我们仍然可以通过Class对象取得所有的enum实例
 * 
 * 5. 无法从enum继承子类，如果需要扩展enum中的元素，在一个接口的内部，创建实现该接口的枚举，以此将元素进行分组。达到将枚举元素进行分组。
 * 
 * 6. 使用EnumSet代替标志。enum要求其成员都是唯一的，但是enum中不能删除添加元素。
 * 
 * 7. EnumMap的key是enum，value是任何其他Object对象。
 * 
 * 8. enum允许程序员为eunm实例编写方法。所以可以为每个enum实例赋予各自不同的行为。
 * 
 * 9. 使用enum的职责链(Chain of Responsibility)
 * .这个关系到设计模式的职责链模式。以多种不同的方法来解决一个问题。然后将他们链接在一起。当一个请求到来时，遍历这个链，
 * 直到链中的某个解决方案能够处理该请求。
 * 
 * 10. 使用enum的状态机
 * 
 * 11. 使用enum多路分发
 * 
 * @author lzl
 *
 */

public enum 枚举常量 {
	RED, GREEN, BLANK, YELLOW
}
