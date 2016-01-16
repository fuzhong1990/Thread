package cn.learn.learn.thread.timer;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Fuzhong on 2016/1/17.
 */
public class TimerTest {
    public static void main(String[] args) {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                System.out.println("后台线程, time = " + System.currentTimeMillis());
            }
        };
        Timer timer = new Timer("thread timer", true);
        timer.schedule(timerTask, new Date(new Date().getTime() + 2000));
        System.out.println("主线程开始执行!");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("主线程执行完成!");
    }
}
