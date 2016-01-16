
/**
 * x
 * @author Fuzhong
 *
 */
package cn.learn.learn.thread;
/*

新线程创建的过程不会阻塞主线程的后续执行.

start方法 —— start()用来启动一个线程，当调用start方法后，系统才会开启一个新的线程来执行用户定义的子任务，
            在这个过程中，会为相应的线程分配需要的资源。

run方法 —— run()方法是不需要用户来调用的，当通过start方法启动一个线程之后，当线程获得了CPU执行时间，便进入run方法体去执行具体的任务。
           注意，继承Thread类必须重写run方法，在run方法中定义具体要执行的任务。

sleep方法 —— sleep相当于让线程睡眠，交出CPU，让CPU去执行其他的任务。
　　         但是有一点要非常注意，sleep方法不会释放锁，也就是说如果当前线程持有对某个对象的锁，则即使调用sleep方法，其他线程也无法访问这个对象。看下面这个例子就清楚了：

yield方法 —— 调用yield方法会让当前线程交出CPU权限，让CPU去执行其他的线程。它跟sleep方法类似，同样不会释放锁。
             但是yield不能控制具体的交出CPU的时间，另外，yield方法只能让拥有相同优先级的线程有获取CPU执行时间的机会。
　　          注意，调用yield方法并不会让线程进入阻塞状态，而是让线程重回就绪状态，它只需要等待重新获取CPU执行时间，这一点是和sleep方法不一样的。

join方法 —— 假如在main线程中，调用thread.join方法，则main方法会等待thread线程执行完毕或者等待一定的时间。
            如果调用的是无参join方法，则等待thread执行完毕，如果调用的是指定了时间参数的join方法，则等待一定的事件。

interrupt方法 —— interrupt，顾名思义，即中断的意思。单独调用interrupt方法可以使得处于阻塞状态的线程抛出一个异常，
                 也就说，它可以用来中断一个正处于阻塞状态的线程；另外，通过interrupt方法和isInterrupted()方法来停止正在运行的线程。

stop方法 —— stop方法已经是一个废弃的方法，它是一个不安全的方法。因为调用stop方法会直接终止run方法的调用，并且会抛出一个ThreadDeath错误，
            如果线程持有某个对象锁的话，会完全释放锁，导致对象状态不一致。所以stop方法基本是不会被用到的。

destroy方法 —— destroy方法也是废弃的方法。基本不会被使用到。

以下是关系到线程属性的几个方法：

　　1）getId —— 用来得到线程ID

　　2）getName和setName —— 用来得到或者设置线程名称。

　　3）getPriority和setPriority —— 用来获取和设置线程优先级。

　　4）setDaemon和isDaemon —— 用来设置线程是否成为守护线程和判断线程是否是守护线程。

守护线程和用户线程的区别在于：守护线程依赖于创建它的线程，而用户线程则不依赖。
举个简单的例子：如果在main线程中创建了一个守护线程，当main方法运行完毕之后，守护线程也会随着消亡。而用户线程则不会，用户线程会一直运行直到其运行完毕。

¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤

1）wait()、notify()和notifyAll()方法是本地方法，并且为final方法，无法被重写。

2）调用某个对象的wait()方法能让当前线程阻塞，并且当前线程必须拥有此对象的monitor（即锁）

3）调用某个对象的notify()方法能够唤醒一个正在等待这个对象的monitor的线程，如果有多个线程都在等待这个对象的monitor，则只能唤醒其中一个线程；

4）调用notifyAll()方法能够唤醒所有正在等待这个对象的monitor的线程；

有朋友可能会有疑问：为何这三个不是Thread类声明中的方法，而是Object类中声明的方法（当然由于Thread类继承了Object类，所以Thread也可以调用者三个方法）？
其实这个问题很简单，由于每个对象都拥有monitor（即锁），所以让当前线程等待某个对象的锁，当然应该通过这个对象来操作了。而不是用当前线程来操作，因为当前线程可能会等待多个线程的锁，如果通过线程来操作，就非常复杂了。

上面已经提到，如果调用某个对象的wait()方法，当前线程必须拥有这个对象的monitor（即锁），因此调用wait()方法必须在同步块或者同步方法中进行（synchronized块或者synchronized方法）。

调用某个对象的wait()方法，相当于让当前线程交出此对象的monitor，然后进入等待状态，等待后续再次获得此对象的锁（Thread类中的sleep方法使当前线程暂停执行一段时间，从而让其他线程有机会继续执行，但它并不释放对象锁）；

notify()方法能够唤醒一个正在等待该对象的monitor的线程，当有多个线程都在等待该对象的monitor的话，则只能唤醒其中一个线程，具体唤醒哪个线程则不得而知。
同样地，调用某个对象的notify()方法，当前线程也必须拥有这个对象的monitor，因此调用notify()方法必须在同步块或者同步方法中进行（synchronized块或者synchronized方法）。

nofityAll()方法能够唤醒所有正在等待该对象的monitor的线程，这一点与notify()方法是不同的。

这里要注意一点：notify()和notifyAll()方法只是唤醒等待该对象的monitor的线程，并不决定哪个线程能够获取到monitor。

举个简单的例子：假如有三个线程Thread1、Thread2和Thread3都在等待对象objectA的monitor，此时Thread4拥有对象objectA的monitor，当在Thread4中调用objectA.notify()方法之后，Thread1、Thread2和Thread3只有一个能被唤醒。
注意，被唤醒不等于立刻就获取了objectA的monitor。假若在Thread4中调用objectA.notifyAll()方法，则Thread1、Thread2和Thread3三个线程都会被唤醒，至于哪个线程接下来能够获取到objectA的monitor就具体依赖于操作系统的调度了。

上面尤其要注意一点，一个线程被唤醒不代表立即获取了对象的monitor，只有等调用完notify()或者notifyAll()并退出synchronized块，释放对象锁后，其余线程才可获得锁执行。

参考：ThreadTest.java

Condition是在java 1.5中才出现的，它用来替代传统的Object的wait()、notify()实现线程间的协作，相比使用Object的wait()、notify()，
使用Condition的await()、0()这种方式实现线程间协作更加安全和高效。因此通常来说比较推荐使用Condition，在阻塞队列那一篇博文中就讲述到了，阻塞队列实际上是使用了Condition来模拟线程间协作。

Condition是个接口，基本的方法就是await()和signal()方法；

Condition依赖于Lock接口，生成一个Condition的基本代码是lock.newCondition() 

调用Condition的await()和signal()方法，都必须在lock保护之内，就是说必须在lock.lock()和lock.unlock之间才可以使用

Condition中的await()对应Object的wait()；

Condition中的signal()对应Object的notify()；

Condition中的signalAll()对应Object的notifyAll()

参考：ThreadObjectTest.java 和 ThreadConditionTest.java
------优先队列:PriorityQueue

¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤

在Java的集合容器框架中，主要有四大类别：List、Set、Queue、Map。

List、Set、Queue接口分别继承了Collection接口，Map本身是一个接口。注意Collection和Map是一个顶层接口，而List、Set、Queue则继承了Collection接口，分别代表数组、集合和队列这三大类容器。

像ArrayList、LinkedList都是实现了List接口，HashSet实现了Set接口，而Deque（双向队列，允许在队首、队尾进行入队和出队操作）继承了Queue接口，PriorityQueue实现了Queue接口。
另外LinkedList（实际上是双向链表）实现了了Deque接口。像ArrayList、LinkedList、HashMap这些容器都是非线程安全的。

Java中的同步容器类:
	1）Vector、Stack、HashTable
	2）Collections类中提供的静态工厂方法创建的类
	
	Vector实现了List接口，Vector实际上就是一个数组，和ArrayList类似，但是Vector中的方法都是synchronized方法，即进行了同步措施。
	Stack也是一个同步容器，它的方法也用synchronized进行了同步，它实际上是继承于Vector类。
	HashTable实现了Map接口，它和HashMap很相似，但是HashTable进行了同步处理，而HashMap没有。
	Collections类是一个工具提供类，注意，它和Collection不同，Collection是一个顶层的接口。在Collections类中提供了大量的方法，比如对集合或者容器进行排序、查找等操作。
	最重要的是，在它里面提供了几个静态工厂方法来创建同步容器类.
	
同步容器的缺陷：
	1）性能问题
	2）同步容器真的是安全的吗？事实上这可不一定。
	3）ConcurrentModificationException异常

ConcurrentModificationException异常:
	
	1)单线程环境下的解决办法:迭代器中如果要删除元素的话，需要调用Iterator类的remove方法。
	
	2)多线程环境下的解决方法:
			1.在使用iterator迭代的时候使用synchronized或者Lock进行同步
			2.使用并发容器CopyOnWriteArrayList代替ArrayList和Vector
	
¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤

什么是CopyOnWrite容器：

	CopyOnWrite容器即写时复制的容器。通俗的理解是当我们往一个容器添加元素的时候，不直接往当前容器添加，而是先将当前容器进行Copy，复制出一个新的容器，然后新的容器里添加元素，添加完元素之后，
	再将原容器的引用指向新的容器。这样做的好处是我们可以对CopyOnWrite容器进行并发的读，而不需要加锁，因为当前容器不会添加任何元素。所以CopyOnWrite容器也是一种读写分离的思想，读和写不同的容器。

CopyOnWriteArrayList的实现原理：
	1)在添加的时候是需要加锁的，否则多线程写的时候会Copy出N个副本出来。
	2)读的时候不需要加锁，如果读的时候有多个线程正在向CopyOnWriteArrayList添加数据，读还是会读到旧的数据，因为写的时候不会锁住旧的CopyOnWriteArrayList。
	3)JDK中并没有提供CopyOnWriteMap，我们可以参考CopyOnWriteArrayList来实现一个，基本代码如下：
	
	public class CopyOnWriteMap<K, V> implements Map<K, V>, Cloneable {
	    private volatile Map<K, V> internalMap;
	 
	    public CopyOnWriteMap() {
	        internalMap = new HashMap<K, V>();
	    }
	 
	    public V put(K key, V value) {
	 
	        synchronized (this) {
	            Map<K, V> newMap = new HashMap<K, V>(internalMap);
	            V val = newMap.put(key, value);
	            internalMap = newMap;
	            return val;
	        }
	    }
	 
	    public V get(Object key) {
	        return internalMap.get(key);
	    }
	 
	    public void putAll(Map<? extends K, ? extends V> newData) {
	        synchronized (this) {
	            Map<K, V> newMap = new HashMap<K, V>(internalMap);
	            newMap.putAll(newData);
	            internalMap = newMap;
	        }
	    }
	}

CopyOnWrite的应用场景：
	CopyOnWrite并发容器用于读多写少的并发场景。
	
	代码很简单，但是使用CopyOnWriteMap需要注意两件事情：
	　　1. 减少扩容开销。根据实际需要，初始化CopyOnWriteMap的大小，避免写时CopyOnWriteMap扩容的开销。
	　　2. 使用批量添加。因为每次添加，容器每次都会进行复制，所以减少添加次数，可以减少容器的复制次数。

CopyOnWrite的缺点：
	1）内存占用问题。因为CopyOnWrite的写时复制机制，所以在进行写操作的时候，内存里会同时驻扎两个对象的内存，旧的对象和新写入的对象
	     （注意:在复制的时候只是复制容器里的引用，只是在写的时候会创建新对象添加到新容器里，而旧容器的对象还在使用，所以有两份对象内存）。
	       如果这些对象占用的内存比较大，比如说200M左右，那么再写入100M数据进去，内存就会占用300M，那么这个时候很有可能造成频繁的Yong GC和Full GC。
	       
	       针对内存占用问题，可以通过压缩容器中的元素的方法来减少大对象的内存消耗，比如，如果元素全是10进制的数字，可以考虑把它压缩成36进制或64进制。
	       或者不使用CopyOnWrite容器，而使用其他的并发容器，如：ConcurrentHashMap。

	2）数据一致性问题。CopyOnWrite容器只能保证数据的最终一致性，不能保证数据的实时一致性。所以如果你希望写入的的数据，马上能读到，请不要使用CopyOnWrite容器。
	

*/