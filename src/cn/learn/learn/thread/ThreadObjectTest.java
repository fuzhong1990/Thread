package cn.learn.learn.thread;

import java.util.PriorityQueue;

public class ThreadObjectTest {
	
	private static int queueSize = 10;
	private PriorityQueue<Integer> queue = new PriorityQueue<>(queueSize);
	
	class Consumer extends Thread {
		public Consumer() {
			super("Thread-Consumer");
		}

		@Override
		public void run() {
			while (true) {
				synchronized (queue) {
					while (queue.size() == 0) {
						try {
							System.out.println("队列空，等待数据");
							queue.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
							queue.notify();
						}
					}
					queue.poll(); //每次移走队首元素
					queue.notify();
					System.out.println("从队列取走一个元素，队列剩余"+queue.size()+"个元素");
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
                synchronized (queue) {
                    while(queue.size() == queueSize){
                        try {
                            System.out.println("队列满，等待有空余空间");
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            queue.notify();
                        }
                    }
                    queue.offer(1);        //每次插入一个元素
                    queue.notify();
                    System.out.println("向队列取中插入一个元素，队列剩余空间："+(queueSize-queue.size()));
                }
            }
		}
	}
	
	public static void main(String[] args) {
		ThreadObjectTest test = new ThreadObjectTest();
		Consumer consumer = test.new Consumer();
		Producer producer = test.new Producer();
		
		consumer.start();
		producer.start();
	}

}
