  > BlockingQueue是一系列阻塞队列类的父接口，阻塞队列的共同特性是当存取条件不满足时，阻塞在操作处，因此常常被用于生产者和消费者场景中。。
 ![结构图](https://pic1.zhimg.com/v2-e11a5c34827a48bb00035a7937978df0_r.png)
 
 ## BlockingQueue四组操作方法
1. add、remove、element：在遇到异常时（例如add时队列已满，或者remove时队列已空）会抛出异常
2. offer、poll、peek：在遇到异常时会返回特殊值，例如offer时队列已满返回false，poll时队列已空会返回null。
3. put、take：它没有提供像element、peek类似的仅仅读取的方法。put在队列已满时阻塞，直到队列中空出位置，然后将元素加入队列；take在队列已空时阻塞，直到队列中重新有了元素，然后从队列头取出元素并返回。 
4. offer(e, time, unit)和poll(time, unit)：为了提供timeout特性，BlockingQueue提供了offer和poll的timeout形式，它们会在特殊情况时阻塞直到条件满足或者超时。

 ## fork-join的主要工具
 