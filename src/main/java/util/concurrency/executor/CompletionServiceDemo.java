package util.concurrency.executor;

import java.util.concurrent.*;

/**
 * 得到异步执行结果.
 * 创建5个线程计算一些值，执行完成后使用CompletionService依次取出结果并打印.
 * Future：泛型接口，代表依次异步执行的结果值，调用其get方法可以得到一次异步执行的结果，如果运算未完成，则阻塞直到完成；调用其cancel方法可以取消一次异步执行；
 * CompletionService：一种执行者，可将submit的多个任务的结果按照完成的先后顺序存入一个内部队列，然后可以使用take方法从队列中依次取出结果并移除，如果调用take时计算未完成则会阻塞。
 * @author: liuluming
 * @CreatedDate: 2017/9/20 下午4:38
 */
public class CompletionServiceDemo {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService service = Executors.newCachedThreadPool();
        CompletionService<Integer> completionService = new ExecutorCompletionService<>(service);
        for (int i = 0; i < 5; i++) {
            completionService.submit(new TaskInteger(i));
        }
        service.shutdown();
        //will block
        for (int i = 0; i < 5; i++) {
            Future<Integer> future = completionService.take();
            System.out.println(future.get());
        }

    }
}

class TaskInteger implements Callable<Integer> {
    private final int sum;

    TaskInteger(int sum)  {
        this.sum = sum;
    }

    @Override
    public Integer call() throws Exception {
        TimeUnit.SECONDS.sleep(sum);
        return sum * sum;
    }
}
