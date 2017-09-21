package util.concurrency.forkjoin;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

/**
 * 寻找一个大型数组的最小值，这里我使用RecursiveTask（其实使用RecursiveAction也行，在它内部用一个成员变量保存结果即可）.
 *
 * pool.invoke(task)将一个最初的任务扔进了线程池执行，这个任务将会执行它的compute()方法。
 * 在此方法中，若满足某个条件（例如数组上界和下界只差小于阈值THRESHOLD）则直接在这一段数组中查找最大值；
 * 若不满足条件，则找出中值mid，然后new出两个子任务lhs(left hand side)和rhs(right hand side)，并调用invokeAll(lhs, rhs)将这两个子任务都扔进线程池执行。
 * 任务的join()方法会得到返回值，若任务尚未执行完毕则会在此阻塞。
 * 通过这种编程模式，很好的将递归思想用到了多线程领域。
 * 值得注意的是，通过调整THRESHOLD可以增加或减少任务的个数，从而极大的影响线程的执行。
 * 在很多情况下，使用fork-join框架并不会比普通的多线程效率更高，甚至比单线程运行效率更低。
 * 因此，必须找到适合的场景，然后进行多次调优，才能获得性能的改进。
 * @author: liuluming
 * @CreatedDate: 2017/9/21 上午10:07
 */
public class RecursiveTaskDemo {
    private static Random rand = new Random(47);
    private static final int NUMBER = 1000000;

    public static void main(String[] args) {
        double[] array = new double[NUMBER];
        for (int i = 0; i < NUMBER; i++) {
            array[i] = rand.nextDouble();
        }

        ForkJoinPool pool = new ForkJoinPool();
        TaskFindMax task = new TaskFindMax(0, array.length - 1, array);
        pool.invoke(task);
        System.out.println("MaxValue = " + task.join());
    }
}

class TaskFindMax extends RecursiveTask<Double> {
    private final int lo;
    private final int hi;
    private final double[] array;
    //you can change THRESHOLD to get better efficiency
    private final static int THRESHOLD = 10;

    TaskFindMax(int lo, int hi, double[] array) {
        this.lo = lo;
        this.hi = hi;
        this.array = array;
    }

    @Override
    protected Double compute() {
        if ((hi - lo) < THRESHOLD) {
            double max = array[lo];
            for (int i = lo; i < hi; i++) {
                max = Math.max(max, array[i + 1]);
            }
            return max;
        } else {
            int mid = (lo + hi) >>> 1;
            TaskFindMax lhs = new TaskFindMax(lo, mid, array);
            TaskFindMax rhs = new TaskFindMax(mid, hi, array);
            invokeAll(lhs, rhs);
            return Math.max(lhs.join(), rhs.join());
        }
    }
}
