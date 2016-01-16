/**
 * @author Fuzhong
 *
 */
package cn.learn.learn.thread._queue;
/*

同步容器:Hashtable,Vector
并发容器:ConcurrentHashMap,CopyOnWriteArrayList

非阻塞队列，比如PriorityQueue、LinkedList(LinkedList是双向链表，它实现了Dequeue接口)

非阻塞队列的时候有一个很大问题就是：它不会对当前线程产生阻塞，那么在面对类似消费者-生产者的模型时，就必须额外地实现同步策略以及线程间唤醒策略，这个实现起来就非常麻烦。
但是有了阻塞队列就不一样了，它会对当前线程产生阻塞，比如一个线程从一个空的阻塞队列中取元素，此时线程会被阻塞直到阻塞队列中有了元素。当队列中有元素后，被阻塞的线程会自动被唤醒（不需要我们编写代码去唤醒）。

自从Java 1.5之后，在java.util.concurrent包下提供了若干个阻塞队列，主要有以下几个：

1)	ArrayBlockingQueue：基于数组实现的一个阻塞队列，在创建ArrayBlockingQueue对象时必须制定容量大小。
	并且可以指定公平性与非公平性，默认情况下为非公平的，即不保证等待时间最长的队列最优先能够访问队列。

2)	LinkedBlockingQueue：基于链表实现的一个阻塞队列，在创建LinkedBlockingQueue对象时如果不指定容量大小，则默认大小为Integer.MAX_VALUE。

3)	PriorityBlockingQueue：以上2种队列都是先进先出队列，而PriorityBlockingQueue却不是，它会按照元素的优先级对元素进行排序，
	按照优先级顺序出队，每次出队的元素都是优先级最高的元素。注意，此阻塞队列为无界阻塞队列，即容量没有上限（通过源码就可以知道，它没有容器满的信号标志），前面2种都是有界队列。

4)	DelayQueue：基于PriorityQueue，一种延时阻塞队列，DelayQueue中的元素只有当其指定的延迟时间到了，才能够从队列中获取到该元素。
	DelayQueue也是一个无界队列，因此往队列中插入数据的操作（生产者）永远不会被阻塞，而只有获取数据的操作（消费者）才会被阻塞。

非阻塞队列中的几个主要方法：

	对于非阻塞队列，一般情况下建议使用offer、poll和peek三个方法，不建议使用add和remove方法。
	因为使用offer、poll和peek三个方法可以通过返回值判断操作成功与否，而使用add和remove方法却不能达到这样的效果。注意，非阻塞队列中的方法都没有进行同步措施。

	add(E e):将元素e插入到队列末尾，如果插入成功，则返回true；如果插入失败（即队列已满），则会抛出异常；

　　	remove()：移除队首元素，若移除成功，则返回true；如果移除失败（队列为空），则会抛出异常；

　　	offer(E e)：将元素e插入到队列末尾，如果插入成功，则返回true；如果插入失败（即队列已满），则返回false；

　　	poll()：移除并获取队首元素，若成功，则返回队首元素；否则返回null；

　　	peek()：获取队首元素，若成功，则返回队首元素；否则返回null

阻塞队列中的几个主要方法：
	
	阻塞队列包括了非阻塞队列中的大部分方法，上面列举的5个方法在阻塞队列中都存在，但是要注意这5个方法在阻塞队列中都进行了同步措施。除此之外，阻塞队列提供了另外4个非常有用的方法：
	
	put(E e):方法用来向队尾存入元素，如果队列满，则等待；

　　	take():方法用来从队首取元素，如果队列为空，则等待；

　　	offer(E e,long timeout, TimeUnit unit):方法用来向队尾存入元素，如果队列满，则等待一定的时间，当时间期限达到时，如果还没有插入成功，则返回false；否则返回true；

　　	poll(long timeout, TimeUnit unit):方法用来从队首取元素，如果队列空，则等待一定的时间，当时间期限达到时，如果取到，则返回null；否则返回取得的元素；

示例：
	非阻塞队列：PriorityQueueTest.java
	阻塞队列：ArrayBlockingQueueTest.java

 */