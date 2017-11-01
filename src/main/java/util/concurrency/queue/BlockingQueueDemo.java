package util.concurrency.queue;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * 生产者和消费者。
 *
 * 一般而言，有n个生产者，各自生产产品，并放入队列。同时有m个消费者，各自从队列中取出产品消费。
 * 当队列已满时（队列可以在初始化时设置Capacity容量），生产者会在放入队列时阻塞；当队列空时，消费者会在取出产品时阻塞。
 * @author: liuluming
 * @CreatedDate: 2017/9/26 上午10:10
 */
public class BlockingQueueDemo {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>(3);
        ExecutorService service = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {
            service.submit(new Producer("Producer" + i, blockingQueue));
        }
        for (int i = 0; i < 5; i++) {
            service.submit(new Consumer("Consumer" + i, blockingQueue));
        }
        service.shutdown();
    }
}

class Producer implements Runnable {
    private final String name;
    private final BlockingQueue<String> blockingQueue;
    private static Random rand = new Random(47);
    private static AtomicInteger productID = new AtomicInteger(0);

    Producer(String name, BlockingQueue<String> blockingQueue) {
        this.name = name;
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                SECONDS.sleep(rand.nextInt(5));
                String str = "Product" + productID.getAndIncrement();
                blockingQueue.add(str);
                //注意，这里得到的size()有可能是错误的
                System.out.println(name + " product " + str + ", queue size = " + blockingQueue.size());
            }
            System.out.println(name + " is over");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Consumer implements Runnable {
    private final String name;
    private final BlockingQueue<String> blockingQueue;
    private static Random rand = new Random(47);

    Consumer(String name, BlockingQueue<String> blockingQueue) {
        this.name = name;
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                SECONDS.sleep(rand.nextInt(5));
                String str = blockingQueue.take();
                //注意，这里得到的size()有可能是错误的
                System.out.println(name + " consume " + str + ", queue size = " + blockingQueue.size());
            }
            System.out.println(name + " is over");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


// 以上代码中的阻塞队列是LinkedBlockingQueue，初始化容量为3。生产者5个，每个生产者间隔随机时间后生产一个产品put放入队列，每个生产者生产10个产品；
// 消费者也是5个，每个消费者间隔随机时间后take取出一个产品进行消费，每个消费者消费10个产品。
// 可以看到，当队列满时，所有生产者被阻塞；当队列空时，所有消费者被阻塞。代码中还用到了AtomicInteger原子整数，用来确保产品的编号不会混乱。


// BlockingQueue是所有阻塞队列的父接口，具体的实现类有：
// 1、LinkedBlockingQueue、
// 2、ArrayedBlockingQueue、
// 3、SynchronousQueue、
// 4、PriorityBlockingQueue
// 5、DelayQueue。
//
//      LinkedBlockingQueue和ArrayedBlockingQueue的共同点在于，它们都是一个FIFO（先入先出）队列，列头元素就是最老的元素，列尾元素就是最新的元素。元素总是从列尾插入队列，从列头取出队列。
//      它们的不同之处在于，LinkedBlockingQueue的内部实现是一个链表，而ArrayedBlockingQueue的内部实现是一个原生数组。正如其他Java集合一样，链表形式的队列，其存取效率要比数组形式的队列高。
//      但是在一些并发程序中，数组形式的队列由于具有一定的可预测性，因此可以在某些场景中获得更好的效率。
//      另一个不同点在于，ArrayedBlockingQueue支持“公平”策略。若在构造函数中指定了“公平”策略为true，则阻塞在插入或取出方法的所有线程也会按照FIFO进行排队，这样可以有效避免一些线程被“饿死”。
//      BlockingQueue queue = new ArrayBlockingQueue<>(3, true);
//      总体而言，LinkedBlockingQueue是阻塞队列的最经典实现，在不需要“公平”策略时，基本上使用它就够了。