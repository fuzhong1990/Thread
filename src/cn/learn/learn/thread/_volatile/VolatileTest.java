package cn.learn.learn.thread._volatile;

// 不推荐
public class VolatileTest {
	
	private volatile static VolatileTest test = null;
	
	private VolatileTest() {}
	
	@SuppressWarnings("unused")
	private static VolatileTest getInstance() {
		if (test == null) {
			synchronized (VolatileTest.class) {
				if (test == null) {
					test = new VolatileTest();
				}
			}
		}
		return test;
	}
	
}

/*
等效示例：
	推荐使用下面这张方法：

//  推荐[静态内部类]
public class Singleton {
    static class SingletonHolder {
        static Singleton instance = new Singleton();
    }
    
    public static Singleton getInstance(){
        return SingletonHolder.instance;
    }
}

// 极推荐[枚举实现单例模式]
public enum Singleton {  
  INSTANCE;  
  public static Singleton getInstance() {  
    return INSTANCE;  
  }  
}

*/
