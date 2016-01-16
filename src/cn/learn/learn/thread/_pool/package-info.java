/**
 * @author Fuzhong
 *
 */
package cn.learn.learn.thread._pool;

/*

java.uitl.concurrent.ThreadPoolExecutor类是线程池中最核心的一个类，因此如果要透彻地了解Java中的线程池，必须先了解这个类。

1)——>构造参数说明：

	1)corePoolSize：核心池的大小，这个参数跟后面讲述的线程池的实现原理有非常大的关系。在创建了线程池后，默认情况下，线程池中并没有任何线程，而是等待有任务到来才创建线程去执行任务，
	除非调用了prestartAllCoreThreads()或者prestartCoreThread()方法，从这2个方法的名字就可以看出，是预创建线程的意思，即在没有任务到来之前就创建corePoolSize个线程或者一个线程。
	默认情况下，在创建了线程池后，线程池中的线程数为0，当有任务来之后，就会创建一个线程去执行任务，当线程池中的线程数目达到corePoolSize后，就会把到达的任务放到缓存队列当中；
	
	2)maximumPoolSize：线程池最大线程数，这个参数也是一个非常重要的参数，它表示在线程池中最多能创建多少个线程；
	
	3)keepAliveTime：表示线程没有任务执行时最多保持多久时间会终止。默认情况下，只有当线程池中的线程数大于corePoolSize时，keepAliveTime才会起作用，
	直到线程池中的线程数不大于corePoolSize，即当线程池中的线程数大于corePoolSize时，如果一个线程空闲的时间达到keepAliveTime，则会终止，直到线程池中的线程数不超过corePoolSize。
	但是如果调用了allowCoreThreadTimeOut(boolean)方法，在线程池中的线程数不大于corePoolSize时，keepAliveTime参数也会起作用，直到线程池中的线程数为0；
	
	4)unit：参数keepAliveTime的时间单位，有7种取值，在TimeUnit类中有7种静态属性：
		TimeUnit.DAYS;              //天
		TimeUnit.HOURS;             //小时
		TimeUnit.MINUTES;           //分钟
		TimeUnit.SECONDS;           //秒
		TimeUnit.MILLISECONDS;      //毫秒
		TimeUnit.MICROSECONDS;      //微妙
		TimeUnit.NANOSECONDS;       //纳秒
		
	5)workQueue：一个阻塞队列，用来存储等待执行的任务，这个参数的选择也很重要，会对线程池的运行过程产生重大影响，一般来说，这里的阻塞队列有以下几种选择：
		ArrayBlockingQueue; LinkedBlockingQueue; SynchronousQueue;
		ArrayBlockingQueue和PriorityBlockingQueue使用较少，一般使用LinkedBlockingQueue和SynchronousQueue。线程池的排队策略与BlockingQueue有关。

	6)threadFactory：线程工厂，主要用来创建线程；
	
	7)handler：表示当拒绝处理任务时的策略，有以下四种取值：
		ThreadPoolExecutor.AbortPolicy:丢弃任务并抛出RejectedExecutionException异常。 
		ThreadPoolExecutor.DiscardPolicy：也是丢弃任务，但是不抛出异常。 
		ThreadPoolExecutor.DiscardOldestPolicy：丢弃队列最前面的任务，然后重新尝试执行任务（重复此过程）
		ThreadPoolExecutor.CallerRunsPolicy：由调用线程处理该任务 
		
	大家应该明白了ThreadPoolExecutor、AbstractExecutorService、ExecutorService和Executor几个之间的关系：

	　　Executor是一个顶层接口，在它里面只声明了一个方法execute(Runnable)，返回值为void，参数为Runnable类型，从字面意思可以理解，就是用来执行传进去的任务的；
	　　然后ExecutorService接口继承了Executor接口，并声明了一些方法：submit、invokeAll、invokeAny以及shutDown等；
	　　抽象类AbstractExecutorService实现了ExecutorService接口，基本实现了ExecutorService中声明的所有方法；
	　　然后ThreadPoolExecutor继承了类AbstractExecutorService。
	　　在ThreadPoolExecutor类中有几个非常重要的方法：execute(),submit(),shutdown(),shutdownNow()

	1)	execute()：方法实际上是Executor中声明的方法，在ThreadPoolExecutor进行了具体的实现，这个方法是ThreadPoolExecutor的核心方法，
		通过这个方法可以向线程池提交一个任务，交由线程池去执行。
	
	2)	submit()方法是在ExecutorService中声明的方法，在AbstractExecutorService就已经有了具体的实现，在ThreadPoolExecutor中并没有对其进行重写，
		这个方法也是用来向线程池提交任务的，但是它和execute()方法不同，它能够返回任务执行的结果，去看submit()方法的实现，会发现它实际上还是调用的execute()方法，
		只不过它利用了Future来获取任务执行结果。
		
	3)	shutdown()和shutdownNow()是用来关闭线程池的。

2)——>任务的执行
在ThreadPoolExecutor中定义了一个volatile变量，另外定义了几个static final变量表示线程池的各个状态：

volatile int runState;
static final int RUNNING    = 0;
static final int SHUTDOWN   = 1;
static final int STOP       = 2;
static final int TERMINATED = 3;

runState表示当前线程池的状态，它是一个volatile变量用来保证线程之间的可见性；

　　下面的几个static final变量表示runState可能的几个取值。
　　当创建线程池后，初始时，线程池处于RUNNING状态；
　　如果调用了shutdown()方法，则线程池处于SHUTDOWN状态，此时线程池不能够接受新的任务，它会等待所有任务执行完毕；
　　如果调用了shutdownNow()方法，则线程池处于STOP状态，此时线程池不能接受新的任务，并且会去尝试终止正在执行的任务；
　　当线程池处于SHUTDOWN或STOP状态，并且所有工作线程已经销毁，任务缓存队列已经清空或执行结束后，线程池被设置为TERMINATED状态。

ThreadPoolExecutor类中其他的一些比较重要成员变量：

	private final BlockingQueue<Runnable> workQueue;              //任务缓存队列，用来存放等待执行的任务
	private final ReentrantLock mainLock = new ReentrantLock();   //线程池的主要状态锁，对线程池状态（比如线程池大小、runState等）的改变都要使用这个锁
	private final HashSet<Worker> workers = new HashSet<Worker>();  //用来存放工作集
	private volatile long  keepAliveTime;    //线程存货时间   
	private volatile boolean allowCoreThreadTimeOut;   //是否允许为核心线程设置存活时间
	private volatile int   corePoolSize;     //核心池的大小（即线程池中的线程数目大于这个参数时，提交的任务会被放进任务缓存队列）
	private volatile int   maximumPoolSize;   //线程池最大能容忍的线程数
	private volatile int   poolSize;       //线程池中当前的线程数
	private volatile RejectedExecutionHandler handler; //任务拒绝策略
	private volatile ThreadFactory threadFactory;   //线程工厂，用来创建线程
	private int largestPoolSize;   //用来记录线程池中曾经出现过的最大线程数
	private long completedTaskCount;   //用来记录已经执行完毕的任务个数
	
也就是说corePoolSize就是线程池大小，maximumPoolSize在我看来是线程池的一种补救措施，即任务量突然过大时的一种补救措施。

largestPoolSize只是一个用来起记录作用的变量，用来记录线程池中曾经有过的最大线程数目，跟线程池的容量没有任何关系。

注意：
	1）首先，要清楚corePoolSize和maximumPoolSize的含义；

　　	2）其次，要知道Worker是用来起到什么作用的；

　　	3）要知道任务提交给线程池之后的处理策略，这里总结一下主要有4点：
		如果当前线程池中的线程数目小于corePoolSize，则每来一个任务，就会创建一个线程去执行这个任务；	
		如果当前线程池中的线程数目>=corePoolSize，则每来一个任务，会尝试将其添加到任务缓存队列当中，若添加成功，则该任务会等待空闲线程将其取出去执行；若添加失败（一般来说是任务缓存队列已满），则会尝试创建新的线程去执行这个任务；
		如果当前线程池中的线程数目达到maximumPoolSize，则会采取任务拒绝策略进行处理；
		如果线程池中的线程数量大于 corePoolSize时，如果某线程空闲时间超过keepAliveTime，线程将被终止，直至线程池中的线程数目不大于corePoolSize；如果允许为核心池中的线程设置存活时间，那么核心池中的线程空闲时间超过keepAliveTime，线程也会被终止。

3)——>线程池中的线程初始化:
	默认情况下，创建线程池之后，线程池中是没有线程的，需要提交任务之后才会创建线程。

　　	在实际中如果需要线程池创建之后立即创建线程，可以通过以下两个方法办到：

	prestartCoreThread()：初始化一个核心线程；
	prestartAllCoreThreads()：初始化所有核心线程

4)——>任务缓存队列及排队策略:
	在前面我们多次提到了任务缓存队列，即workQueue，它用来存放等待执行的任务。

　　	workQueue的类型为BlockingQueue<Runnable>，通常可以取下面三种类型：

　　	1）ArrayBlockingQueue：基于数组的先进先出队列，此队列创建时必须指定大小；
　　	2）LinkedBlockingQueue：基于链表的先进先出队列，如果创建时没有指定此队列大小，则默认为Integer.MAX_VALUE；
　　	3）synchronousQueue：这个队列比较特殊，它不会保存提交的任务，而是将直接新建一个线程来执行新来的任务。

5)——>任务拒绝策略:
	当线程池的任务缓存队列已满并且线程池中的线程数目达到maximumPoolSize，如果还有任务到来就会采取任务拒绝策略，通常有以下四种策略：
	
	ThreadPoolExecutor.AbortPolicy:丢弃任务并抛出RejectedExecutionException异常。
	ThreadPoolExecutor.DiscardPolicy：也是丢弃任务，但是不抛出异常。
	ThreadPoolExecutor.DiscardOldestPolicy：丢弃队列最前面的任务，然后重新尝试执行任务（重复此过程）
	ThreadPoolExecutor.CallerRunsPolicy：由调用线程处理该任务
6)——>线程池的关闭:
	ThreadPoolExecutor提供了两个方法，用于线程池的关闭，分别是shutdown()和shutdownNow()，其中：

	shutdown()：不会立即终止线程池，而是要等所有任务缓存队列中的任务都执行完后才终止，但再也不会接受新的任务
	shutdownNow()：立即终止线程池，并尝试打断正在执行的任务，并且清空任务缓存队列，返回尚未执行的任务

7)——>线程池容量的动态调整:
	ThreadPoolExecutor提供了动态调整线程池容量大小的方法：setCorePoolSize()和setMaximumPoolSize()，

	setCorePoolSize：设置核心池大小
	setMaximumPoolSize：设置线程池最大能创建的线程数目大小
	
	当上述参数从小变大时，ThreadPoolExecutor进行线程赋值，还可能立即创建新的线程来执行任务。

ThreadPoolExecutor示例：Example1.java

不过在java doc中，并不提倡我们直接使用ThreadPoolExecutor，而是使用Executors类中提供的几个静态方法来创建线程池：
	Executors.newCachedThreadPool();        //创建一个缓冲池，缓冲池容量大小为Integer.MAX_VALUE
	Executors.newSingleThreadExecutor();   //创建容量为1的缓冲池
	Executors.newFixedThreadPool(int);    //创建固定容量大小的缓冲池
	
从它们的具体实现来看，它们实际上也是调用了ThreadPoolExecutor，只不过参数都已配置好了。
	
	newFixedThreadPool创建的线程池corePoolSize和maximumPoolSize值是相等的，它使用的LinkedBlockingQueue；
	
	newSingleThreadExecutor将corePoolSize和maximumPoolSize都设置为1，也使用的LinkedBlockingQueue；
	
	newCachedThreadPool将corePoolSize设置为0，将maximumPoolSize设置为Integer.MAX_VALUE，使用的SynchronousQueue，也就是说来了任务就创建线程运行，
	当线程空闲超过60秒，就销毁线程。
	
	实际中，如果Executors提供的三个静态方法能满足要求，就尽量使用它提供的三个方法，因为自己去手动配置ThreadPoolExecutor的参数有点麻烦，要根据实际任务的类型和数量来进行配置。
	
	另外，如果ThreadPoolExecutor达不到要求，可以自己继承ThreadPoolExecutor类进行重写。

 */