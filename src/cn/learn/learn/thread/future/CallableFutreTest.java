package cn.learn.learn.thread.future;

import java.util.concurrent.*;

/**
 * Created by Fuzhong on 2016/1/17.
 */
public class CallableFutreTest {
    public static void main(String[] args) {
        ExecutorService service = Executors.newCachedThreadPool();
        Task task = new Task();
        Future<Integer> future = service.submit(task);
        service.shutdown();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("主线程在执行任务");
        try {
            System.out.println("task运行结果" + future.get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("所有任务执行完毕");
    }
}

class Task implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        System.out.println("子线程在进行计算");
        Thread.sleep(3000);
        int sum = 0;
        for (int i=0; i<100; i++) {
            sum += i;
        }
        return sum;
    }
}
