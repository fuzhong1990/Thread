package cn.learn.learn.thread.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadLockTest {
	
	public static void main(String[] args) {
		
		final ThreadLockTest.Outputter output = new ThreadLockTest().new Outputter();
		
		new Thread() {
			public void run() {
				output.output("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
			};
		}.start();
		
		new Thread() {
			public void run() {
				output.output("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
			};
		}.start();
	}
	
	public class Outputter {
		
		private Lock lock = new ReentrantLock();// 锁对象
		
		public void output(String name) {
			lock.lock();// 得到锁
			try {
				for(int i = 0; i < name.length(); i++) {
					System.out.print(name.charAt(i));
				}
			} finally {
				lock.unlock();// 释放锁
			}
		}
	}
	
}
