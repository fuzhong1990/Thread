package cn.learn.learn.thread;

import java.util.PriorityQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadConditionTest {
	private static int queueSize = 10;
	private PriorityQueue<Integer> queue = new PriorityQueue<>(queueSize);
	private Lock lock = new ReentrantLock();
	private Condition notFull = lock.newCondition();
	private Condition notEmpty = lock.newCondition();
	
	class Consumer extends Thread {
		public Consumer() {
			super("Thread-Consumer");
		}

		@Override
		public void run() {
			while (true) {
				lock.lock();
				try {
					while (queue.size() == 0) {
						try {
							System.out.println("队列空，等待数据");
							notEmpty.await();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					queue.poll(); //每次移走队首元素
					notFull.signal();
					System.out.println("从队列取走一个元素，队列剩余"+queue.size()+"个元素");
				} finally {
					lock.unlock();
				}
			}
		}
	}

	class Producer extends Thread {
		public Producer () {
			super("Thread-Producer");
		}
		
		@Override
		public void run() {
			while(true){
				lock.lock();
				try {
                    while(queue.size() == queueSize){
                        try {
                            System.out.println("队列满，等待有空余空间");
                            notEmpty.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    queue.offer(1);        //每次插入一个元素
                    notEmpty.signal();
                    System.out.println("向队列取中插入一个元素，队列剩余空间："+(queueSize-queue.size()));
                } finally {
                	lock.unlock();
                }
            }
		}
	}
	
	public static void main(String[] args) {
		ThreadConditionTest test = new ThreadConditionTest();
		Consumer consumer = test.new Consumer();
		Producer producer = test.new Producer();
		
		consumer.start();
		producer.start();
	}

}
