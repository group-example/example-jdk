package util.concurrency.executor;

/**
 * @author: liuluming
 * @CreatedDate: 2017/10/13 下午3:21
 */
public class Test {

    public static void main(String[] args) {
//        //创建资源对象
//        Resource r = new Resource();
//
//        //创建生产者对象
//        Producer pro = new Producer(r);
//        //创建消费者对象
//        Consumer con = new Consumer(r);
//
//        //创建线程对象
//        Thread t1 = new Thread(pro);
//        Thread t2 = new Thread(pro);
//        Thread t3 = new Thread(con);
//        Thread t4 = new Thread(con);
//        //开启线程
//        t1.start();
//        t2.start();
//        t3.start();
//        t4.start();

//        new Thread(new Runnable() {
//            public void run() {
//                System.out.println("runnable run");
//            }
//        }) {
//            public void run() {
//                System.out.println("subthread run");//执行。
//            }
//        }.start();
Son son=new Son();
Father father=(Father)son;

Father father1=new Father();
Son son1=(Son)father1;
    }
}

class Resource {
    private String name;
    private int count = 1;

    private boolean flag = false;
    final

    // 对外提供设置商品的方法
    public synchronized void set(String name) {
        while (flag) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
// 给成员变量赋值并加上编号。
        this.name = name + count;
// 编号自增。
        count++;
// 打印生产了哪个商品。
        System.out.println(Thread.currentThread().getName() + ".....生产者...." + this.name);
// 将标记改为true。
        flag = true;
// 唤醒消费者。
        this.notify();
    }

    public synchronized void get() {
        while (!flag) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        System.out.println(Thread.currentThread().getName() + ".....消费者...." + this.name);
// 将标记改为false。
        flag = false;
// 唤醒生产者。
        this.notify();
    }
}

//描述生产者
class Producer implements Runnable {
    private Resource r;

    //生产者以创建就应该明确资源
    Producer(Resource r) {
        this.r = r;
    }

    //生产者生产商品的任务
    public void run() {
        //生产者无限制的生产
        while (true) {
            r.set("面包");
        }
    }
}

//描述消费者
class Consumer implements Runnable {
    private Resource r;

    //消费者以创建就应该明确资源
    Consumer(Resource r) {
        this.r = r;
    }

    //消费者消费商品的任务
    public void run() {
        while (true) {
            r.get();
        }
    }
}


class Father {
    int age;
    String name;
}

class Son extends Father{
    String school;
}

class ThreadTest {
    public static void main(String[] args) {
        new Thread() {
            public void run() {
                for (int x = 0; x < 40; x++) {
                    System.out.println(Thread.currentThread().getName() + "...X...." + x);
                }
            }
        }.start();
        Runnable r = new Runnable() {
            public void run() {
                for (int x = 0; x < 40; x++) {
                    System.out.println(Thread.currentThread().getName() + "...Y...." + x);
                }
            }
        };
        new Thread(r).start();
        for (int x = 0; x < 40; x++) {
            System.out.println(Thread.currentThread().getName() + "...Z...." + x);
        }
        System.out.println("Hello World!");
    }
}

