package util.concurrency.forkjoin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * 计算一个大型数组中每个元素x的一个公式的值，这个公式是sin(x)+cos(x)+tan(x).
 *
 * 代码还是比较简单的，首先创建一个ForkJoinPool，然后new一个ComputeTask对象丢到线程池中运行。
 * ComputeTask继承自RecursiveAction，实现了其compute()方法。这个方法中，当上界和下界的差小于一个值（这里是2）时，直接计算；否则创建两个子任务，并丢到线程池中。
 * 最值得注意的是compute()方法，这个方法里面体现了“分而治之”的思想，对程序员来说，叫递归思想更加合适。只不过普通的递归是在单线程中完成的，而这里的递归则把递归任务通过invokeAll()方法丢进了线程池中，让线程池来调度执行。
 * 运行结果是Time span = 3798。
 * @author: liuluming
 * @CreatedDate: 2017/9/21 上午10:07
 */
public class RecursiveActionDemo {
    private final static int NUMBER = 10000000;

    public static void main(String[] args) {
        double[] array = new double[NUMBER];
        for (int i = 0; i < NUMBER; i++) {
            array[i] = i;
        }
        long startTime = System.currentTimeMillis();
        System.out.println(Runtime.getRuntime().availableProcessors());
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        forkJoinPool.invoke(new ComputeTask(array, 0, array.length));
        long endTime = System.currentTimeMillis();
        System.out.println("Time span = " + (endTime - startTime));
    }
}

class ComputeTask extends RecursiveAction {
    final double[] array;
    final int lo, hi;

    ComputeTask(double[] array, int lo, int hi) {
        this.array = array;
        this.lo = lo;
        this.hi = hi;
    }

    protected void compute() {
        if (hi - lo < 2) {
            for (int i = lo; i < hi; ++i)
                array[i] = Math.sin(array[i]) + Math.cos(array[i]) + Math.tan(array[i]);
        } else {
            int mid = (lo + hi) >>> 1;
            invokeAll(new ComputeTask(array, lo, mid),
                    new ComputeTask(array, mid, hi));
        }
    }
}
