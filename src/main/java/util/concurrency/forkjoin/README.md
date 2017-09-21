  > 在JDK1.7引入了一种新的并行编程模式“fork-join”，它是实现了“分而治之”思想的Java并发编程框架。
 ![结构图](https://pic1.zhimg.com/v2-e11a5c34827a48bb00035a7937978df0_r.png)
 
 ## fork-join框架的特点
1. 它对问题的解决思路是分而治之，先将一个问题fork（分为）几个子问题，然后子问题又分为孙子问题，直至细分为一个容易计算的问题，然后再将结果依次join(结合)为最终的答案。是不是感觉和云计算中的Map-reduce计算模型很像？思路是一样的，只不过fork-join运行在一个JVM中的多个线程内，而map-reduce运行在分布式计算节点上。 
2. 在运行线程时，它使用“work-steal”（任务偷取）算法。一般来说，fork-join会启动多个线程（由参数指定，若不指定则默认为CPU核心数量），每个线程负责一个任务队列，并依次从队列头部获得任务并执行。当某个线程空闲时，它会从其他线程的任务队列尾部偷取一个任务来执行，这样就保证了线程的运行效率达到最高。 
3. 它面向的问题域是可以大量并行执行的计算任务，例如计算某个大型数组中每个元素的平方（当然这个有些无趣），其计算对象最好是一些独立的元素，不会被其他线程访问，也没有同步、互斥要求，更不要涉及IO或者无限循环。当然此框架也可以执行普通的并发编程任务，但是这时就失去了性能优势。 
4. 细分的计算任务有一个粗略的优化标准，即可以在100~10000条指令中执行完毕。

 ## fork-join的主要工具
 这个功能主要由三个接口和类提供，分别是： 
1. ForkJoinPool：支持fork-join框架的线程池，所有ForkJoinTask任务都必须在其中运行，线程池主要使用invoke()、invokeAll()等方法来执行任务，当然也可以使用原有的execute()和submit()方法； 
1. ForkJoinTask：支持fork-join框架的任务抽象类，它是Future接口，它代表一个支持fork()和join()方法的任务； 
1. RecursiveAction：ForkJoinTask的两个具体子类之一，代表没有返回值的ForkJoinTask任务； 
1. RecursiveTask：ForkJoinTask的两个具体子类之一，代表有返回值的ForkJoinTask任务。


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