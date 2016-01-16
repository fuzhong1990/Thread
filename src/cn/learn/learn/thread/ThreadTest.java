package cn.learn.learn.thread;

import java.util.TimerTask;

public class ThreadTest {
	public static Object obj = new Object();
	
	static class ThreadA extends Thread {
		public ThreadA () {
			super("Thread-A");
		}

		@Override
		public void run() {
			synchronized (obj) {
				try {
					obj.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("线程-"+Thread.currentThread().getName()+"获取到了锁");
			}
		}
	}
	
	static class ThreadB extends Thread {
		public ThreadB () {
			super("Thread-B");
		}
		
		@Override
		public void run() {
			synchronized (obj) {
				obj.notify();
				System.out.println("线程-"+Thread.currentThread().getName()+"调用了:notify()");
			}
			System.out.println("线程-"+Thread.currentThread().getName()+"释放了锁");
		}
	}
	
	public static void main (String [] args) {
		ThreadA threadA = new ThreadA();
		ThreadB threadB = new ThreadB();
		
		threadA.start();
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threadB.start();
	}
	
}
