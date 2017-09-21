package util.concurrency.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 在java.util.concurrent包中多数的执行器实现都使用了由工作线程组成的线程池。
 *
 *
 * 执行器种类：
 * 1、单线程：newSingleThreadExecutor()方法创建，五个参数分别是ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue())。含义是池中保持一个线程，最多也只有一个线程，也就是说这个线程池是顺序执行任务的，多余的任务就在队列中排队。
 * 2、固定线程池：newFixedThreadPool(nThreads)方法创建，五个参数分别是ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue())。含义是池中保持nThreads个线程，最多也只有nThreads个线程，多余的任务也在队列中排队。
 * 3、缓存线程池：newCachedThreadPool()创建，五个参数分别是ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue())。含义是池中不保持固定数量的线程，随需创建，最多可以创建Integer.MAX_VALUE个线程（说一句，这个数量已经大大超过目前任何操作系统允许的线程数了），空闲的线程最多保持60秒，多余的任务在SynchronousQueue（所有阻塞、并发队列在后续文章中具体介绍）中等待。
 * 4、单线程调度线程池：newSingleThreadScheduledExecutor()创建，五个参数分别是 (1, Integer.MAX_VALUE, 0, NANOSECONDS, new DelayedWorkQueue())。含义是池中保持1个线程，多余的任务在DelayedWorkQueue中等待。
 * 5、固定调度线程池：newScheduledThreadPool(n)创建，五个参数分别是 (n, Integer.MAX_VALUE, 0, NANOSECONDS, new DelayedWorkQueue())。含义是池中保持n个线程，多余的任务在DelayedWorkQueue中等待。
 *
 * 而进一步查看源代码，这些方法最终都调用了ThreadPoolExecutor和ScheduledExecutorService的构造函数。
 * 而ScheduledExecutorService继承自ThreadPoolExecutor，因此最终所有线程池的构造函数都调用了ThreadPoolExecutor的如下构造函数：
 * public ThreadPoolExecutor(int corePoolSize,
    int maximumPoolSize,
     long keepAliveTime,
    TimeUnit unit,
    BlockingQueue<Runnable> workQueue)

  注意这五个参数（重点）：
 第一个参数corePoolSize，指明了线程池中应该保持的线程数量，即使线程处于空闲状态，除非设置了allowCoreThreadTimeOut这个参数；
 第二个参数maximumPoolSize，线程池中允许的最大线程数，线程池中的线程一旦到达这个数，后续任务就会等待池中的线程空闲，而不会去创建新的线程了；
 第三个参数keepAliveTime，当池中线程数量大于corePoolSize时，多出的线程在空闲时能够生存的最大时间，若一个线程空闲超过这个时间，它就会被终止并从池中删除；
 第四个参数unit，指示第三个参数的时间单位；
 第五个参数workQueue，存储待执行任务的阻塞队列，这些任务必须是Runnable的对象（如果是Callable对象，会在submit内部转换为Runnable对象）。

 * 总结：如果没有特殊要求，使用缓存线程池总是合适的；如果只能运行固定数量的线程，就使用固定线程池；如果只能运行一个线程，就使用单线程池。如果要运行调度任务，则按需使用调度线程池或单线程调度线程池。
 * 如果有其他特殊要求，则可以直接使用ThreadPoolExecutor类的构造函数来创建线程池，并自己给定那五个参数。
 * @author: liuluming
 * @CreatedDate: 2017/9/20 下午5:13
 */
public class ExecutorDemo {
    public static void main(String[] args) {
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(new Task("task1"));
        service.execute(new Task("task2"));
        service.execute(new Task("task3"));
        service.shutdown();
    }
}

class Task implements Runnable {
    private final String name;

    Task(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 5; i++) {
                TimeUnit.SECONDS.sleep(1);
                System.out.println(name + "-[" + i + "]");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
