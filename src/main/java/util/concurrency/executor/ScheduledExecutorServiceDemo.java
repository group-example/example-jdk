package util.concurrency.executor;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 重复执行和延期执行.
 * ScheduledExecutorService的实例scheduler调度了两个任务，第一个任务使用scheduleAtFixedRate()方法每隔一秒重复打印“beep”;
 * 第二个任务使用schedule()方法在10秒后延迟执行，它的作用是取消第一个任务.
 *
 * 在Java1.4之前，一般使用Timer来重复或者延期执行任务。Java Concurrency为了使之与Executor理念协调，引入了ScheduledExecutorService来完成同样的工作。
 * ScheduledExecutorService：另一种执行者，可以将提交的任务延期执行，也可以将提交的任务反复执行。
 * ScheduledFuture：与Future接口类似，代表一个被调度执行的异步任务的返回值。
 * @author: liuluming
 * @CreatedDate: 2017/9/20 下午5:04
 */
public class ScheduledExecutorServiceDemo {
    public static void main(String[] args) {
        ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(2);
        ScheduledFuture<?> scheduledFuture = scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("beep");
            }
        }, 1, 1, TimeUnit.SECONDS);

        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("cancel beep");
                scheduledFuture.cancel(true);
                scheduler.shutdown();
            }
        }, 10, TimeUnit.SECONDS);
    }
}
