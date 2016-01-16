package cn.learn.learn.thread._queue;

import java.util.concurrent.ArrayBlockingQueue;

public class ArrayBlockingQueueTest {
	
	private int queuesize = 10;
	private ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(queuesize);
	
	public static void main(String[] args) {
		ArrayBlockingQueueTest test = new ArrayBlockingQueueTest();
		Producer producer = test.new Producer();
		Consumer consumer = test.new Consumer();
		
		producer.start();
		consumer.start();
	}
	
	class Consumer extends Thread {
		public void run() {
			while (true) {
				try {
					queue.take();
					System.out.println("从队列取走一个元素，队列剩余"+queue.size()+"个元素");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	class Producer extends Thread {
		public void run() {
			while(true) {
				try {
					queue.put(1);
					System.out.println("向队列取中插入一个元素，队列剩余空间："+(queuesize-queue.size()));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		}
	}
	
}
