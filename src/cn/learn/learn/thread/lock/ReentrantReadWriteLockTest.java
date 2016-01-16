package cn.learn.learn.thread.lock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantReadWriteLockTest {
	
	ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	
	public synchronized void getSynchronized(Thread thread) {
        long start = System.currentTimeMillis();
        while(System.currentTimeMillis() - start <= 1) {
            System.out.println(thread.getName()+"正在进行读操作");
        }
        System.out.println(thread.getName()+"读操作完毕");
    }
	
	public void getReentrantReadWriteLock (Thread thread) {
        lock.readLock().lock();
        try {
        	long start = System.currentTimeMillis();
        	while(System.currentTimeMillis() - start <= 1) {
        		System.out.println(thread.getName()+"正在进行读操作");
        	}
        	System.out.println(thread.getName()+"读操作完毕");
        } finally {
        	lock.readLock().unlock();
        }
    }
	
	public static void main(String[] args) {
		final ReentrantReadWriteLockTest test = new ReentrantReadWriteLockTest();
		new Thread(){
            public void run() {
//                test.getSynchronized(Thread.currentThread());
                test.getReentrantReadWriteLock(Thread.currentThread());
            };
        }.start();
         
        new Thread(){
            public void run() {
//                test.getSynchronized(Thread.currentThread());
                test.getReentrantReadWriteLock(Thread.currentThread());
            };
        }.start();
	}
	
}
