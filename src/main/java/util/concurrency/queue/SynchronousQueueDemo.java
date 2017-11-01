package util.concurrency.queue;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 下面是一个例子，5个Producer产生产品，存入队列；5个Consumer从队列中取出产品，进行消费.
 *
 *
 * SynchronousQueue是一个比较特殊的阻塞队列，它具有以下几个特点：
 1. 一个调用插入方法的线程必须等待另一个线程调用取出方法；
 2. 队列没有容量Capacity（或者说容量为0），事实上队列中并不存储元素，它只是提供两个线程进行信息交换的场所；
 3. 由于以上原因，队列在很多场合表现的像一个空队列。不能对元素进行迭代，不能peek元素，poll会返回null；
 4. 队列中不允许存入null元素。
 5. SynchronousQueue如同ArrayedBlockingQueue一样，支持“公平”策略，在构造函数中可以传入false或true表示是否支持该策略。
 * @author: liuluming
 * @CreatedDate: 2017/9/27 下午4:42
 */
public class SynchronousQueueDemo {

    public static void main(String[] args) {
        SynchronousQueue<String> queue = new SynchronousQueue<>(false);
        ExecutorService service = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {
            service.submit(new Producer(queue, "Producer" + i));
        }
        for (int i = 0; i < 5; i++) {
            service.submit(new Consumer(queue, "Consumer" + i));
        }
        service.shutdown();
    }

    static class Producer implements Runnable {
        private final SynchronousQueue<String> queue;
        private final String name;
        private static Random rand = new Random(47);
        private static AtomicInteger productID = new AtomicInteger(0);

        Producer(SynchronousQueue<String> queue, String name) {
            this.queue = queue;
            this.name = name;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 5; i++) {
                    TimeUnit.SECONDS.sleep(rand.nextInt(5));
                    String str = "Product" + productID.incrementAndGet();
                    queue.put(str);
                    System.out.println(name + " put " + str);
                }
                System.out.println(name + " is over.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    static class Consumer implements Runnable {
        private final SynchronousQueue<String> queue;
        private final String name;

        Consumer(SynchronousQueue<String> queue, String name) {
            this.queue = queue;
            this.name = name;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 5; i++) {
                    String str = queue.take();
                    System.out.println(name + " take " + str);
                }
                System.out.println(name + " is over.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
