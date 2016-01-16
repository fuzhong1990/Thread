package cn.learn.learn.thread._pool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Example1 {
	
	public static void main(String[] args) {
		ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 200, TimeUnit.MICROSECONDS, 
				new ArrayBlockingQueue<Runnable>(5));
		for (int i=0; i<15; ++i) {
			MyTask task = new MyTask(i);
			executor.execute(task);
			System.out.println("线程池中线程数目：" + executor.getPoolSize() + 
					"，队列中等待执行的任务数目：" + executor.getQueue().size() +
					"，已执行玩别的任务数目：" + executor.getCompletedTaskCount());
		}
		executor.shutdown();
	}
	
}

class MyTask implements Runnable {
	private int num;
	
	public MyTask (int num) {
		this.num = num;
	}
	
	@SuppressWarnings("static-access")
	public void run() {
		System.out.println("正在执行task " + num);
		try {
			Thread.currentThread().sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("task " + num + "执行完毕");
	};
}