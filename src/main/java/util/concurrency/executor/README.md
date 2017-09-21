  > 
 ![结构图](https://pic1.zhimg.com/v2-e11a5c34827a48bb00035a7937978df0_r.png)
 
 ## Executor的设计思路
 在介绍具体的工具之前，先讲讲设计者的思路。  
   在Java1.5之前，已经提供了Runnable接口、Thread类、Timer类和synchronize关键字，它们已经足以完成各种各样的多线程编程任务，为什么还要提供执行者这样的概念呢？  
   这是因为Java的设计者想把线程的创建、执行和调度分离。在Concurrency包出现之前，线程的创建基本上靠new一个Thread对象，执行靠start()方法，而线程的调度则完全依赖程序员在具体代码中自己写出来。  
   而Concurrency包出现之后，线程的创建还是依靠Thread、Runnable和Callable（新加入）对象的实例化；而线程的执行则靠Executor、ExecutorService的对象执行execute()方法或submit()方法；线程的调度则被固化为几个具体的线程池类，如ThreadPoolExecutor、ScheduledThreadPoolExecutor、ExecutorCompletionService等等。  
   这样虽然表面上增加了复杂度，而实际上成功将线程的创建、执行和调度的业务逻辑分离，使程序员能够将精力集中在线程中业务逻辑的编写，大大提高了编码效率，降低了出错的概率，而且大大提高了性能。
  
 ## Executor的主要内容
 这个功能主要由三个接口和类提供，分别是： 
 1. Executor：执行者接口，所有执行者的父类； 
 1. ExecutorService：执行者服务接口，具体的执行者类都继承自此接口； 
 1. Executors：执行者工具类，大部分执行者的实例以及线程池都由它的工厂方法创建。 
 
 ## Executor的执行流程
 
 
 ## 如何得到异步执行结果？
 在Java1.4之前，要得到一个线程运行后产生的值，没有一套现成的机制，程序员可以通过Thread类的成员变量、程序的全局变量等方式来得到一个线程运行后产生的某个值。  
 但是这样的话，你必须不断探测线程是否已经成功结束，或者运用同步技术来等待线程执行完成，再去获取异步执行的结果。  
 在Java Concurrency中，得到异步结果有了一套固定的机制，即通过Callable接口、Future接口和ExecutorService的submit方法来得到异步执行的结果，相关工具的介绍如下： 
 1. Callable：泛型接口，与Runnable接口类似，它的实例可以被另一个线程执行，内部有一个call方法，返回一个泛型变量V； 
 1. Future：泛型接口，代表依次异步执行的结果值，调用其get方法可以得到一次异步执行的结果，如果运算未完成，则阻塞直到完成；调用其cancel方法可以取消一次异步执行； 
 1. CompletionService：一种执行者，可将submit的多个任务的结果按照完成的先后顺序存入一个内部队列，然后可以使用take方法从队列中依次取出结果并移除，如果调用take时计算未完成则会阻塞。 

## 如何重复执行和延期执行？
在Java1.4之前，一般使用Timer来重复或者延期执行任务。Java Concurrency为了使之与Executor理念协调，引入了ScheduledExecutorService来完成同样的工作。 
ScheduledExecutorService：另一种执行者，可以将提交的任务延期执行，也可以将提交的任务反复执行。 
ScheduledFuture：与Future接口类似，代表一个被调度执行的异步任务的返回值。 
下面的例子中ScheduledExecutorService的实例scheduler调度了两个任务，第一个任务使用scheduleAtFixedRate()方法每隔一秒重复打印“beep”，第二个任务使用schedule()方法在10秒后延迟执行，它的作用是取消第一个任务，