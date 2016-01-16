package cn.learn.learn.thread.threadlocal;

public class ThreadLocalTest {
	ThreadLocal<Long> longLocal = new ThreadLocal<Long>() {
		protected Long initialValue() {
			return Thread.currentThread().getId();
		};
	};
	ThreadLocal<String> stringLocal = new ThreadLocal<String>(){
		protected String initialValue() {
			return Thread.currentThread().getName();
		};
	};
	
	public void set() {
		longLocal.set(Thread.currentThread().getId());
		stringLocal.set(Thread.currentThread().getName());
	}

	public Long getLong() {
		return longLocal.get();
	}
	
	public String getString() {
		return stringLocal.get();
	}
	
	public static void main(String[] args) throws InterruptedException {
		final ThreadLocalTest test = new ThreadLocalTest();
//		test.set();
		System.out.println(test.getLong());
		System.out.println(test.getString());
		
		Thread thread = new Thread() {
			@Override
			public void run() {
//				test.set();
				System.out.println(test.getLong());
				System.out.println(test.getString());
			}
		};
		thread.start();
		thread.join();
		
		System.out.println(test.getLong());
		System.out.println(test.getString());
	}
	
	
	
}
