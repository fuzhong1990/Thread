/**
 * Created by Fuzhong on 2016/1/17.
 */
package cn.learn.learn.thread.timer;
/*
其实就Timer来讲就是一个调度器,而TimerTask只是一个实现了run方法的一个类,而具体的TimerTask需要由你自己来实现,例如这样:

    public void schedule(TimerTask task, long delay)：这个方法是调度一个task，经过delay(ms)后开始进行调度，仅仅调度一次。

    public void schedule(TimerTask task, Date time):在指定的时间点time上调度一次。

    public void schedule(TimerTask task, long delay, long period):这个方法是调度一个task，在delay（ms）后开始调度，每次调度完后，最少等待period（ms）后才开始调度。

    public void schedule(TimerTask task, Date firstTime, long period):和上一个方法类似，唯一的区别就是传入的第二个参数为第一次调度的时间。

    public void scheduleAtFixedRate(TimerTask task, long delay, long period):
        调度一个task，在delay(ms)后开始调度，然后每经过period(ms)再次调度，貌似和方法：schedule是一样的，其实不然，后面你会根据源码看到，schedule在计算下一次执行的时间的时候，
        是通过当前时间（在任务执行前得到） + 时间片，而scheduleAtFixedRate方法是通过当前需要执行的时间（也就是计算出现在应该执行的时间）+ 时间片，前者是运行的实际时间，
        而后者是理论时间点，例如：schedule时间片是5s，那么理论上会在5、10、15、20这些时间片被调度，但是如果由于某些CPU征用导致未被调度，假如等到第8s才被第一次调度，
        那么schedule方法计算出来的下一次时间应该是第13s而不是第10s，这样有可能下次就越到20s后而被少调度一次或多次，
        而scheduleAtFixedRate方法就是每次理论计算出下一次需要调度的时间用以排序，若第8s被调度，那么计算出应该是第10s，所以它距离当前时间是2s，那么再调度队列排序中，
        会被优先调度，那么就尽量减少漏掉调度的情况。

    public void scheduleAtFixedRate(TimerTask task, Date firstTime,long period):
        方法同上，唯一的区别就是第一次调度时间设置为一个Date时间，而不是当前时间的一个时间片，我们在源码中会详细说明这些内容。

Timer的构造方法:
    public Timer() { ... }  //创建的线程不为主线程，则主线程结束后，timer自动结束，而无需使用cancel来完成对timer的结束。
    public Timer(boolean isDaemon){ ... } //传入了是否为后台线程，后台线程当且仅当进程结束时，自动注销掉。
    public Timer(String name, boolean isDaemon) { ... } //传入名称和将timer启动

Timer和TimerTask的简单组合是多线程的嘛？
    不是，一个Timer内部包装了“一个Thread”和“一个Task”队列，这个队列按照一定的方式将任务排队处理，包含的线程在Timer的构造方法调用时被启动，
    这个Thread的run方法无限循环这个Task队列，若队列为空且没发生cancel操作，此时会一直等待，如果等待完成后，队列还是为空，则认为发生了cancel从而跳出死循环，结束任务；
    循环中如果发现任务需要执行的时间小于系统时间，则需要执行，那么根据任务的时间片从新计算下次执行时间，若时间片为0代表只执行一次，则直接移除队列即可。

　　但是是否能实现多线程呢？可以，任何东西是否是多线程完全看个人意愿，多个Timer自然就是多线程的，每个Timer都有自己的线程处理逻辑，
    当然Timer从这里来看并不是很适合很多任务在短时间内的快速调度，至少不是很适合同一个timer上挂很多任务，在多线程的领域中我们更多是使用多线程中的：
        Executors.newScheduledThreadPool
    来完成对调度队列中的线程池的处理，内部通过new ScheduledThreadPoolExecutor来创建线程池的Executor的创建，当然也可以调用：
        Executors.unconfigurableScheduledExecutorService
　　方法来创建一个DelegatedScheduledExecutorService其实这个类就是包装了下下scheduleExecutor，也就是这只是一个壳，英文理解就是被委派的意思，被托管的意思。

 */